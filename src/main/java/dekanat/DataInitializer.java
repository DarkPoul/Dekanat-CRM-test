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
    private final FacultyRepo facultyRepo;
    private final SessionRepo sessionRepo;
    private final RolesRepo rolesRepo;

    @Autowired
    public DataInitializer(UserRepository userRepository, StudentRepo studentRepo, PasswordEncoder passwordEncoder, ControlRepo controlRepo, DiscRepo discRepo, DepartmentRepo departmentRepo, FacultyRepo facultyRepo, SessionRepo sessionRepo, RolesRepo rolesRepo) {
        this.userRepository = userRepository;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.controlRepo = controlRepo;
        this.discRepo = discRepo;
        this.departmentRepo = departmentRepo;
        this.facultyRepo = facultyRepo;
        this.sessionRepo = sessionRepo;
        this.rolesRepo = rolesRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            UserEntity user1 = new UserEntity(1L, "admin", passwordEncoder.encode("admin"), "Іванович", "Іван", "Іванов", true, "ROLE_ADMIN");
            UserEntity user2 = new UserEntity(2L, "dekanat", passwordEncoder.encode("dekanat"), "Іванович", "Іван", "Іванов", true, "ROLE_DEKANAT_TT");
            UserEntity user3 = new UserEntity(3L, "kafedra", passwordEncoder.encode("kafedra"), "Іванович", "Іван", "Іванов", true, "ROLE_KAFEDRA_KIADIB");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            System.out.println("""
                    ################################################
                    #Admin, department, dekanat users created with:#
                    ################################################
                            __________________________
                            |Admin - email: admin     |
                            |        password: admin  |
                            |_________________________|
                            |Dekanat - email: dekanat |
                            |       password: dekanat |
                            |_________________________|
                            |Kafedra - email: kafedra |
                            |       password: kafedra |
                            |_________________________|
                    """);
        }
        if (studentRepo.count() == 0){
            StudentEntity student1 = new StudentEntity(1L, "Гончар", "Павло", "Олександрович", "IBK", "4", "1", "2019", 1);
            StudentEntity student2 = new StudentEntity(2L, "Поттер", "Гаррі", "Джеймсович", "IBK", "4", "1", "2019", 1);
            StudentEntity student3 = new StudentEntity(3L, "Шевченко", "Тарас", "Григорович", "IBK", "4", "1", "2019", 1);
            StudentEntity student4 = new StudentEntity(4L, "Куліков", "Андрій", "Сергійович", "IBK", "4", "1", "2019", 1);
            StudentEntity student5 = new StudentEntity(5L, "Левченко", "Марія", "Іванівна", "IBK", "4", "1", "2019", 1);
            StudentEntity student6 = new StudentEntity(6L, "Остапчук", "Дмитро", "Анатолійович", "IBK", "3", "1", "2020", 1);
            StudentEntity student7 = new StudentEntity(7L, "Соловйов", "Олексій", "Ігорович", "IBK", "3", "1", "2020", 1);
            StudentEntity student8 = new StudentEntity(8L, "Бондаренко", "Ольга", "Петрівна", "IBK", "3", "1", "2020", 1);
            StudentEntity student9 = new StudentEntity(9L, "Мельник", "Катерина", "Олексіївна", "IBK", "3", "1", "2020", 1);
            StudentEntity student10 = new StudentEntity(10L, "Коваль", "Віталій", "Миколайович", "IBK", "3", "1", "2020", 1);

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
            ControlEntity controlEntity8 = new ControlEntity(8L, "Відсутній", "2");
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
            List<DepartmentEntity> departmentEntityList = List.of(
                    new DepartmentEntity(1L, "Кафедра автомобілів", "KA", "Department of Automobiles"),
                    new DepartmentEntity(2L, "Кафедра виробництва, ремонту та матеріалознавства", "KBRM", "Department of Production, Repair and Materials Science"),
                    new DepartmentEntity(3L, "Кафедра вищої математики", "KVM", "Department of Higher Mathematics"),
                    new DepartmentEntity(4L, "Кафедра двигунів і теплотехніки", "KDIT", "Department of Engines and Heat Engineering"),
                    new DepartmentEntity(5L, "Кафедра дорожньо-будівельних матеріалів та хімії", "KDBMH", "Department of Road-Building Materials and Chemistry"),
                    new DepartmentEntity(6L, "Кафедра екології та безпеки життєдіяльності", "KEBZ", "Department of Ecology and Life Safety"),
                    new DepartmentEntity(7L, "Кафедра економіки", "KE", "Department of Economics"),
                    new DepartmentEntity(8L, "Кафедра інженерії машин транспортного будівництва", "KIMTB", "Department of Engineering of Transport Construction Machinery"),
                    new DepartmentEntity(9L, "Кафедра іноземних мов", "KIM", "Department of Foreign Languages"),
                    new DepartmentEntity(10L, "Кафедра іноземної філоології та перекладу", "KIFP", "Department of Foreign Philology and Translation"),
                    new DepartmentEntity(11L, "Кафедра інформаційних систем і технологій", "KIST", "Department of Information Systems and Technologies"),
                    new DepartmentEntity(12L, "Кафедра інформаційно-аналітичної діяльності та інформаційної безпеки", "KIADIB", "Department of Information and Analytical Activities and Information Security"),
                    new DepartmentEntity(13L, "Кафедра комп'ютерної, інженерної графіки та дизайну", "KKIGD", "Department of Computer, Engineering Graphics and Design"),
                    new DepartmentEntity(14L, "Кафедра конституційного та адміністративного права", "KKAP", "Department of Constitutional and Administrative Law"),
                    new DepartmentEntity(15L, "Кафедра менеджменту", "KM", "Department of Management"),
                    new DepartmentEntity(16L, "Кафедра міжнародних перевезень та митного контролю", "KMPMK", "Department of International Transport and Customs Control"),
                    new DepartmentEntity(17L, "Кафедра мостів, тунелів та гідротехнічних споруд", "KMTGS", "Department of Bridges, Tunnels and Hydraulic Structures"),
                    new DepartmentEntity(18L, "Кафедра опору матеріалів та машинознавства", "KOM", "Department of Resistance of Materials and Machine Science"),
                    new DepartmentEntity(19L, "Кафедра проектування доріг, геодезії та землеустрою", "KPDGZ", "Department of Road Design, Geodesy and Land Management"),
                    new DepartmentEntity(20L, "Кафедра системного проектування об'єктів транспортної інфраструктури та геодезії", "KSPDGZ", "Department of System Design of Transport Infrastructure Facilities and Geodesy"),
                    new DepartmentEntity(21L, "Кафедра теорії та історії держави і права", "KTIDP", "Department of Theory and History of State and Law"),
                    new DepartmentEntity(22L, "Кафедра теоретичної і прикладної механіки", "KTIPM", "Department of Theoretical and Applied Mechanics"),
                    new DepartmentEntity(23L, "Кафедра технологічної експлуатації автомобілів та автосервісу", "KTEAA", "Department of Technological Operation of Vehicles and Car Service"),
                    new DepartmentEntity(24L, "Кафедра транспортних систем та безпеки дорожнього руху", "KTSBDR", "Department of Transport Systems and Road Safety"),
                    new DepartmentEntity(25L, "Кафедра транспортних технологій", "KTT", "Department of Transport Technologies"),
                    new DepartmentEntity(26L, "Кафедра транспортного будівництва та управління майном", "KTBUM", "Department of Transport Construction and Property Management"),
                    new DepartmentEntity(27L, "Кафедра транспортного права та логістики", "KTPL", "Department of Transport Law and Logistics"),
                    new DepartmentEntity(28L, "Кафедра туризму", "KT", "Department of Tourism"),
                    new DepartmentEntity(29L, "Кафедра управління виробництвом і майном", "KUVM", "Department of Production and Property Management"),
                    new DepartmentEntity(30L, "Кафедра фізики", "KF", "Department of Physics"),
                    new DepartmentEntity(31L, "Кафедра фізичного виховання і спорту", "KFVS", "Department of Physical Education and Sports"),
                    new DepartmentEntity(32L, "Кафедра філософії і педагогіки", "KFP", "Department of Philosophy and Pedagogy"),
                    new DepartmentEntity(33L, "Кафедра фінансів, обліку і аудиту", "KFOA", "Department of Finance, Accounting and Audit")
            );

            departmentRepo.saveAll(departmentEntityList);
        }

        if (facultyRepo.count() == 0){
            List<FacultyEntity> facultyEntityList = List.of(
                    new FacultyEntity(1L, "Факультет транспортного будівництва", "DEKANAT_DB"),
                    new FacultyEntity(2L, "Автомеханічний факультет", "DEKANAT_AM"),
                    new FacultyEntity(3L, "Факультет менеджменту, логістики та туризму", "DEKANAT_EM"),
                    new FacultyEntity(4L, "Факультет транспортних та інформаційних технологій", "DEKANAT_TT"),
                    new FacultyEntity(5L, "Факультет заочного, дистанційного навчання та підготовки іноземних громадян", "DEKANAT_ZDN"),
                    new FacultyEntity(6L, "Факультет економіки та права", "DEKANAT_EP")
            );


            facultyRepo.saveAll(facultyEntityList);
        }

        if (sessionRepo.count() == 0){
            sessionRepo.save(new SessionEntity(1, "Зимова"));
        }

        if (rolesRepo.count() == 0){
            List<RolesEntity> rolesEntityList = List.of(
                    new RolesEntity(1L, "ROLE_ADMIN", 1),
                    new RolesEntity(3L, "ROLE_DEKANAT_DB", 2),
                    new RolesEntity(4L, "ROLE_DEKANAT_AM", 2),
                    new RolesEntity(5L, "ROLE_DEKANAT_EM", 2),
                    new RolesEntity(6L, "ROLE_DEKANAT_TT", 2),
                    new RolesEntity(7L, "ROLE_DEKANAT_EP", 2),
                    new RolesEntity(8L, "ROLE_DEKANAT_ZDN", 2),
                    new RolesEntity(9L, "ROLE_KAFEDRA_KA", 3),
                    new RolesEntity(10L, "ROLE_KAFEDRA_KVRM", 3),
                    new RolesEntity(11L, "ROLE_KAFEDRA_KVM", 3),
                    new RolesEntity(12L, "ROLE_KAFEDRA_KDIT", 3),
                    new RolesEntity(13L, "ROLE_KAFEDRA_KDBMX", 3),
                    new RolesEntity(14L, "ROLE_KAFEDRA_KEBJ", 3),
                    new RolesEntity(15L, "ROLE_KAFEDRA_KE", 3),
                    new RolesEntity(16L, "ROLE_KAFEDRA_KIMTB", 3),
                    new RolesEntity(17L, "ROLE_KAFEDRA_KIM", 3),
                    new RolesEntity(18L, "ROLE_KAFEDRA_KIFP", 3),
                    new RolesEntity(19L, "ROLE_KAFEDRA_KIST", 3),
                    new RolesEntity(20L, "ROLE_KAFEDRA_KIADIB", 3),
                    new RolesEntity(21L, "ROLE_KAFEDRA_KKIGD", 3),
                    new RolesEntity(22L, "ROLE_KAFEDRA_KKAP", 3),
                    new RolesEntity(23L, "ROLE_KAFEDRA_KM", 3),
                    new RolesEntity(24L, "ROLE_KAFEDRA_KMPMK", 3),
                    new RolesEntity(25L, "ROLE_KAFEDRA_KMTGS", 3),
                    new RolesEntity(26L, "ROLE_KAFEDRA_KOM", 3),
                    new RolesEntity(27L, "ROLE_KAFEDRA_KPDGZ", 3),
                    new RolesEntity(28L, "ROLE_KAFEDRA_KSPDGZ", 3),
                    new RolesEntity(29L, "ROLE_KAFEDRA_KTIDP", 3),
                    new RolesEntity(30L, "ROLE_KAFEDRA_KTIPIP", 3),
                    new RolesEntity(31L, "ROLE_KAFEDRA_KTEAA", 3),
                    new RolesEntity(32L, "ROLE_KAFEDRA_KTSBDR", 3),
                    new RolesEntity(33L, "ROLE_KAFEDRA_KTT", 3),
                    new RolesEntity(34L, "ROLE_KAFEDRA_KTBUM", 3),
                    new RolesEntity(35L, "ROLE_KAFEDRA_KTPL", 3),
                    new RolesEntity(36L, "ROLE_KAFEDRA_KT", 3),
                    new RolesEntity(37L, "ROLE_KAFEDRA_KUVM", 3),
                    new RolesEntity(38L, "ROLE_KAFEDRA_KF", 3),
                    new RolesEntity(39L, "ROLE_KAFEDRA_KFVS", 3),
                    new RolesEntity(40L, "ROLE_KAFEDRA_KFP", 3),
                    new RolesEntity(41L, "ROLE_KAFEDRA_KFOA", 3)
            );

            rolesRepo.saveAll(rolesEntityList);
        }
    }
}
