package dekanat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlansModel {
    private long planId;
    private int number;
    private String disc;
    private int hours;
    private String first;
    private String second;
    private String choice;
    private int part;
    private String department;

}
