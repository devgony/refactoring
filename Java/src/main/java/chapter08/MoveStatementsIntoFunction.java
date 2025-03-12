package chapter08;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;

class MoveStatementsIntoFunction {
    @AllArgsConstructor
    static class Photo {
        String title;
        String location;
        Date date;
    }

    static class Person {
        String name;
        Photo photo;

        Person(String name, Photo photo) {
            this.name = name;
            this.photo = photo;
        }
    }

    static String renderPerson(StringWriter _outStream, Person person) {
        List<String> result = new ArrayList<>();
        result.add("<p>" + person.name + "</p>");
        result.add(photoDiv(person.photo));
        result.add(zznew(person.photo));

        return String.join("\n", result);
    }

    static String photoDiv(Photo p) {
        List<String> result = Arrays.asList("<div>", zznew(p), "</div>");

        return String.join("\n", result);
    }

    static String zznew(Photo p) {
        List<String> result = Arrays.asList("<p>title: " + p.title + "</p>", emitPhotoData(p));

        return String.join("\n", result);
    }

    static String emitPhotoData(Photo aPhoto) {
        List<String> result = new ArrayList<>();
        result.add("<p>location: " + aPhoto.location + "</p>");
        result.add("<p>date: " + aPhoto.date.toString() + "</p>");

        return String.join("\n", result);
    }
}
