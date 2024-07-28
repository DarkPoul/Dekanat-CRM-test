package dekanat;

import dekanat.entity.UserEntity;
import dekanat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            UserEntity user = new UserEntity();
            user.setEmail("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole("ADMIN");
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("Admin user created with email: admin and password: admin");
        }
    }
}
