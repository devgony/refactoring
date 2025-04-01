package chapter11;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static chapter11.ReplaceFunctionWithCommand.*;

class ReplaceFunctionWithCommandTest {
    @Test
    void client1() {
        Candidate candidate = new Candidate("low");
        MedicalExam medicalExam = new MedicalExam(true);
        ScoringGuide scoringGuide = new ScoringGuide();
        int actual = score(candidate, medicalExam, scoringGuide);
        assertThat(actual).isEqualTo(-10);
    }
}
