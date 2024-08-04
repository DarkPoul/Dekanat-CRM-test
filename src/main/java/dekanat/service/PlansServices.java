package dekanat.service;


import dekanat.entity.MarkEntity;
import dekanat.entity.PlansEntity;
import dekanat.entity.StudentEntity;
import dekanat.model.PlansModel;
import dekanat.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlansServices {

    private final PlansRepo plansRepo;
    private final StudentRepo studentRepo;
    private final DiscRepo discRepo;
    private final ControlRepo controlRepo;
    private final DepartmentRepo departmentRepo;
    private final StudentService studentService;
    private final MarkRepo markRepo;

    public PlansServices(PlansRepo plansRepo, StudentRepo studentRepo, DiscRepo discRepo, ControlRepo controlRepo, DepartmentRepo departmentRepo, StudentService studentService, MarkRepo markRepo) {
        this.plansRepo = plansRepo;
        this.studentRepo = studentRepo;
        this.discRepo = discRepo;
        this.controlRepo = controlRepo;
        this.departmentRepo = departmentRepo;
        this.studentService = studentService;
        this.markRepo = markRepo;
    }

    public List<PlansModel> getStudyPlansForGroup(String groupName, String semester) {
        // Розділяємо групове ім'я на частини
        String[] groupParts = groupName.split("-");
        List<StudentEntity> students = studentRepo.findAllByGroupAndCourseAndNumberAndYear(
                groupParts[0],
                groupParts[1],
                groupParts[2],
                groupParts[3]
        );

        // Отримуємо список ID студентів як рядки
        List<String> studentsIds = students.stream().map(StudentEntity::getId).map(String::valueOf).toList();

        // Використовуємо Set для зберігання унікальних дисциплін
        Set<String> uniqueDisciplinesSet = new HashSet<>();
        List<PlansEntity> uniquePlansModelList = new ArrayList<>();

        for (String studentId : studentsIds) {
            List<PlansEntity> plans = plansRepo.findByStudentsContainingValues(studentId, getNumberSemester(groupName, semester));
            for (PlansEntity plan : plans) {
                if (uniqueDisciplinesSet.add(discRepo.findById(plan.getDiscipline()).getTitle())) { // додається тільки якщо ще не існує
                    uniquePlansModelList.add(plan);
                }
            }
        }

        List<PlansModel> plansEntityList = new ArrayList<>();
        int iterator = 1;

        for (PlansEntity model : uniquePlansModelList) {
            PlansModel plansModel = new PlansModel();
            plansModel.setPlanId(model.getId());
            plansModel.setNumber(iterator);
            plansModel.setDisc(discRepo.findById(model.getDiscipline()).getTitle());
            plansModel.setHours(model.getCredits());
            plansModel.setChoice(getChoice(studentsIds, Arrays.stream(model.getStudents().split(", ")).toList()));
            plansModel.setFirst(controlRepo.findById(model.getFirst()).getTitle());
            plansModel.setSecond(controlRepo.findById(model.getSecond()).getTitle());
            plansModel.setPart(model.getParts());
            plansModel.setDepartment(departmentRepo.findById(model.getDepartment()).getTitle());
            iterator++;
            plansEntityList.add(plansModel);
        }

        return plansEntityList;
    }

    public void savePlan(PlansModel plansModel, List<String> students, String groupTitle, String semester){

        PlansEntity entity = new PlansEntity();

        entity.setId(Optional.ofNullable(plansRepo.findMaxId()).orElse(0L) + 1);
        entity.setStudents(String.join(", ", studentService.getStudentsId(students, groupTitle)));
        entity.setDiscipline(discRepo.findByTitle(plansModel.getDisc()).getId());
        entity.setSemester(getNumberSemester(groupTitle, semester));
        entity.setCredits(plansModel.getHours());
        entity.setFirst(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
        entity.setSecond(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
        entity.setParts(plansModel.getPart());
        entity.setDepartment(Math.toIntExact(departmentRepo.findByTitle(plansModel.getDepartment()).getId()));

        plansRepo.save(entity);

        System.out.println("plan create successful");

        for (String s : studentService.getStudentsId(students, groupTitle)){
            MarkEntity markEntity = new MarkEntity();
            MarkEntity markEntity1 = new MarkEntity();

            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setPlan(entity.getId());
            markEntity.setStudent(Long.parseLong(s));
            markEntity.setDisc(entity.getDiscipline());
            markEntity.setSemester(entity.getSemester());
            markEntity.setControl(entity.getFirst());
            markEntity.setMark("0");

            markRepo.save(markEntity);

            if (entity.getSecond()!=8){
                markEntity1.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
                markEntity1.setPlan(entity.getId());
                markEntity1.setStudent(Long.parseLong(s));
                markEntity1.setDisc(entity.getDiscipline());
                markEntity1.setSemester(entity.getSemester());
                markEntity1.setControl(entity.getSecond());
                markEntity1.setMark(String.join(", ", Collections.nCopies(entity.getParts(), "0")));

                markRepo.save(markEntity1);
            }




        }

        System.out.println("mark create successful");


    }

    public void updatePlan(PlansModel plansModel, List<String> students, String groupTitle) {
        PlansEntity entity = plansRepo.findById(plansModel.getPlanId());

        // Get current students
        List<String> currentStudents = Arrays.asList(entity.getStudents().split(", "));

        // Get new students
        List<String> newStudents = studentService.getStudentsId(students, groupTitle);

        // Determine students to add and remove
        List<String> studentsToAdd = new ArrayList<>(newStudents);
        studentsToAdd.removeAll(currentStudents);

        List<String> studentsToRemove = new ArrayList<>(currentStudents);
        studentsToRemove.removeAll(newStudents);

        // Remove MarkEntity records for students no longer in the list
        for (String studentId : studentsToRemove) {
            List<MarkEntity> marksToRemove = markRepo.findByStudentAndPlan(Long.parseLong(studentId), entity.getId());
            markRepo.deleteAll(marksToRemove);
        }

        // Add new MarkEntity records for new students
        for (String studentId : studentsToAdd) {
            createMarkEntitiesForStudent(entity, studentId, plansModel.getSecond(), true);
        }

        // Handle existing students if the second control type has changed
        if (entity.getSecond() == 8 && !Objects.equals(plansModel.getSecond(), "Відсутній")) {
            for (String studentId : currentStudents) {
                createMarkEntitiesForStudent(entity, studentId, plansModel.getSecond(), false);
            }
        } else if (entity.getSecond() != 8 && Objects.equals(plansModel.getSecond(), "Відсутній")) {
            for (String studentId : currentStudents) {
                List<MarkEntity> marksToRemove = markRepo.findByStudentAndPlanAndControl(Long.parseLong(studentId), entity.getId(), entity.getSecond());
                markRepo.deleteAll(marksToRemove);
            }
        }

        // Remove old MarkEntity records for current students for the second control type and add new ones if the student list changed
        if (entity.getSecond() != Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId())) {
            for (String studentId : currentStudents) {
                List<MarkEntity> marksToRemove = markRepo.findByStudentAndPlanAndControl(Long.parseLong(studentId), entity.getId(), entity.getSecond());
                markRepo.deleteAll(marksToRemove);
            }
            for (String studentId : newStudents) {
                createMarkEntitiesForStudent(entity, studentId, plansModel.getSecond(), false);
            }
        }

        // Update MarkEntity records for control types if changed
        updateControlTypesForStudents(entity, currentStudents, plansModel);

        // Update PlansEntity fields
        updatePlanEntityFields(entity, plansModel, newStudents);

        plansRepo.save(entity);
        System.out.println("update successful");
    }

    private void createMarkEntitiesForStudent(PlansEntity entity, String studentId, String secondControlType, boolean includeFirstControl) {
        long nextId = Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1;

        if (includeFirstControl) {
            MarkEntity markEntity = new MarkEntity();
            markEntity.setId(nextId);
            markEntity.setPlan(entity.getId());
            markEntity.setStudent(Long.parseLong(studentId));
            markEntity.setDisc(entity.getDiscipline());
            markEntity.setSemester(entity.getSemester());
            markEntity.setControl(entity.getFirst());
            markEntity.setMark("0");

            markRepo.save(markEntity);
        }

        if (!Objects.equals(secondControlType, "Відсутній")) {
            MarkEntity markEntity1 = new MarkEntity();
            markEntity1.setId(nextId + 1);
            markEntity1.setPlan(entity.getId());
            markEntity1.setStudent(Long.parseLong(studentId));
            markEntity1.setDisc(entity.getDiscipline());
            markEntity1.setSemester(entity.getSemester());
            markEntity1.setControl(entity.getSecond());
            markEntity1.setMark(String.join(", ", Collections.nCopies(entity.getParts(), "0")));

            markRepo.save(markEntity1);
        }
    }

    private void updateControlTypesForStudents(PlansEntity entity, List<String> currentStudents, PlansModel plansModel) {
        if (entity.getFirst() != Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId())) {
            for (String studentId : currentStudents) {
                List<MarkEntity> marksToUpdate = markRepo.findByStudentAndPlanAndControl(Long.parseLong(studentId), entity.getId(), entity.getFirst());
                for (MarkEntity markEntity : marksToUpdate) {
                    markEntity.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
                    markRepo.save(markEntity);
                }
            }
        }

        if (entity.getSecond() != Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId())) {
            for (String studentId : currentStudents) {
                List<MarkEntity> marksToUpdate = markRepo.findByStudentAndPlanAndControl(Long.parseLong(studentId), entity.getId(), entity.getSecond());
                for (MarkEntity markEntity : marksToUpdate) {
                    markEntity.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
                    markRepo.save(markEntity);
                }
            }
        }
    }

    private void updatePlanEntityFields(PlansEntity entity, PlansModel plansModel, List<String> newStudents) {
        entity.setStudents(String.join(", ", newStudents));
        entity.setDiscipline(discRepo.findByTitle(plansModel.getDisc()).getId());
        entity.setCredits(plansModel.getHours());
        entity.setFirst(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
        entity.setSecond(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
        entity.setParts(plansModel.getPart());
        entity.setDepartment(Math.toIntExact(departmentRepo.findByTitle(plansModel.getDepartment()).getId()));
    }

    public void removePlan(long planId) {
        // Find the plan entity by its ID
        PlansEntity plan = plansRepo.findById(planId);
        if (plan == null) {
            System.out.println("Plan not found");
            return;
        }

        // Find all marks associated with this plan
        List<MarkEntity> marksToRemove = markRepo.findByPlan(planId);

        // Delete all associated marks
        markRepo.deleteAll(marksToRemove);

        // Delete the plan itself
        plansRepo.deleteById(planId);

        System.out.println("Plan and associated marks removed successfully");
    }

    public List<String> findChoiceStudent(PlansModel plansModel){
        return Arrays.stream(plansRepo.findById(plansModel.getPlanId()).getStudents().split(", ")).toList();
    }

    private String getChoice(List<String> students, List<String> studentChoice){

        List<String> modifiableStudents = new ArrayList<>(students);
        List<String> modifiableStudentChoice = new ArrayList<>(studentChoice);

        Collections.sort(modifiableStudents);
        Collections.sort(modifiableStudentChoice);

        if (modifiableStudents.equals(modifiableStudentChoice)) {
            return "Ні";
        } else {
            return "Так";
        }
    }

    private int getNumberSemester(String groupTitle, String semester){
        String[] groupParts = groupTitle.split("-");
        if (semester.equals("Зимова")) return (Integer.parseInt(groupParts[1]) * 2 - 1);
        else return Integer.parseInt(groupParts[1]) * 2;


    }



}
