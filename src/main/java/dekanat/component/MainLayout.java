package dekanat.component;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import dekanat.entity.RolesEntity;
import dekanat.repository.RolesRepo;
import dekanat.service.CustomUserDetailsService;
import dekanat.service.SecurityService;
import dekanat.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final CustomUserDetailsService customUserDetailsService;

    public MainLayout(@Autowired SecurityService securityService, AuthenticationContext authenticationContext, CustomUserDetailsService customUserDetailsService, RolesRepo rolesRepo) {
        this.securityService = securityService;
        this.customUserDetailsService = customUserDetailsService;

        List<String> roleAdmin = rolesRepo.findByAccessType(1).stream().map(RolesEntity::getTitle).toList();
        List<String> roleDekanat = rolesRepo.findByAccessType(2).stream().map(RolesEntity::getTitle).toList();
        List<String> roleKafedra = rolesRepo.findByAccessType(3).stream().map(RolesEntity::getTitle).toList();


        authenticationContext.getAuthenticatedUser(UserDetails.class).ifPresent(user -> {
            boolean isAdmin = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(roleAdmin::contains);
            System.out.println(isAdmin);
            boolean isDekanat = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(roleDekanat::contains);
            System.out.println(isDekanat);
            boolean isKafedra = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(roleKafedra::contains);
            System.out.println(isKafedra);
            if (isAdmin){
                createHeaderDekanat();
                createDrawerDekanat();
            }
            if (isDekanat){
                createHeaderDekanat();
                createDrawerDekanat();
            }
            if (isKafedra){
                createHeaderKafedra();
                UI.getCurrent().access(() -> UI.getCurrent().navigate("marks"));
            }
        });

    }

    private void createHeaderDekanat() {
        H1 logo = new H1("Dekanat CRM");
        logo.addClassNames("text-l", "m-m");
        Button logout = new Button("Вихід", e -> securityService.logout());



        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);



        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);


        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawerDekanat() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.MENU, "Головна", HomeView.class),
                createTab(VaadinIcon.CLIPBOARD_CHECK, "Навчальні плани", TrainingPlansView.class),
                createTab(VaadinIcon.LINE_BAR_CHART, "Успішність", SuccessView.class),
                createTab(VaadinIcon.ABACUS, "Боржники", DebtorView.class),
                createTab(VaadinIcon.LIST_SELECT, "Друк інформації", StudentCardView.class),
                createTab(VaadinIcon.USER_CARD, "Перегляд карток", ReviewingCardsView.class),
                createTab(VaadinIcon.PENCIL, "Введення оцінок", EnterMarksView.class),
                createTab(VaadinIcon.ARCHIVE, "Архів", ArchiveView.class),
                createTab(VaadinIcon.BOOK, "Довідники", HandbookView.class)
//                ,
//                createTab(VaadinIcon.USER, "Адмін", AdminView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> navigationTarget) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(navigationTarget);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    private void createHeaderKafedra() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email = ((UserDetails) principal).getUsername();
        H1 logo = new H1(customUserDetailsService.getPIB(email));
        logo.addClassNames("text-l", "m-m");
        Button logout = new Button("Вихід", e -> securityService.logout());



        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);



        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);


        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

}
