package chapter06;

class RenameVariable {
    String _title = "untitled";

    String headingOne() {
        return "<h1>" + title() + "</h1>";
    }

    void mutateTpHd(String newTitle) {
        setTitle(newTitle);
    }

    String title() {
        return _title;
    };

    void setTitle(String arg) {
        _title = arg;
    }

}
