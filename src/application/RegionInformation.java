package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import entity.Region;

public class RegionInformation {

	@FXML
	private TextField textName;
	@FXML
	private TextField textPopulation;
	@FXML
	private TextArea textGeography;
	@FXML
	private TextArea textHistory;

	public RegionInformation() {

	}

	@FXML
	private void initialize() {

	}

	public void setRegion(Region region) {
		textName.setText(region.getFullName());
		textPopulation.setText(String.valueOf(region.getPopulation()));
		textGeography.setText(region.getGeography());
		textHistory.setText(region.getHistory());
	}

}
