package dekanat.view;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import dekanat.component.TrainingPlanDialog;
import dekanat.model.PlansModel;
import dekanat.service.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@PageTitle("Навчальні плани | Деканат")
@Route(value = "learning-plans", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class TrainingPlansView extends Div {

    private final Select<String> groupSelect = new Select<>();
    private final Select<String> sessionSelect = new Select<>();
    private final Grid<PlansModel> trainingPlansGrid = new Grid<>(PlansModel.class, false);
    private final Button newItemButton = new Button("Додати");

    private TrainingPlanDialog trainingPlanDialog;
    private final StudentService studentService;

    private final PlansServices plansServices;
    private final DiscService discService;
    private final DepartmentService departmentService;
    private final ControlService controlService;

    public TrainingPlansView(StudentService studentService, PlansServices plansServices, DiscService discService, DepartmentService departmentService, ControlService controlService) {
        this.studentService = studentService;
        this.plansServices = plansServices;
        this.discService = discService;
        this.departmentService = departmentService;
        this.controlService = controlService;


        List<String> studentChoice = new ArrayList<>();


        newItemButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newItemButton.getStyle().set("margin", "20px 20px 20px 20px");

        groupSelect.setLabel("Група");
        groupSelect.setItems(studentService.getAllGroups());
        sessionSelect.setLabel("Сессія");
        sessionSelect.setItems("Зимова", "Літня");

        sessionSelect.addValueChangeListener(event -> {

            getGroupAndSession();
        });

        groupSelect.addValueChangeListener(event -> {
           getGroupAndSession();
        });

        trainingPlansGrid.addColumn(PlansModel::getNumber).setHeader("№");
        trainingPlansGrid.addColumn(PlansModel::getDisc).setHeader("Дисципліна");
        trainingPlansGrid.addColumn(PlansModel::getHours).setHeader("Години");
        trainingPlansGrid.addColumn(PlansModel::getChoice).setHeader("Вибіркова");
        trainingPlansGrid.addColumn(PlansModel::getFirst).setHeader("Перший к.");
        trainingPlansGrid.addColumn(PlansModel::getSecond).setHeader("Другий к.");
        trainingPlansGrid.addColumn(PlansModel::getPart).setHeader("Частини");
        trainingPlansGrid.addColumn(PlansModel::getDepartment).setHeader("Кафедра");
        trainingPlansGrid.addComponentColumn(trainingPlans -> {
            Button button = new Button(new Icon(VaadinIcon.EDIT));
            button.addClickListener(event -> {
                trainingPlanDialog = new TrainingPlanDialog(discService.getAllDisc(), departmentService.getAllDepartment(), controlService.getControlOfType("1"), controlService.getControlOfType("2"), studentService.getGroupStudentsPIB(groupSelect.getValue()));
                trainingPlanDialog.setSavePlanListener((trainingPlanEntity, students) -> {
                });
                trainingPlanDialog.setRemovePlanListener(id -> {
                    plansServices.removePlan(id);
                    trainingPlansGrid.setItems(plansServices.getStudyPlansForGroup(groupSelect.getValue(), sessionSelect.getValue()));
                });
                trainingPlanDialog.setUpdatePlanListener((plansEntity, students) -> {
                    plansServices.updatePlan(plansEntity, students, groupSelect.getValue());
                    trainingPlansGrid.setItems(plansServices.getStudyPlansForGroup(groupSelect.getValue(), sessionSelect.getValue()));
                });
                trainingPlanDialog.open(trainingPlans, studentService.getStudentsPIB(plansServices.findChoiceStudent(trainingPlans).stream().toList()) );
            });
            return button;
        });

        newItemButton.addClickListener(event -> {
                trainingPlanDialog = new TrainingPlanDialog(discService.getAllDisc(), departmentService.getAllDepartment(), controlService.getControlOfType("1"), controlService.getControlOfType("2"), studentService.getGroupStudentsPIB(groupSelect.getValue()));
                trainingPlanDialog.setSavePlanListener((trainingPlanEntity, students) -> {
                    plansServices.savePlan(trainingPlanEntity, students, groupSelect.getValue(), sessionSelect.getValue());
                    trainingPlansGrid.setItems(plansServices.getStudyPlansForGroup(groupSelect.getValue(), sessionSelect.getValue()));
//
                });

                trainingPlanDialog.open();
        });

        trainingPlansGrid.setHeight("100%");



        HorizontalLayout filterLayout = new HorizontalLayout();
        HorizontalLayout tableLayout = new HorizontalLayout();

        filterLayout.getStyle().set("padding", "20px 20px 0px 20px");
        tableLayout.getStyle().set("padding", "20px");
        tableLayout.setHeight("80%");

        // Apply padding to grid
        trainingPlansGrid.getStyle().set("padding", "10px");

        filterLayout.add(groupSelect, sessionSelect);
        tableLayout.add(trainingPlansGrid);

        setHeight("90%");
        add(filterLayout, tableLayout, newItemButton);
    }

    private void getGroupAndSession(){
        if (sessionSelect.getValue() != null && groupSelect.getValue() != null){
            trainingPlansGrid.setItems(plansServices.getStudyPlansForGroup(groupSelect.getValue(), sessionSelect.getValue()));
        }
    }
}
