package application;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import util.Setting;
import entity.House;

public class HouseList {

	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet rs;
	private String sql;

	private LinkedHashMap<String, House> list = new LinkedHashMap<>();

	public HouseList() {

		try {
			connection = DriverManager.getConnection(Setting.url,
					Setting.username, Setting.password);
			sql = "SELECT id, name, words, seat, x, y, region, emblem FROM gameofthrones.house";
			pStatement = connection.prepareStatement(sql);
			rs = pStatement.executeQuery();

			while (rs.next()) {
				House house = new House();
				house.setId(rs.getInt("id"));
				house.setName(rs.getString("name"));
				house.setWords(rs.getString("words"));
				house.setSeat(rs.getString("seat"));
				house.setX(rs.getInt("x"));
				house.setY(rs.getInt("y"));
				house.setRegion(rs.getString("region"));
				InputStream is = rs.getBinaryStream("emblem");   //    читаем blob поле с эмблемой
				house.setEmblem(new Image(is));
				house.setFlag(getFlag(house.getId()));
				house.setPersons(getPersons(house.getId()));
				list.put(house.getName(), house);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		/*  это если заполнять вручную */
		/*
		House house = new House();
		//---------------- House Stark ------------------------------------
		house.setName("House Stark");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_stark.png")));
		house.setWords("Winter is Coming"); 
		house.setSeat("Scattered (formerly Winterfell)");
		house.setX(191);
		house.setY(219);
		house.setRegion("The North");
		ArrayList<Color> flag = new ArrayList<>();
		flag.add(Color.WHITE);
		flag.add(Color.GREY);
		house.setFlag(flag);
		ArrayList<String> persons = new ArrayList<>();
		persons.add("Brandon Stark");
		persons.add("Eddard \"Ned\" Stark");
		persons.add("Catelyn Tully");
		persons.add("Robb Stark");
		persons.add("Sansa Stark");
		persons.add("Arya Stark");
		persons.add("Bran Stark");
		persons.add("Rickon Stark");
		persons.add("Jon Snow");
		house.setPersons(persons);
		list.put(house.getName(), house);
		//---------------- House Bolton ------------------------------------
		house = new House();
		house.setName("House Bolton");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_bolton.png")));
		house.setWords("Our Blades are Sharp"); 
		house.setSeat("The Dreadfort");
		house.setX(283);
		house.setY(206);
		house.setRegion("The North");
		flag = new ArrayList<>();
		flag.add(Color.RED);
		flag.add(Color.MAGENTA);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Walton");
		persons.add("Maester Tybald");
		persons.add("Walder Frey");
		house.setPersons(persons);
		list.put(house.getName(), house);				
		//---------------- House Karstark ------------------------------------
		house = new House();
		house.setName("House Karstark");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_karstark.png")));
		house.setWords("The Sun of Winter"); 
		house.setSeat("Karhold");
		house.setX(347);
		house.setY(167);
		house.setRegion("The North");
		flag = new ArrayList<>();
		flag.add(Color.BLACK);
		flag.add(Color.WHITE);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Robb Stark");
		persons.add("Harrion Karstark");
		persons.add("Torrhen Karstark");
		house.setPersons(persons);
		list.put(house.getName(), house);	
		//---------------- House Tully ------------------------------------
		house = new House();
		house.setName("House Tully");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_tully.png")));
		house.setWords("Family, Duty, Honor"); 
		house.setSeat("Scattered (formerly Riverrun)");
		house.setX(178);
		house.setY(521);
		house.setRegion("Riverlands");
		flag = new ArrayList<>();
		flag.add(Color.DARKBLUE);
		flag.add(Color.DARKRED);
		flag.add(Color.WHITE);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Hoster Tully");
		persons.add("Minisa Whent");
		persons.add("Catelyn Tully");
		house.setPersons(persons);
		list.put(house.getName(), house);			
		//---------------- House Frey ------------------------------------
		house = new House();
		house.setName("House Frey");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_frey.png")));
		house.setWords("Unknown"); 
		house.setSeat("The Twins");
		house.setX(180);
		house.setY(434);
		house.setRegion("Riverlands");
		flag = new ArrayList<>();
		flag.add(Color.DARKBLUE);
		flag.add(Color.SILVER);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Walder Frey");
		persons.add("Perra Royce");
		persons.add("Cyrenna Swann");
		persons.add("Amarei Crakehall");
		house.setPersons(persons);
		list.put(house.getName(), house);	
		//---------------- House Baratheon ------------------------------------
		house = new House();
		house.setName("House Baratheon");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_baratheon.png")));
		house.setWords("Unknown"); 
		house.setSeat("King's Landing");
		house.setX(279);
		house.setY(615);
		house.setRegion("Crownlands");
		flag = new ArrayList<>();
		flag.add(Color.ORANGE);
		flag.add(Color.DARKRED);
		flag.add(Color.GOLD);
		flag.add(Color.BLACK);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Robert I");
		persons.add("Cersei Lannister");
		persons.add("Joffrey I");
		persons.add("Myrcella Baratheon");
		persons.add("Tommen I");
		house.setPersons(persons);
		list.put(house.getName(), house);	
		//---------------- House Martell of Sunspear ------------------------------------
		house = new House();
		house.setName("House Martell of Sunspear");
		house.setEmblem(new Image(getClass().getResourceAsStream("images/emblem/house_martell.png")));
		house.setWords("Unbowed, Unbent, Unbroken"); 
		house.setSeat("Old Palace within Sunspear");
		house.setX(377);
		house.setY(837);
		house.setRegion("Dorne");
		flag = new ArrayList<>();
		flag.add(Color.ORANGE);
		flag.add(Color.DARKRED);
		flag.add(Color.GOLD);
		house.setFlag(flag);
		persons = new ArrayList<>();
		persons.add("Doran Nymeros Martell");
		persons.add("Mellario of Norvos");
		persons.add("Arianne Martell");
		persons.add("Quentyn Martell");
		persons.add("Trystane Martell");
		house.setPersons(persons);
		list.put(house.getName(), house);	
		
		*/
	}

	public House getElementAtIndex(int index) {
		ArrayList<House> valueList = new ArrayList<House>(list.values());
		return valueList.get(index);
	}

	private ArrayList<Color> getFlag(int idHouse) throws SQLException {
		sql = "SELECT id, id_house, color FROM gameofthrones.flag WHERE id_house = ?";
		PreparedStatement pStatement = connection.prepareStatement(sql);
		pStatement.setInt(1, idHouse);
		ResultSet rs = pStatement.executeQuery();
		ArrayList<Color> flag = new ArrayList<Color>();
		while (rs.next()) {
			Color color = Color.web(rs.getString("color"));
			flag.add(color);
		}
		rs.close();
		pStatement.close();
		return flag;
	}

	public int getIndexOfName(String name) {
		int i = 0;
		for (String s : list.keySet()) {
			if (s.equals(name))
				return i;
			i++;
		}
		return 0;
	}

	public LinkedHashMap<String, House> getList() {
		return list;
	}

	private ArrayList<String> getPersons(int idHouse) throws SQLException {
		sql = "SELECT id, id_house, name FROM gameofthrones.person WHERE id_house = ?";
		PreparedStatement pStatement = connection.prepareStatement(sql);
		pStatement.setInt(1, idHouse);
		ResultSet rs = pStatement.executeQuery();
		ArrayList<String> persons = new ArrayList<String>();
		while (rs.next()) {
			String s = rs.getString("name");
			persons.add(s);
		}
		rs.close();
		pStatement.close();
		return persons;
	}

}
