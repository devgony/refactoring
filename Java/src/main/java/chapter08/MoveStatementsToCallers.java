package chapter08;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;

class MoveStatementsToCallers {
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

    static void renderPerson(StringWriter outStream, Person person) {
        outStream.write("<p>" + person.name + "</p>\n");
        renderPhoto(outStream, person.photo);
        zztmp(outStream, person.photo);
        outStream.write("<p>location: " + person.photo.location + "</p>\n");
    }

    static void renderPhoto(StringWriter outStream, Photo photo) {
        // Implementation for renderPhoto
        outStream.write("<img src=\"" + photo.title + "\">\n");
    }

    static void listRecentPhotos(StringWriter outStream, List<Photo> photos) {
        photos.stream()
                .filter(p -> p.date.after(recentDateCutoff()))
                .forEach(p -> {
                    outStream.write("<div>\n");
                    zztmp(outStream, p);
                    outStream.write("<p>location: " + p.location + "</p>\n");
                    outStream.write("</div>\n");
                });
    }

    static private Date recentDateCutoff() {
        // Implementation for recent date cutoff
        return new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000); // 7 days ago
    }

    private static void zztmp(StringWriter outStream, Photo photo) {
        outStream.write("<p>title: " + photo.title + "</p>\n");
        outStream.write("<p>date: " + photo.date.toString() + "</p>\n");
    }

}
