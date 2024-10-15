package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentRatingModel {
    private String group; // Група
    private String surname; // Прізвище
    private double five; // 5, %
    private double fivePercentage; // 5, %
    private double four; // 4, %
    private double fourPercentage; // 4, %
    private double three; // 3, %
    private double threePercentage; // 3, %
    private double averageScore; // С.б.
    private int zero; // 0
}
