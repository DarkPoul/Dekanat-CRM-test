package dekanat;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import org.springframework.stereotype.Component;

@Component
@PWA(name = "Dekanat", shortName = "DT", iconPath = "favicon.ico")

public class AppShell implements AppShellConfigurator {
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "favicon.ico", "256x256");
    }
}
