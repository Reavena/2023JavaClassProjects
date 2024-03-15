module com.example.studentattendence {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;


    opens com.example.studentattendence to javafx.fxml;
    exports com.example.studentattendence;
}