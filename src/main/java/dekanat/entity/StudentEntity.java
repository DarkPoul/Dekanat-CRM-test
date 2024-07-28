package dekanat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentEntity {

    @Id
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private String group;
    private String course;
    private String number;
    private String year;
    private int faculty;
    private int department;

}
