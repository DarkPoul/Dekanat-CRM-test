package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCardModel {
    private String lastName;
    private String firstName;
    private String patronymic;
    private int RecordBookNumber;
}