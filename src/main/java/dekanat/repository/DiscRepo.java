package dekanat.repository;


import dekanat.entity.DiscEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscRepo extends JpaRepository<DiscEntity, Long> {
    DiscEntity findByTitle(String title);
    DiscEntity findById(long id);
}
