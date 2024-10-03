package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewingCardsModel {
    private String lastNameUkrField;
    private String firstNameUkrField;
    private String middleNameUkrField;
    private String lastNameEngField;
    private String firstNameEngField;
    private String groupField;
    private int  courseField;
    private String groupNumberField;
    private int  admissionYearField;
    private String recordBookNumberField;
    private String birthDate;
    private String gender;

    public ReviewingCardsModel(String lastNameUkrField, String firstNameUkrField, String middleNameUkrField,
                               String groupField, int courseField, String groupNumberField,
                               int admissionYearField, String birthDate, String gender) {
        this.lastNameUkrField = lastNameUkrField;
        this.firstNameUkrField = firstNameUkrField;
        this.middleNameUkrField = middleNameUkrField;
        this.groupField = groupField;
        this.courseField = courseField;
        this.groupNumberField = groupNumberField;
        this.admissionYearField = admissionYearField;
        this.birthDate = birthDate;
        this.gender = gender;
    }
}
