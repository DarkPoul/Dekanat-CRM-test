package dekanat.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import dekanat.component.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@PageTitle("Адміністратор | Деканат")
@Route(value = "admin", layout = MainLayout.class)
@Component
@UIScope
@PermitAll
public class AdminView extends Div {

    private static final String UPLOAD_DIR = "uploads/";

    public AdminView(){
        Button uploadButton = new Button("Завантажити файл", e -> openUploadDialog());
        add(uploadButton);
    }

    private void openUploadDialog() {
        // Створюємо діалог для завантаження файлу
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Завантаження файлу");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            try {
                // Логіка збереження файлу
                saveFile(fileName, inputStream);
                Notification.show("Файл успішно завантажено!", 3000, Notification.Position.TOP_CENTER);
            } catch (IOException e) {
                Notification.show("Помилка при збереженні файлу: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }

            dialog.close();
        });

        dialog.add(upload);
        dialog.open();
    }

    private void saveFile(String fileName, InputStream inputStream) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        createUploadDirectory();
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    private void createUploadDirectory() {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                Notification.show("Не вдалося створити директорію для завантаження файлів: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void downloadFile(String fileName) {
        // Створення StreamResource для завантаження файлу
        StreamResource resource = new StreamResource(fileName, (InputStreamFactory) () -> {
            try {
                return Files.newInputStream(Paths.get(UPLOAD_DIR + fileName));
            } catch (IOException e) {
                Notification.show("Помилка при завантаженні файлу: " + e.getMessage(), 5000, Notification.Position.TOP_CENTER);
                return null;
            }
        });

        // Відкриття ресурсу для завантаження у новій вкладці браузера
//        getUI().ifPresent(ui -> ui.getPage().open(resource, "_blank"));
    }



}
