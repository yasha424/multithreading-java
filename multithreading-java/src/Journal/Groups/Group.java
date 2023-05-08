package Journal.Groups;

public class Group {
    private final String name;
    private final Student[] students;

    public Group(String name, int groupSize) {
        this.name = name;
        students = new Student[groupSize];
        for (int i = 0; i < groupSize; i++) {
            students[i] = new Student(this, "Student №" + i);
        }
    }

    public Student[] getStudents() {
        return this.students;
    }

    public String getName() {
        return this.name;
    }
}