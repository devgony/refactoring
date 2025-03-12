package chapter08;

import java.util.Date;

import org.junit.jupiter.api.Test;

import chapter08.MoveStatementsIntoFunction.*;
import static chapter08.MoveStatementsIntoFunction.*;
import static org.assertj.core.api.Assertions.assertThat;

class MoveStatementsIntoFunctionTest {
    @Test
    void client1() {
        Photo photo = new Photo("My Photo", "My Location", new Date());

        assertThat(photoDiv(photo)).isEqualTo("<div>\n" +
                "<p>title: My Photo</p>\n" +
                "<p>location: My Location</p>\n" +
                "<p>date: " + photo.date.toString() + "</p>\n" +
                "</div>");

        assertThat(emitPhotoData(photo)).isEqualTo("<p>location: My Location</p>\n" +
                "<p>date: " + photo.date.toString() + "</p>");

        Person person = new Person("John", photo);
        assertThat(renderPerson(null, person)).isEqualTo("<p>John</p>\n" +
                "<div>\n" +
                "<p>title: My Photo</p>\n" +
                "<p>location: My Location</p>\n" +
                "<p>date: " + photo.date.toString() + "</p>\n" +
                "</div>\n" +
                "<p>title: My Photo</p>\n" +
                "<p>location: My Location</p>\n" +
                "<p>date: " + photo.date.toString() + "</p>");
    }
}
