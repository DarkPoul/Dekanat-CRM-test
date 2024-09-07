package dekanat.service;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import dekanat.entity.MarkEntity;
import dekanat.entity.PlansEntity;
import dekanat.entity.StudentEntity;
import dekanat.model.MarksModel;
import dekanat.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MarkServices {

    private final MarkRepo markRepo;
    private final PlansServices plansServices;
    private final ControlRepo controlRepo;
    private final StudentRepo studentRepo;
    private final UserRepository userRepository;
    private final SessionRepo sessionRepo;
    private final DiscService discService;
    private final StudentService studentService;
    private final CustomUserDetailsService customUserDetailsService;

    public MarkServices(MarkRepo markRepo, PlansServices plansServices, ControlRepo controlRepo, StudentRepo studentRepo, UserRepository userRepository, SessionRepo sessionRepo, DiscService discService, StudentService studentService, CustomUserDetailsService customUserDetailsService) {
        this.markRepo = markRepo;
        this.plansServices = plansServices;
        this.controlRepo = controlRepo;
        this.studentRepo = studentRepo;
        this.userRepository = userRepository;
        this.sessionRepo = sessionRepo;
        this.discService = discService;
        this.studentService = studentService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public List<MarksModel> getMarks(String faculty, String department, String spec, String course, String group, String disc, String control){

        String groupTitle = spec + "-" + course.charAt(0) + "-" + group + "-" + course.substring(3, course.length()-1);
        System.out.println(department);
        System.out.println(faculty);
        System.out.println(disc);
        System.out.println(groupTitle);

        PlansEntity plansEntity = plansServices.getPlanModel(department, faculty, disc, groupTitle);


        List<MarkEntity> markEntities = markRepo.findAllByPlanAndControlAndSemester
                (
                        plansEntity.getId(),
                        Math.toIntExact(controlRepo.findByTitle(control).getId()),
                        plansEntity.getSemester()
                );
        List<MarkEntity> markEntitiesFC = markRepo.findAllByPlanAndControlAndSemester
                (
                        plansEntity.getId(),
                        Math.toIntExact(controlRepo.findByTitle("Перший модульний контроль").getId()),
                        plansEntity.getSemester()
                );
        List<MarkEntity> markEntitiesSC = markRepo.findAllByPlanAndControlAndSemester
                (
                        plansEntity.getId(),
                        Math.toIntExact(controlRepo.findByTitle("Другий модульний контроль").getId()),
                        plansEntity.getSemester()
                );



        List<MarksModel> marksModelList = new ArrayList<>();
        int iterator = 0;

        for (MarkEntity entity : markEntities){
            iterator++;
            MarksModel marksModel = new MarksModel();
            marksModel.setId(iterator);
            marksModel.setPlanId(entity.getPlan());
            marksModel.setPIB(studentRepo.findById(entity.getStudent()).getSurname() + " " + studentRepo.findById(entity.getStudent()).getName() + " " + studentRepo.findById(entity.getStudent()).getPatronymic());
            marksModel.setMark(entity.getMark());
            marksModel.setMarkFirstModule(markEntitiesFC.get(iterator-1).getMark());
            marksModel.setMarkSum(String.valueOf(Integer.parseInt(markEntitiesFC.get(iterator-1).getMark()) +  Integer.parseInt(markEntitiesSC.get(iterator-1).getMark())));

            try {
                marksModel.setNationalScale(getNationalGrade(Integer.parseInt(entity.getMark())));
                marksModel.setECTS(getEctsGrade(Integer.parseInt(entity.getMark())));
                marksModel.setEnabled(createColoredIcon(entity.isEnabled()));
                marksModel.setDate(entity.getDate());
                marksModel.setUser(customUserDetailsService.findById(Integer.parseInt(entity.getUser())));

                marksModelList.add(marksModel);
            } catch (NumberFormatException e){
                int markSum = 0;
                int parts = entity.getMark().split(",").length;

                if (parts>=2){
                    marksModel.setPart1(entity.getMark().split(",")[0]);
                    marksModel.setPart2(entity.getMark().split(",")[1]);
                    markSum = markSum + Integer.parseInt(marksModel.getPart1()) +Integer.parseInt(marksModel.getPart2());
                }
                if (parts>=4){
                    marksModel.setPart3(entity.getMark().split(",")[2]);
                    marksModel.setPart4(entity.getMark().split(",")[3]);
                    markSum = markSum + Integer.parseInt(marksModel.getPart3()) + Integer.parseInt(marksModel.getPart4());
                }
                if (parts>=6){
                    marksModel.setPart5(entity.getMark().split(",")[4]);
                    marksModel.setPart6(entity.getMark().split(",")[5]);
                    markSum = markSum + Integer.parseInt(marksModel.getPart5()) +Integer.parseInt(marksModel.getPart6());
                }
                if (parts==8){
                    marksModel.setPart7(entity.getMark().split(",")[6]);
                    marksModel.setPart8(entity.getMark().split(",")[7]);
                    markSum = markSum + Integer.parseInt(marksModel.getPart7()) +Integer.parseInt(marksModel.getPart8());
                }
                marksModel.setMark(String.valueOf(markSum));
                marksModel.setNationalScale(String.valueOf(markSum));
                marksModel.setECTS(getEctsGrade(markSum));
                marksModel.setEnabled(createColoredIcon(entity.isEnabled()));
                marksModel.setDate(entity.getDate());
                marksModel.setUser(entity.getUser());

                marksModelList.add(marksModel);
            }

        }

        return marksModelList;
    }

    private Icon createColoredIcon(boolean bool) {
        Icon icon;
        if (bool){
            icon = VaadinIcon.CHECK.create();
            icon.getStyle().set("color", "green");
        } else {
            icon = VaadinIcon.CLOSE.create();
            icon.getStyle().set("color", "red");
        }
        return icon;
    }



    public void saveMarks(MarksModel marksModel, String control){

        System.out.println("START");


        MarkEntity markEntity = markRepo.findByPlanAndControlAndStudent
                (
                        marksModel.getPlanId(),
                        Math.toIntExact(controlRepo.findByTitle(control).getId()),
                        studentService.getIdByPIB
                                (
                                        marksModel.getPIB().split(" ")[0],
                                        marksModel.getPIB().split(" ")[1],
                                        marksModel.getPIB().split(" ")[2]
                                )
                );
        if (control.equals("Розрахункова робота") || control.equals("Розрахунково-графічна робота")){
            System.out.println("Start rgr");
            StringBuilder mark = new StringBuilder(marksModel.getPart1() + ",");
            if (marksModel.getPart2() != null){
                mark.append(marksModel.getPart2());
            }
            if (marksModel.getPart4() != null){
                mark.append(",").append(marksModel.getPart3());
                mark.append(",").append(marksModel.getPart4());
            }
            if (marksModel.getPart6() != null){
                mark.append(",").append(marksModel.getPart5());
                mark.append(",").append(marksModel.getPart6());
            }
            if (marksModel.getPart6() != null){
                mark.append(",").append(marksModel.getPart7());
                mark.append(",").append(marksModel.getPart8());
            }

            System.out.println(mark);
            markEntity.setMark(String.valueOf(mark));
        } else markEntity.setMark(marksModel.getMark());

        markEntity.setNumber("1"); //todo необхідно придумати/розібратися з системою ведення відомостей (на даний момент використовується останній номер відомості + 1, плюс перевірка на унікальність(на групу))
        markEntity.setDate(LocalDate.now().toString());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email = ((UserDetails) principal).getUsername();

        markEntity.setUser(String.valueOf(userRepository.findByEmail(email).get().getId()));
        markEntity.setEnabled(isEnabled(marksModel.getEnabled()));

        markRepo.save(markEntity);



    }



    private boolean isEnabled(Icon icon){
        return Objects.equals(icon.getIcon(), "vaadin:check");
    }
    public static String getNationalGrade(int score) {
        if (score >= 90) {
            return "Відмінно";
        } else if (score >= 75) {
            return "Добре";
        } else if (score >= 60) {
            return "Задовільно";
        } else {
            return "Незадовільно";
        }
    }
    public static String getEctsGrade(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else if (score >= 50) {
            return "E";
        } else {
            return "F";
        }
    }
    private int getSession(String course){
        if (Objects.equals(sessionRepo.findAll().get(0).getSession(), "Зимова")){
            return Integer.parseInt(course) - 1;
        } else return Integer.parseInt(course);

    }




}
