package dekanat.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

@PageTitle("Друк інформації | Деканат")
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
    private Select<String> selectSemester = new Select<>(); // Semester selection
    private Select<String> selectControl = new Select<>(); // Control type selection
    private Grid<StudentCardModel> studentGrid = new Grid<>(StudentCardModel.class, false);
    private Button evaluateAppButton = new Button("Оцінка додатку");
    private Button callStudentButton = new Button("Виклик студента");
    private Button notificationButton = new Button("Повідомлення");

    // New buttons for table actions
    private Button generalTableButton = new Button("Загальна таблиця");
    private Button summarizedTableButton = new Button("Зведена таблиця");
    private Button missingDataButton = new Button("Не введені дані");
    private Button unsatisfactoryResultsButton = new Button("Студенти з низькими результатами");

    public StudentCardView() {
        selectGroup.setLabel("Виберіть групу");
        selectGroup.setItems("ІБК-4-1-20", "ІБК-4-2-20", "ІБК-4-3-20");

        // Semester selection
        selectSemester.setLabel("Cеместр для створення звіту");
        selectSemester.setItems("1", "2");
        selectSemester.setWidth("250px");

        // Control type selection
        selectControl.setLabel("Вибір контролю");
        selectControl.setItems("Перший модульний контроль", "Другий модульний контроль");
        selectControl.setWidth("250px");

        // Configure the grid
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

        // Apply custom styling to the grid
        studentGrid.getStyle().set("border", "1px solid #ddd");
        studentGrid.getStyle().set("border-radius", "8px");
        studentGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        studentGrid.getStyle().set("padding", "20px");
        studentGrid.getStyle().set("background", "white");
        studentGrid.getStyle().set("width", "97%");
        studentGrid.addAttachListener(event -> {
            studentGrid.getElement().executeJs(
                    "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                            "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
            );
        });


// Add select and buttons to a single layout (topLayout)
        topLayout.add(selectGroup, evaluateAppButton, callStudentButton, notificationButton);
        topLayout.setSpacing(true);
        topLayout.setAlignItems(FlexComponent.Alignment.BASELINE); // Align items properly
        topLayout.setWidth("95%");
        topLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Evenly space components
        evaluateAppButton.getStyle().set("flex-grow", "1");
        callStudentButton.getStyle().set("flex-grow", "1");
        notificationButton.getStyle().set("flex-grow", "1");



        // Create a new VerticalLayout for semester and control selection, and action buttons
        VerticalLayout controlsLayout = new VerticalLayout();
        controlsLayout.add(selectSemester, selectControl, generalTableButton, summarizedTableButton, missingDataButton, unsatisfactoryResultsButton);
        controlsLayout.setSpacing(true);
        controlsLayout.setWidth("250px"); // Set a fixed width for control layout
        controlsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Add controlsLayout to rightLayout
        rightLayout.add(controlsLayout);
        rightLayout.setWidth("250px"); // Adjust as necessary
        rightLayout.getStyle().set("padding", "0px");
        rightLayout.getStyle().set("padding-top", "50px");
        rightLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Add grid and right layout (empty in this case) to the main layout
        leftLayout.add(topLayout, studentGrid);
        leftLayout.getStyle().set("padding", "0px");

        // Ensure the layout widths match the grid's width
        mainLayout.add(Layout);
        Layout.add(leftLayout, rightLayout);
        Layout.getStyle().set("gap", "0px");

        mainLayout.setWidth("100%");
        Layout.setWidth("100%");
        leftLayout.setWidth("70%");
        rightLayout.setWidth("30%");

        add(mainLayout);
        setHeight("100%");
    }
}