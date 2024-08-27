package dekanat;

import lombok.Getter;

@Getter
public class Student {
    private final int index;
    private final String name;
    private final String studentNumber;
    private final int mark;

    public Student(int index, String name, String studentNumber, int mark) {
        this.index = index;
        this.name = name;
        this.studentNumber = studentNumber;
        this.mark = mark;
    }

}
