package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentModel {
    private long id;
    private String name;
    private String surname;
    private String patronymic;

    public String getStudent(){
        return (name + " " + surname + " " + patronymic);
    }

}
