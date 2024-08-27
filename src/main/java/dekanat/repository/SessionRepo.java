package dekanat.repository;

import dekanat.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepo extends JpaRepository<SessionEntity, Integer> {
}
