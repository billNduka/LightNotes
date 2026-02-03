package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    // Hidden folder in the user’s home directory
    Path APP_DIR = Path.of(System.getProperty("user.home"), ".lightnotes");

    // The file that will store recent notes info
    Path RECENTS_FILE = APP_DIR.resolve("recent.txt");

    private void createPersistentStorage(){
        try {
            Files.createDirectories(APP_DIR);  // creates folder if it doesn’t exist
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        createPersistentStorage();

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainTextArea.fxml"));
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("LightNotes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
