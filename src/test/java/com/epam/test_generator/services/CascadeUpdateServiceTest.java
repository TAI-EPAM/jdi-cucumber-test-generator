package com.epam.test_generator.services;

import static org.mockito.Mockito.verify;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Action;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.google.common.collect.Lists;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class CascadeUpdateServiceTest {

    private final static Long PROJECT_ID = 1L;
    private final static Long SUIT_ID = 2L;
    private final static Long CASE_ID_FOR_CREATE = 0L;
    private final static Long CASE_ID_FOR_UPDATE = 3L;
    private final static Long STEP_ID_FOR_CREATE = 0L;
    private final static Long STEP_ID_FOR_UPDATE = 3L;

    private final SuitDTO suitDTO = new SuitDTO();
    private final CaseDTO caseDTO = new CaseDTO();
    private final EditCaseDTO editCaseDTO = new EditCaseDTO();
    private final StepDTO stepDTO = new StepDTO();

    @Mock
    private StepService stepService;

    @Mock
    private CaseService caseService;

    @InjectMocks
    private CascadeUpdateService cascadeUpdateService;

    @Before
    public void setUp() {
        suitDTO.setId(SUIT_ID);
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitWithoutCaseForUpdateWillWorkSuccessfully()
        throws MethodArgumentNotValidException {
        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitWithCaseForUpdateWillWorkSuccessfully()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_UPDATE);

        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.UPDATE);

        suitDTO.setCases(Lists.newArrayList(caseDTO));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
        verify(caseService).updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTO));
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitWithCaseForCreateWillBeUpdatedSuccessfully()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_CREATE);

        suitDTO.setCases(Lists.newArrayList(caseDTO));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
        verify(caseService).addCaseToSuit(PROJECT_ID, SUIT_ID, caseDTO);
    }

    @Test(expected = BadRequestException.class)
    public void cascadeSuitCasesUpdate_SuitWithCaseForCreateAndStepForCreateThrowsExceptionDueToExistedStep()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_CREATE);

        stepDTO.setId(STEP_ID_FOR_UPDATE);
        stepDTO.setAction(Action.CREATE);

        caseDTO.setSteps(Lists.newArrayList(stepDTO));
        suitDTO.setCases(Lists.newArrayList(caseDTO));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
        verify(caseService).addCaseToSuit(PROJECT_ID, SUIT_ID, caseDTO);
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitWithCaseForUpdateAndStepForCreateWillBeUpdatedSuccessfullyAndReturnEmptyListAsStepStatusNotFAILED()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_UPDATE);

        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.UPDATE);

        stepDTO.setId(STEP_ID_FOR_CREATE);
        stepDTO.setStatus(Status.PASSED);
        stepDTO.setAction(Action.CREATE);

        caseDTO.setSteps(Lists.newArrayList(stepDTO));
        editCaseDTO.setSteps(Lists.newArrayList(stepDTO));
        suitDTO.setCases(Lists.newArrayList(caseDTO));

        Assert.assertEquals(Collections.emptyList(),
            cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO));
        verify(stepService).cascadeUpdateSteps(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE,
            Lists.newArrayList(stepDTO));
        verify(caseService).updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTO));
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitWithCaseForUpdateAndStepForUpdateWillBeUpdatedSuccessfullyAndReturnListWithStepIdAsStepStatusFAILED()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_UPDATE);

        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.UPDATE);

        stepDTO.setId(STEP_ID_FOR_UPDATE);
        stepDTO.setStatus(Status.FAILED);
        stepDTO.setAction(Action.UPDATE);

        caseDTO.setSteps(Lists.newArrayList(stepDTO));
        editCaseDTO.setSteps(Lists.newArrayList(stepDTO));
        suitDTO.setCases(Lists.newArrayList(caseDTO));

        Assert.assertEquals(Lists.newArrayList(STEP_ID_FOR_UPDATE),
            cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO));
        verify(stepService).cascadeUpdateSteps(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE,
            Lists.newArrayList(stepDTO));
        verify(caseService).updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTO));
    }

    @Test
    public void cascadeSuitCasesUpdate_SuitIsAbleToUpdateItsStatementsByEveryPossibleOperationSimultaneously()
        throws MethodArgumentNotValidException {
        final CaseDTO caseDTOForCreate = new CaseDTO();
        caseDTOForCreate.setId(CASE_ID_FOR_CREATE);

        final StepDTO stepDTOForCreate = new StepDTO();
        stepDTOForCreate.setId(STEP_ID_FOR_CREATE);
        stepDTOForCreate.setStatus(Status.NOT_RUN);
        stepDTOForCreate.setAction(Action.CREATE);

        caseDTOForCreate.setSteps(Lists.newArrayList(stepDTOForCreate));

        final CaseDTO caseDTOForUpdate = new CaseDTO();
        caseDTOForUpdate.setId(CASE_ID_FOR_UPDATE);

        final StepDTO stepDTOForUpdate = new StepDTO();
        stepDTOForUpdate.setId(STEP_ID_FOR_UPDATE);
        stepDTOForUpdate.setStatus(Status.PASSED);
        stepDTOForUpdate.setAction(Action.UPDATE);

        caseDTOForUpdate.setSteps(Lists.newArrayList(stepDTOForUpdate, stepDTOForCreate));

        final EditCaseDTO editCaseDTOForUpdate = new EditCaseDTO();
        editCaseDTOForUpdate.setId(CASE_ID_FOR_UPDATE);
        editCaseDTOForUpdate.setAction(Action.UPDATE);

        editCaseDTOForUpdate.setSteps(Lists.newArrayList(stepDTOForUpdate, stepDTOForCreate));
        suitDTO.setCases(Lists.newArrayList(caseDTOForCreate, caseDTOForUpdate));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);

        verify(stepService).cascadeUpdateSteps(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE,
            caseDTOForUpdate.getSteps());
        verify(caseService)
            .updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTOForUpdate));
        verify(caseService).addCaseToSuit(PROJECT_ID, SUIT_ID, caseDTOForCreate);
    }

    @Test(expected = BadRequestException.class)
    public void cascadeSuitCasesUpdate_SuitWithCaseForCreateAndStepForUpdateWillThrowException()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_CREATE);

        editCaseDTO.setId(CASE_ID_FOR_CREATE);
        editCaseDTO.setAction(Action.CREATE);

        stepDTO.setId(STEP_ID_FOR_UPDATE);
        stepDTO.setStatus(Status.NOT_RUN);
        stepDTO.setAction(Action.UPDATE);

        caseDTO.setSteps(Lists.newArrayList(stepDTO));
        editCaseDTO.setSteps(Lists.newArrayList(stepDTO));
        suitDTO.setCases(Lists.newArrayList(caseDTO));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
        verify(stepService)
            .cascadeUpdateSteps(PROJECT_ID, SUIT_ID, CASE_ID_FOR_CREATE, editCaseDTO.getSteps());
        verify(caseService).updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTO));
    }

    @Test(expected = BadRequestException.class)
    public void cascadeSuitCasesUpdate_methodThrowsExceptionIfThereAreIdDuplicatesInListOfCases()
        throws MethodArgumentNotValidException {
        caseDTO.setId(CASE_ID_FOR_UPDATE);

        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.CREATE);

        suitDTO.setCases(Lists.newArrayList(caseDTO, caseDTO));

        cascadeUpdateService.cascadeSuitCasesUpdate(PROJECT_ID, SUIT_ID, suitDTO);
        verify(caseService).updateCases(PROJECT_ID, SUIT_ID, Lists.newArrayList(editCaseDTO));
    }

    @Test(expected = BadRequestException.class)
    public void cascadeCaseStepsUpdate_methodThrowsExceptionIfActionNotUpdate() {
        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.CREATE);

        cascadeUpdateService
            .cascadeCaseStepsUpdate(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE, editCaseDTO);
    }

    @Test
    public void cascadeCaseStepsUpdate_CaseWithoutStepsWillUpdateSuccessfully() {
        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.UPDATE);

        cascadeUpdateService
            .cascadeCaseStepsUpdate(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE, editCaseDTO);
    }

    @Test
    public void cascadeCaseStepsUpdate_CaseWithStepsWillUpdateSuccessfully() {
        editCaseDTO.setId(CASE_ID_FOR_UPDATE);
        editCaseDTO.setAction(Action.UPDATE);

        final StepDTO stepDTOForCreate = new StepDTO();
        stepDTOForCreate.setId(STEP_ID_FOR_CREATE);
        stepDTOForCreate.setAction(Action.CREATE);

        final StepDTO stepDTOForUpdate = new StepDTO();
        stepDTOForUpdate.setId(STEP_ID_FOR_UPDATE);
        stepDTOForUpdate.setStatus(Status.NOT_RUN);
        stepDTOForUpdate.setAction(Action.UPDATE);

        editCaseDTO.setSteps(Lists.newArrayList(stepDTOForCreate, stepDTOForUpdate));

        cascadeUpdateService
            .cascadeCaseStepsUpdate(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE, editCaseDTO);
        verify(stepService)
            .cascadeUpdateSteps(PROJECT_ID, SUIT_ID, CASE_ID_FOR_UPDATE, editCaseDTO.getSteps());
    }

}
