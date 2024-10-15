package dekanat.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.model.StudentRatingModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Рейтинг студентів | Деканат")
@Route(value = "studentrating", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class StudentRatingView extends Div {
    private final MultiSelectComboBox<String> groupComboBox = new MultiSelectComboBox<>("Група");
    private final Button selectAllButton = new Button("Вибрати всі");
    private final Button printButton = new Button("Друк");
    private final Checkbox oneSpecialtyCheckbox = new Checkbox("Одна спеціальність");
    private final Grid<StudentRatingModel> studentsGrid = new Grid<>(StudentRatingModel.class, false);

    public StudentRatingView() {
        // Setup ComboBox with multiple selection
        groupComboBox.setItems(getAllGroups());
        groupComboBox.setAllowCustomValue(true);
        groupComboBox.setPlaceholder("Оберіть групи");
        groupComboBox.setItemLabelGenerator(String::toString);

        // Set up buttons
        selectAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        selectAllButton.addClickListener(event -> {
            // Logic for selecting all
            Notification.show("Всі групи вибрано");
        });

        printButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printButton.addClickListener(event -> {
            // Logic for printing
            Notification.show("Друк...");
        });

        // Set up the grid with columns
        studentsGrid.addColumn(StudentRatingModel::getGroup).setHeader("Група");
        studentsGrid.addColumn(StudentRatingModel::getSurname).setHeader("Прізвище");
        studentsGrid.addColumn(StudentRatingModel::getFive).setHeader("5");
        studentsGrid.addColumn(StudentRatingModel::getFivePercentage).setHeader("%");
        studentsGrid.addColumn(StudentRatingModel::getFour).setHeader("4");
        studentsGrid.addColumn(StudentRatingModel::getFourPercentage).setHeader("%");
        studentsGrid.addColumn(StudentRatingModel::getThree).setHeader("3");
        studentsGrid.addColumn(StudentRatingModel::getThreePercentage).setHeader("%");
        studentsGrid.addColumn(StudentRatingModel::getAverageScore).setHeader("С.б.");
        studentsGrid.addColumn(StudentRatingModel::getZero).setHeader("0");

        // Adding sample data to the grid
        studentsGrid.setItems(getSampleStudentData());

        // Set layout
        HorizontalLayout buttonsAndCheckboxesLayout = new HorizontalLayout(groupComboBox, selectAllButton, oneSpecialtyCheckbox, printButton);
        buttonsAndCheckboxesLayout.setAlignItems(FlexComponent.Alignment.END);
        VerticalLayout mainLayout = new VerticalLayout(buttonsAndCheckboxesLayout, studentsGrid);


        add(mainLayout);
        setHeight("100%");
    }

    // Example method to get all groups (replace with actual implementation)
    private List<String> getAllGroups() {
        return new ArrayList<>(List.of("Група 1", "Група 2", "Група 3")); // Example data
    }

    // Example method to get sample student data (replace with actual implementation)
    private List<StudentRatingModel> getSampleStudentData() {
        List<StudentRatingModel> sampleData = new ArrayList<>();
        // Add sample data to the list
        sampleData.add(new StudentRatingModel("Група 1", "Іванов", 10, 50, 5, 25, 2, 10, 3.5, 0));
        sampleData.add(new StudentRatingModel("Група 2", "Петров", 8, 40, 6, 30, 4, 20, 4.0, 1));
        sampleData.add(new StudentRatingModel("Група 3", "Сидоров", 5, 25, 7, 35, 2, 10, 2.5, 2));
        return sampleData;
    }
}
