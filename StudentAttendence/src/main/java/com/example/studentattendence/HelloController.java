package com.example.studentattendence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class HelloController implements Initializable {

    @FXML
    private Button addGroup;

    @FXML
    private Button addStudent;

    @FXML
    private Button addToGroup;

    @FXML
    private DatePicker listDateFrom;

    @FXML
    private DatePicker listDateUntil;

    @FXML
    private Button listStudents;

    @FXML
    private Button loadCSV;

    @FXML
    private Button removeFromGroup;

    @FXML
    private Button removeGroup;

    @FXML
    private Button removeStudent;

    @FXML
    private Button saveCSV;

    @FXML
    private Button savePDF;

    @FXML
    private ComboBox<String> selectScope;

    @FXML
    private CheckBox studentAttendance;

    @FXML
    private DatePicker studentDate;

    @FXML
    private TextField studentGroup;

    @FXML
    private TextField studentID;

    @FXML
    private TableView<Student> table;

    @FXML
    private TableColumn<Student, Boolean> tableAttended;

    @FXML
    private TableColumn<Student, LocalDate> tableDate;

    @FXML
    private TableColumn<Student, String> tableGroup;

    @FXML
    private TableColumn<Student, String> tableID;

    @FXML
    void listStudentsClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("table.fxml"));

        String tempID = studentID.getText();
        String tempGroup = studentGroup.getText();
        String tempScope = selectScope.getValue();
        if(tempScope == null) {
            tempScope = "All";
        }

        LocalDate tempFrom = listDateFrom.getValue();
        if(tempFrom == null) {
            tempFrom = LocalDate.of(1970,1,1);
        }

        LocalDate tempUntil = listDateUntil.getValue();
        if(tempUntil == null) {
            tempUntil = LocalDate.of(2036,1,1);
        }

        Set<Student> tempSet = new HashSet<>();

        if(tempScope.equals("All"))
            for(Student tempStud : StudentSingleton.getInstance().getStudentList()) {
                if(tempStud.getDate().isAfter(tempFrom) && tempStud.getDate().isBefore(tempUntil)) {
                    tempSet.add(tempStud);
                }
            }

        if(tempScope.equals("By ID"))
            for(Student tempStud : StudentSingleton.getInstance().getStudentList()) {
                if(tempStud.getDate().isAfter(tempFrom) && tempStud.getDate().isBefore(tempUntil) && tempStud.getID().equals(tempID)) {
                    tempSet.add(tempStud);
                }
            }

        if(tempScope.equals("By Group"))
            for(Student tempStud : StudentSingleton.getInstance().getStudentList()) {
                if(tempStud.getDate().isAfter(tempFrom) && tempStud.getDate().isBefore(tempUntil) && tempStud.getGroup().equals(tempGroup)) {
                    tempSet.add(tempStud);
                }
            }

        StudentSingleton.getInstance().setResults( tempSet );

        Parent parent;
        try {
            parent = loader.load(); //nuo cia
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("Table");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectScope.getItems().addAll("All", "By ID", "By Group");

        tableDate.setCellValueFactory(features -> features.getValue().dateProperty());  // Use dateProperty() from Person class
        tableID.setCellValueFactory(features -> features.getValue().IDProperty());
        tableAttended.setCellValueFactory(features -> features.getValue().attendanceProperty());
        tableGroup.setCellValueFactory(features -> features.getValue().groupProperty());
        table.setItems(StudentSingleton.getInstance().getResults());
    }


    Student obtainData() {
        String ID = studentID.getText();
        String group = studentGroup.getText();
        LocalDate date = studentDate.getValue();
        boolean attendance = studentAttendance.isSelected();

        return new Student(ID,group,attendance,date);
    }

    @FXML
    void addStudentClick(ActionEvent event) {
        Student stud = obtainData();

        StudentSingleton.getInstance().addStudent(stud);
        System.out.println("Success!");
    }

    @FXML
    void addGroupClick(ActionEvent event) {
        Student stud = obtainData();

        StudentSingleton.getInstance().addGroup(stud);
        System.out.println("Success!");
    }


    @FXML
    void removeGroupClick(ActionEvent event) {
        Student stud = obtainData();

        StudentSingleton.getInstance().removeGroup(stud);
    }

    @FXML
    void removeStudentClick(ActionEvent event) {
        Student stud = obtainData();

        StudentSingleton.getInstance().removeStudent(stud);
    }

    @FXML
    void saveCSVClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text files", "*.csv")
        );

        File out = fileChooser.showSaveDialog(Stage.getWindows().get(0));

        if(out == null) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Date,ID,Group,Attendance\n");
        for(Student stud : StudentSingleton.getInstance().getStudentList()) {
            builder.append(stud.getDate()).append(",");
            builder.append(stud.getID()).append(",");
            builder.append(stud.getGroup()).append(",");
            builder.append(stud.getAttendance()).append("\n");
        }

        String result = builder.toString();

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(out);
            stream.write(result.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void loadCSVClick(ActionEvent event) {
        //Open file explorer
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(Stage.getWindows().get(0));

        if (selectedFile != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                br.readLine(); // Skip header line

                while ((line = br.readLine()) != null) {
                    //Split line with delimiter ,
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        // load the parts to create a Student object
                        LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_DATE);
                        String ID = parts[1];
                        String group = parts[2];
                        boolean attendance = Boolean.parseBoolean(parts[3]);

                        // add it to singleton
                        Student student = new Student(ID, group, attendance, date);
                        StudentSingleton.getInstance().addStudent(student);
                    }
                }

                // Update TableView
                table.setItems(StudentSingleton.getInstance().getResults());
                System.out.println("CSV file loaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                //
            }
        }
    }

    @FXML
    void savePDFClick(ActionEvent event) {
        // file explorer
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File out = fileChooser.showSaveDialog(Stage.getWindows().get(0));

        if (out == null) {
            return; //user exited
        }

        try {
            // Create a new PDF document and add a page
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Get the page and set up a content stream
            PDPageTree pages = document.getDocumentCatalog().getPages();
            PDPageContentStream contentStream = new PDPageContentStream(document, pages.get(0));

            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.setLeading(14.5f);    //!! Leading - vertical space between lines
            contentStream.newLineAtOffset(50, 750);

            // Write header
            contentStream.showText("Date          ID          Group          Attendance");
            contentStream.newLine();

            // Write data
            for (Student stud : StudentSingleton.getInstance().getStudentList()) {
                String line = String.format("%s          %s          %s          %s",
                        stud.getDate(), stud.getID(), stud.getGroup(), stud.getAttendance());
                contentStream.showText(line);
                contentStream.newLine();

            }

            contentStream.endText();
            contentStream.close();

            document.save(out);
            document.close();

            System.out.println("PDF file saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            //
        }
    }


}