package chapter07;

import java.util.ArrayList;
import java.util.List;

class EncapsulateCollection {
    static class Person {
        String _name;
        List<Course> _courses;

        Person(String name) {
            this._name = name;
            this._courses = new ArrayList<>();
        }

        String name() {
            return this._name;
        }

        List<Course> courses() {
            return this._courses;
        }

        void courses(List<Course> aList) {
            this._courses = aList;
        }
    }

    static class Course {
        String _name;
        boolean _isAdvanced;

        Course(String name, boolean isAdvanced) {
            this._name = name;
            this._isAdvanced = isAdvanced;
        }

        String name() {
            return this._name;
        }

        boolean isAdvanced() {
            return this._isAdvanced;
        }
    }
}
