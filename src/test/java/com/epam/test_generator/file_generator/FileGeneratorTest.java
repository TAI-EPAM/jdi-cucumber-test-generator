package com.epam.test_generator.file_generator;


import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.StepType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileGeneratorTest extends Assert {

    private FileGenerator fileGenerator;

    private SuitDTO suit;
    private List<CaseDTO> cases;
    private Set<TagDTO> tags;

    @Before
    public void prepareFileGenerator() {
        fileGenerator = new FileGenerator();
    }

    @Test
    public void generate_SuitWithCases_Success() throws IOException {
        suit = new SuitDTO();
        suit.setId(1L);
        suit.setName("suit1");
        suit.setDescription("description1");
        suit.setPriority(1);
        suit.setTags(new HashSet<>());

        cases = new ArrayList<>();

        tags = new HashSet<>();
        tags.add(new TagDTO("@tags"));

        final CaseDTO case1 = new CaseDTO();
        case1.setId(1L);
        case1.setName("name1");
        case1.setDescription("case1");
        case1.setPriority(1);
        case1.setTags(null);

        final CaseDTO case2 = new CaseDTO();
        case2.setId(2L);
        case2.setName("name2");
        case2.setDescription("case2");
        case2.setPriority(1);
        case2.setTags(tags);

        final List<StepDTO> steps = new ArrayList<>();
        final StepDTO step1 = new StepDTO();
        step1.setId(1L);
        step1.setDescription("given1");
        step1.setRowNumber(1);
        step1.setType(StepType.GIVEN);

        final StepDTO step2 = new StepDTO();
        step2.setId(2L);
        step2.setRowNumber(2);
        step2.setDescription("when1");
        step2.setType(StepType.WHEN);

        final StepDTO step3 = new StepDTO();
        step3.setId(3L);
        step3.setRowNumber(3);
        step3.setDescription("then1");
        step3.setType(StepType.THEN);

        steps.add(step1);
        steps.add(step2);
        steps.add(step3);
        case1.setSteps(steps);

        final StepDTO step4 = new StepDTO();
        step4.setId(4L);
        step4.setRowNumber(4);
        step4.setDescription("given2");
        step4.setType(StepType.GIVEN);

        final List<StepDTO> steps2 = new ArrayList<>();
        steps2.add(step4);
        case2.setSteps(steps2);

        cases.add(case1);
        cases.add(case2);
        suit.setCases(cases);

        final File expectedFile = new File("src/test/resources/FileGeneratorTest1");
        final String realResult = fileGenerator.generate(suit, cases);
        final String expectedResult = new Scanner(expectedFile).useDelimiter("\\Z").next();
        assertEquals(expectedResult.trim(), realResult.trim());
    }

    @Test
    public void generate_SuitWithCaseWithoutSteps_Success() throws IOException {

        suit = new SuitDTO();
        suit.setId(1L);
        suit.setName("suit1");
        suit.setDescription("description1");
        suit.setPriority(1);
        suit.setTags(new HashSet<>());

        cases = new ArrayList<>();

        final CaseDTO case1 = new CaseDTO();
        case1.setId(1L);
        case1.setName("name3");
        case1.setDescription("case3");
        case1.setPriority(1);
        case1.setTags(null);
        case1.setSteps(new ArrayList<StepDTO>());
        cases.add(case1);
        suit.setCases(cases);

        final File expectedFile = new File("src/test/resources/FileGeneratorTest2");

        final String realResult = fileGenerator.generate(suit, cases);
        final String expectedResult = new Scanner(expectedFile).useDelimiter("\\Z").next();
        assertEquals(expectedResult.trim(), realResult.trim());

    }

    @Test(expected = NullPointerException.class)
    public void generate_NullSuitAndEmptyCasesList_NullPointerException() throws IOException {
        fileGenerator.generate(null, new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void generate_SuitWithNullCasesList_NullPointerException() throws IOException {

        suit = new SuitDTO();
        suit.setId(1L);
        suit.setName("suit1");
        suit.setDescription("description1");
        suit.setPriority(1);
        suit.setTags(new HashSet<>());
        fileGenerator.generate(suit, null);
    }

    @Test
    public void generate_SuitWithoutInnerCases_Success() throws IOException {
        suit = new SuitDTO();
        suit.setId(1L);
        suit.setName("suit1");
        suit.setDescription("description1");
        suit.setPriority(1);
        suit.setTags(new HashSet<>());

        cases = new ArrayList<>();
        final CaseDTO caze1 = new CaseDTO();
        caze1.setId(1L);
        caze1.setName("name3");
        caze1.setDescription("case3");
        caze1.setPriority(1);
        caze1.setTags(null);
        caze1.setSteps(new ArrayList<>());
        cases.add(caze1);

        final File expectedFile = new File("src/test/resources/FileGeneratorTest2");
        final String realResult = fileGenerator.generate(suit, cases);
        final String expectedResult = new Scanner(expectedFile).useDelimiter("\\Z").next();
        assertEquals(expectedResult.trim(), realResult.trim());
    }
}