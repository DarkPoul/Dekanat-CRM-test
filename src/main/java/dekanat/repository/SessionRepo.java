package dekanat.repository;

import dekanat.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<SessionEntity, Integer> {
}
