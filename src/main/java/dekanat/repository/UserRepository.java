package dekanat.repository;



import dekanat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    UserEntity findByLastnameAndFirstnameAndPatronymic(String p, String i, String b);
}
