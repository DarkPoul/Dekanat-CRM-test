package dekanat.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.model.StudentModel;
import dekanat.model.SuccessModel;
import dekanat.service.StudentService;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Успішність | Деканат")
@Route(value = "success", layout = MainLayout.class)
@Component
@PermitAll
@UIScope
public class SuccessView extends Div {
    private HorizontalLayout mainLayout = new HorizontalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private HorizontalLayout synchronizationLayout = new HorizontalLayout();
    private Select<String> selectGroup = new Select<>();
    private Select<String> selectFirstStudent = new Select<>();
    private Select<String> selectSecondStudent = new Select<>();
    private Button buttonSynchronization = new Button("Перенести");
    private Button buttonEditHours = new Button("Редагувати години");
    private Button buttonEditDiscipline = new Button("Редагувати дисципліни");
    private ListBox<String> listStudents = new ListBox<>();
    private Grid<SuccessModel> marks = new Grid<>(SuccessModel.class, false);

    private final StudentService studentService;

    public SuccessView(StudentService studentService) {
        this.studentService = studentService;

        List<StudentModel> studentEntities = new ArrayList<>();

        selectGroup.setLabel("Оберіть групу");
        selectGroup.setItems(studentService.getAllGroups());

        selectGroup.addValueChangeListener(event -> {
            studentEntities.clear();

            studentEntities.addAll(studentService.getStudents(selectGroup.getValue()));
            List<String> student = new ArrayList<>();
            for (StudentModel students: studentEntities){
                student.add(students.getSurname() + " " + students.getName() + " " + students.getPatronymic());
            }

            listStudents.setItems(student);

            SuccessModel test = new SuccessModel("Математика", 1, "160", "90", "", "", "", "90", "90", "90");
            SuccessModel test1 = new SuccessModel("Фізика", 2, "160", "", "95", "", "", "", "90", "90");
            SuccessModel test2 = new SuccessModel("Хімія", 3, "160", "90", "", "", "", "90", "90", "90");
            SuccessModel test3 = new SuccessModel("Англійська мова", 4, "160", "", "", "", "", "90", "90", "90");
            SuccessModel test4 = new SuccessModel("Українська мова", 5, "160", "", "", "", "95", "90", "90", "90");
            SuccessModel test5 = new SuccessModel("Історія України", 6, "160", "", "", "", "", "90", "90", "90");

            marks.setItems(test, test1, test2, test3, test4, test5);


            selectFirstStudent.setItems(student);
            selectSecondStudent.setItems(student);
        });

        selectFirstStudent.setLabel("Перенести з");
        selectSecondStudent.setLabel("Перенести для");
//        listStudents.setItems("Пупков", "Непупков");
        listStudents.getStyle().set("border", "1px solid #ddd");
        listStudents.getStyle().set("border-radius", "8px");
        listStudents.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        listStudents.getStyle().set("padding", "20px");
        listStudents.getStyle().set("position", "relative");
        listStudents.getStyle().set("background", "white");
        listStudents.setWidth("100%");
        listStudents.setHeight("90%");

        marks.addColumn(SuccessModel::getDiscipline).setHeader("Дисципліна").setAutoWidth(true);
        marks.addColumn(SuccessModel::getSemester).setHeader("Семестр").setAutoWidth(true);
        marks.addColumn(SuccessModel::getHours).setHeader("К. годин").setAutoWidth(true);
        marks.addColumn(SuccessModel::getTest).setHeader("Залік").setAutoWidth(true);
        marks.addColumn(SuccessModel::getExam).setHeader("Екзамен").setAutoWidth(true);
        marks.addColumn(SuccessModel::getD_exam).setHeader("Диферен. Е.").setAutoWidth(true);
        marks.addColumn(SuccessModel::getKp).setHeader("Курс. п.").setAutoWidth(true);
        marks.addColumn(SuccessModel::getKr).setHeader("Курс. р.").setAutoWidth(true);
        marks.addColumn(SuccessModel::getRr).setHeader("РР").setAutoWidth(true);
        marks.addColumn(SuccessModel::getRgr).setHeader("РГР").setAutoWidth(true);


        marks.getStyle().set("border", "1px solid #ddd");
        marks.getStyle().set("border-radius", "8px");
        marks.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        marks.getStyle().set("padding", "20px");
        marks.getStyle().set("position", "relative");
        marks.getStyle().set("background", "white");
        marks.getElement().executeJs(
                "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                        "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
        );

        leftLayout.add(selectGroup, listStudents);
        synchronizationLayout.add(selectFirstStudent, selectSecondStudent, buttonSynchronization, buttonEditHours, buttonEditDiscipline);
        synchronizationLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END); // Вирівнювання елементів вертикально
        rightLayout.add(synchronizationLayout, marks);
        mainLayout.add(leftLayout, rightLayout);
        mainLayout.setHeight("100%");
        leftLayout.setHeight("100%");
        leftLayout.setWidth("30%");

        add(mainLayout);
        setHeight("100%");

    }
}
