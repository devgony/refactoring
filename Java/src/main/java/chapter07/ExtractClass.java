package chapter07;

import lombok.NoArgsConstructor;

public class ExtractClass {
    @NoArgsConstructor
    static class Person {
        String _name;
        String _officeNumber;
        TelephoneNumber _telephoneNumber;

        Person(String name, TelephoneNumber telephoneNumber) {
            this._name = name;
            this._telephoneNumber = telephoneNumber;
        }

        Person(String name, String officeAreaCode, String officeNumber) {
            this._name = name;
            this._officeNumber = officeNumber;
        }

        String name() {
            return this._name;
        }

        void name(String arg) {
            this._name = arg;
        }

        String telephoneNumber() {
            return ("(" + this._telephoneNumber._officeAreaCode + ") " + this._officeNumber);
        }

        void telephoneNumber(TelephoneNumber arg) {
            this._telephoneNumber = arg;
        }

        String officeAreaCode() {
            return this._telephoneNumber._officeAreaCode;
        }

        String officeNumber() {
            return this._officeNumber;
        }

        void officeNumber(String arg) {
            this._officeNumber = arg;
        }
    }

    static class TelephoneNumber {
        String _officeAreaCode;

        TelephoneNumber(String officeAreaCode) {
            this._officeAreaCode = officeAreaCode;
        }

        String officeAreaCode() {
            return _officeAreaCode;
        }

        void officeAreaCode(String arg) {
            this._officeAreaCode = arg;
        }

    }

}
