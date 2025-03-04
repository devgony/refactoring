package chapter07;

class SubstituteAlgorithm {
    static String foundPerson(String[] people) {
        for (String person : people) {
            if (person.equals("Don")) {
                return "Don";
            }
            if (person.equals("John")) {
                return "John";
            }
            if (person.equals("Kent")) {
                return "Kent";
            }
        }
        return "";
    }
}
