package chapter10;

import java.util.List;

class ReplaceControlFlagWithBreak {
    int client1(List<String> people) {
        boolean found = false;
        int loop = 0;
        for (String p : people) {
            if (!found) {
                loop++;
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

        return loop;
    }

    void sendAlert() {

    }
}
