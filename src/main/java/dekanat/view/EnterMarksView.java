package dekanat.view;

import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.spring.security.AuthenticationContext;
import dekanat.component.MainLayout;
import dekanat.entity.*;
import dekanat.model.EnterMarksModel;
import dekanat.model.PlansModel;
import dekanat.repository.ControlRepo;
import dekanat.repository.FacultyRepo;
import dekanat.repository.MarkRepo;
import dekanat.repository.SessionRepo;
import dekanat.service.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<PlansModel> plansModels = new ArrayList<>();
    private final SecurityService securityService;

    public EnterMarksView(FacultyRepo facultyRepo, DepartmentService departmentService, StudentService studentService, PlansServices plansServices, SessionRepo sessionRepo, DiscService discService, MarkRepo markRepo, ControlRepo controlRepo, SecurityService securityService, AuthenticationContext authenticationContext) {
        this.facultyRepo = facultyRepo;
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.plansServices = plansServices;
        this.sessionRepo = sessionRepo;
        this.discService = discService;
        this.markRepo = markRepo;
        this.controlRepo = controlRepo;
        this.securityService = securityService;
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



        //Backend

//        selectFaculty.setItems(facultyRepo.findAll().stream().map(FacultyEntity::getTitle).toList());
////        selectFaculty.addValueChangeListener(event
////                -> selectDepartment.setItems(departmentService.getDepartmentFilterFaculty(selectFaculty.getValue())));
//        selectDepartment.addValueChangeListener(event
//                -> selectSpecialty.setItems(studentService.getGroupTitleFilterFacultyAndDepartment
//                (
//                        selectFaculty.getValue(),
//                        selectDepartment.getValue()
//                )
//        ));
//        selectSpecialty.addValueChangeListener(event -> {
//           selectCourse.setItems(studentService.getGroupCourseFilterFacultyAndDepartmentAndSpec
//                   (
//                           selectFaculty.getValue(),
//                           selectDepartment.getValue(),
//                           selectSpecialty.getValue()
//                   )
//           );
//        });
//        selectCourse.addValueChangeListener(event ->{
//            System.out.println(selectCourse.getValue());
//            selectGroup.setItems(studentService.getGroupNumberFilterFacultyAndDepartmentAndSpecAndCourse
//                    (
//                            selectFaculty.getValue(),
//                            selectDepartment.getValue(),
//                            selectSpecialty.getValue(),
//                            selectCourse.getValue()
//                    )
//            );
//        });
//
//
//        selectGroup.addValueChangeListener(event -> { //todo створити сервіс, та перенести метод
//            StudentEntity student = studentService.getFullGroupTitle(
//                    selectFaculty.getValue(),
//                    selectDepartment.getValue(),
//                    selectSpecialty.getValue(),
//                    selectCourse.getValue(),
//                    selectGroup.getValue()
//            );
//
//            String groupTitle = student.getGroup() + "-" + student.getCourse() + "-" + student.getNumber() + "-" + student.getYear();
//
//            plansModels = plansServices.getStudyPlansForGroup(groupTitle, sessionRepo.findById(1).map(SessionEntity::getSession).orElseThrow());
//
//            selectDiscipline.setItems(plansModels.stream().map(PlansModel::getDisc).collect(Collectors.toList()));
//
//        });
//
//        selectDiscipline.addValueChangeListener(event -> { //todo створити сервіс, та перенести метод
//           long planId = plansModels.get(0).getPlanId();
//           List<MarkEntity> markEntities = markRepo.findAllByPlan(planId);
//           List<String> control = new ArrayList<>();
//           for (MarkEntity markEntity : markEntities){
//               String disc = controlRepo.findById(markEntity.getControl()).getTitle();
//               if (!control.contains(disc)){
//                   control.add(disc);
//               }
//           }
//           selectControlType.setItems(control);
//
//
//        });


        //Backend

//        authenticationContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
//            boolean isAdmin = user.getAuthorities().stream().anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
//            boolean isDekanat = user.getAuthorities().stream().anyMatch(grantedAuthority -> "ROLE_DEKANAT_TT".equals(grantedAuthority.getAuthority()));
//            boolean isKafedra = user.getAuthorities().stream().anyMatch(grantedAuthority -> "ROLE_KAFEDRA_1".equals(grantedAuthority.getAuthority()));
//            if (isAdmin){
//                //todo
//            }
//            if (isDekanat){
//                //todo
//                selectFaculty.setReadOnly(true);
//            }
//            if (isKafedra){
//                //todo
//                selectDepartment.setReadOnly(true);
//            }
//        });



    }
}