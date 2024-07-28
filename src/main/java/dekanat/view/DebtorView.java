package dekanat.view;


import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.model.DebtorModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

@PageTitle("Боржники | Деканат")
@Route(value = "debtor", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class DebtorView extends Div {
    private VerticalLayout mainLayout = new VerticalLayout();
    private VerticalLayout leftLayout = new VerticalLayout();
    private VerticalLayout rightLayout = new VerticalLayout();
    private HorizontalLayout selectors = new HorizontalLayout();
    private HorizontalLayout gridLayout = new HorizontalLayout();
    private HorizontalLayout countLabels1 = new HorizontalLayout();
    private HorizontalLayout countLabels2 = new HorizontalLayout();
    private Select<String> selectFaculty = new Select<>();
    private Select<String> selectCourse = new Select<>();
    private Select<String> selectGroup = new Select<>();
    private Select<String> selectGroupNumber = new Select<>();
    private Grid<DebtorModel> studentGrid = new Grid<>(DebtorModel.class, false);
    private Grid<String> reasonGrid = new Grid<>();
    private Span totalStudentsLabel = new Span();
    private Span totalStudentsInt = new Span();
    private Span arrearsCountLabel = new Span();
    private Span arrearsCountInt = new Span();

    public DebtorView() {
        selectFaculty.setLabel("Факультет");
        selectFaculty.setItems("Транспортних та інформаційних технологій", "Інші факультети");
        selectFaculty.setWidth("100%");

        selectCourse.setLabel("Курс");
        selectCourse.setItems("1", "2", "3", "4", "Всі");
        selectCourse.setWidth("100%");

        selectGroup.setLabel("Група");
        selectGroup.setItems("МП", "Інші групи");
        selectGroup.setWidth("100%");

        selectGroupNumber.setLabel("Номер групи");
        selectGroupNumber.setItems("Всі", "1", "2", "3", "4");
        selectGroupNumber.setWidth("100%");

        studentGrid.addColumn(DebtorModel::getLastName).setHeader("Прізвище").setAutoWidth(true);
        studentGrid.addColumn(DebtorModel::getFirstName).setHeader("Ім'я").setAutoWidth(true);
        studentGrid.addColumn(DebtorModel::getPatronymic).setHeader("По батькові").setAutoWidth(true);
        studentGrid.addColumn(DebtorModel::getCourseYear).setHeader("Рік Курсу").setAutoWidth(true);

        studentGrid.setItems(
                new DebtorModel("Абраменко", "Олексій", "Романович", 21),
                new DebtorModel("Костюк", "Максим", "Миколайович", 21),
                new DebtorModel("Крисько", "Віталій", "Андрійович", 21),
                new DebtorModel("Лемзяков", "Олександр", "Віталійович", 21),
                new DebtorModel("Тріц", "Дмитро", "Вікторович", 21),
                new DebtorModel("Тушенко", "Нікіта", "Миколайович", 21),
                new DebtorModel("Юрченя", "Владислав", "Юрійович", 21)
        );

        studentGrid.getStyle().set("border", "1px solid #ddd");
        studentGrid.getStyle().set("border-radius", "8px");
        studentGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        studentGrid.getStyle().set("position", "relative");

        reasonGrid.addColumn(String::toString).setHeader("Причини");
        reasonGrid.setItems(
                "Неявка на екзамен",
                "Неуспішна здача сесії",
                "Проблеми з академічною доброчесністю",
                "Інші причини"
        );

        reasonGrid.getStyle().set("border", "1px solid #ddd");
        reasonGrid.getStyle().set("border-radius", "8px");
        reasonGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        reasonGrid.getStyle().set("position", "relative");

        totalStudentsLabel.setText("Кількість студентів які мають заборгованості:");
        arrearsCountLabel.setText("Кількість заборгованостей студента Костюк М.М. [ МП-3-1(21) ]:");
        totalStudentsInt.setText("7");
        arrearsCountInt.setText("8");

        selectors.add(selectFaculty, selectCourse, selectGroup, selectGroupNumber);
        selectors.setWidth("100%");

        countLabels1.add(totalStudentsLabel, totalStudentsInt);
        countLabels2.add(arrearsCountLabel, arrearsCountInt);

        leftLayout.add(studentGrid, countLabels1);
        rightLayout.add(reasonGrid, countLabels2);
        gridLayout.add(leftLayout, rightLayout);
        mainLayout.add(selectors, gridLayout);

        gridLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        leftLayout.setHeight("100%");
        leftLayout.setWidth("100%");
        leftLayout.getStyle().set("padding", "0px");
        rightLayout.getStyle().set("padding", "0px");

        add(mainLayout);
        setHeight("100%");
    }
}
