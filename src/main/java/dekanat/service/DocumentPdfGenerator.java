package dekanat.service;


import dekanat.Student;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class DocumentPdfGenerator {

    public DocumentPdfGenerator() {
    }

    public void generateDocument(){ };

    public void generate(){
        String inputFilePath = "uploads/test.docx";
        String tempFilePath = "uploads/mcontrol1u.docx";
        String finalFilePath = "uploads/mcontrol1u.pdf";

        List<Student> students = List.of(
                new Student(1, "Іванов Іван Іванович", "123456", 55),
                new Student(2, "Петров Петро Петрович", "234567", 48),
                new Student(3, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(4, "Іванов Іван Іванович", "123456", 55),
                new Student(5, "Петров Петро Петрович", "234567", 48),
                new Student(6, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(7, "Іванов Іван Іванович", "123456", 55),
                new Student(8, "Петров Петро Петрович", "234567", 48),
                new Student(9, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(10, "Іванов Іван Іванович", "123456", 55),
                new Student(11, "Петров Петро Петрович", "234567", 48),
                new Student(12, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(13, "Іванов Іван Іванович", "123456", 55),
                new Student(14, "Петров Петро Петрович", "234567", 48),
                new Student(15, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(16, "Іванов Іван Іванович", "123456", 55),
                new Student(17, "Петров Петро Петрович", "234567", 48),
                new Student(18, "Сидорова Марія Олександрівна", "345678", 60),
                new Student(19, "Іванов Іван Іванович", "123456", 55)
                // Додайте більше студентів за потреби
        );


        try (FileInputStream fis = new FileInputStream(inputFilePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            // Замінити теги у текстових параграфах
            replaceTagsInParagraphs(document);

            // Замінити теги у таблицях
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        replaceTagsInParagraphs(cell);
                    }
                }
            }

            List<XWPFTable> tables = document.getTables();

            // Перевірити чи у документі є хоча б дві таблиці
            if (tables.size() < 2) {
                System.out.println("У документі немає другої таблиці.");
                return;
            }

            // Отримати другу таблицю (індексація з 0)
            XWPFTable table = tables.get(1);

            // Зберегти ширину стовпців другої таблиці
            List<BigInteger> columnWidths = new ArrayList<>();
            for (XWPFTableCell cell : table.getRow(0).getTableCells()) {
                if (cell.getCTTc().getTcPr() != null && cell.getCTTc().getTcPr().getTcW() != null) {
                    columnWidths.add((BigInteger) cell.getCTTc().getTcPr().getTcW().getW());
                }
            }

            // Максимальна кількість рядків на одній сторінці
            int maxRowsPerPage = 20;

            int currentRow = 0;
            for (Student student : students) {
                if (currentRow >= maxRowsPerPage) {
                    // Додаємо розрив сторінки
                    XWPFParagraph paragraph = document.createParagraph();
                    paragraph.setPageBreak(true);

                    // Створюємо нову таблицю і додаємо заголовок
                    table = document.createTable();

                    // Встановити ширину стовпців нової таблиці

                    // Створюємо рядок заголовка з значеннями 1, 2, 3, 4, 5
                    XWPFTableRow headerRow = table.getRow(0); // Отримуємо перший рядок нової таблиці
                    setCellText1(headerRow.getCell(0), "1");
                    setCellText2(headerRow.addNewTableCell(), "2");
                    setCellText3(headerRow.addNewTableCell(), "3");
                    setCellText4(headerRow.addNewTableCell(), "4");
                    setCellText5(headerRow.addNewTableCell(), "5");

                    currentRow = 0; // Починаємо знову рахувати рядки на новій сторінці
                }

                XWPFTableRow newRow = table.createRow();

                // Перевіряємо та ініціалізуємо комірки, якщо необхідно
                ensureCellExists(newRow, 0);
                ensureCellExists(newRow, 1);
                ensureCellExists(newRow, 2);
                ensureCellExists(newRow, 3);
                ensureCellExists(newRow, 4);

                setCellText(newRow.getCell(0), String.valueOf(student.getIndex()));
                setCellTextPIB(newRow.getCell(1), student.getName());
                setCellText(newRow.getCell(2), student.getStudentNumber());
                setCellText(newRow.getCell(3), String.valueOf(student.getMark()));
                setCellText(newRow.getCell(4), ""); // Підпис викладача

                currentRow++;
            }

            // Зберегти змінений документ
            try (FileOutputStream fos = new FileOutputStream(tempFilePath)) {
                document.write(fos);

                try {
                    // Вкажіть шлях до вашого JAR файлу
                    String jarPath = "C:\\Users\\poulp\\IdeaProjects\\WordToDocxConverter-1.0-SNAPSHOT-jar-with-dependencies.jar";
                    // Вкажіть аргументи для JAR файлу


                    // Виконайте JAR файл
                    runJar(jarPath, tempFilePath, finalFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ensureCellExists(XWPFTableRow row, int cellIndex) {
        // Додаємо нову комірку, якщо вона не існує
        if (row.getCell(cellIndex) == null) {
            row.createCell();
        }
    }

    private static void replaceTagsInParagraphs(IBody body) {
        for (XWPFParagraph paragraph : body.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                String text = run.getText(0);
                if (text != null) {
                    text = text.replace("{facultyName}", "Факультет транспортних та інформаційних технологій");
                    text = text.replace("{specialityName}", "Інформаційна безпека в комп'ютеризованих системах");
                    text = text.replace("{courseNumber}", "4");
                    text = text.replace("{groupName}", "ІБК-4-1");
                    text = text.replace("{studyYear}", "2024/2025");
                    text = text.replace("{day}", "23");
                    text = text.replace("{month}", "серпня");
                    text = text.replace("{year}", "2024");
                    text = text.replace("{disciplineName}", "Інформаційна безпека");
                    text = text.replace("{sN}", "7");
                    text = text.replace("{controlTypeName}", "Перший модульний контроль");
                    text = text.replace("{h}", "120");
                    text = text.replace("{f}", "Іванов Іван Іванович");
                    text = text.replace("{s}", "Клочан Арсен Євгенійович");
                    run.setText(text, 0); // Установка оновленого тексту
                }
            }
        }
    }

    private static void setCellText(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        paragraph.setAlignment(ParagraphAlignment.CENTER); // По горизонталі
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // По вертикалі
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11

        run.setText(text); // Встановити текст комірки
    }

    private static void setCellTextPIB(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11

        run.setText(text); // Встановити текст комірки
    }

    private static void setCellText1(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        paragraph.setAlignment(ParagraphAlignment.CENTER); // По горизонталі
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // По вертикалі
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11

        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(27));

        run.setText(text); // Встановити текст комірки
    }
    private static void setCellText2(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11

        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(4000));

        run.setText(text); // Встановити текст комірки
    }
    private static void setCellText3(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        paragraph.setAlignment(ParagraphAlignment.CENTER); // По горизонталі
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // По вертикалі
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11
        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2300));
        run.setText(text); // Встановити текст комірки
    }
    private static void setCellText4(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        paragraph.setAlignment(ParagraphAlignment.CENTER); // По горизонталі
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // По вертикалі
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11
        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2300));
        run.setText(text); // Встановити текст комірки
    }
    private static void setCellText5(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0); // Отримати перший параграф комірки
        paragraph.setAlignment(ParagraphAlignment.CENTER); // По горизонталі
        paragraph.setVerticalAlignment(TextAlignment.CENTER); // По вертикалі
        XWPFRun run = paragraph.createRun();
        run.setFontSize(10); // Встановити розмір шрифту на 11
        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(2300));
        run.setText(text); // Встановити текст комірки
    }

    public static void runJar(String jarPath, String inputFilePath, String outputFilePath) throws IOException, InterruptedException {
        // Створіть команду для запуску JAR
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jarPath);
        command.add(inputFilePath);
        command.add(outputFilePath);

        // Використання ProcessBuilder для запуску процесу
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.inheritIO(); // Це дозволяє виводити stdout і stderr у консоль
        Process process = processBuilder.start();

        // Очікування завершення процесу
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("JAR файл виконано успішно.");
        } else {
            System.out.println("Сталася помилка під час виконання JAR файлу.");
        }
    }

}