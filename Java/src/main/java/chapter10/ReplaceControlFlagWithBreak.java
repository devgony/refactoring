package chapter10;

import java.util.List;

class ReplaceControlFlagWithBreak {
    void client1(List<String> people) {

        boolean found = false;
        for (String p : people) {
            if (!found) {
                if ("Don".equals(p)) {
                    sendAlert();
                    found = true;
                }
                if ("John".equals(p)) {
                    sendAlert();
                    found = true;
                }
            }
        }

    }

    void sendAlert() {

    }
}
