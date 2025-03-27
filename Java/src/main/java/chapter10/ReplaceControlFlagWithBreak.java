package chapter10;

import java.util.List;

class ReplaceControlFlagWithBreak {
    void client1(List<String> people) {
        checkForMiscreants(people);
    }

    private void checkForMiscreants(List<String> people) {
        // for (String p : people) {
        // if ("Don".equals(p)) {
        // sendAlert();
        // return;
        // }
        // if ("John".equals(p)) {
        // sendAlert();
        // return;
        // }
        // }
        if (people.stream().anyMatch(p -> "Don".equals(p) || "John".equals(p))) {
            sendAlert();
        }
    }

    void sendAlert() {

    }
}
