package dekanat.repository;

import dekanat.entity.PlansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlansRepo extends JpaRepository<PlansEntity, Long> {

    @Query("SELECT p FROM PlansEntity p WHERE " +
            "p.students LIKE %:values% AND p.semester = :semester")
    List<PlansEntity> findByStudentsContainingValues(String values, int semester);

    @Query(value = "SELECT MAX(id) FROM PlansEntity ")
    Long findMaxId();

    PlansEntity findById(long id);

    List<PlansEntity> findByFacultyAndDepartment(long f, long d);

    @Query("SELECT p FROM PlansEntity p WHERE p.faculty = :faculty AND p.department = :department AND p.group LIKE %:speciality%")
    List<PlansEntity> findByFacultyAndDepartmentAndSpeciality(@Param("faculty") long faculty,
                                                              @Param("department") long department,
                                                              @Param("speciality") String speciality);

    List<PlansEntity> findByFacultyAndDepartmentAndGroup(long f, long d, String g);

    PlansEntity findByFacultyAndDepartmentAndGroupAndDiscipline(long f, long d, String g, long disc);

    PlansEntity findByFacultyAndDepartmentAndGroupAndDisciplineAndSemester(long f, long d, String g, long disc, int s);
}
