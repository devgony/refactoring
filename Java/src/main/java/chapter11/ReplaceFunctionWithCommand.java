package chapter11;

import lombok.AllArgsConstructor;

class ReplaceFunctionWithCommand {
    @AllArgsConstructor
    static class Candidate {
        String originState;
    }

    @AllArgsConstructor
    static class MedicalExam {
        boolean isSmoker;
    }

    @AllArgsConstructor
    static class ScoringGuide {
        boolean stateWithLowCertification(String state) {
            return state.equals("low");
        }
    }

    static int score(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
        int result = 0;
        int healthLevel = 0;
        boolean highMedicalRiskFlag = false;

        if (medicalExam.isSmoker) {
            healthLevel += 10;
            highMedicalRiskFlag = true;
        }
        String certificationGrade = "regular";
        if (scoringGuide.stateWithLowCertification(candidate.originState)) {
            certificationGrade = "low";
            result -= 5;
        }
        // lots more code like this
        result -= Math.max(healthLevel - 5, 0);

        return result;
    }
}
