package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Note;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class RecentNotesManager {

    private static final Path APP_DIR = Path.of(System.getProperty("user.home"), ".lightnotes");
    private static final Path RECENTS_FILE = APP_DIR.resolve("recent.json");

    private final Gson gson = new Gson();
    private final ObservableList<Note> recentNotes = FXCollections.observableArrayList();

    public RecentNotesManager() {
        loadRecentNotes();
    }

    public ObservableList<Note> getRecentNotes() {
        return recentNotes;
    }

    public void addNote(Note note) {
        // Remove duplicates
        recentNotes.removeIf(n -> n.getPath().equals(note.getPath()));

        // Add to top
        recentNotes.add(0, note);

        // Limit size (e.g., 5)
        if (recentNotes.size() > 5) {
            recentNotes.remove(5);
        }

        saveRecentNotes();
    }

    private void saveRecentNotes() {
        try {
            Files.createDirectories(APP_DIR);
            try (FileWriter writer = new FileWriter(RECENTS_FILE.toFile())) {
                gson.toJson(recentNotes, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecentNotes() {
        try {
            if (!Files.exists(RECENTS_FILE)) return;

            Type listType = new TypeToken<List<Note>>() {}.getType();
            List<Note> loaded = gson.fromJson(Files.newBufferedReader(RECENTS_FILE), listType);
            recentNotes.setAll(loaded);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
