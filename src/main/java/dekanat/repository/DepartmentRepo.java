package dekanat.repository;

import dekanat.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<DepartmentEntity, Long> {
    DepartmentEntity findByTitle(String title);
    DepartmentEntity findById(long id);
}
