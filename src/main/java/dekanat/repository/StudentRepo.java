package dekanat.repository;

import dekanat.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {

    List<StudentEntity> findAllByGroupAndCourseAndNumberAndYear(String group, String course, String number, String year);

    StudentEntity findById(long id);

    StudentEntity findByGroupAndCourseAndNumberAndYearAndSurnameAndNameAndPatronymic(String group, String course, String number, String year, String SurName, String name, String patronymic);

    @Query("SELECT DISTINCT s.group FROM StudentEntity s WHERE s.faculty = ?1")
    List<String> findDistinctGroupsByFaculty(int faculty);

    List<StudentEntity> findAllByGroup(String title);

    List<StudentEntity> findAllByGroupAndCourse(String group, String course);

    StudentEntity findBySurnameAndNameAndPatronymic(String p, String i, String b);



}
