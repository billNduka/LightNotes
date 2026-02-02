package model;

public class Note {
	private String title;
	private String text;
	private String path;
	
	public Note(String title, String text) {
		this.title = title;
		this.text = text;
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
	
	@Override
	public String toString() {
		return title;
	}
}
