package dekanat.view;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.security.AuthenticationContext;
import dekanat.component.MainLayout;
import dekanat.entity.*;

import dekanat.generate.DataModelForMC1;
import dekanat.generate.DocxUpdater;
import dekanat.generate.StudentModelToDocumentGenerate;
import dekanat.model.MarksModel;
import dekanat.model.PlansModel;
import dekanat.repository.ControlRepo;
import dekanat.repository.FacultyRepo;
import dekanat.repository.MarkRepo;
import dekanat.repository.SessionRepo;
import dekanat.service.*;
import jakarta.annotation.security.PermitAll;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;


@PageTitle("Введення оцінок | Деканат")
@Route(value = "marks", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class EnterMarksView extends Div {

    private final FacultyRepo facultyRepo;
    private final DepartmentService departmentService;
    private final StudentService studentService;
    private final PlansServices plansServices;
    private final SessionRepo sessionRepo;
    private final DiscService discService;
    private final MarkRepo markRepo;
    private final ControlRepo controlRepo;
    private final CustomUserDetailsService customUserDetailsService;

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
    private Grid<MarksModel> studentGrid = new Grid<>(MarksModel.class, false);
    private List<PlansModel> plansModels = new ArrayList<>();
    private final SecurityService securityService;
    private final MarkServices markServices;

    List<String> controls = new ArrayList<>();

    public EnterMarksView(FacultyRepo facultyRepo, DepartmentService departmentService, StudentService studentService, PlansServices plansServices, SessionRepo sessionRepo, DiscService discService, MarkRepo markRepo, ControlRepo controlRepo, CustomUserDetailsService customUserDetailsService, SecurityService securityService, AuthenticationContext authenticationContext, MarkServices markServices) {
        this.facultyRepo = facultyRepo;
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.plansServices = plansServices;
        this.sessionRepo = sessionRepo;
        this.discService = discService;
        this.markRepo = markRepo;
        this.controlRepo = controlRepo;
        this.customUserDetailsService = customUserDetailsService;
        this.securityService = securityService;
        this.markServices = markServices;
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


        // Sample data






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



        //Backend



        String Role = authenticationContext.getGrantedRoles().stream().findFirst().get().split("_")[0];

        selectFaculty.setItems(facultyRepo.findAll().stream().map(FacultyEntity::getTitle).toList());
        selectDepartment.setItems(departmentService.getAllDepartment());

        switch (Role){
            case "ADMIN":
                System.out.println("ADMIN");
                break;
            case "DEKANAT":
                FacultyEntity facultyEntity = facultyRepo.findByAbr(authenticationContext.getGrantedRoles().stream().findFirst().get());
                selectFaculty.setValue(facultyEntity.getTitle());
                System.out.println(facultyEntity.getId());
                selectFaculty.setReadOnly(true);

                selectDepartment.addValueChangeListener(event -> {
                    selectSpecialty.setItems(plansServices.getGroupTitle(selectFaculty.getValue(), selectDepartment.getValue()));
                    selectSpecialty.setValue(null);
                });

                break;
            case "KAFEDRA":
                selectDepartment.setValue(departmentService.getDepartmentRole(authenticationContext.getGrantedRoles().stream().findFirst().get().split("_")[1]));
                selectDepartment.setReadOnly(true);
                System.out.println("KAFEDRA");
                selectFaculty.addValueChangeListener(event->{
                   selectSpecialty.setItems(plansServices.getGroupTitle(selectFaculty.getValue(), selectDepartment.getValue()));
                   selectSpecialty.setValue(null);
                });
                break;
        }


        selectSpecialty.addValueChangeListener(event -> {
            if (selectSpecialty.getValue() != null){
                selectCourse.setItems(plansServices.getCourse(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue()));
            } else selectCourse.setValue(null);
        });

        selectCourse.addValueChangeListener(event -> {
            if (selectCourse.getValue() != null){
                selectGroup.setItems(plansServices.getGroupNumber(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue(), selectCourse.getValue().split(" ")[0]));
            } else selectGroup.setValue(null);
        });

        selectGroup.addValueChangeListener(event -> {
            if (selectGroup.getValue() != null){
                selectDiscipline.setItems(plansServices.getDiscipline(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue(), selectCourse.getValue(), selectGroup.getValue()));
            } else selectDiscipline.setValue(null);
        });

        selectDiscipline.addValueChangeListener(event -> {
            if (selectDiscipline.getValue() != null){
                controls.clear();
                controls.addAll(plansServices.getTypeControl(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue(), selectCourse.getValue(), selectGroup.getValue(), selectDiscipline.getValue()));
                selectControlType.setItems(controls);

            } else selectControlType.setValue(null);
        });

        selectControlType.addValueChangeListener(event -> {


            List<MarksModel> marksModels = new ArrayList<>();
            studentGrid.setItems(marksModels);
            studentGrid.removeAllColumns();

            if (selectControlType.getValue() != null) {

                marksModels  = markServices.getMarks(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue(), selectCourse.getValue(), selectGroup.getValue(), selectDiscipline.getValue(), selectControlType.getValue());

                studentGrid.setSelectionMode(Grid.SelectionMode.MULTI);

                studentGrid.addColumn(MarksModel::getId).setHeader("№").setWidth("35px").setFlexGrow(0);
                studentGrid.addColumn(MarksModel::getPIB).setHeader("ПІБ студента").setWidth("200px").setAutoWidth(true);

                switch (selectControlType.getValue()) {
                    case ("Залік"):
                    case ("Екзамен"):
                    case ("Диференційний залік"):
                    case ("Курсова робота"):
                    case ("Курсовий проєкт"):
                        createTextField();
                        studentGrid.addColumn(MarksModel::getMarkSum).setHeader("Сума за модулі").setWidth("120px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                        studentGrid.addColumn(MarksModel::getNationalScale).setHeader("Оцінка за національною шкалою").setWidth("160px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                        studentGrid.addColumn(MarksModel::getECTS).setHeader("ECTS").setWidth("80px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                        break;
                    case ("Перший модульний контроль"):
                        createTextField();
                        break;
                    case ("Другий модульний контроль"):
                        studentGrid.addColumn(MarksModel::getMarkFirstModule).setHeader("Перший модуль").setWidth("100px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                        createTextField();
                        studentGrid.addColumn(MarksModel::getMarkSum).setHeader("Сума за модулі").setWidth("100px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                        break;
                    case ("Розрахункова робота"):
                    case ("Розрахунково-графічна робота"):
                        if (marksModels.get(0).getPart2()!=null){
                            createTextField("РГР1", 1);
                            createTextField("РГР2", 2);
                        }
                        if (marksModels.get(0).getPart4()!=null){
                            createTextField("РГР3", 3);
                            createTextField("РГР4", 4);
                        }
                        if (marksModels.get(0).getPart6()!=null){
                            createTextField("РГР5", 5);
                            createTextField("РГР6", 6);
                        }
                        if (marksModels.get(0).getPart8()!=null){
                            createTextField("РГР7", 7);
                            createTextField("РГР8", 8);
                        }
                        studentGrid.addColumn(MarksModel::getMark).setHeader("Оцінка").setWidth("80px").setAutoWidth(true);
                        break;
                }
                studentGrid.addColumn(new ComponentRenderer<>(MarksModel::getEnabled)).setHeader("Статус").setWidth("80px").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);;
                studentGrid.addColumn(MarksModel::getDate).setHeader("Дата зміни").setWidth("120px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
                studentGrid.addColumn(MarksModel::getUser).setHeader("Користувач").setWidth("250px").setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);

                studentGrid.setItems(marksModels);
            }
        });

        saveButton.addClickListener(event -> {

            studentGrid.getDataProvider().refreshAll();

            List<MarksModel> marksModels = studentGrid.getDataProvider().fetch(new Query<>()).toList();
            for (MarksModel marksModel : marksModels){
                markServices.saveMarks(marksModel, selectControlType.getValue());
            }

            marksModels  = markServices.getMarks(selectFaculty.getValue(), selectDepartment.getValue(), selectSpecialty.getValue(), selectCourse.getValue(), selectGroup.getValue(), selectDiscipline.getValue(), selectControlType.getValue());
            studentGrid.setItems(marksModels);

        });

        approveButton.addClickListener(event -> {

            List<StudentModelToDocumentGenerate> students10 = List.of
                    (
                            new StudentModelToDocumentGenerate(1, "Іванов Іван Іванович", "123456", 55),
                            new StudentModelToDocumentGenerate(2, "Петров Петро Петрович", "234567", 48),
                            new StudentModelToDocumentGenerate(3, "Сидорова Марія Олександрівна", "345678", 60),
                            new StudentModelToDocumentGenerate(4, "Іванов Іван Іванович", "123456", 55),
                            new StudentModelToDocumentGenerate(5, "Петров Петро Петрович", "234567", 48),
                            new StudentModelToDocumentGenerate(6, "Сидорова Марія Олександрівна", "345678", 60),
                            new StudentModelToDocumentGenerate(7, "Іванов Іван Іванович", "123456", 55),
                            new StudentModelToDocumentGenerate(8, "Петров Петро Петрович", "234567", 48),
                            new StudentModelToDocumentGenerate(9, "Сидорова Марія Олександрівна", "345678", 60),
                            new StudentModelToDocumentGenerate(10, "Іванов Іван Іванович", "123456", 55)
                    );

            DataModelForMC1 data10 = new DataModelForMC1
                    (
                            "Факультет транспортних та інформаційних технологій",
                            "Інформаційна безпека в комп'ютеризованих системах",
                            "4",
                            "ІБК-4-1",
                            "2024/2025",
                            "23",
                            "серпня",
                            "2024",
                            "Інформаційна безпека",
                            "7",
                            "Перший модульний контроль",
                            "120",
                            "Іванов Іван Іванович",
                            "Клочан Арсен Євгенійович",
                            "Клочан Арсен Євгенійович",
                            students10
                    );
            DocxUpdater docxUpdater = new DocxUpdater();
            String path = docxUpdater.generateForMC1(data10);

            // Шлях до згенерованого PDF файлу
            String finalFilePath = "uploads/mcontrol1u.pdf";

            // Перевірка чи файл існує
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                // Створення StreamResource для передачі файлу у браузер
                StreamResource resource = new StreamResource("mcontrol1u.pdf", () -> {
                    try {
                        return new FileInputStream(pdfFile);
                    } catch (IOException e) {
                        e.fillInStackTrace();
                        Notification.show("Помилка при завантаженні файлу");
                        return null;
                    }
                });

                // Використання компонента Anchor для створення посилання на ресурс
                com.vaadin.flow.component.html.Anchor downloadLink = new com.vaadin.flow.component.html.Anchor(resource, "");
                downloadLink.getElement().setAttribute("download", true); // Додає атрибут для завантаження файлу
                downloadLink.getElement().setAttribute("target", "_blank"); // Відкриває у новій вкладці
                add(downloadLink);

                // Виконання JavaScript для симуляції кліку на посиланні
                UI.getCurrent().getPage().executeJs("document.querySelector('a[download]').click();");
            } else {
                Notification.show("PDF файл не знайдено.");
            }

        });
    }

    private void createTextField(){
        studentGrid.addColumn(new ComponentRenderer<>(item -> {

            TextField textField = new TextField();
            textField.setValue(String.valueOf(item.getMark()));
            textField.setWidth("80px");
            textField.getStyle().set("--vaadin-input-field-background", "white");
            textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            textField.getStyle().set("border-bottom", "1px solid black");

            textField.addValueChangeListener(event2 -> {
                // Оновлюємо значення в об'єкті MarksModel
                item.setMark(event2.getValue());
            });

            return textField;
        })).setHeader("Оцінка").setWidth("80px").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
    }

    private void createTextField(String header, int i){
        studentGrid.addColumn(new ComponentRenderer<>(item -> {

            TextField textField = new TextField();

            if (i == 1) {
                textField.setValue(String.valueOf(item.getPart1()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart1(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 2) {
                textField.setValue(String.valueOf(item.getPart2()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart2(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 3) {
                textField.setValue(String.valueOf(item.getPart3()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart3(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 4) {
                textField.setValue(String.valueOf(item.getPart4()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart4(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 5) {
                textField.setValue(String.valueOf(item.getPart5()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart5(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 6) {
                textField.setValue(String.valueOf(item.getPart6()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart6(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 7) {
                textField.setValue(String.valueOf(item.getPart7()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart7(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }
            if (i == 8) {
                textField.setValue(String.valueOf(item.getPart8()));
                textField.addValueChangeListener(event2 -> {
                    // Оновлюємо значення в об'єкті MarksModel
                    item.setPart8(event2.getValue());
                    studentGrid.getDataProvider().refreshAll();
                });
            }

            textField.setWidth("80px");
            textField.getStyle().set("--vaadin-input-field-background", "white");
            textField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
            textField.getStyle().set("border-bottom", "1px solid black");



            return textField;
        })).setHeader(header).setWidth("80px").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
    }





}