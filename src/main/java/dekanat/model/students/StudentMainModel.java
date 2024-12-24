package dekanat.model.students;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentMainModel {
    private String lastNameUkr;
    private String firstNameUkr;
    private String middleNameUkr;
    private String lastNameEng;
    private String firstNameEng;
    private String group;
    private String course;
    private int groupNumber;
    private int admissionYear;
    private String recordBookNumber;
}
