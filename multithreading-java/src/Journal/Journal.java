package Journal;

import Journal.Groups.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class Journal {
    private final Map<Student, ArrayBlockingQueue<Integer>[]> grades = new HashMap<>();
    private final int weeks;

    public Journal(Group[] groups, int weeks) {
        this.weeks = weeks;
        for (Group group : groups) {
            for (Student student : group.getStudents()) {
                ArrayBlockingQueue<Integer>[] grades = new ArrayBlockingQueue[weeks];
                for (int i = 0; i < weeks; i++) {
                    grades[i] = new ArrayBlockingQueue<>(4);
                }
                this.grades.put(student, grades);
            }
        }
    }

    public void addGrade(Student student, int week, int grade) {
        if (grade < 0 || grade > 100) {
            throw new RuntimeException("Invalid grade");
        }
        grades.get(student)[week].add(grade);
    }

    public void print() {
        for (Map.Entry<Student, ArrayBlockingQueue<Integer>[]> entry : this.grades.entrySet()) {
            System.out.println(entry.getKey().group().getName() + " " + entry.getKey().name());
            for (int i = 0; i < entry.getValue().length; i++) {
                System.out.print("\tWeek №" + i + " ");
                for (int grade : entry.getValue()[i]) {
                    System.out.print(grade + " ");
                }
                System.out.println();
            }
        }
    }

    public int getWeeks() {
        return this.weeks;
    }
}