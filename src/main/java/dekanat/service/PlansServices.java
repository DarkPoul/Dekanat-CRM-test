package dekanat.service;


import com.vaadin.flow.spring.security.AuthenticationContext;
import dekanat.entity.FacultyEntity;
import dekanat.entity.MarkEntity;
import dekanat.entity.PlansEntity;
import dekanat.entity.StudentEntity;
import dekanat.model.PlansModel;
import dekanat.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlansServices {

    private final PlansRepo plansRepo;
    private final StudentRepo studentRepo;
    private final DiscRepo discRepo;
    private final ControlRepo controlRepo;
    private final DepartmentRepo departmentRepo;
    private final StudentService studentService;
    private final MarkRepo markRepo;
    private final AuthenticationContext authenticationContext;
    private final SessionRepo sessionRepo;

    private final FacultyRepo facultyRepo;
    public PlansServices(PlansRepo plansRepo, StudentRepo studentRepo, DiscRepo discRepo, ControlRepo controlRepo, DepartmentRepo departmentRepo, StudentService studentService, MarkRepo markRepo, AuthenticationContext authenticationContext, AuthenticationContext authenticationContext1, SessionRepo sessionRepo, FacultyRepo facultyRepo) {
        this.plansRepo = plansRepo;
        this.studentRepo = studentRepo;
        this.discRepo = discRepo;
        this.controlRepo = controlRepo;
        this.departmentRepo = departmentRepo;
        this.studentService = studentService;
        this.markRepo = markRepo;
        this.authenticationContext = authenticationContext1;
        this.sessionRepo = sessionRepo;
        this.facultyRepo = facultyRepo;

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

        FacultyEntity facultyEntity = facultyRepo.findByAbr(authenticationContext.getGrantedRoles().stream().findFirst().get());
        System.out.println(facultyEntity.getId());

        entity.setId(Optional.ofNullable(plansRepo.findMaxId()).orElse(0L) + 1);
        entity.setStudents(String.join(", ", studentService.getStudentsId(students, groupTitle)));
        entity.setDiscipline(discRepo.findByTitle(plansModel.getDisc()).getId());
        entity.setSemester(getNumberSemester(groupTitle, semester));
        entity.setCredits(plansModel.getHours());
        entity.setFirst(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
        entity.setSecond(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
        entity.setParts(plansModel.getPart());
        entity.setDepartment(Math.toIntExact(departmentRepo.findByTitle(plansModel.getDepartment()).getId()));
        entity.setFaculty((int)facultyEntity.getId());
        entity.setGroup(groupTitle);

        plansRepo.save(entity);

        System.out.println("plan create successful");

        for (String s : studentService.getStudentsId(students, groupTitle)){
            MarkEntity markEntity = new MarkEntity();


            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setPlan(entity.getId());
            markEntity.setStudent(Long.parseLong(s));
            markEntity.setSemester(entity.getSemester());
            markEntity.setControl(entity.getFirst());
            markEntity.setMark("0");

            markRepo.save(markEntity);

            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setControl(Integer.parseInt(String.valueOf(controlRepo.findByTitle("Перший модульний контроль").getId())));

            markRepo.save(markEntity);

            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setControl(Integer.parseInt(String.valueOf(controlRepo.findByTitle("Другий модульний контроль").getId())));

            markRepo.save(markEntity);



            if (entity.getSecond()!=8){
                markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
                markEntity.setControl(entity.getSecond());


                StringBuilder marksBuilder = new StringBuilder();
                int parts = entity.getParts();
                for (int i = 0; i < parts; i++) {
                    marksBuilder.append("0");
                    if (i < parts - 1) {
                        marksBuilder.append(",");
                    }
                }
                markEntity.setMark(marksBuilder.toString());

                markRepo.save(markEntity);
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
            createMarkEntitiesForStudent(entity, studentId, plansModel.getFirst(), true);
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

        for (String studentId : currentStudents) {
            List<MarkEntity> marks = markRepo.findByStudentAndPlan(Long.parseLong(studentId), entity.getId());
            for (MarkEntity mark : marks) {
                if (mark.getControl() == entity.getFirst()) {
                    mark.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
                    mark.setMark("0");
                    markRepo.save(mark);
                } else if (mark.getControl() == entity.getSecond()) {
                    mark.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
                    mark.setMark(createMarksString(entity.getParts()));
                    markRepo.save(mark);
                }
            }
        }

        plansRepo.save(entity);
        System.out.println("update successful");
    }



    private void createMarkEntitiesForStudent(PlansEntity entity, String studentId, String controlType, boolean isNew) {

        MarkEntity markEntity = new MarkEntity();

        markEntity.setPlan(entity.getId());
        markEntity.setStudent(Long.parseLong(studentId));
        markEntity.setSemester(entity.getSemester());

        if (entity.getSecond()==4 || entity.getSecond()==5){

            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setControl(Math.toIntExact(controlRepo.findByTitle(controlType).getId()));

            markEntity.setMark("0");
            markRepo.save(markEntity);
        }

        if (entity.getFirst() == 1 || entity.getFirst() == 2 || entity.getFirst() == 3){

            MarkEntity markEntity1 = new MarkEntity();
            markEntity1.setPlan(entity.getId());
            markEntity1.setStudent(Long.parseLong(studentId));
            markEntity1.setSemester(entity.getSemester());
            markEntity1.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity1.setControl(Math.toIntExact(controlRepo.findByTitle(controlType).getId()));
            markEntity1.setMark("0");
            markRepo.save(markEntity1);


            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setControl(Integer.parseInt(String.valueOf(controlRepo.findByTitle("Перший модульний контроль").getId())));
            markRepo.save(markEntity);

            markEntity.setId(Optional.ofNullable(markRepo.findMaxId()).orElse(0L) + 1);
            markEntity.setControl(Integer.parseInt(String.valueOf(controlRepo.findByTitle("Другий модульний контроль").getId())));
            markRepo.save(markEntity);
        }



    }

    private void updateControlTypesForStudents(PlansEntity entity, List<String> currentStudents, PlansModel plansModel) {
        for (String studentId : currentStudents) {
            List<MarkEntity> marks = markRepo.findByStudentAndPlan(Long.parseLong(studentId), entity.getId());

            for (MarkEntity mark : marks) {
                if (mark.getControl() == entity.getFirst()) {
                    mark.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getFirst()).getId()));
                    markRepo.save(mark);
                } else if (mark.getControl() == entity.getSecond()) {
                    mark.setControl(Math.toIntExact(controlRepo.findByTitle(plansModel.getSecond()).getId()));
                    markRepo.save(mark);
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

    private String createMarksString(int parts) {
        StringBuilder marksBuilder = new StringBuilder("0;");
        for (int i = 0; i < parts; i++) {
            marksBuilder.append("0");
            if (i < parts - 1) {
                marksBuilder.append(",");
            }
        }
        return marksBuilder.toString();
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

    public Set<String> getGroupTitle(String faculty, String department){
        List<PlansEntity> plansEntityList = plansRepo.findByFacultyAndDepartment
                (
                        facultyRepo.findByTitle(faculty).getId(),
                        departmentRepo.findByTitle(department).getId()
                );
        return plansEntityList.stream()
                .map(PlansEntity::getGroup)
                .map(group -> group.split("-"))
                .filter(parts -> parts.length > 3)
                .map(parts -> parts[0])
                .collect(Collectors.toSet());
    }

    public Set<String> getCourse(String faculty, String department, String speciality){
        return plansRepo.findByFacultyAndDepartmentAndSpeciality
                        (
                                facultyRepo.findByTitle(faculty).getId(),
                                departmentRepo.findByTitle(department).getId(),
                                speciality
                        ).stream().map(PlansEntity::getGroup)
                .map(group -> group.split("-"))
                .filter(parts -> parts.length > 3)
                .map(parts -> parts[1] + " (" + parts[3] + ")")
                .collect(Collectors.toSet());
    }

    public Set<String> getGroupNumber(String faculty, String department, String speciality, String course) {
        long facultyId = facultyRepo.findByTitle(faculty).getId();
        long departmentId = departmentRepo.findByTitle(department).getId();
        List<PlansEntity> results = plansRepo.findByFacultyAndDepartmentAndSpeciality(facultyId, departmentId, speciality);
        for (PlansEntity plansEntity : results){
            System.out.println(plansEntity.getGroup());
        }

        return results.stream()
                .map(PlansEntity::getGroup)
                .map(group -> group.split("-"))
                .filter(parts -> parts.length > 3)
                .filter(parts -> Objects.equals(parts[1], course))
                .map(parts -> parts[2])
                .collect(Collectors.toSet());
    }

    public List<String> getDiscipline(String faculty, String department, String speciality, String course, String number){
        String group = speciality + "-" + course.charAt(0) + "-" + number + "-" + course.substring(3, course.length() - 1);
        return plansRepo.findByFacultyAndDepartmentAndGroup
                        (
                                facultyRepo.findByTitle(faculty).getId(),
                                departmentRepo.findByTitle(department).getId(),
                                group
                        ).stream().map(PlansEntity::getDiscipline)
                .map(discRepo::findById)
                .filter(Optional::isPresent)
                .map(discEntity -> discEntity.get().getTitle())
                .collect(Collectors.toList());
    }

    public List<String> getTypeControl(String faculty, String department, String speciality, String course, String number, String disc) {
        String group = speciality + "-" + course.charAt(0) + "-" + number + "-" + course.substring(3, course.length() - 1);

        PlansEntity controls = plansRepo.findByFacultyAndDepartmentAndGroupAndDiscipline
                (
                        facultyRepo.findByTitle(faculty).getId(),
                        departmentRepo.findByTitle(department).getId(),
                        group,
                        discRepo.findByTitle(disc).getId()
                );

        List<String> controlList = new ArrayList<>();
        controlList.add("Перший модульний контроль");
        controlList.add("Другий модульний контроль");
        controlList.add(controlRepo.findById(controls.getFirst()).getTitle());
        if (controls.getSecond() != 8){
            controlList.add(controlRepo.findById(controls.getSecond()).getTitle());
        }

        return controlList;
    }

    public PlansEntity getPlanModel(String department, String faculty, String disc, String group){
        int semester = getNumberSemester(group, sessionRepo.findAll().get(0).getSession());


        return plansRepo.findByFacultyAndDepartmentAndGroupAndDisciplineAndSemester
                (
                        facultyRepo.findByTitle(faculty).getId(),
                        departmentRepo.findByTitle(department).getId(),
                        group,
                        discRepo.findByTitle(disc).getId(),
                        semester
                );
    }
}
