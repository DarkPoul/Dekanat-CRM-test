package dekanat.repository;

import dekanat.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolesRepo extends JpaRepository<RolesEntity, Long> {
    List<RolesEntity> findByAccessType(int i);
}
