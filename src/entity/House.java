package entity;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class House {
	
	private int id;
	private String name;
	private String words;
	private String seat;
	private int x;
	private int y;
	private String region;
	private ArrayList<String> persons;
	private Image emblem;
	private ArrayList<Color> flag;
	
	public House() {
		id = 0;
		name = "";
		words = "";
		seat = "";
		x = 0;
		y = 0;
		region = "";
		persons = new ArrayList<String>();		
		flag = new ArrayList<Color>();
	}

	public House(String name, String words, String seat, int x, int y, String region,
			ArrayList<String> persons, Image emblem, ArrayList<Color> flag) {
		this.name = name;
		this.words = words;
		this.seat = seat;
		this.x = x;
		this.y = y;
		this.region = region;
		this.persons = persons;
		this.emblem = emblem;
		this.flag = flag;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}
	
	public String getSeat() {
		return seat;
	}
	
	public void setSeat(String seat) {
		this.seat = seat;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}

	public ArrayList<String> getPersons() {
		return persons;
	}
	
	public void setPersons(ArrayList<String> persons) {
		this.persons = persons;
	}

	public Image getEmblem() {
		return emblem;
	}
	
	public void setEmblem(Image emblem) {
		this.emblem = emblem;
	}

	public ArrayList<Color> getFlag() {
		return flag;
	}
	
	public void setFlag(ArrayList<Color> flag) {
		this.flag = flag;
	}

	
}
