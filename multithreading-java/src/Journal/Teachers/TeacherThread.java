package Journal.Teachers;

import Journal.Journal;
import Journal.Groups.*;

import java.util.Random;

public class TeacherThread extends Thread {
    private final String name;
    private final String type;

    private final Journal journal;
    private final Group[] groups;


    public TeacherThread(String name, String type, Journal journal, Group[] groups) {
        this.name = name;
        this.type = type;
        this.journal = journal;
        this.groups = groups;
    }

    @Override
    public void run() {
        Random random = new Random();
        for (int i = 0; i < journal.getWeeks(); i++) {
            for (Group group : groups) {
                for (Student student : group.getStudents()) {
                    journal.addGrade(student, i, random.nextInt(101));
                }
            }
        }
    }
}