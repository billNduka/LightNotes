package model;

import java.time.LocalDateTime;

public class Note {
	private String title;
	private String text;
	private String path;
    private LocalDateTime lastOpened;
	
	public Note(String title, String text, String path, LocalDateTime lastOpened) {
		this.title = title;
		this.text = text;
        this.path = path;
        this.lastOpened = lastOpened;
	}
	
	public String getTitle() {
		return title;
	}
	public String getText() {
		return text;
	}
	public String getPath() {
		return path;
	}
    public LocalDateTime getLastOpened() {
        return lastOpened;
    }
	
	@Override
	public String toString() {
		return title;
	}
}
