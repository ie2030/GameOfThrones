package application;

import entity.House;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class HouseMembers {

	@FXML
	private BorderPane root;
	@FXML
	private ComboBox<String> houseBox;
	@FXML
	private ListView<String> membersList;

	@FXML
	private Button addButton, editButton, deleteButton, closeWindow;

	private HouseList houseList;

	public HouseMembers() {

	}

	@FXML
	private void initialize() {

		//listener для изменения выбора дома
		houseBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				House h = houseList.getList().get(
						houseBox.getSelectionModel().getSelectedItem());
				membersList.getItems().clear();
				for (String s : h.getPersons()) {
					membersList.getItems().add(s);
				}
				editButton.setDisable(true);
				deleteButton.setDisable(true);
			}
		});

		membersList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						editButton.setDisable(false);
						deleteButton.setDisable(false);
					}
				});

		addButton.setOnMouseClicked(e -> {
			House h = houseList.getList().get(
					houseBox.getSelectionModel().getSelectedItem());
			DialogWindow dialog = new DialogWindow(new String[] { "Ok",
					"Cancel" }, "Input House Member", "", true, 350, 100);
			dialog.showAndWait();
			int option = dialog.getOption();
			if (option == 1) {
				h.getPersons().add(dialog.getText());
				membersList.getItems().add(dialog.getText());
			}
		});

		editButton.setOnMouseClicked(e -> {
			House h = houseList.getList().get(
					houseBox.getSelectionModel().getSelectedItem());
			String s = membersList.getSelectionModel().getSelectedItem();
			DialogWindow dialog = new DialogWindow(new String[] { "Ok",
					"Cancel" }, "Edit House Member", s, true, 350, 100);
			dialog.showAndWait();
			int option = dialog.getOption();
			if (option == 1) {
				h.getPersons().set(
						membersList.getSelectionModel().getSelectedIndex(),
						dialog.getText());
				membersList.getItems().set(
						membersList.getSelectionModel().getSelectedIndex(),
						dialog.getText());
			}
		});

		deleteButton.setOnMouseClicked(e -> {
			House h = houseList.getList().get(
					houseBox.getSelectionModel().getSelectedItem());
			// delete member from houseList;
				h.getPersons().remove(
						membersList.getSelectionModel().getSelectedIndex());
				// also from ListView
				membersList.getItems().remove(
						membersList.getSelectionModel().getSelectedIndex());
			});

		closeWindow.setOnMouseClicked(e -> {
			root.sceneProperty().get().getWindow().hide();
		});
	}

	public void setHouseList(HouseList houseList) {
		this.houseList = houseList;
		for (House house : houseList.getList().values()) {
			houseBox.getItems().add(house.getName());
		}
	}

}
