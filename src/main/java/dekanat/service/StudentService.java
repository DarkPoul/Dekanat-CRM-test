package dekanat.service;


import dekanat.entity.StudentEntity;
import dekanat.model.StudentModel;
import dekanat.repository.FacultyRepo;
import dekanat.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final DepartmentService departmentService;
    private final FacultyRepo facultyRepo;

    public StudentService(StudentRepo studentRepo, DepartmentService departmentService, FacultyRepo facultyRepo) {
        this.studentRepo = studentRepo;
        this.departmentService = departmentService;
        this.facultyRepo = facultyRepo;
    }

    public List<String> getAllGroups(){
        List<StudentEntity> studentEntities = new ArrayList<>(studentRepo.findAll());
        List<String> allGroup = new ArrayList<>();
        for (StudentEntity entity : studentEntities){
            String group = entity.getGroup() + "-" + entity.getCourse() + "-" + entity.getNumber() + "-" + entity.getYear();
            if (!allGroup.contains(group)){
                allGroup.add(group);
            }
        }
        return allGroup;
    }

    public List<String> getGroupStudentsPIB(String fullTitleGroup) {

        List<String> students = new ArrayList<>();

        String speciality = fullTitleGroup.split("-")[0];
        String course = fullTitleGroup.split("-")[1];
        String group = fullTitleGroup.split("-")[2];
        String enterYear = fullTitleGroup.split("-")[3];

        List<StudentEntity> studentEntities =  studentRepo.findAllByGroupAndCourseAndNumberAndYear(speciality, course, group, enterYear);


        for (StudentEntity student : studentEntities){
            students.add(student.getSurname() + " " + student.getName() + " " + student.getPatronymic());
        }

        return students;
    }

    public List<StudentModel> getStudents(String groupTitle){

        List<StudentEntity> allStudentEntities = studentRepo.findAllByGroupAndCourseAndNumberAndYear(
                groupTitle.split("-")[0],
                groupTitle.split("-")[1],
                groupTitle.split("-")[2],
                groupTitle.split("-")[3]
        );
        List<StudentModel> allStudents = new ArrayList<>();

        for (StudentEntity entity: allStudentEntities){

            allStudents.add(new StudentModel(
                    entity.getId(),
                    entity.getName(),
                    entity.getSurname(),
                    entity.getPatronymic()
            ));
        }

        return allStudents;
    }

    public List<String> getStudentsPIB(List<String> studentsId){
        List<String> studentsPIB = new ArrayList<>();
        for (String student: studentsId){
            long id = Long.parseLong(student);
            StudentEntity studentEntity = studentRepo.findById(id);
            studentsPIB.add(studentEntity.getSurname() + " " + studentEntity.getName() + " " + studentEntity.getPatronymic());
        }

        return studentsPIB;
    }

    public List<String> getStudentsId(List<String> studentsPIB, String groupTitle){
        List<String> studentsId = new ArrayList<>();
        for (String student : studentsPIB){
            studentsId.add(String.valueOf(studentRepo.findByGroupAndCourseAndNumberAndYearAndSurnameAndNameAndPatronymic(
                    groupTitle.split("-")[0],
                    groupTitle.split("-")[1],
                    groupTitle.split("-")[2],
                    groupTitle.split("-")[3],
                    student.split(" ")[0],
                    student.split(" ")[1],
                    student.split(" ")[2]
            ).getId()));
        }
        return studentsId;
    }


    public List<String> getGroupTitleFilterFacultyAndDepartment(String faculty, String department){
        int facultyId = (int) facultyRepo.findByTitle(faculty).getId();
        int departmentId = (int) departmentService.getDepId(department);

        // Отримуємо список студентів за факультетом та кафедрою
        List<StudentEntity> studentEntities = studentRepo.findAllByDepartmentAndFaculty(departmentId, facultyId);

        // Використовуємо Set для уникнення дублікатів і збираємо результати в List
        return studentEntities.stream()
                .map(StudentEntity::getGroup)
                .distinct() // Відфільтровуємо унікальні значення
                .collect(Collectors.toList());

    }

    public List<String> getGroupCourseFilterFacultyAndDepartmentAndSpec(String faculty, String department, String spec){
        int facultyId = (int) facultyRepo.findByTitle(faculty).getId();
        int departmentId = (int) departmentService.getDepId(department);
        List<StudentEntity> studentEntities = studentRepo.findAllByDepartmentAndFacultyAndGroup(departmentId, facultyId, spec);

        return studentEntities.stream()
                .map(StudentEntity::getCourse)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getGroupNumberFilterFacultyAndDepartmentAndSpecAndCourse(String faculty, String department, String spec, String course){
        int facultyId = (int) facultyRepo.findByTitle(faculty).getId();
        int departmentId = (int) departmentService.getDepId(department);

        List<StudentEntity> studentEntities = studentRepo.findAllByDepartmentAndFacultyAndGroupAndCourse(departmentId, facultyId, spec, course);
        return studentEntities.stream()
                .map(StudentEntity::getNumber)
                .distinct()
                .collect(Collectors.toList());
    }

    public StudentEntity getFullGroupTitle(String faculty, String department, String spec, String course, String number){
        int facultyId = (int) facultyRepo.findByTitle(faculty).getId();
        int departmentId = (int) departmentService.getDepId(department);
        return studentRepo.findFirstByDepartmentAndFacultyAndGroupAndCourseAndNumber(departmentId, facultyId, spec, course, number);
    }









}
