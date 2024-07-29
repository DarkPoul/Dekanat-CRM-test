package dekanat.view;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dekanat.component.MainLayout;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Головна | Dekanat CRM")
public class HomeView extends VerticalLayout {

    public HomeView(){
        H3 hi = new H3("Hello");
        add(hi);
    }



}
