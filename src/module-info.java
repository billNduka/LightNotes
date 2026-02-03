module LightNotes {
    requires transitive javafx.graphics;
    requires org.commonmark;
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
	requires javafx.base;
    requires com.google.gson;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;

    exports application;
}
