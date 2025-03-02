package chapter06;

class RenameVariable {
    String tpHd = "untitled";

    String headingOne() {
        return "<h1>" + title() + "</h1>";
    }

    void mutateTpHd(String newTitle) {
        setTitle(newTitle);
    }

    String title() {
        return tpHd;
    };

    void setTitle(String arg) {
        tpHd = arg;
    }

}
