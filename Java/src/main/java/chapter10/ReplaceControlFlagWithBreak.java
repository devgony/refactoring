package chapter10;

import java.util.List;

class ReplaceControlFlagWithBreak {
    void client1(List<String> people) {
        checkForMiscreants(people);
    }

    private void checkForMiscreants(List<String> people) {
        boolean found = false;
        for (String p : people) {
            if (!found) {
                if ("Don".equals(p)) {
                    sendAlert();
                    return;
                }
                if ("John".equals(p)) {
                    sendAlert();
                    return;
                }
            }
        }
    }

    void sendAlert() {

    }
}
