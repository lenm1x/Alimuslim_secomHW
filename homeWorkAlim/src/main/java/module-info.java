module ayaz.com.homeworkalim {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens kg.mega.project_javafx_1 to javafx.fxml;
    exports kg.mega.project_javafx_1;
}