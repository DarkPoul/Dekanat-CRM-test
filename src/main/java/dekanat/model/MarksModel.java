package dekanat.model;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.Icon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarksModel {

    private long id;
    private long planId;
    private String PIB;

    private String mark;
    private String markFirstModule;
    private String markSum;
    private String nationalScale;
    private String ECTS;

    private String part1;
    private String part2;
    private String part3;
    private String part4;
    private String part5;
    private String part6;
    private String part7;
    private String part8;


    private Icon enabled;
    private String date;
    private String user;

    public MarksModel(int id, String PIB, String mark, Icon enabled, String date, String user){
        this.id = id;
        this.PIB = PIB;
        this.mark = mark;
        this.enabled = enabled;
        this.date = date;
        this.user = user;
    }

    public MarksModel(int id, String PIB, String mark, String markSum, String nationalScale, String ECTS, Icon enabled, String date, String user){
        this.id = id;
        this.PIB = PIB;
        this.mark = mark;
        this.markSum = markSum;
        this.nationalScale = nationalScale;
        this.ECTS = ECTS;
        this.enabled = enabled;
        this.date = date;
        this.user = user;
    }

    @Override
    public String toString() {
        return "MarksModel{" +
                "id=" + id + "\n" +
                ", planId=" + planId + "\n" +
                ", PIB='" + PIB + "\n" +
                ", mark='" + mark + "\n" +
                ", markFirstModule='" + markFirstModule + "\n" +
                ", markSum='" + markSum + "\n" +
                ", nationalScale='" + nationalScale + "\n" +
                ", ECTS='" + ECTS + "\n" +
                ", part1='" + part1 + "\n" +
                ", part2='" + part2 + "\n" +
                ", part3='" + part3 + "\n" +
                ", part4='" + part4 + "\n" +
                ", part5='" + part5 + "\n" +
                ", part6='" + part6 + "\n" +
                ", part7='" + part7 + "\n" +
                ", part8='" + part8 + "\n" +
                ", enabled=" + enabled + "\n" +
                ", date='" + date + "\n" +
                ", user='" + user + "\n" +
                '}';
    }
}
