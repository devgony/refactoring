package chapter08;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter08.MoveFunction.Account;
import chapter08.MoveFunction.AccountType;
import chapter08.MoveFunction.Point;
import chapter08.MoveFunction.Summary;

class MoveFunctionTest {
    @Test
    void client1() {
        Point[] points = {
                new Point(0, 0),
                new Point(0, 1),
        };

        Summary actual = MoveFunction.trackSummary(points);
        assertThat(actual).isEqualTo(new Summary(600, 1.0, 10.0));
    }

    @Test
    void client2() {
        AccountType accountType = new AccountType(true);
        Account account = new Account(accountType, 10);
        double actual = account.bankCharge();
        assertThat(actual).isEqualTo(17.05);
    }
}
