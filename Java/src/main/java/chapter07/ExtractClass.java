package chapter07;

import lombok.NoArgsConstructor;

public class ExtractClass {
    @NoArgsConstructor
    static class Person {
        String _name;
        String _officeAreaCode;
        String _officeNumber;

        Person(String name, String officeAreaCode, String officeNumber) {
            this._name = name;
            this._officeAreaCode = officeAreaCode;
            this._officeNumber = officeNumber;
        }

        String name() {
            return this._name;
        }

        void name(String arg) {
            this._name = arg;
        }

        String telephoneNumber() {
            return ("(" + this._officeAreaCode + ") " + this._officeNumber);
        }

        String officeAreaCode() {
            return this._officeAreaCode;
        }

        void officeAreaCode(String arg) {
            this._officeAreaCode = arg;
        }

        String officeNumber() {
            return this._officeNumber;
        }

        void officeNumber(String arg) {
            this._officeNumber = arg;
        }
    }

}
