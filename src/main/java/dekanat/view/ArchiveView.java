package dekanat.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.model.ReviewingCardsModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Архів | Деканат")
@Route(value = "archive", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class ArchiveView extends Div {

    // Таблиця
    private Grid<ReviewingCardsModel> grid = new Grid<>(ReviewingCardsModel.class, false);
    private GridListDataView<ReviewingCardsModel> dataView;

    // Переменные для хранения фильтров
    private String lastNameFilter = "";
    private String firstNameFilter = "";
    private String middleNameFilter = "";
    private String specialtyFilter = "";
    private String courseFilter = "";
    private String groupFilter = "";
    private String yearFilter = "";
    private String birthDateFilter = "";
    private String genderFilter = "";


    public ArchiveView() {
// Поле для вводу № наказу
        TextField orderNumberField = new TextField("№ Наказу");

// Кнопки
        Button refreshFromArchiveButton = new Button("Поновити з архіву");
        Button academicCertificateButton = new Button("Академічна довідка");
        Button printDiplomaButton = new Button("Друк диплому");
        Button printAppendixButton = new Button("Друк додатку");

// Створення горизонтального макета для полів та кнопок
        HorizontalLayout toolbar = new HorizontalLayout(orderNumberField,
                refreshFromArchiveButton, academicCertificateButton,
                printDiplomaButton, printAppendixButton);
        orderNumberField.getStyle().set("padding", "0px 0px 0px 16px");


// Выравнивание по центру по вертикали
        toolbar.setAlignItems(FlexComponent.Alignment.END);
        toolbar.getStyle().set("padding", "16px 0px 0px 0px");


        // Додавання макету в основну панель
        add(toolbar);

        // Настройка колонок с фильтрами
        grid.addColumn(ReviewingCardsModel::getLastNameUkrField)
                .setHeader(createHeaderWithFilter("Прізвище", value -> {
                    lastNameFilter = value;
                    applyFilters();
                }))
                .setKey("lastName")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getFirstNameUkrField)
                .setHeader(createHeaderWithFilter("Ім'я", value -> {
                    firstNameFilter = value;
                    applyFilters();
                }))
                .setKey("firstName")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getMiddleNameUkrField)
                .setHeader(createHeaderWithFilter("По батькові", value -> {
                    middleNameFilter = value;
                    applyFilters();
                }))
                .setKey("middleName")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getGroupField)
                .setHeader(createHeaderWithFilter("Спеціальність", value -> {
                    specialtyFilter = value;
                    applyFilters();
                }))
                .setKey("specialty")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getCourseField)
                .setHeader(createHeaderWithFilter("Курс", value -> {
                    courseFilter = value;
                    applyFilters();
                }))
                .setKey("course")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getGroupNumberField)
                .setHeader(createHeaderWithFilter("Група", value -> {
                    groupFilter = value;
                    applyFilters();
                }))
                .setKey("group")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getAdmissionYearField)
                .setHeader(createHeaderWithFilter("Рік", value -> {
                    yearFilter = value;
                    applyFilters();
                }))
                .setKey("year")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getBirthDate)
                .setHeader(createHeaderWithFilter("День народження", value -> {
                    birthDateFilter = value;
                    applyFilters();
                }))
                .setKey("birthDate")
                .setSortable(true);

        grid.addColumn(ReviewingCardsModel::getGender)
                .setHeader(createHeaderWithFilter("Стать", value -> {
                    genderFilter = value;
                    applyFilters();
                }))
                .setKey("gender")
                .setSortable(true);

        grid.getStyle().set("border", "1px solid #ddd");
        grid.getStyle().set("border-radius", "8px");
        grid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        grid.getStyle().set("margin", "16px");
        grid.getStyle().set("position", "relative");
        grid.getStyle().set("background", "white");
        grid.getStyle().set("min-height", "530px");
        grid.addAttachListener(event -> {
            grid.getElement().executeJs(
                    "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                            "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
            );
        });
        grid.getStyle().set("height", "70vh");
        // Додавання даних в таблицю з прикладом
        List<ReviewingCardsModel> sampleData = new ArrayList<>();
        sampleData.add(new ReviewingCardsModel("Іванов", "Іван", "Іванович", "Комп'ютерні науки", 1, "КН-101", 2021, "2000-01-01", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Петренко", "Петро", "Петрович", "Математика", 2, "М-202", 2020, "1999-02-02", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Сидоренко", "Оксана", "Сидорівна", "Фізика", 3, "Ф-303", 2019, "1998-03-03", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Коваленко", "Марія", "Ковалівна", "Хімія", 1, "Х-404", 2022, "2001-04-04", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Бондаренко", "Анатолій", "Володимирович", "Економіка", 2, "Е-505", 2020, "1999-05-05", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Дорошенко", "Світлана", "Анатоліївна", "Біологія", 3, "Б-606", 2021, "2002-06-06", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Кравченко", "Юрій", "Олександрович", "Інформатика", 1, "І-707", 2021, "2000-07-07", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Гончар", "Наталія", "Володимирівна", "Географія", 4, "Г-808", 2019, "1997-08-08", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Шевченко", "Тарас", "Григорович", "Література", 2, "Л-909", 2020, "1998-09-09", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Левченко", "Олена", "Іванівна", "Історія", 3, "Іст-1010", 2021, "1999-10-10", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Зайцева", "Роман", "Сергійович", "Філософія", 1, "Філ-1111", 2022, "2000-11-11", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Мельник", "Анна", "Вікторівна", "Психологія", 5, "Пс-1212", 2019, "1998-12-12", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Семененко", "Костянтин", "Васильович", "Соціологія", 6, "С-1313", 2020, "1997-01-01", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Крючко", "Марина", "Станіславівна", "Правознавство", 7, "П-1414", 2021, "1996-02-02", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Пащенко", "Дмитро", "Анатолійович", "Фінансовий облік", 2, "ФО-1515", 2020, "1995-03-03", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Федоренко", "Ольга", "Олегівна", "Менеджмент", 1, "М-1616", 2019, "1994-04-04", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Демченко", "Віталій", "Петрович", "Маркетинг", 3, "Мк-1717", 2022, "1993-05-05", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Білоус", "Яна", "Сергіївна", "Англійська мова", 4, "А-1818", 2020, "1992-06-06", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Тимошенко", "Олександр", "Володимирович", "Німецька мова", 5, "Н-1919", 2021, "1991-07-07", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Гавриленко", "Ірина", "Іванівна", "Французька мова", 2, "Фр-2020", 2022, "1990-08-08", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Костенко", "Сергій", "Миколайович", "Російська мова", 3, "Р-2121", 2019, "1989-09-09", "Чоловіча"));
        sampleData.add(new ReviewingCardsModel("Майстренко", "Алла", "Ігорівна", "Музика", 4, "Муз-2222", 2020, "1988-10-10", "Жіноча"));
        sampleData.add(new ReviewingCardsModel("Луговий", "Тетяна", "Станіславівна", "Образотворче мистецтво", 5, "Ом-2323", 2021, "1987-11-11", "Жіноча"));

        // Установка данных в таблицю
        dataView = grid.setItems(sampleData);

        // Додавання таблиці в основну панель
        add(grid);
    }

    private void applyFilters() {
        dataView.setFilter(item ->
                matches(item.getLastNameUkrField(), lastNameFilter) &&
                        matches(item.getFirstNameUkrField(), firstNameFilter) &&
                        matches(item.getMiddleNameUkrField(), middleNameFilter) &&
                        matches(item.getGroupField(), specialtyFilter) &&
                        matches(String.valueOf(item.getCourseField()), courseFilter) &&
                        matches(item.getGroupNumberField(), groupFilter) &&
                        matches(String.valueOf(item.getAdmissionYearField()), yearFilter) &&
                        matches(item.getBirthDate().toString(), birthDateFilter) &&
                        matches(item.getGender(), genderFilter)
        );
    }

    private static VerticalLayout createHeaderWithFilter(String headerText, Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder("Фільтр");
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));

        NativeLabel headerLabel = new NativeLabel(headerText);
        VerticalLayout layout = new VerticalLayout(headerLabel, textField);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        return layout;
    }

    private static boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty() || value.toLowerCase().contains(searchTerm.toLowerCase());
    }
}