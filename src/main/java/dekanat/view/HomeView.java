package dekanat.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dekanat.component.MainLayout;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "", layout = MainLayout.class)
@PageTitle("Головна | Dekanat CRM")
public class HomeView extends VerticalLayout {

    public HomeView() {
        // Створення компонента для фонового зображення
        Image backgroundImage = new Image("images/background2.jpg", "background");
        backgroundImage.getStyle().set("position", "absolute");
        backgroundImage.getStyle().set("top", "0");
        backgroundImage.getStyle().set("left", "0");
        backgroundImage.getStyle().set("width", "100%");
        backgroundImage.getStyle().set("height", "100%");
        backgroundImage.getStyle().set("object-fit", "cover");
        backgroundImage.getStyle().set("z-index", "-1"); // Фон за текстом

        // Додаємо прозорий чорний шар
        Div overlay = new Div();
        overlay.getStyle().set("position", "absolute");
        overlay.getStyle().set("top", "0");
        overlay.getStyle().set("left", "0");
        overlay.getStyle().set("width", "100%");
        overlay.getStyle().set("height", "100%");
        overlay.getStyle().set("background-color", "rgba(0, 0, 0, 0.5)"); // Прозорий чорний колір
        overlay.getStyle().set("z-index", "0");

        // Створення персоналізованого привітання
        String userName = "Іванов Іван Іванович"; // Замість цього значення можна брати ім'я з сесії або бази даних
        Span welcomeText = new Span(
                "Шановний " + userName + "! Ласкаво просимо до системи Деканат! " +
                        "Ваш внесок у розвиток навчального процесу є неоціненним. Разом ми створюємо якісну освіту для наших студентів."
        );
        welcomeText.getStyle().set("font-size", "48px");
        welcomeText.getStyle().set("font-weight", "bold");
        welcomeText.getStyle().set("color", "white");
        welcomeText.getStyle().set("text-shadow", "2px 2px 4px rgba(0, 0, 0, 0.8)");
        welcomeText.getStyle().set("text-align", "center");
        welcomeText.getStyle().set("opacity", "0"); // Початкова прозорість для анімації
        welcomeText.getStyle().set("transition", "opacity 3s ease-in-out"); // Плавна поява
        welcomeText.getStyle().set("z-index", "1");
        welcomeText.getStyle().set("padding", "20px");

        // Використовуємо FlexLayout для централізованого вирівнювання
        FlexLayout flexLayout = new FlexLayout();
        flexLayout.setSizeFull();
        flexLayout.getStyle().set("display", "flex");
        flexLayout.getStyle().set("align-items", "center"); // Вертикальне вирівнювання
        flexLayout.getStyle().set("justify-content", "center"); // Горизонтальне вирівнювання
        flexLayout.add(welcomeText);

        // Додаємо фон, накладення та текст у основний макет
        Div backgroundWrapper = new Div();
        backgroundWrapper.getStyle().set("position", "relative");
        backgroundWrapper.getStyle().set("height", "100vh");
        backgroundWrapper.getStyle().set("width", "100%");
        backgroundWrapper.add(backgroundImage, overlay, flexLayout);

        // Стилізація основного макету
        setSizeFull();
        setMargin(false);
        setPadding(false);
        setSpacing(false);

        // Додаємо всі елементи до головного макету
        add(backgroundWrapper);

        // Викликаємо анімацію після рендерингу сторінки
        getElement().executeJs("setTimeout(function() { $0.style.opacity = '1'; }, 100);", welcomeText.getElement());
    }
}
