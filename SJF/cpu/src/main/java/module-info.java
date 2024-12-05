module com.example.cpu {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.cpu to javafx.fxml;
    exports com.example.cpu;
}