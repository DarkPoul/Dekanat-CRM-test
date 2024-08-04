package dekanat.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "`group`", nullable = false)
    private String group;

    @Column(name = "course", nullable = false)
    private String course;

    @Column(name = "`number`", nullable = false)
    private String number;

    @Column(name = "year", nullable = false)
    private String year;

    @Column(name = "faculty", nullable = false)
    private int faculty;


}
