package chapter09;

class ChangeReferenceToValue {
    static class Person {
        TelephoneNumber _telephoneNumber;

        Person() {
            this._telephoneNumber = new TelephoneNumber();
        }

        String officeAreaCode() {
            return this._telephoneNumber.areaCode();
        }

        void officeAreaCode(String arg) {
            this._telephoneNumber.areaCode(arg);
        }

        String officeNumber() {
            return this._telephoneNumber.number();
        }

        void officeNumber(String arg) {
            this._telephoneNumber.number(arg);
        }
    }

    static class TelephoneNumber {
        String _number;
        String _areaCode;

        String areaCode() {
            return this._areaCode;
        }

        void areaCode(String arg) {
            this._areaCode = arg;
        }

        String number() {
            return this._number;
        }

        void number(String arg) {
            this._number = arg;
        }
    }
}
