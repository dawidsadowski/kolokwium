package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    Fan fanMock;

    @Mock
    HeatingModule heatingModuleMock;

    Oven oven;

    @BeforeEach
    void setUp() {
        oven = new Oven(heatingModuleMock, fanMock);
    }

    @Test
    void runStageShouldThrowOvenExceptionOnHeatingException() throws HeatingException {
        // given
        final int TEMP = 220, TIME = 90;

        ProgramStage stage = ProgramStage.builder()
                .withTargetTemp(TEMP)
                .withStageTime(TIME)
                .withHeat(HeatType.THERMO_CIRCULATION)
                .build();
        List<ProgramStage> stages = List.of(stage);

        HeatingSettings settings = HeatingSettings.builder()
                .withTargetTemp(TEMP)
                .withTimeInMinutes(TIME)
                .build();

        BakingProgram program = BakingProgram.builder()
                .withInitialTemp(0)
                .withStages(stages)
                .build();

        // when
        doThrow(HeatingException.class).when(heatingModuleMock).termalCircuit(settings);

        // then
        assertThrows(OvenException.class, () -> oven.start(program));
    }

    @Test
    void checkIfGrillWasRun() {
        final int TEMP = 220, TIME = 90;

        ProgramStage stage = ProgramStage.builder()
                .withTargetTemp(TEMP)
                .withStageTime(TIME)
                .withHeat(HeatType.GRILL)
                .build();
        List<ProgramStage> stages = List.of(stage);

        HeatingSettings settings = HeatingSettings.builder()
                .withTargetTemp(TEMP)
                .withTimeInMinutes(TIME)
                .build();

        BakingProgram program = BakingProgram.builder()
                .withInitialTemp(0)
                .withStages(stages)
                .build();

        oven.start(program);

        verify(heatingModuleMock).grill(settings);
    }

    @Test
    void checkIfHeaterWasRun() {
        final int TEMP = 220, TIME = 90;

        ProgramStage stage = ProgramStage.builder()
                .withTargetTemp(TEMP)
                .withStageTime(TIME)
                .withHeat(HeatType.HEATER)
                .build();
        List<ProgramStage> stages = List.of(stage);

        HeatingSettings settings = HeatingSettings.builder()
                .withTargetTemp(TEMP)
                .withTimeInMinutes(TIME)
                .build();

        BakingProgram program = BakingProgram.builder()
                .withInitialTemp(0)
                .withStages(stages)
                .build();

        oven.start(program);

        verify(heatingModuleMock).heater(settings);
    }

}
