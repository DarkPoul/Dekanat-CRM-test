package dekanat;

import dekanat.entity.*;
import dekanat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final ControlRepo controlRepo;
    private final DiscRepo discRepo;
    private final DepartmentRepo departmentRepo;

    @Autowired
    public DataInitializer(UserRepository userRepository, StudentRepo studentRepo, PasswordEncoder passwordEncoder, ControlRepo controlRepo, DiscRepo discRepo, DepartmentRepo departmentRepo) {
        this.userRepository = userRepository;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.controlRepo = controlRepo;
        this.discRepo = discRepo;
        this.departmentRepo = departmentRepo;
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
        if (studentRepo.count() == 0){
            StudentEntity student1 = new StudentEntity(1L, "Гончар", "Павло", "Олександрович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student2 = new StudentEntity(2L, "Поттер", "Гаррі", "Джеймсович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student3 = new StudentEntity(3L, "Шевченко", "Тарас", "Григорович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student4 = new StudentEntity(4L, "Куліков", "Андрій", "Сергійович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student5 = new StudentEntity(5L, "Левченко", "Марія", "Іванівна", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student6 = new StudentEntity(6L, "Остапчук", "Дмитро", "Анатолійович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student7 = new StudentEntity(7L, "Соловйов", "Олексій", "Ігорович", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student8 = new StudentEntity(8L, "Бондаренко", "Ольга", "Петрівна", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student9 = new StudentEntity(9L, "Мельник", "Катерина", "Олексіївна", "IBK", "4", "1", "19", 1, 1);
            StudentEntity student10 = new StudentEntity(10L, "Коваль", "Віталій", "Миколайович", "IBK", "4", "1", "19", 1, 1);

            studentRepo.save(student1);
            studentRepo.save(student2);
            studentRepo.save(student3);
            studentRepo.save(student4);
            studentRepo.save(student5);
            studentRepo.save(student6);
            studentRepo.save(student7);
            studentRepo.save(student8);
            studentRepo.save(student9);
            studentRepo.save(student10);

            System.out.println("Test students have been created!");

        }

        if (controlRepo.count() == 0){
            ControlEntity controlEntity1 = new ControlEntity(1L, "Залік", "1");
            ControlEntity controlEntity2 = new ControlEntity(2L, "Екзамен", "1");
            ControlEntity controlEntity3 = new ControlEntity(3L, "Диференційний залік", "1");
            ControlEntity controlEntity4 = new ControlEntity(4L, "Курсова робота", "2");
            ControlEntity controlEntity5 = new ControlEntity(5L, "Курсовий проєкт", "2");
            ControlEntity controlEntity6 = new ControlEntity(6L, "Розрахункова робота", "2");
            ControlEntity controlEntity7 = new ControlEntity(7L, "Розрахунково-графічна робота", "2");
            ControlEntity controlEntity8 = new ControlEntity(7L, "Відсутній", "2");
            List<ControlEntity> controlEntities = new ArrayList<>();
            controlEntities.add(controlEntity1);
            controlEntities.add(controlEntity2);
            controlEntities.add(controlEntity3);
            controlEntities.add(controlEntity4);
            controlEntities.add(controlEntity5);
            controlEntities.add(controlEntity6);
            controlEntities.add(controlEntity7);
            controlEntities.add(controlEntity8);

            controlRepo.saveAll(controlEntities);
            System.out.println("Control methods added!");
        }

        if (discRepo.count()==0){
            DiscEntity disc1 = new DiscEntity(1L, "Математика", "q");
            DiscEntity disc2 = new DiscEntity(2L, "Фізика", "w");
            DiscEntity disc3 = new DiscEntity(3L, "Хімія", "e");
            DiscEntity disc4 = new DiscEntity(4L, "Англійська мова", "r");
            DiscEntity disc5 = new DiscEntity(5L, "Українська мова", "t");
            DiscEntity disc6 = new DiscEntity(6L, "Історія Укріїни", "y");
            List<DiscEntity> discEntities = new ArrayList<>();
            discEntities.add(disc1);
            discEntities.add(disc2);
            discEntities.add(disc3);
            discEntities.add(disc4);
            discEntities.add(disc5);
            discEntities.add(disc6);

            discRepo.saveAll(discEntities);
        }

        if (departmentRepo.count() == 0){
            DepartmentEntity department1 = new DepartmentEntity(1L, "Кафедра 1", "qwe", "q");
            DepartmentEntity department2 = new DepartmentEntity(1L, "Кафедра 2", "qwe", "q");
            DepartmentEntity department3 = new DepartmentEntity(1L, "Кафедра 3", "qwe", "q");

            List<DepartmentEntity> departmentEntities = new ArrayList<>();
            departmentEntities.add(department1);
            departmentEntities.add(department2);
            departmentEntities.add(department3);

            departmentRepo.saveAll(departmentEntities);
        }
    }
}
