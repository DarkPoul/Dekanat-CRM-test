package dekanat.repository;

import dekanat.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Long> {

    List<StudentEntity> findAllByGroupAndCourseAndNumberAndYear(String group, String course, String number, String year);

    StudentEntity findById(long id);

    StudentEntity findByGroupAndCourseAndNumberAndYearAndSurnameAndNameAndPatronymic(String group, String course, String number, String year, String SurName, String name, String patronymic);


}
