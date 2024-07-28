package dekanat.repository;

import dekanat.entity.ControlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlRepo extends JpaRepository<ControlEntity, Integer> {
    ControlEntity findByTitle(String title);
    ControlEntity findByType(String type);
    ControlEntity findById(long id);
}
