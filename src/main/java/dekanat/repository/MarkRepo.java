package dekanat.repository;

import dekanat.entity.MarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepo extends JpaRepository<MarkEntity, Long> {

    @Query(value = "SELECT MAX(id) FROM MarkEntity ")
    Long findMaxId();

    List<MarkEntity> findAllByPlan(long plan);

    List<MarkEntity> findAllByStudentAndDiscAndSemesterAndControl(long student, long disc, int semester, int control);

    MarkEntity findById(long id);

    List<MarkEntity> findByStudentAndPlan(long studentId, long planId);

    List<MarkEntity> findByStudentAndPlanAndControl(long student, long planId, int control);

    List<MarkEntity> findByPlan(long planId);
}
