package Journal.Teachers;

import Journal.Groups.*;
import Journal.Journal;

public class Assistant extends TeacherThread {
    public Assistant(String name, Journal journal, Group[] groups) {
        super(name, "Lecturer", journal, groups);
    }
}
