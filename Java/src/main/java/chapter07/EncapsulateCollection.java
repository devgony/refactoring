package chapter07;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
            return new ArrayList<>(_courses);
        }

        void courses(List<Course> aList) {
            this._courses = aList;
        }

        void addCourse(Course arg) {
            this._courses.add(arg);
        }

        void removeCourse(Course arg, Function<Void, Boolean> fnIfAbsent) {
            int index = this._courses.indexOf(arg);
            if (index == -1) {
                fnIfAbsent.apply(null);
            } else {
                this._courses.remove(index);
            }
        }

        void removeCourse(Course arg) {
            int index = this._courses.indexOf(arg);
            if (index == -1) {
                throw new UnsupportedOperationException("RangeError");
            } else {
                this._courses.remove(index);
            }
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
