package Journal;

import Journal.Groups.*;
import Journal.Teachers.*;

public class JournalExample {
    public static void main(String[] args) {

        final int countOfGroups = 3;
        final int groupSize = 10;
        final int countOfWeeks = 4;

        Group[] groups = new Group[countOfGroups];

        for (int i = 0; i < countOfGroups; i++) {
            groups[i] = new Group("Group " + i, groupSize);
        }

        Journal journal = new Journal(groups, countOfWeeks);

        Lecturer lecturer = new Lecturer("Lecturer", journal, groups);
        Assistant assistant1 = new Assistant("Assistant", journal, groups);
        Assistant assistant2 = new Assistant("Assistant", journal, groups);
        Assistant assistant3 = new Assistant("Assistant", journal, groups);

        lecturer.start();
        assistant1.start();
        assistant2.start();
        assistant3.start();

        try {
            lecturer.join();
            assistant1.join();
            assistant2.join();
            assistant3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        journal.print();
    }
}