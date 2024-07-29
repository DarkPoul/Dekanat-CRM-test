package dekanat.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.model.StudentCardModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

@PageTitle("Перегляд інформації | Деканат")
@Route(value = "group", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class StudentCardView extends Div {
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout topLayout = new HorizontalLayout();
    private HorizontalLayout Layout = new HorizontalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private Select<String> selectGroup = new Select<>();
    private Grid<StudentCardModel> studentGrid = new Grid<>(StudentCardModel.class, false);
    private Button evaluateAppButton = new Button("Оцінка додатку");

    public StudentCardView() {
        selectGroup.setLabel("Виберіть групу");
        selectGroup.setItems("ІБК-4-1-20", "ІБК-4-2-20", "ІБК-4-3-20");
        selectGroup.setWidth("250px"); // Set a fixed width

        studentGrid.addColumn(StudentCardModel::getLastName).setHeader("Прізвище").setAutoWidth(true);
        studentGrid.addColumn(StudentCardModel::getFirstName).setHeader("Ім'я").setAutoWidth(true);
        studentGrid.addColumn(StudentCardModel::getPatronymic).setHeader("По батькові").setAutoWidth(true);
        studentGrid.addColumn(StudentCardModel::getRecordBookNumber).setHeader("№ Залікової книжки").setAutoWidth(true);

        studentGrid.setItems(
                new StudentCardModel("Абдулла", "Олександра", "Віталіївна", 40258),
                new StudentCardModel("Аль-Хуссейн", "Данііл", "Турчи", 40259),
                new StudentCardModel("Андрющко", "Олександр", "Анатолійович", 40260),
                new StudentCardModel("Бутова", "Марія", "Олексіївна", 40261),
                new StudentCardModel("Кондратенко", "Сергій", "Сергійович", 40263),
                new StudentCardModel("Костяков", "Владислав", "Володимирович", 49223),
                new StudentCardModel("Криницький", "Микола", "Георгійович", 40264)
        );

        studentGrid.getStyle().set("border", "1px solid #ddd");
        studentGrid.getStyle().set("border-radius", "8px");
        studentGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        studentGrid.getStyle().set("padding", "20px");
        studentGrid.getStyle().set("position", "relative");
        studentGrid.getStyle().set("background", "white");
        studentGrid.getStyle().set("width", "97%"); // Set the width to 97%

        topLayout.add(selectGroup);
        topLayout.setWidth("100%");
        leftLayout.add(studentGrid);
        rightLayout.add(evaluateAppButton);
        mainLayout.add(topLayout, Layout );
        Layout.add(leftLayout, rightLayout);

        mainLayout.setWidth("100%");
        Layout.setWidth("100%");
        leftLayout.setWidth("70%");
        rightLayout.setWidth("30%");

        add(mainLayout);
        setHeight("100%");
    }
}
