package chapter11;

import java.util.List;

class SeparateQueryFromModifier {
    static String alertForMiscreant(List<String> people) {
        for (String p : people) {
            if (p == "Don") {
                setOffAlarms();
                return "Don";
            }
            if (p == "John") {
                setOffAlarms();
                return "John";
            }
        }
        return "";
    }

    private static void setOffAlarms() {
    }
}
