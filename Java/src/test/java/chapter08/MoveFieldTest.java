package chapter08;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter08.MoveField.Account;
import chapter08.MoveField.AccountType;
import chapter08.MoveField.Customer;

class MoveFieldTest {
    @Test
    void client1() {
        Customer customer = new Customer("John", 0.1);
        customer.becomePreferred();
        assertThat(customer.discountRate()).isEqualTo(0.13);
        assertThat(customer.applyDiscount(100)).isEqualTo(87.0);

    }

    @Test
    void client2() {
        AccountType accountType = new AccountType("Savings");
        Account account = new Account(123, accountType, 0.05);
        double actual = account.interestRate();
        assertThat(actual).isEqualTo(0.05);
    }
}
