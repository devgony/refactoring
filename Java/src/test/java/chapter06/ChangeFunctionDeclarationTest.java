package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chapter06.ChangeFunctionDeclaration.Address;
import chapter06.ChangeFunctionDeclaration.Book;
import chapter06.ChangeFunctionDeclaration.Customer;

class ChangeFunctionDeclarationTest {
    Address address;
    Customer customer;
    Book book;

    @BeforeEach
    void setUp() {
        address = new Address("CA");
        customer = new Customer(address);
        book = new Book(new ArrayList<>());
    }

    @Test
    void circum() {
        double actual = ChangeFunctionDeclaration.circumference(6);
        assertThat(actual).isEqualTo(37.69911184307752);
    }

    @Test
    void book() {
        book.addReservation(customer);
        assertThat(book._reservations).contains(customer);
    }

    @Test
    void isNewEngland() {
        boolean actual = ChangeFunctionDeclaration.inNewEngland(customer.address.state);
        assertThat(actual).isFalse();
    }
}
