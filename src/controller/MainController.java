package controller;


import javafx.collections.FXCollections;

import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.io.FileWriter;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import java.io.IOException;
import model.Note;

public class MainController implements Initializable{
	@FXML private TextField titleField;
	@FXML private TextArea noteArea;
	@FXML private WebView webView;
	@FXML private BorderPane rootPane;
	@FXML private ListView<Note> recentNotesList;
	
	Note currentNote;
    File currentFile;
	private boolean isViewing = false;
	private ObservableList<Note> notes = FXCollections.observableArrayList();


	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		recentNotesList.setItems(notes);
	}

    private void setCurrentNote(String fileTitle, String fileText, String filePath, LocalDateTime currentTime){
        currentNote = new Note(fileTitle, fileText, filePath, currentTime);
    }
    private void setCurrentFile(File file){
        currentFile = file;
    }
    private void loadNoteIntoEditor(Note note) {
        titleField.setText(note.getTitle());
        noteArea.setText(note.getText());
    }


    public void loadFileToNote() throws IOException {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Load Note");
	    
	    FileChooser.ExtensionFilter extFilter =
	            new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
	    fileChooser.getExtensionFilters().add(extFilter);

	    File selectedFile = fileChooser.showOpenDialog(noteArea.getScene().getWindow());
	    if (selectedFile != null) {
	    	String fileText = Files.readString(selectedFile.toPath());
	    	String fileName = selectedFile.getName();
	    	String fileTitle = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
            String filePath = selectedFile.getPath();
	    	LocalDateTime currentTime = LocalDateTime.now();
	    	
	    	setCurrentNote(fileTitle, fileText, filePath, currentTime);
            setCurrentFile(selectedFile);
            loadNoteIntoEditor(currentNote);
	    }
	    displayWebview();
	}
	
	private void saveTextToFile (String content, File file) {
	    try (FileWriter writer = new FileWriter(file)) {
	        writer.write(content);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void save() {
        if (currentFile != null) {
            saveTextToFile(noteArea.getText(), currentFile);
        } else{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Note");

            // Default extension
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            // Suggest a default name based on the title
            fileChooser.setInitialFileName(titleField.getText() + ".txt");

            File file = fileChooser.showSaveDialog(noteArea.getScene().getWindow());

            if (file != null) {
                saveTextToFile(noteArea.getText(), file);
            }
        }
	}

    public void displayWebview() {
        String text = noteArea.getText();

        Parser parser = Parser.builder().build();
        Node document = parser.parse(text);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        webView.getEngine().loadContent(html, "text/html");
    }


    public void displayEditor() {
	    if (!isViewing) {
	        displayWebview();
	        rootPane.setRight(null);      
	        rootPane.setCenter(webView);  
	        isViewing = true;
	    } else {
	        rootPane.setCenter(noteArea); 
	        rootPane.setRight(webView);    
	        isViewing = false;
	    }
	}

	
}
