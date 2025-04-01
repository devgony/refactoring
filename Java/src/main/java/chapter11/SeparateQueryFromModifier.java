package chapter11;

import java.util.List;

class SeparateQueryFromModifier {
    static void alertForMiscreant(List<String> people) {
        for (String p : people) {
            if (p == "Don") {
                setOffAlarms();
            }
            if (p == "John") {
                setOffAlarms();
            }
        }
    }

    static void setOffAlarms() {
    }

    static String findMiscreant(List<String> people) {
        for (String p : people) {
            if (p == "Don") {
                return "Don";
            }
            if (p == "John") {
                return "John";
            }
        }
        return "";
    }
}
