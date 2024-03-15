module com.example.bustopaskola {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.bustopaskola to javafx.fxml;
    exports com.example.bustopaskola;
    exports com.example.bustopaskola.Controllers;
    opens com.example.bustopaskola.Controllers to javafx.fxml;
    opens com.example.bustopaskola.Calculations to javafx.base;
}