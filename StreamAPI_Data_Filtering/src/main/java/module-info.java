module com.example.streamapi_data_filtering {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.streamapi_data_filtering to javafx.fxml;
    exports com.example.streamapi_data_filtering;
}