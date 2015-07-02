package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.Setting;
import entity.Region;

public class RegionList {

	private Connection connection;
	private PreparedStatement pStatement;
	private ResultSet rs;
	private String sql;

	private ArrayList<Region> list = new ArrayList<Region>();

	public RegionList() {
		try {
			connection = DriverManager.getConnection(Setting.url,
					Setting.username, Setting.password);
			sql = "SELECT id, name, full_name, coordinates, population, geography, history FROM gameofthrones.region order by id";
			pStatement = connection.prepareStatement(sql);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				Region region = new Region();
				region.setId(rs.getInt("id"));
				region.setName(rs.getString("name"));
				region.setFullName(rs.getString("full_name"));
				region.setCoordinates(rs.getString("coordinates"));
				region.setPopulation(rs.getInt("population"));
				region.setGeography(rs.getString("geography"));
				region.setHistory(rs.getString("history"));
				list.add(region);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public ArrayList<Region> getRegions() {
		return list;
	}

}
