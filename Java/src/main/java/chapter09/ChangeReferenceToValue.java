package chapter09;

import lombok.AllArgsConstructor;

class ChangeReferenceToValue {
    static class Person {
        TelephoneNumber _telephoneNumber;

        Person(String number, String areaCode) {
            this._telephoneNumber = new TelephoneNumber(number, areaCode);
        }

        String officeNumber() {
            return this._telephoneNumber.number();
        }

        void officeNumber(String arg) {
            this._telephoneNumber = new TelephoneNumber(arg, this._telephoneNumber.areaCode());
        }

        String officeAreaCode() {
            return this._telephoneNumber.areaCode();
        }

        void officeAreaCode(String arg) {
            this._telephoneNumber = new TelephoneNumber(this._telephoneNumber.number(), arg);
        }

    }

    @AllArgsConstructor
    static class TelephoneNumber {
        String _number;
        String _areaCode;

        String areaCode() {
            return this._areaCode;
        }

        String number() {
            return this._number;
        }
    }
}
