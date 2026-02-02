package controller;


import javafx.collections.FXCollections;
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
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	@FXML private ListView<Note> openNotesList;
	
	Note currentNote;
	private boolean isViewing = false;
	private ObservableList<Note> notes = FXCollections.observableArrayList();


	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		openNotesList.setItems(notes);
	}

	
	private void setCurrentNote() {
		currentNote = new Note(titleField.getText(), noteArea.getText());
	}
	
	public void loadNote(Note note) {
	    titleField.setText(note.getTitle());
	    noteArea.setText(note.getText());
	}
	
	public void load() throws IOException {
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
	    	
	    	
	    	currentNote = new Note(fileTitle, fileText);
	        noteArea.setText(currentNote.getText());
	        titleField.setText(currentNote.getTitle());
	    }
	    view();
	}
	
	private void saveTextToFile(String content, File file) {
	    try (FileWriter writer = new FileWriter(file)) {
	        writer.write(content);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void save() {
		FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Note");

	    // Default extension
	    FileChooser.ExtensionFilter extFilter =
	            new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
	    fileChooser.getExtensionFilters().add(extFilter);

	    // Suggest a default name based on the title
	    fileChooser.setInitialFileName(titleField.getText() + ".txt");

	    File file = fileChooser.showSaveDialog(noteArea.getScene().getWindow());

	    if (file != null) {
	        saveTextToFile(noteArea.getText(), file);
	    }
	}
	
	public void view() {
		setCurrentNote();
		
		Parser parser = Parser.builder().build();
		Node document = parser.parse(currentNote.getText());
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String html = renderer.render(document);
		
		WebEngine engine = webView.getEngine();
		engine.loadContent(html, "text/html");
	}
	
	public void display() {
	    if (!isViewing) {
	        view();
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
