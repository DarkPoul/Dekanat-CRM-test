package dekanat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "marks")
@Entity
public class MarkEntity {
    @Id
    private long id;
    private long plan;
    private long student;
    private int semester;
    private int control;
    private String mark;
    private String number;
    private String date;
    private String user;
    private boolean enabled;

    @Override
    public String toString() {
        return "MarkEntity{" +
                "id=" + id +
                ", plan=" + plan +
                ", student=" + student +
                ", semester=" + semester +
                ", control=" + control +
                ", mark='" + mark + '\'' +
                ", number='" + number + '\'' +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
