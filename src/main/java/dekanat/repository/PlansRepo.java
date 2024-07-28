package dekanat.repository;

import dekanat.entity.PlansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlansRepo extends JpaRepository<PlansEntity, Long> {

    @Query("SELECT p FROM PlansEntity p WHERE " +
            "p.students LIKE %:values% AND p.semester = :semester")
    List<PlansEntity> findByStudentsContainingValues(String values, int semester);

    @Query(value = "SELECT MAX(id) FROM PlansEntity ")
    Long findMaxId();

    PlansEntity findById(long id);


}
