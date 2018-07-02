package com.epam.test_generator.services;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.entities.request.FeatureFileDTO;
import com.epam.test_generator.controllers.project.ProjectTransformer;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.controllers.step.response.StepDTO;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.file_generator.FileGenerator;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOService {

    @Autowired
    private FileGenerator fileGenerator;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private ProjectTransformer projectTransformer;

    private static final String ILLEGAL_CHARACTERS = "[/\n\r\t\f\\\\`'?*<>\":|]";

    /**
     * Generates zip archive which contains set of feature files.
     *
     * @param projectId id of project
     * @param featureFileDTOs ids of suits and cases
     */
    public byte[] generateZipFile(Long projectId, List<FeatureFileDTO> featureFileDTOs)
        throws IOException {
        validateInput(projectId, featureFileDTOs);
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

                for (FeatureFileDTO featureFileDTO : featureFileDTOs) {
                    SuitDTO suitDTO = getSuitDTOByFeatureFile(featureFileDTO);
                    String fileName = suitDTO.getName().replaceAll(ILLEGAL_CHARACTERS, "_");
                    ZipEntry zipEntry = new ZipEntry(fileName + "_" + suitDTO.getId() + ".feature");
                    zipOutputStream.putNextEntry(zipEntry);
                    zipOutputStream.write(generateFile(suitDTO));
                    zipOutputStream.closeEntry();
                }
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * Checks whether the suites belong to the project and cases to the suit
     *
     * @param projectId id of project
     * @param featureFileDTOs ids of suits and cases
     */
    private void validateInput(Long projectId, List<FeatureFileDTO> featureFileDTOs) {
        ProjectFullDTO projectFullDTO = projectTransformer
            .toFullDto(projectService.getProjectByProjectId(projectId));

        for (FeatureFileDTO featureFileDTO : featureFileDTOs) {
            SuitDTO suitDTO = projectFullDTO.getSuits().stream()
                .filter(s -> s.getId()
                    .equals(featureFileDTO.getSuitId())).findFirst()
                .orElseThrow(NotFoundException::new);

            if (suitDTO.getCases().isEmpty()) {
                throw new NotFoundException();
            }

            List<Long> casesIdsFromSuit = suitDTO.getCases().stream()
                .map(CaseDTO::getId).collect(Collectors.toList());

            if (!casesIdsFromSuit.containsAll(featureFileDTO.getCaseIds())) {
                throw new NotFoundException();
            }

            for (Long caseId : featureFileDTO.getCaseIds()) {
                suitDTO.getCases()
                    .stream()
                    .filter( c -> c.getId().equals(caseId))
                    .filter(c -> !c.getSteps().isEmpty())
                    .findFirst().orElseThrow(NotFoundException::new);
            }
        }
    }

    /**
     * Returns a suite with existing cases which are sorted by row number
     *
     * @param featureFileDTO ids of suits and cases
     */
    private SuitDTO getSuitDTOByFeatureFile(FeatureFileDTO featureFileDTO) {
        Suit suit = suitDAO.findById(featureFileDTO.getSuitId())
            .orElseThrow(NotFoundException::new);
        SuitDTO suitDTO = suitTransformer.toDto(suit);
        List<CaseDTO> caseDTOs = suitDTO.getCases().stream()
            .filter(caseDTO -> featureFileDTO.getCaseIds().contains(caseDTO.getId()))
            .sorted(Comparator.comparingLong(CaseDTO::getRowNumber))
            .peek(caseDTO -> {
                List<StepDTO> orderedStepsDTOs = caseDTO.getSteps().stream()
                    .sorted(Comparator.comparingLong(StepDTO::getRowNumber))
                    .collect(Collectors.toList());
                caseDTO.setSteps(orderedStepsDTOs);
            })
            .collect(Collectors.toList());
        suitDTO.setCases(caseDTOs);
        return suitDTO;
    }

    /**
     * Generates file which contains suit and it's cases.
     *
     * @param suitDTO list of ids of suit's cases
     * @return byte array generated by fileGenerator
     */
    private byte[] generateFile(SuitDTO suitDTO) throws IOException {
        return fileGenerator.generate(suitDTO, suitDTO.getCases()).getBytes(StandardCharsets.UTF_8);
    }
}