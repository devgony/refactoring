package chapter07;

import lombok.NoArgsConstructor;

public class ExtractClass {
    @NoArgsConstructor
    static class Person {
        String _name;
        TelephoneNumber _telephoneNumber;

        Person(String name, TelephoneNumber telephoneNumber) {
            this._name = name;
            this._telephoneNumber = telephoneNumber;
        }

        Person(String name, String officeAreaCode, String officeNumber) {
            this._name = name;
            this._telephoneNumber._number = officeNumber;
        }

        String name() {
            return this._name;
        }

        void name(String arg) {
            this._name = arg;
        }

        String telephoneNumber() {
            return this._telephoneNumber.telephoneNumber();
        }

        void telephoneNumber(TelephoneNumber arg) {
            this._telephoneNumber = arg;
        }

        String officeAreaCode() {
            return this._telephoneNumber._areaCode;
        }

        String officeNumber() {
            return this._telephoneNumber._number;
        }

        void officeNumber(String arg) {
            this._telephoneNumber._number = arg;
        }
    }

    static class TelephoneNumber {
        String _areaCode;
        String _number;

        TelephoneNumber(String areaCode, String number) {
            this._areaCode = areaCode;
            this._number = number;
        }

        String officeAreaCode() {
            return _areaCode;
        }

        void officeAreaCode(String arg) {
            this._areaCode = arg;
        }

        String officeNumber() {
            return _number;
        }

        void officeNumber(String arg) {
            this._number = arg;
        }

        String telephoneNumber() {
            return ("(" + this._areaCode + ") " + this._number);
        }

    }

}
