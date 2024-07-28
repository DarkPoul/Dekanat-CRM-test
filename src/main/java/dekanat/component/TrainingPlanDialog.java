package dekanat.component;



import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import dekanat.listener.RemovePlanListener;
import dekanat.listener.SavePlanListener;
import dekanat.listener.UpdatePlanListener;
import dekanat.model.PlansModel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class TrainingPlanDialog extends Dialog {

    @Setter
    private SavePlanListener savePlanListener;
    @Setter
    private RemovePlanListener removePlanListener;
    @Setter
    private UpdatePlanListener updatePlanListener;

    private ComboBox<String> discipline = new ComboBox<>();
    private TextField hours = new TextField();
    private Select<String> choiceDiscipline = new Select<>();
    private Select<String> firstControl = new Select<>();
    private Select<String> secondControl = new Select<>();
    private Select<String> parts = new Select<>();
    private ComboBox<String> depart = new ComboBox<>();
    private Button save = new Button("Зберегти");
    private Button cancel = new Button("Відміна");
    private Button remove = new Button("Видалити");
    private Button update = new Button("Оновити");





    private HorizontalLayout HLayoutAll = new HorizontalLayout();
    private HorizontalLayout HLayoutDisc = new HorizontalLayout();

    private HorizontalLayout HButtonLayout = new HorizontalLayout();
    private VerticalLayout VLayoutDisc1 = new VerticalLayout();
    private VerticalLayout VLayoutDisc2 = new VerticalLayout();
    private VerticalLayout VLayoutDisc3 = new VerticalLayout();
    private VerticalLayout VLayoutStudent = new VerticalLayout();



    Checkbox checkAllStudent = new Checkbox();
    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();



    public TrainingPlanDialog(List<String> disc, List<String> department, List<String> firstType, List<String> secondType, List<String> students){



        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        update.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants();
        remove.addThemeVariants(ButtonVariant.LUMO_ERROR);





        choiceDiscipline.setItems("Так", "Ні");

        List<String> partList = new ArrayList<>();
        partList.add("1");
        partList.add("2");
        partList.add("4");
        partList.add("6");
        partList.add("8");






        discipline.setItems(disc);
        depart.setItems(department);
        firstControl.setItems(firstType);
        secondControl.setItems(secondType);
        parts.setItems(partList);
        parts.setValue("1");

        checkboxGroup.setItems(students);
        checkboxGroup.select(students);




        discipline.setLabel("Дисципліни");
        hours.setLabel("Години");
        choiceDiscipline.setLabel("Вибіркові");
        firstControl.setLabel("Перший контроль");
        secondControl.setLabel("Другий контроль");
        parts.setLabel("Частини");
        depart.setLabel("Кафедра");







//        verticalLayout1.setWidth("250px");

        checkboxGroup.setWidth("100%");
        checkboxGroup.setHeight("95%");

        checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
        checkboxGroup.addValueChangeListener(event -> {
            if (event.getValue().size() == students.size()) {
                checkAllStudent.setValue(true);
                checkAllStudent.setIndeterminate(false);
            } else if (event.getValue().isEmpty()) {
                checkAllStudent.setValue(false);
                checkAllStudent.setIndeterminate(false);
            } else {
                checkAllStudent.setIndeterminate(true);
            }
        });
        checkAllStudent.addValueChangeListener(event -> {
            if (checkAllStudent.getValue()) {
                checkboxGroup.setValue(new HashSet<>(students));
            } else {
                checkboxGroup.deselectAll();
            }
        });

        checkAllStudent.setLabel("Обрати всіх");


        cancel.addClickListener(event -> close());

        parts.setReadOnly(true);
        secondControl.addValueChangeListener(event -> {
            if (secondControl.getValue().equals("Розрахункова робота") || secondControl.getValue().equals("Розрахунково-графічна робота")) {
                parts.setReadOnly(false);
            } else {
                parts.setReadOnly(true);
                parts.setValue("1");
            }
        });







        VLayoutDisc1.add(discipline, firstControl, choiceDiscipline);
        VLayoutDisc2.add(hours, secondControl, parts);

        VLayoutStudent.add(checkboxGroup, checkAllStudent);
        VLayoutStudent.setWidth("400px");
        VLayoutStudent.setHorizontalComponentAlignment(FlexComponent.Alignment.END, checkAllStudent);
        VLayoutStudent.getStyle().set("border", "1px solid #e0e0e0");

        HLayoutDisc.add(VLayoutDisc1, VLayoutDisc2);
        HLayoutDisc.setWidth("600px");

        VLayoutDisc3.add(HLayoutDisc, depart);

        HLayoutAll.add(VLayoutDisc3, VLayoutStudent);

        HButtonLayout.add(save, update, cancel, remove);

        add(HLayoutAll, HButtonLayout);


    }

    public void open(PlansModel trainingPlansModel, List<String> choiceStudent) {

        update.setVisible(true);
        save.setVisible(false);

        hours.setValue(String.valueOf(trainingPlansModel.getHours()));
        discipline.setValue(trainingPlansModel.getDisc());
        choiceDiscipline.setValue(trainingPlansModel.getChoice());
        firstControl.setValue(trainingPlansModel.getFirst());
        secondControl.setValue(trainingPlansModel.getSecond());
        parts.setValue(String.valueOf(trainingPlansModel.getPart()));
        depart.setValue(trainingPlansModel.getDepartment());

        VLayoutStudent.setEnabled(!choiceDiscipline.getValue().equals("Ні"));
        choiceDiscipline.addValueChangeListener(event -> VLayoutStudent.setEnabled(!VLayoutStudent.isEnabled()));

        checkboxGroup.deselectAll();
        checkboxGroup.setValue(new HashSet<>(choiceStudent));

        remove.setVisible(true);

        saveUpdateRemovePlan(trainingPlansModel);
    }

    public void open() {
        update.setVisible(false);
        save.setVisible(true);

        depart.setValue("");
        parts.setValue("1");
        choiceDiscipline.setValue("Ні");
        firstControl.setValue("");
        secondControl.setValue("");
        discipline.setValue("");
        hours.setValue("");

        VLayoutStudent.setEnabled(false);
        choiceDiscipline.addValueChangeListener(event -> VLayoutStudent.setEnabled(!VLayoutStudent.isEnabled()));




        remove.setVisible(false);

        PlansModel trainingPlansModel = new PlansModel();


        saveUpdateRemovePlan(trainingPlansModel);
    }

    private void saveUpdateRemovePlan(PlansModel trainingPlansModel) {



        save.addClickListener(event -> {

            List<String> students = checkboxGroup.getSelectedItems().stream().toList();

            trainingPlansModel.setHours(Integer.parseInt(hours.getValue()));
            trainingPlansModel.setDisc(discipline.getValue());
            trainingPlansModel.setChoice(choiceDiscipline.getValue());
            trainingPlansModel.setFirst(firstControl.getValue());
            trainingPlansModel.setSecond(secondControl.getValue());
            trainingPlansModel.setPart(Integer.parseInt(parts.getValue()));
            trainingPlansModel.setDepartment(depart.getValue());

            if (savePlanListener != null){
                savePlanListener.onSave(trainingPlansModel,students);
            }
            close();
        });

        remove.addClickListener(event -> {
            if (removePlanListener != null){
                removePlanListener.onRemove(trainingPlansModel.getPlanId());
            }
            close();
        });

        update.addClickListener(event -> {

            List<String> students = checkboxGroup.getSelectedItems().stream().toList();

            trainingPlansModel.setHours(Integer.parseInt(hours.getValue()));
            trainingPlansModel.setDisc(discipline.getValue());
            trainingPlansModel.setChoice(choiceDiscipline.getValue());
            trainingPlansModel.setFirst(firstControl.getValue());
            trainingPlansModel.setSecond(secondControl.getValue());
            trainingPlansModel.setPart(Integer.parseInt(parts.getValue()));
            trainingPlansModel.setDepartment(depart.getValue());

            if (updatePlanListener != null){
                updatePlanListener.onUpdate(trainingPlansModel, students);
            }
            close();
        });



        super.open();
    }
}
