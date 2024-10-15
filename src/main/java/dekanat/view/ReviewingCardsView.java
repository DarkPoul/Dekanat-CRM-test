package dekanat.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.model.OrderModel;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Перегляд карток | Деканат")
@Route(value = "reviewingcards", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class ReviewingCardsView extends Div {
    private VerticalLayout mainLayout = new VerticalLayout();
    private HorizontalLayout leftLayout1Page = new HorizontalLayout();
    private HorizontalLayout rightLayout1Page = new HorizontalLayout();
    private HorizontalLayout selectors = new HorizontalLayout();
    private Select<String> selectStudent = new Select<>();
    private Select<String> selectGroup = new Select<>();
    private Tabs tabs = new Tabs();

    Grid<OrderModel> orderGrid = new Grid<>(OrderModel.class, false);

    // Buttons
    private Button addCardButton = new Button("Додати картку");
    private Button sendToArchiveButton = new Button("Відправити в архів");
    private Button editButton = new Button("Редагувати");
    private Button submitDataButton = new Button("Внести відомість");

    // Additional Selects and Inputs
    private Select<String> typeOfInformationSelect = new Select<>();
    private DatePicker datePicker = new DatePicker("Дата");
    private TextField numberField = new TextField("Номер");
    private Select<String> studentOrGroupSelect = new Select<>();
    public ReviewingCardsView() {
        // Setup selectors
        selectStudent.setLabel("Студент");
        selectStudent.setItems("Олексій Абраменко", "Максим Костюк", "Віталій Крисько"); // Example items
        selectStudent.setWidth("300px");
        selectStudent.getStyle().set("padding", "0");

        selectGroup.setLabel("Група");
        selectGroup.setItems("КН-4-1", "ІБК-4-1");
        selectGroup.setWidth("300px");
        selectGroup.getStyle().set("padding", "0");

        selectors.add(selectGroup, selectStudent);
        selectors.setWidth("100%");

        typeOfInformationSelect.setLabel("Тип відомості");
        typeOfInformationSelect.setItems(
                "Зарахований",
                "Відрахований",
                "Академвідпустка",
                "Поновлений",
                "Переведений на наступний курс",
                "Такий що закінчив навчання"
        );

        typeOfInformationSelect.getStyle().set("padding", "0");
        typeOfInformationSelect.setWidth("100%");

        datePicker.getStyle().set("padding", "0");
        datePicker.setWidth("100%");
        datePicker.setI18n(setLocal());

        numberField.getStyle().set("padding", "0");
        numberField.setWidth("100%");

        studentOrGroupSelect.setLabel("Тип");
        studentOrGroupSelect.setItems("Один студент", "Вся група");
        studentOrGroupSelect.setWidth("100%");
        studentOrGroupSelect.getStyle().set("padding", "0");

        submitDataButton.setWidth("100%");
        submitDataButton.getStyle().set("padding", "0");


// Create the additional controls layout
        HorizontalLayout additionalControlsLayout = new HorizontalLayout();
        additionalControlsLayout.add(typeOfInformationSelect, datePicker, numberField, studentOrGroupSelect, submitDataButton);
        additionalControlsLayout.setAlignSelf(FlexComponent.Alignment.END, submitDataButton);
        additionalControlsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        additionalControlsLayout.setWidth("100%");
        additionalControlsLayout.getStyle().set("padding", "0");

        // Button Layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(selectGroup, selectStudent, addCardButton, sendToArchiveButton, editButton);
        buttonLayout.setWidth("100%");
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("padding", "0");
        buttonLayout.setAlignItems(FlexComponent.Alignment.BASELINE);


        orderGrid.addColumn(OrderModel::getOrderNumber).setHeader("№ наказу").setWidth("20%");
        orderGrid.addColumn(OrderModel::getStatus).setHeader("Стан").setWidth("40%");
        orderGrid.addColumn(OrderModel::getDate).setHeader("Дата").setWidth("40%");

        OrderModel test = new OrderModel("12346", "В процесі", LocalDate.now().minusDays(2));
        OrderModel test1 = new OrderModel("12346", "В процесі", LocalDate.now().minusDays(2));
        OrderModel test2 = new OrderModel("12346", "В процесі", LocalDate.now().minusDays(2));
        OrderModel test3 = new OrderModel("12346", "В процесі", LocalDate.now().minusDays(2));

        orderGrid.setItems(test,test1,test2,test3);
        orderGrid.getStyle().set("border", "1px solid #ddd");
        orderGrid.getStyle().set("border-radius", "8px");
        orderGrid.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        orderGrid.getStyle().set("padding", "20px");
        orderGrid.getStyle().set("position", "relative");
        orderGrid.getStyle().set("background", "white");
        orderGrid.getStyle().set("min-height", "230px");
        orderGrid.addAttachListener(event -> {
            orderGrid.getElement().executeJs(
                    "this.shadowRoot.querySelector('#table').style.marginTop = '5px'; " +
                            "this.shadowRoot.querySelector('#table').style.marginBottom = '5px'; "
            );
        });

        Div orderGridWrapper = new Div();
        orderGridWrapper.getStyle().set("position", "relative");

        Span orderLeftTitle = new Span("Накази");
        orderLeftTitle.getStyle().set("position", "absolute");
        orderLeftTitle.getStyle().set("top", "-10px");
        orderLeftTitle.getStyle().set("left", "20px");
        orderLeftTitle.getStyle().set("background", "white");
        orderLeftTitle.getStyle().set("padding", "0 10px");
        orderLeftTitle.getStyle().set("font-weight", "bold");
        orderLeftTitle.getStyle().set("z-index", "100000");

        orderGridWrapper.add(orderLeftTitle, orderGrid);
        orderGridWrapper.getStyle().set("width", "100%");

        // Create a main layout for the left and right sections
        HorizontalLayout orderLayout = new HorizontalLayout();
        orderLayout.setWidth("100%");


// Additional Controls Layout on the right side
        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.add(typeOfInformationSelect, datePicker, numberField, studentOrGroupSelect, submitDataButton);
        rightColumn.setAlignItems(FlexComponent.Alignment.END); // Align items to the end of the column
        rightColumn.setWidth("100%"); // Adjust width as needed
        rightColumn.getStyle().set("padding", "0px");

        Div InningLayoutWrapper = new Div();
        InningLayoutWrapper.getStyle().set("border", "1px solid #ddd");
        InningLayoutWrapper.getStyle().set("border-radius", "8px");
        InningLayoutWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        InningLayoutWrapper.getStyle().set("padding", "20px");
        InningLayoutWrapper.getStyle().set("position", "relative");
        InningLayoutWrapper.getStyle().set("background", "white");
        InningLayoutWrapper.getStyle().set("width", "30%");

        Span orderTitle = new Span("Внесення");
        orderTitle.getStyle().set("position", "absolute");
        orderTitle.getStyle().set("top", "-10px");
        orderTitle.getStyle().set("left", "20px");
        orderTitle.getStyle().set("background", "white");
        orderTitle.getStyle().set("padding", "0 10px");
        orderTitle.getStyle().set("font-weight", "bold");

        InningLayoutWrapper.add(orderTitle, rightColumn);



// Add the columns to the main layout
        orderLayout.add(orderGridWrapper,InningLayoutWrapper);
        orderLayout.setSpacing(false); // Adjust spacing between columns
        orderLayout.getStyle().set("padding", "0px");
        orderLayout.getStyle().set("gap", "10px");


        // Setup tabs
        Tab mainInfoTab = new Tab("Основна Інформація");
        Tab additionalInfoTab = new Tab("Додаткова Інформація");
        Tab passportInfoTab = new Tab("Паспортна Інформація");
        Tab educationDocumentsTab = new Tab("Документи про освіту");
        tabs.add(mainInfoTab, passportInfoTab, additionalInfoTab, educationDocumentsTab);

        // Main info text fields
        TextField lastNameUkrField = new TextField("Прізвище");
        lastNameUkrField.setWidth("24%");

        TextField firstNameUkrField = new TextField("Ім'я");
        firstNameUkrField.setWidth("24%");

        TextField middleNameUkrField = new TextField("По батькові");
        middleNameUkrField.setWidth("24%");

        TextField lastNameEngField = new TextField("Прізвище (англ)");
        lastNameEngField.setWidth("24%");

        TextField firstNameEngField = new TextField("Ім'я (англ)");
        firstNameEngField.setWidth("24%");

        Select<String> groupSelect = new Select<>();
        groupSelect.setLabel("Група");
        groupSelect.setWidth("24%");
        groupSelect.setItems("Група 1", "Група 2", "Група 3"); // Додайте реальні варіанти

        Select<String> courseSelect = new Select<>();
        courseSelect.setLabel("Курс");
        courseSelect.setWidth("24%");
        courseSelect.setItems("1", "2", "3", "4", "5"); // Додайте реальні варіанти курсів

        TextField groupNumberField = new TextField("Номер групи");
        groupNumberField.setWidth("24%");
        groupNumberField.setPattern("[1-9]{1,}"); // Дозволяє тільки цифри від 1 до 9
        groupNumberField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[1-9]+")) {
                groupNumberField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                groupNumberField.setErrorMessage("Введіть цифру від 1 до 9");
                Notification.show("Неправильний ввід. Введіть тільки цифри від 1 до 9.");
            }
        });

        Select<String> admissionYearSelect = new Select<>();
        admissionYearSelect.setLabel("Рік вступу");
        admissionYearSelect.setWidth("24%");
        admissionYearSelect.setItems("2020", "2021", "2022", "2023", "2024"); // Додайте реальні варіанти років


        TextField recordBookNumberField = new TextField("Номер заліковки");
        recordBookNumberField.setWidth("24%");
        recordBookNumberField.setPattern("[0-9]{1,}"); // Дозволяє тільки цифри від 1 до 9
        recordBookNumberField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]+")) {
                recordBookNumberField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                recordBookNumberField.setErrorMessage("Введіть цифри від 0 до 9");
                Notification.show("Неправильний ввід. Введіть тільки цифри від 0 до 9.");
            }
        });

        // Add border and title to leftLayout1Page
        Div leftLayoutWrapper = new Div();
        leftLayoutWrapper.getStyle().set("border", "1px solid #ddd");
        leftLayoutWrapper.getStyle().set("border-radius", "8px");
        leftLayoutWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        leftLayoutWrapper.getStyle().set("padding", "20px");
        leftLayoutWrapper.getStyle().set("position", "relative");
        leftLayoutWrapper.getStyle().set("background", "white");

        Span leftLayoutTitle = new Span("Персональні дані");
        leftLayoutTitle.getStyle().set("position", "absolute");
        leftLayoutTitle.getStyle().set("top", "-10px");
        leftLayoutTitle.getStyle().set("left", "20px");
        leftLayoutTitle.getStyle().set("background", "white");
        leftLayoutTitle.getStyle().set("padding", "0 10px");
        leftLayoutTitle.getStyle().set("font-weight", "bold");

        leftLayoutWrapper.add(leftLayoutTitle, leftLayout1Page);

        // Add border and title to rightLayout1Page
        Div rightLayoutWrapper = new Div();
        rightLayoutWrapper.getStyle().set("border", "1px solid #ddd");
        rightLayoutWrapper.getStyle().set("border-radius", "8px");
        rightLayoutWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        rightLayoutWrapper.getStyle().set("padding", "20px");
        rightLayoutWrapper.getStyle().set("position", "relative");
        rightLayoutWrapper.getStyle().set("background", "white");

        Span rightLayoutTitle = new Span("Академічні дані");
        rightLayoutTitle.getStyle().set("position", "absolute");
        rightLayoutTitle.getStyle().set("top", "-10px");
        rightLayoutTitle.getStyle().set("left", "20px");
        rightLayoutTitle.getStyle().set("background", "white");
        rightLayoutTitle.getStyle().set("padding", "0 10px");
        rightLayoutTitle.getStyle().set("font-weight", "bold");

        rightLayoutWrapper.add(rightLayoutTitle, rightLayout1Page);

        leftLayout1Page.add(lastNameUkrField, firstNameUkrField, middleNameUkrField, lastNameEngField, firstNameEngField);
        rightLayout1Page.add(groupSelect, courseSelect, groupNumberField, admissionYearSelect, recordBookNumberField);

        // Layout for main info text fields
        VerticalLayout mainInfoLayout = new VerticalLayout();
        mainInfoLayout.setWidth("100%");
        mainInfoLayout.add(leftLayoutWrapper, rightLayoutWrapper);
        mainInfoLayout.getStyle().set("padding", "0px");
        leftLayoutWrapper.getStyle().set("width", "97%");
        rightLayoutWrapper.getStyle().set("width", "97%");

// Additional info text fields
        TextField caseNumberField = new TextField("Номер справи");
        TextField idCodeField = new TextField("Ідентифікаційний код");
        idCodeField.setPattern("[0-9]{1,}"); // Дозволяє тільки цифри
        idCodeField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]+")) {
                idCodeField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                idCodeField.setErrorMessage("Введіть тільки цифри");
                Notification.show("Неправильний ввід. Введіть тільки цифри.");
            }
        });
        TextField unzrField = new TextField("УНЗР");
        DatePicker birthDatePicker = new DatePicker("Дата народження");
        birthDatePicker.setI18n(setLocal());
        Select<String> nationalityField = new Select<>();
        nationalityField.setLabel("Національність");
        nationalityField.setItems("Україна", "Іноземець");
        Select<String> regionSelect = new Select<>();
        regionSelect.setLabel("Область");
        regionSelect.setItems(
                "Вінницька область",
                "Волинська область",
                "Дніпропетровська область",
                "Донецька область",
                "Житомирська область",
                "Закарпатська область",
                "Запорізька область",
                "Івано-Франківська область",
                "Київська область",
                "Кіровоградська область",
                "Луганська область",
                "Львівська область",
                "Миколаївська область",
                "Одеська область",
                "Полтавська область",
                "Рівненська область",
                "Сумська область",
                "Тернопільська область",
                "Харківська область",
                "Херсонська область",
                "Хмельницька область",
                "Черкаська область",
                "Чернівецька область",
                "Чернігівська область",
                "Автономна Республіка Крим",
                "м. Київ",
                "м. Севастополь"
        );
        TextField indexField = new TextField("Індекс");
        indexField.setPattern("[0-9]{1,5}"); // Дозволяет только цифры от 0 до 9, максимум 5 цифр
        indexField.setMaxLength(5); // Ограничение на 5 символов

        indexField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]{1,5}")) {
                indexField.setErrorMessage(null); // Очистить сообщение об ошибке
            } else {
                indexField.setErrorMessage("Індекс повинен містити до 5 цифр");
                Notification.show("Неправильний ввід. Введіть до 5 цифр.");
            }
        });
        TextField fullAddressField = new TextField("Повна адреса");
        TextField phoneNumberField = new TextField("Номер телефону");
        TextField emailField = new TextField("E-mail");
        MultiSelectComboBox<String> benefitsSelect = new MultiSelectComboBox<>();
        benefitsSelect.setLabel("Пільги");
        benefitsSelect.setItems("Пільга 1", "Пільга 2", "Пільга 3"); // Приклад елементів
        // Text fields for ЄДЕБО numbers
        TextField personNumberEDEBOField = new TextField("Номер фіз. особи ЄДЕБО");
        TextField studentCardNumberEDEBOField = new TextField("Номер картки здобувача ЄДЕБО");

// Set the pattern to allow only digits and enforce a minimum of 7 characters
        personNumberEDEBOField.setPattern("\\d{7,}");
        studentCardNumberEDEBOField.setPattern("\\d{7,}");

// Set error messages and add value change listeners to validate input
        personNumberEDEBOField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("\\d{7,}")) {
                personNumberEDEBOField.setErrorMessage(null);
            } else {
                personNumberEDEBOField.setErrorMessage("Введіть мінімум 7 цифр");
                Notification.show("Неправильний ввід. Введіть мінімум 7 цифр.");
            }
        });

        studentCardNumberEDEBOField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("\\d{7,}")) {
                studentCardNumberEDEBOField.setErrorMessage(null);
            } else {
                studentCardNumberEDEBOField.setErrorMessage("Введіть мінімум 7 цифр");
                Notification.show("Неправильний ввід. Введіть мінімум 7 цифр.");
            }
        });

// Add these fields to the appropriate layout
        VerticalLayout edeboFieldsLayout = new VerticalLayout();
        edeboFieldsLayout.add();
        Select<String> genderSelect = new Select<>();
        genderSelect.setLabel("Стать");
        genderSelect.setItems("Чоловіча", "Жіноча");

        TextField passportSeriesField = new TextField("Серія паспорту");
        TextField passportNumberField = new TextField("№ паспорту");
        passportNumberField.setPattern("[0-9]{1,}"); // Дозволяє тільки цифри
        passportNumberField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]+")) {
                passportNumberField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                passportNumberField.setErrorMessage("Введіть тільки цифри");
                Notification.show("Неправильний ввід. Введіть тільки цифри.");
            }
        });
        DatePicker passportIssueDatePicker = new DatePicker("Коли виданий");
        passportIssueDatePicker.setI18n(setLocal());
        TextField passportIssuedByField = new TextField("Ким виданий");
        DatePicker passportExpiryDatePicker = new DatePicker("Коли закінчиться дія паспорту");
        passportExpiryDatePicker.setI18n(setLocal());
        Select<String> educationFormSelect = new Select<>();
        educationFormSelect.setLabel("Форма навчання");
        educationFormSelect.setItems("Денна", "Заочна");

        Select<String> degreeSelect = new Select<>();
        degreeSelect.setLabel("Здобуття звання");
        degreeSelect.setItems(
                "Бакалавр",
                "Бакалавр (за скороченим строком)",
                "Спеціаліст",
                "Спеціаліст (за скороченим строком)",
                "Магістр"
        );
        Select<String> admissionConditionSelect = new Select<>();
        admissionConditionSelect.setLabel("Умови вступу");
        admissionConditionSelect.setItems("За конкурсом", "За конкурсом без стажу", "У порядку переведення", "У порядку позаконкурсного набору", "Як відмінника"); // Example items
        Select<String> paymentSourceSelect = new Select<>();
        paymentSourceSelect.setLabel("Тип особи");
        paymentSourceSelect.setItems("Фізичних осіб", "Юридичних осіб", "Держбюджет");

        TextField contractNumberField = new TextField("Договір за номером");
        contractNumberField.setPattern("[0-9]{1,}"); // Дозволяє тільки цифри від 0 до 9
        contractNumberField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]+")) {
                contractNumberField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                contractNumberField.setErrorMessage("Введіть цифри від 0 до 9");
                Notification.show("Неправильний ввід. Введіть тільки цифри від 0 до 9.");
            }
        });
        TextField amountField = new TextField("Сума");
        amountField.setPattern("[0-9]{1,}"); // Дозволяє тільки цифри від 0 до 9
        amountField.addValueChangeListener(event -> {
            String value = event.getValue();
            if (value.matches("[0-9]+")) {
                amountField.setErrorMessage(null); // Очистити повідомлення про помилку
            } else {
                amountField.setErrorMessage("Введіть цифри від 0 до 9");
                Notification.show("Неправильний ввід. Введіть тільки цифри від 0 до 9.");
            }
        });


// Group 2: Address Details
        Div addressDetailsWrapper = new Div();
        addressDetailsWrapper.getStyle().set("border", "1px solid #ddd");
        addressDetailsWrapper.getStyle().set("border-radius", "8px");
        addressDetailsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        addressDetailsWrapper.getStyle().set("padding", "20px");
        addressDetailsWrapper.getStyle().set("position", "relative");
        addressDetailsWrapper.getStyle().set("background", "white");
        addressDetailsWrapper.getStyle().set("width", "97%"); // Set the width to 97%

        Span addressDetailsTitle = new Span("Адреса");
        addressDetailsTitle.getStyle().set("position", "absolute");
        addressDetailsTitle.getStyle().set("top", "-10px");
        addressDetailsTitle.getStyle().set("left", "20px");
        addressDetailsTitle.getStyle().set("background", "white");
        addressDetailsTitle.getStyle().set("padding", "0 10px");
        addressDetailsTitle.getStyle().set("font-weight", "bold");

        FormLayout addressDetailsLayout = new FormLayout();
        addressDetailsLayout.add(regionSelect, indexField, fullAddressField);
        addressDetailsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1), // 1 column for narrow layout
                new FormLayout.ResponsiveStep("500px", 2) // 2 columns for wider layout
        );
        addressDetailsLayout.setColspan(fullAddressField, 2);

        addressDetailsWrapper.add(addressDetailsTitle, addressDetailsLayout);

// Group 3: Passport Details
        Div passportDetailsWrapper = new Div();
        passportDetailsWrapper.getStyle().set("border", "1px solid #ddd");
        passportDetailsWrapper.getStyle().set("border-radius", "8px");
        passportDetailsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        passportDetailsWrapper.getStyle().set("padding", "20px");
        passportDetailsWrapper.getStyle().set("position", "relative");
        passportDetailsWrapper.getStyle().set("background", "white");

        Span passportDetailsTitle = new Span("Паспортні дані");
        passportDetailsTitle.getStyle().set("position", "absolute");
        passportDetailsTitle.getStyle().set("top", "-10px");
        passportDetailsTitle.getStyle().set("left", "20px");
        passportDetailsTitle.getStyle().set("background", "white");
        passportDetailsTitle.getStyle().set("padding", "0 10px");
        passportDetailsTitle.getStyle().set("font-weight", "bold");

        FormLayout passportDetailsLayout = new FormLayout();
        passportDetailsLayout.add(passportSeriesField, passportNumberField, passportIssueDatePicker, passportExpiryDatePicker, passportIssuedByField,  idCodeField,unzrField, birthDatePicker, nationalityField, genderSelect,personNumberEDEBOField, studentCardNumberEDEBOField);

        passportDetailsWrapper.add(passportDetailsTitle, passportDetailsLayout);

// Group 4: Education Details
        Div educationDetailsWrapper = new Div();
        educationDetailsWrapper.getStyle().set("border", "1px solid #ddd");
        educationDetailsWrapper.getStyle().set("border-radius", "8px");
        educationDetailsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        educationDetailsWrapper.getStyle().set("padding", "20px");
        educationDetailsWrapper.getStyle().set("position", "relative");
        educationDetailsWrapper.getStyle().set("background", "white");
        educationDetailsWrapper.getStyle().set("width", "97%"); // Set the width to 97%

        Span educationDetailsTitle = new Span("Дані про навчання");
        educationDetailsTitle.getStyle().set("position", "absolute");
        educationDetailsTitle.getStyle().set("top", "-10px");
        educationDetailsTitle.getStyle().set("left", "20px");
        educationDetailsTitle.getStyle().set("background", "white");
        educationDetailsTitle.getStyle().set("padding", "0 10px");
        educationDetailsTitle.getStyle().set("font-weight", "bold");

        FormLayout educationDetailsLayout = new FormLayout();
        educationDetailsLayout.add(caseNumberField, educationFormSelect, degreeSelect, admissionConditionSelect, paymentSourceSelect, contractNumberField, amountField, benefitsSelect);

        educationDetailsWrapper.add(educationDetailsTitle, educationDetailsLayout);

        // Group 4: Education Details
        Div contactDetailsWrapper = new Div();
        contactDetailsWrapper.getStyle().set("border", "1px solid #ddd");
        contactDetailsWrapper.getStyle().set("border-radius", "8px");
        contactDetailsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        contactDetailsWrapper.getStyle().set("padding", "20px");
        contactDetailsWrapper.getStyle().set("position", "relative");
        contactDetailsWrapper.getStyle().set("background", "white");
        contactDetailsWrapper.getStyle().set("width", "97%"); // Set the width to 97%

        Span contactDetailsTitle = new Span("Контактні дані");
        contactDetailsTitle.getStyle().set("position", "absolute");
        contactDetailsTitle.getStyle().set("top", "-10px");
        contactDetailsTitle.getStyle().set("left", "20px");
        contactDetailsTitle.getStyle().set("background", "white");
        contactDetailsTitle.getStyle().set("padding", "0 10px");
        contactDetailsTitle.getStyle().set("font-weight", "bold");

        FormLayout contactDetailsLayout = new FormLayout();
        contactDetailsLayout.add(phoneNumberField, emailField);

        contactDetailsWrapper.add(contactDetailsTitle, contactDetailsLayout);

// Layout for additional info text fields
        VerticalLayout additionalInfoLayout = new VerticalLayout();
        additionalInfoLayout.setWidth("100%");
        additionalInfoLayout.add(educationDetailsWrapper,contactDetailsWrapper, addressDetailsWrapper);
        additionalInfoLayout.getStyle().set("padding", "0px");

        VerticalLayout passportInfoLayout = new VerticalLayout();
        passportInfoLayout.setWidth("100%");
        passportInfoLayout.add(passportDetailsWrapper);
        passportInfoLayout.getStyle().set("padding", "0px");

// Group 1: General Education Documents
        Div generalEducationDocumentsWrapper = new Div();
        generalEducationDocumentsWrapper.getStyle().set("border", "1px solid #ddd");
        generalEducationDocumentsWrapper.getStyle().set("border-radius", "8px");
        generalEducationDocumentsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        generalEducationDocumentsWrapper.getStyle().set("padding", "20px");
        generalEducationDocumentsWrapper.getStyle().set("position", "relative");
        generalEducationDocumentsWrapper.getStyle().set("background", "white");
        generalEducationDocumentsWrapper.getStyle().set("width", "97%"); // Set the width to 97%

        Span generalEducationDocumentsTitle = new Span("Попередня освіта");
        generalEducationDocumentsTitle.getStyle().set("position", "absolute");
        generalEducationDocumentsTitle.getStyle().set("top", "-10px");
        generalEducationDocumentsTitle.getStyle().set("left", "20px");
        generalEducationDocumentsTitle.getStyle().set("background", "white");
        generalEducationDocumentsTitle.getStyle().set("padding", "0 10px");
        generalEducationDocumentsTitle.getStyle().set("font-weight", "bold");

        TextField documentSeriesField = new TextField("Серія документу");
        TextField documentNumberField = new TextField("№ документу");
        DatePicker documentIssueDatePicker = new DatePicker("Дата видачі");
        documentIssueDatePicker.setI18n(setLocal());
        TextField institutionNameField = new TextField("Назва навчального закладу");
        TextField institutionNameEngField = new TextField("Назва навчального закладу (англ)");
        Checkbox distinctionCheckbox = new Checkbox("З відзнакою");

// Create the dropdown (select) field for document type
        Select<String> documentTypeSelect = new Select<>();
        documentTypeSelect.setLabel("Тип документу");
        documentTypeSelect.setItems("Атестат", "Диплом", "Сертифікат", "Інший");
        documentTypeSelect.setPlaceholder("Оберіть тип документу");

// Arrange the fields in a FormLayout
        FormLayout generalEducationDocumentsLayout = new FormLayout();

// Create a horizontal layout for the series, number, and date fields
        HorizontalLayout seriesNumberDateLayout = new HorizontalLayout();
        seriesNumberDateLayout.setWidthFull(); // Make the horizontal layout full width
        seriesNumberDateLayout.setSpacing(true); // Add spacing between the fields
        seriesNumberDateLayout.add(documentSeriesField, documentNumberField, documentIssueDatePicker);
        seriesNumberDateLayout.setFlexGrow(1, documentSeriesField, documentNumberField, documentIssueDatePicker); // Make each field take up equal space

// Add components to the FormLayout
        generalEducationDocumentsLayout.add(
                documentTypeSelect,
                distinctionCheckbox,
                seriesNumberDateLayout,
                institutionNameField,
                institutionNameEngField
        );

// Set responsive steps
        generalEducationDocumentsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1), // 1 column for narrow layout
                new FormLayout.ResponsiveStep("500px", 1) // 2 columns for wider layout
        );

// Set colspan for distinctionCheckbox to align it properly
        generalEducationDocumentsLayout.setColspan(distinctionCheckbox, 1);

        generalEducationDocumentsWrapper.add(generalEducationDocumentsTitle, generalEducationDocumentsLayout);

// Group 2: Diploma-Specific Fields
        Div diplomaDocumentsWrapper = new Div();
        diplomaDocumentsWrapper.getStyle().set("border", "1px solid #ddd");
        diplomaDocumentsWrapper.getStyle().set("border-radius", "8px");
        diplomaDocumentsWrapper.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.1)");
        diplomaDocumentsWrapper.getStyle().set("padding", "20px");
        diplomaDocumentsWrapper.getStyle().set("position", "relative");
        diplomaDocumentsWrapper.getStyle().set("background", "white");
        diplomaDocumentsWrapper.getStyle().set("width", "97%"); // Set the width to 97%

        Span diplomaSectionTitle = new Span("Диплом");
        diplomaSectionTitle.getStyle().set("position", "absolute");
        diplomaSectionTitle.getStyle().set("top", "-10px");
        diplomaSectionTitle.getStyle().set("left", "20px");
        diplomaSectionTitle.getStyle().set("background", "white");
        diplomaSectionTitle.getStyle().set("padding", "0 10px");
        diplomaSectionTitle.getStyle().set("font-weight", "bold");

// Add new fields for the diploma
        TextField diplomaSeriesField = new TextField("Серія диплому");
        TextField diplomaNumberField = new TextField("№ диплому");
        DatePicker graduationDatePicker = new DatePicker("Дата випуску");
        graduationDatePicker.setI18n(setLocal());
        TextField appendixNumberField = new TextField("Номер додатку");
        TextField thesisTitleUkrField = new TextField("Тема дипломної роботи (укр)");
        TextField thesisTitleEngField = new TextField("Тема дипломної роботи (англ)");

// Create a horizontal layout for the diploma series, number, and graduation date fields
        HorizontalLayout diplomaLayout = new HorizontalLayout();
        diplomaLayout.setWidthFull();
        diplomaLayout.setSpacing(true);
        diplomaLayout.add(diplomaSeriesField, diplomaNumberField, graduationDatePicker);
        diplomaLayout.setFlexGrow(1, diplomaSeriesField, diplomaNumberField, graduationDatePicker); // Equal space for fields

// Arrange diploma-specific fields in a FormLayout
        FormLayout diplomaDocumentsLayout = new FormLayout();
        diplomaDocumentsLayout.add(
                diplomaLayout,
                appendixNumberField,
                thesisTitleUkrField,
                thesisTitleEngField
        );

// Set responsive steps
        diplomaDocumentsLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1), // 1 column for narrow layout
                new FormLayout.ResponsiveStep("500px", 1) // 2 columns for wider layout
        );

        diplomaDocumentsWrapper.add(diplomaSectionTitle, diplomaDocumentsLayout);


        // Update tab selection listener to include the new tab
        tabs.addSelectedChangeListener(event -> {
            mainLayout.removeAll();
            if (tabs.getSelectedTab().equals(mainInfoTab)) {
                mainLayout.add(buttonLayout, tabs, mainInfoLayout, orderLayout);
            } else if (tabs.getSelectedTab().equals(additionalInfoTab)) {
                mainLayout.add(buttonLayout, tabs, additionalInfoLayout);
            } else if (tabs.getSelectedTab().equals(passportInfoTab)) {
                mainLayout.add(buttonLayout, tabs, passportInfoLayout);
            } else if (tabs.getSelectedTab().equals(educationDocumentsTab)) {
                mainLayout.add(buttonLayout, tabs, generalEducationDocumentsWrapper, diplomaDocumentsWrapper);
            }
        });

        mainLayout.add(buttonLayout, tabs, mainInfoLayout, orderLayout);
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(mainLayout);
        setHeight("100%");
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