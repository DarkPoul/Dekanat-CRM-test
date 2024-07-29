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


            markEntity1.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity1.setPlan(entity.getId());
            markEntity1.setStudent(Long.parseLong(s));
            markEntity1.setDisc(entity.getDiscipline());
            markEntity1.setSemester(entity.getSemester());
            markEntity1.setControl(entity.getSecond());
            markEntity1.setMark(String.join(", ", Collections.nCopies(entity.getParts(), "0")));


            markRepo.save(markEntity1);

        }

        System.out.println("mark create successful");


    }

    public void updatePlan(PlansModel plansModel, List<String> students, String groupTitle) {
        PlansEntity entity = plansRepo.findById(plansModel.getPlanId());

        entity.setStudents(String.join(", ", studentService.getStudentsId(students, groupTitle)));
        entity.setDiscipline(discRepo.findByTitle(plansModel.getDisc()).getId());
        entity.setCredits(plansModel.getHours());
        entity.setFirst(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
        entity.setSecond(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
        entity.setParts(plansModel.getPart());
        entity.setDepartment(Math.toIntExact(departmentRepo.findByTitle(plansModel.getDepartment()).getId()));

        plansRepo.save(entity);
        System.out.println("update successful");
    }

    public void removePlan(long id){
        plansRepo.deleteById(id);
        System.out.println("Remove successful");
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
