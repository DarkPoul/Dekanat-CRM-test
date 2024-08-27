package dekanat.repository;

import dekanat.entity.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepo extends JpaRepository<FacultyEntity, Long> {

    FacultyEntity findByTitle(String faculty);
    FacultyEntity findByAbr(String abr);

}
