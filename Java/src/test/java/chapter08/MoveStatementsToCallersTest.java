package chapter08;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chapter08.MoveStatementsToCallers.Photo;
import static chapter08.MoveStatementsToCallers.*;
import static org.assertj.core.api.Assertions.assertThat;

class MoveStatementsToCallersTest {
    final Photo photo = new Photo("My Photo", "My Location", new Date());
    final Person person = new Person("John", photo);
    StringWriter outStream = new StringWriter();

    @BeforeEach
    void setUp() {
        outStream.flush();
    }

    @Test
    void client1() {
        renderPerson(outStream, person);
        assertThat(outStream.toString()).isEqualTo(
                "<p>John</p>\n" +
                        "<img src=\"My Photo\">\n" +
                        "<p>title: My Photo</p>\n" +
                        "<p>date: " + photo.date.toString() + "</p>\n" +
                        "<p>location: My Location</p>\n");
    }

    @Test
    void client2() {
        Photo photo2 = new Photo("My Photo 2", "My Location 2", new Date());
        List<Photo> photos = Arrays.asList(photo, photo2);
        listRecentPhotos(outStream, photos);
        assertThat(outStream.toString()).isEqualTo(
                "<div>\n" +
                        "<p>title: My Photo</p>\n" +
                        "<p>date: " + photo.date.toString() + "</p>\n" +
                        "<p>location: My Location</p>\n" +
                        "</div>\n" +
                        "<div>\n" +
                        "<p>title: My Photo 2</p>\n" +
                        "<p>date: " + photo2.date.toString() + "</p>\n" +
                        "<p>location: My Location 2</p>\n" +
                        "</div>\n");

    }
}
