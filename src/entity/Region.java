package entity;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Region {
	
	private int id;                 
	private String name;             
	private String fullName;
	private String coordinates;	     
	private int population;
	private String geography;
	private String history;
	private Polygon polygon; 
	
	public Region() {
		
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;				
		polygon = new Polygon();
		ArrayList<Double> arrayCoordinates = new ArrayList<Double>();
		String[] array = coordinates.split(",");
		for (String s : array) {   
			arrayCoordinates.add(Double.parseDouble(s));
		}		
		polygon.getPoints().addAll(arrayCoordinates);    // строим область по координатам	
		polygon.setOpacity(0);                           // сразу делаем ее прозрачной                    
		polygon.setFill(Color.RED);                      // пусть она красного цвета будет		
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public String getGeography() {
		return geography;
	}

	public void setGeography(String geography) {
		this.geography = geography;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public Polygon getPolygon() {
		return polygon;
	}	

}

