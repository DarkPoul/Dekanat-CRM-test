package dekanat.service;

import dekanat.entity.DepartmentEntity;
import dekanat.entity.StudentEntity;
import dekanat.repository.DepartmentRepo;
import dekanat.repository.FacultyRepo;
import dekanat.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final DepartmentRepo departmentRepo;
    private final FacultyRepo facultyRepo;
    private final StudentRepo studentRepo;

    public DepartmentService(DepartmentRepo departmentRepo, FacultyRepo facultyRepo, StudentRepo studentRepo) {
        this.departmentRepo = departmentRepo;
        this.facultyRepo = facultyRepo;
        this.studentRepo = studentRepo;
    }

    public String getDepTitle(Long id){
        return departmentRepo.findById(id).isPresent() ? departmentRepo.findById(id).get().getTitle() : "false";
    }

    public long getDepId(String title) {
        return departmentRepo.findByTitle(title).getId();
    }

    public List<String> getAllDepartment(){
        return departmentRepo.findAll().stream().map(DepartmentEntity::getTitle).toList();
    }

    public List<String> getDepartmentFilterFaculty(String faculty){
        long facultyId = facultyRepo.findByTitle(faculty).getId();

        List<StudentEntity> studentEntities = studentRepo.findByFaculty(facultyId);

        // Збираємо всі унікальні номери кафедр
        Set<Long> uniqueDepartmentIds = studentEntities.stream()
                .map(StudentEntity::getDepartment)
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        // Отримуємо назви кафедр за їх номерами

        return uniqueDepartmentIds.stream()
                .map(id -> departmentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid department id: " + id)).getTitle())
                .sorted()
                .collect(Collectors.toList());
    }


}
