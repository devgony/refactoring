package chapter12;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

class ReplaceSuperclassWithDelegate {
    static class CatalogItem {
        String _id;
        String _title;
        List<String> _tags;

        CatalogItem(String id, String title, List<String> tags) {
            this._id = id;
            this._title = title;
            this._tags = tags;
        }

        String id() {
            return this._id;
        }

        String title() {
            return this._title;
        }

        boolean hasTag(String arg) {
            return this._tags.contains(arg);
        }

        List<String> tags() {
            return this._tags;
        }
    }

    static class Scroll extends CatalogItem {
        LocalDate _lastCleaned;

        Scroll(String id, String title, List<String> tags, LocalDate dateLastCleaned) {
            super(id, title, tags);
            this._lastCleaned = dateLastCleaned;
        }

        boolean needsCleaning(LocalDate targetDate) {
            long threshold = this.hasTag("revered") ? 700 : 1500;
            return this.daysSinceLastCleaning(targetDate) > threshold;
        }

        long daysSinceLastCleaning(LocalDate targetDate) {
            return this._lastCleaned.until(targetDate, ChronoUnit.DAYS);
        }
    }
}
