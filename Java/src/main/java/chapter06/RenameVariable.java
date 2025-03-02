package chapter06;

class RenameVariable {
    String tpHd = "untitled";

    String headingOne() {
        return "<h1>" + tpHd + "</h1>";
    }

    void mutateTpHd(String newTitle) {
        tpHd = newTitle;
    }

}
