package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnterMarksModel {
    private int StudentNumber;
    private String FullName;
    private int Rgr1;
    private int Rgr2;
    private int Rgr3;
    private int Rgr4;
    private int TotalScore;
    private boolean isBlocked;
    private String LastModified;
    private String User;
}
