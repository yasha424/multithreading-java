package Journal.Teachers;

import Journal.Groups.*;
import Journal.Journal;

public class Lecturer extends TeacherThread {
    public Lecturer(String name, Journal journal, Group[] groups) {
        super(name, "Lecturer", journal, groups);
    }
}
