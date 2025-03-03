package chapter06;

class RenameVariable {
    public static final String companyName = "Acme Gooseberries";
    public static final String cpyNm = companyName;

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
