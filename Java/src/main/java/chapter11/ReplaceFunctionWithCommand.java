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
        return new Scorer(candidate, medicalExam, scoringGuide).execute();
    }

    static class Scorer {
        Candidate candidate;
        MedicalExam medicalExam;
        ScoringGuide scoringGuide;

        int result;
        int healthLevel;
        boolean highMedicalRiskFlag;
        String certificationGrade;

        Scorer(Candidate candidate, MedicalExam medicalExam, ScoringGuide scoringGuide) {
            this.candidate = candidate;
            this.medicalExam = medicalExam;
            this.scoringGuide = scoringGuide;
        }

        int execute() {
            result = 0;
            healthLevel = 0;
            highMedicalRiskFlag = false;

            if (medicalExam.isSmoker) {
                healthLevel += 10;
                highMedicalRiskFlag = true;
            }
            certificationGrade = "regular";
            if (scoringGuide.stateWithLowCertification(candidate.originState)) {
                certificationGrade = "low";
                result -= 5;
            }
            // lots more code like this
            result -= Math.max(healthLevel - 5, 0);

            return result;
        }
    }
}
