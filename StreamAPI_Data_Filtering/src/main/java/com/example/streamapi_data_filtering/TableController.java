package com.example.streamapi_data_filtering;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TableController implements Initializable {
    @FXML
    private TableView<Record> dataTable;
    @FXML
    private TableColumn<Record, String> personBirthDate;
    @FXML
    private TableColumn<Record,String> personCountry;
    @FXML
    private TableColumn<Record, String> personDomainName;
    @FXML
    private TableColumn<Record, String> personEmail;
    @FXML
    private TableColumn<Record, String> personFirstName;
    @FXML
    private TableColumn<Record, String> personGender;
    @FXML
    private TableColumn<Record, Integer> personID;
    @FXML
    private TableColumn<Record, String> personLastName;
    @FXML
    private DatePicker toDate;
    @FXML
    private DatePicker fromDate;
    @FXML
    private ChoiceBox<String> sortChoice;
    @FXML
    private ChoiceBox<String> sortDirection;
    private ObservableList<Record> filteredData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        personID.setCellValueFactory(new PropertyValueFactory<>("id"));
        personFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        personLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        personEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        personGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        personCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        personDomainName.setCellValueFactory(new PropertyValueFactory<>("domainName"));
        personBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));

        ObservableList<String> sortDir = FXCollections.observableArrayList("Ascending", "Descending");
        sortDirection.setValue("Ascending");
        sortDirection.setItems(sortDir);

        ObservableList<String> sortOptions = FXCollections.observableArrayList("ID", "First name", "Last name", "Email", "Gender", "Country", "Domain name");
        sortChoice.setValue("ID");
        sortChoice.setItems(sortOptions);

        filteredData = FXCollections.observableArrayList();

        fromDate.setValue(LocalDate.of(2000, 1, 1));
        toDate.setValue(LocalDate.now());

//TO FIX
        loadData("C:/Users/gabij/IdeaProjects/StreamAPI_Data_Filtering/src/main/resources/com/example/streamapi_data_filtering/MOCK_DATA1.csv");
        loadData("C:/Users/gabij/IdeaProjects/StreamAPI_Data_Filtering/src/main/resources/com/example/streamapi_data_filtering/MOCK_DATA2.csv");
        loadData("C:/Users/gabij/IdeaProjects/StreamAPI_Data_Filtering/src/main/resources/com/example/streamapi_data_filtering/MOCK_DATA3.csv");
    }

    private void loadData(String filePath) {

        ReadCSV reader = new ReadCSV(this, filePath);

        Thread thread = new Thread(reader);
        thread.start();
    }

    public synchronized void addPerson(Record person) {
        dataTable.getItems().add(person);
        filteredData.add(person);
    }

    @FXML
    void sortByDate(ActionEvent event) {
        // Get the selected 'from' and 'to' dates from the UI
        LocalDate fromDateValue = fromDate.getValue();
        LocalDate toDateValue = toDate.getValue();

        // Create a predicate to filter records within the specified date range
        Predicate<Record> dateRangePredicate = person -> isWithinDateRange(person.getBirthDate(), fromDateValue, toDateValue);

        // Apply the date range filter, sort the results by birth date, and collect them back to an ObservableList
        filteredData = filteredData.stream()
                .filter(dateRangePredicate)
                .sorted(Comparator.comparing(Record::getBirthDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Update the dataTable with the filtered and sorted data
        dataTable.setItems(filteredData);
    }

    // Helper method to check if a date is within a specified date range
    private boolean isWithinDateRange(LocalDate date, LocalDate fromDate, LocalDate toDate) {
        return date.isAfter(fromDate.minusDays(1)) && date.isBefore(toDate.plusDays(1));
    }

    @FXML
    void sortByDirection(ActionEvent event) {
        // Get the selected sorting field and direction from the UI
        String selectedField = sortChoice.getValue();
        String selectedDirection = sortDirection.getValue();

        // Sort the list using Comparator.comparing
        List<Record> sortedList = dataTable.getItems().stream()
                .sorted((r1, r2) -> {
                    // Use Comparator.comparing based on the selected field
                    switch (selectedField) {
                        case "First name":
                            return compareString(r1.getFirstName(), r2.getFirstName(), selectedDirection);
                        case "Last name":
                            return compareString(r1.getLastName(), r2.getLastName(), selectedDirection);
                        case "Gender":
                            return compareString(r1.getGender(), r2.getGender(), selectedDirection);
                        case "Country":
                            return compareString(r1.getCountry(), r2.getCountry(), selectedDirection);
                        case "Domain name":
                            return compareString(r1.getDomainName(), r2.getDomainName(), selectedDirection);
                        case "Email":
                            return compareString(r1.getEmail(), r2.getEmail(), selectedDirection);
                        case "ID":
                            return compareInt(r1.getId(), r2.getId(), selectedDirection);
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());

        // Update the dataTable with the sorted list
        dataTable.getItems().setAll(sortedList);
    }

    private int compareString(String s1, String s2, String direction) {
        return ("Ascending".equals(direction)) ? s1.compareTo(s2) : s2.compareTo(s1);
    }

    private int compareInt(int i1, int i2, String direction) {
        return ("Ascending".equals(direction)) ? Integer.compare(i1, i2) : Integer.compare(i2, i1);
    }

    public class ReadCSV implements Runnable {
        private final TableController tableController;
        private final String filePath;

        public ReadCSV(TableController tableController, String filePath) {
            this.tableController = tableController;
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {

                Files.lines(Paths.get(filePath))
                        .skip(1) // Skip the header row
                        .forEach(this::processLine);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void processLine(String data) {
            Record person = convertToPerson(data);
            tableController.addPerson(person);

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private Record convertToPerson(String data) {
            String fieldDelimiter = ",";
            String[] fields = data.split(fieldDelimiter, -1);

            return new Record(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7]);
        }
    }
}