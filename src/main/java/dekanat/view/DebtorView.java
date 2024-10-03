package dekanat.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import dekanat.component.MainLayout;
import dekanat.model.DebtorModel;
import dekanat.model.DebtorReasonModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@PageTitle("Боржники | Деканат")
@Route(value = "debtor", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class DebtorView extends Div {
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout selectors = new HorizontalLayout();
    private VerticalLayout studentColumn = new VerticalLayout();
    private VerticalLayout disciplineColumn = new VerticalLayout();
    private Select<String> selectGroup = new Select<>();
    private TextField orderField = new TextField();
    private DatePicker dateField = new DatePicker();
    private Button transferButton = new Button("Переведення");
    private Grid<DebtorModel> studentGrid = new Grid<>(DebtorModel.class, false);
    private Grid<DebtorReasonModel> disciplineGrid = new Grid<>(DebtorReasonModel.class, false);

    public DebtorView() {
        // Настройка селекторов
        selectGroup.setLabel("Група");
        selectGroup.setItems("МП", "ІБК","КН","ДЗ","КІ");
        selectGroup.getStyle().set("width", "37%");
        selectGroup.getStyle().set("padding-left", "10px");

        orderField.setLabel("Наказ");

        dateField.setI18n(setLocal());
        dateField.setLabel("Дата");

        // Центрирование кнопки
        transferButton.getStyle().set("margin", "35px auto 0px");
        selectors.add(selectGroup, orderField, dateField, transferButton);
        selectors.setWidth("100%");
        selectors.setSpacing(true);
        selectors.getStyle().set("border", "1px solid #ddd");
        selectors.getStyle().set("border-radius", "8px");
        selectors.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        selectors.getStyle().set("padding", "5px");
        selectors.getStyle().set("position", "relative");
        selectors.getStyle().set("background", "white");


        studentGrid.addColumn(DebtorModel::getLastName).setHeader("Студент").setAutoWidth(true);
        studentGrid.addColumn(DebtorModel::getCourseYear).setHeader("Готовий").setWidth("90px").setFlexGrow(0);;


        studentGrid.getStyle().set("border", "1px solid #ddd");
        studentGrid.getStyle().set("border-radius", "8px");
        studentGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        studentGrid.getStyle().set("position", "relative");
        studentGrid.getStyle().set("width", "100%");

        studentGrid.addAttachListener(event -> {
            studentGrid.getElement().executeJs(
                    "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                            "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
            );
        });

        // Добавление данных в таблицу студентов
        studentGrid.setItems(
                new DebtorModel("Іванов", "qwe", "qwe", 1),
                new DebtorModel("Петров","qwe", "qwe", 0),
                new DebtorModel("Сидоров","qwe", "qwe", 1),
                new DebtorModel("Коваленко","qwe", "qwe", 0),
                new DebtorModel("Шевченко","qwe", "qwe", 1),
                new DebtorModel("Мельник","qwe", "qwe", 0),
                new DebtorModel("Кравченко","qwe", "qwe", 1),
                new DebtorModel("Бондаренко","qwe", "qwe", 0),
                new DebtorModel("Гончар","qwe", "qwe", 1),
                new DebtorModel("Дорошенко","qwe", "qwe", 0)
        );

        // Добавляем таблицу студентов в левую колонку
        studentColumn.add(studentGrid);
        studentColumn.getStyle().set("padding", "0px");
        studentColumn.setWidth("37%"); // Ширина колонки с таблицей студентов

        // Настройка таблицы дисциплин
        disciplineGrid.addColumn(DebtorReasonModel::getCode).setHeader("Семестр").setWidth("100px").setFlexGrow(0);
        disciplineGrid.addColumn(DebtorReasonModel::getName).setHeader("Дисципліна").setAutoWidth(true);
        disciplineGrid.addColumn(DebtorReasonModel::getReason).setHeader("Причини").setAutoWidth(true);

        disciplineGrid.getStyle().set("border", "1px solid #ddd");
        disciplineGrid.getStyle().set("border-radius", "8px");
        disciplineGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        disciplineGrid.getStyle().set("position", "relative");
        disciplineGrid.getStyle().set("width", "100%");

        disciplineGrid.addAttachListener(event -> {
            disciplineGrid.getElement().executeJs(
                    "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                            "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
            );
        });

        disciplineGrid.setItems(
                new DebtorReasonModel(1, "Математика", "Модуль"),
                new DebtorReasonModel(2, "Фізика", "РГР"),
                new DebtorReasonModel(3, "Інформатика", "Курсовая"),
                new DebtorReasonModel(4, "Хімія", "Модуль"),
                new DebtorReasonModel(5, "Біологія", "РГР"),
                new DebtorReasonModel(6, "Історія", "Курсовая"),
                new DebtorReasonModel(7, "Географія", "Модуль"),
                new DebtorReasonModel(1, "Література", "РГР"),
                new DebtorReasonModel(2, "Економіка", "Курсовая"),
                new DebtorReasonModel(3, "Філософія", "Модуль")
        );

        // Добавляем таблицу дисциплин в правую колонку
        disciplineColumn.add(disciplineGrid);
        disciplineColumn.getStyle().set("padding", "0px");
        disciplineColumn.setWidth("62%"); // Ширина колонки с таблицей дисциплин

        // Добавляем элементы на основную страницу
        HorizontalLayout contentLayout = new HorizontalLayout(studentColumn, disciplineColumn);
        contentLayout.getStyle().set("border", "1px solid #ddd");
        contentLayout.getStyle().set("border-radius", "8px");
        contentLayout.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        contentLayout.getStyle().set("padding", "10px");
        contentLayout.getStyle().set("position", "relative");
        contentLayout.getStyle().set("background", "white");
        contentLayout.getStyle().set("border-top-width", "0px");
        contentLayout.setWidthFull(); // Занять всю ширину
        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        contentLayout.setWidth("100%");
        contentLayout.setHeight("600px"); // Установите необходимую высоту

        mainLayout.add(selectors, contentLayout);
        mainLayout.setSizeFull();
        mainLayout.setSizeFull();
        mainLayout.getStyle().set("gap", "0px");
        add(mainLayout);
    }

    private DatePicker.DatePickerI18n setLocal() {
        DatePicker.DatePickerI18n ukrainian = new DatePicker.DatePickerI18n();
        ukrainian.setMonthNames(List.of("Січень", "Лютий", "Березень", "Квітень",
                "Травень", "Червень", "Липень", "Серпень", "Вересень", "Жовтень",
                "Листопад", "Грудень"));
        ukrainian.setWeekdays(List.of("Неділя", "Понеділок", "Вівторок",
                "Середа", "Четвер", "П'ятниця", "Субота"));
        ukrainian.setWeekdaysShort(
                List.of("Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"));
        ukrainian.setToday("Сьогодні");
        ukrainian.setCancel("Скасувати");

        return ukrainian;
    }
}