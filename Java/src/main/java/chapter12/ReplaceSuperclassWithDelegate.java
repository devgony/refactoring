package chapter12;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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

    static class Scroll {
        String _id;
        CatalogItem _catalogItem;
        LocalDate _lastCleaned;

        Scroll(String id, String title, List<String> tags, LocalDate dateLastCleaned, String catalogID,
                Map<String, CatalogItem> catalog) {
            this._id = id;
            this._catalogItem = new CatalogItem(null, title, tags);
            this._lastCleaned = dateLastCleaned;
        }

        boolean needsCleaning(LocalDate targetDate) {
            long threshold = this.hasTag("revered") ? 700 : 1500;
            return this.daysSinceLastCleaning(targetDate) > threshold;
        }

        long daysSinceLastCleaning(LocalDate targetDate) {
            return this._lastCleaned.until(targetDate, ChronoUnit.DAYS);
        }

        String id() {
            return this._id;
        }

        String title() {
            return this._catalogItem.title();
        }

        boolean hasTag(String arg) {
            return this._catalogItem.hasTag(arg);
        }

        List<String> tags() {
            return this._catalogItem.tags();
        }
    }
}
