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

    @BeforeEach
    void setUp() {

    }

    @Test
    void runStageShouldThrowOvenExceptionOnHeatingException() throws HeatingException {
        // given
        final int TEMP = 220, TIME = 90;

        Oven oven = new Oven(heatingModuleMock, fanMock);

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

}
