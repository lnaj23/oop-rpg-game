package ulb.models;

public class Student {
    private int age;

    public Student(int age) {
        this.age = age;
    }

    public boolean isAdult() {
        return this.age >= 18;
    }

    public boolean isNotAdul(){return this.age < 18;}
}
