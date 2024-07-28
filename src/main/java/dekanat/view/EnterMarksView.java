package dekanat.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.model.EnterMarksModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@PageTitle("Введення оцінок | Деканат")
@Route(value = "marks", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class EnterMarksView extends Div {
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout topLayout = new HorizontalLayout();
    private HorizontalLayout contentLayout = new HorizontalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private HorizontalLayout buttonLayout = new HorizontalLayout();
    private Select<String> selectFaculty = new Select<>();
    private Select<String> selectDepartment = new Select<>();
    private Select<String> selectSpecialty = new Select<>();
    private Select<String> selectCourse = new Select<>();
    private Select<String> selectGroup = new Select<>();
    private Select<String> selectDiscipline = new Select<>();
    private Select<String> selectControlType = new Select<>();
    private Grid<EnterMarksModel> studentGrid = new Grid<>(EnterMarksModel.class, false);

    public EnterMarksView() {
        // Form layout
        selectFaculty.setLabel("Факультет");
        selectFaculty.setItems("Транспортних та інформаційних технологій", "Інші факультети");
        selectFaculty.setWidth("100%");
        selectFaculty.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectDepartment.setLabel("Кафедра");
        selectDepartment.setItems("Кафедра інформаційних систем і технологій", "Інші кафедри");
        selectDepartment.setWidth("100%");
        selectDepartment.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectSpecialty.setLabel("Спеціальність");
        selectSpecialty.setItems("ІБК", "Інші спеціальності");
        selectSpecialty.setWidth("100%");
        selectSpecialty.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectCourse.setLabel("Курс");
        selectCourse.setItems("1", "2", "3", "4", "Всі");
        selectCourse.setWidth("100%");
        selectCourse.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectGroup.setLabel("Група");
        selectGroup.setItems("1", "2", "3", "4");
        selectGroup.setWidth("100%");
        selectGroup.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectDiscipline.setLabel("Дисципліна");
        selectDiscipline.setItems("Математичні методи дослідження операцій", "Інші дисципліни");
        selectDiscipline.setWidth("100%");
        selectDiscipline.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        selectControlType.setLabel("Вид контролю");
        selectControlType.setItems("Розрахунково-графічна робота", "Інші види контролю");
        selectControlType.setWidth("100%");
        selectControlType.getStyle().set("padding", "0px").set("margin", "0px").set("margin-bottom", "5px");

        leftLayout.add(selectFaculty, selectDepartment, selectSpecialty, selectCourse, selectGroup, selectDiscipline, selectControlType);
        leftLayout.getStyle().set("padding-top", "0px");
        leftLayout.getStyle().set("gap", "5px");
        leftLayout.getStyle().set("padding-left", "0px");

        // Buttons
        Button saveButton = new Button("Зберегти", new Icon(VaadinIcon.CLIPBOARD_CHECK));
        Button approveButton = new Button("Затвердити", new Icon(VaadinIcon.CHECK_CIRCLE));
        Button unlockButton = new Button("Розблокувати", new Icon(VaadinIcon.UNLOCK));
        Button printReportButton = new Button("Друк відомості", new Icon(VaadinIcon.PRINT));
        Button additionalReportButton = new Button("Додаткова відомість", new Icon(VaadinIcon.FILE_ADD));


        buttonLayout.add(saveButton, approveButton, unlockButton, printReportButton, additionalReportButton);

        // Student Grid
        studentGrid.addColumn(EnterMarksModel::getStudentNumber).setHeader("№").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getFullName).setHeader("ПІБ студента").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getRgr1).setHeader("РГР 1").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getRgr2).setHeader("РГР 2").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getRgr3).setHeader("РГР 3").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getRgr4).setHeader("РГР 4").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getTotalScore).setHeader("Оцінка").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::isBlocked).setHeader("Чи заблоковано").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getLastModified).setHeader("Час зміни").setAutoWidth(true);
        studentGrid.addColumn(EnterMarksModel::getUser).setHeader("Користувач").setAutoWidth(true);

        // Sample data
        List<EnterMarksModel> sampleData = Arrays.asList(
                new EnterMarksModel(1, "Іваненко Іван Іванович", 10, 9, 8, 7, 34, false, "17.08.2020", "user1"),
                new EnterMarksModel(2, "Петренко Петро Петрович", 9, 8, 7, 6, 30, true, "18.08.2020", "user2"),
                new EnterMarksModel(3, "Сидоренко Сидір Сидорович", 8, 7, 6, 5, 26, false, "20.08.2020", "user3")
        );
        studentGrid.setItems(sampleData);

        studentGrid.getStyle().set("border", "1px solid #ddd");
        studentGrid.getStyle().set("border-radius", "8px");
        studentGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        studentGrid.getStyle().set("position", "relative");

        // Align the button layout at the top
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        buttonLayout.getStyle().set("gap", "5px");

        // Add the grid to the right layout
        rightLayout.getStyle().set("padding-left", "0px");
        rightLayout.add(buttonLayout, studentGrid);
        rightLayout.setWidthFull();

        // Add the left layout and the button layout to a vertical layout
        VerticalLayout leftContainer = new VerticalLayout(leftLayout);
        leftContainer.setWidth("20%");
        leftContainer.setPadding(false);

        // Add leftContainer and rightLayout to the content layout
        contentLayout.add(leftContainer, rightLayout);
        contentLayout.setWidthFull();

        mainLayout.add(topLayout, contentLayout);
        mainLayout.setSizeFull();
        add(mainLayout);
    }
}