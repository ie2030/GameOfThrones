package application;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import util.Setting;
import entity.House;
import entity.Region;

public class HouseInformation {

	@FXML
	private BorderPane myroot;
	@FXML
	private ImageView emblemImageView;
	@FXML
	private VBox flagBox;
	@FXML
	private TextField words;
	@FXML
	private TextField seat;
	@FXML
	private Label coordX, coordY;
	@FXML
	private ComboBox<String> regionBox;
	@FXML
	private ListView<String> membersList;
	@FXML
	private Stage dialogStage;
	@FXML
	private Label nameHouseLabel;
	@FXML
	private TextField nameHouse;
	@FXML
	private Pagination pagination;
	@FXML
	private TextField delay;
	@FXML
	private Button runSlideshowButton;
	@FXML
	private Button stopSlideshowButton;
	@FXML
	private Button addMember, editMember, deleteMember, saveButton,
			closeButton, editEmblem, editFlag;

	private int delaySec = 3000; // по умолчанию задержка слайдшоу - 3 секунды
	private Timer timer;
	private HouseList houseList;
	private RegionList regionList;
	private House currentHouse;

	private Connection connection;
	private PreparedStatement pStatement;
	private String sql;

	public HouseInformation() {

	}

	@FXML
	private void initialize() {

		delay.textProperty().addListener(new ChangeListener<String>() {   // Listener для изменения значения задержки
					@Override
					public void changed(
							ObservableValue<? extends String> value,
							String oldValue, String newValue) {   // generic type 
						try {
							Integer.parseInt(newValue);    // проверка является ли числовым значение в delay, если неверно тогда в Exception
							delay.setText(String.valueOf(newValue));
						} catch (NumberFormatException e) {
							delay.setText(oldValue);       // возвращаем старое значение
							DialogWindow dialog = new DialogWindow(
									new String[] { "Ok" }, "Error", "Value \""
											+ newValue + "\" is illegal !",
									false, 350, 100);
							dialog.showAndWait();          // говорим об ошибке пользователю
						}
					}
				});

		pagination.currentPageIndexProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(
							ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue) {
						currentHouse = houseList.getElementAtIndex(pagination
								.getCurrentPageIndex());
						setHouse(currentHouse);
						editMember.setDisable(true);
						deleteMember.setDisable(true);
					}

				});

		membersList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						editMember.setDisable(false);
						deleteMember.setDisable(false);
					}
				});

		addMember.setOnMouseClicked(e -> {
			DialogWindow dialog = new DialogWindow(new String[] { "Ok",
					"Cancel" }, "Input House Member", "", true, 350, 100);
			dialog.showAndWait();
			int option = dialog.getOption();
			if (option == 1) {
				currentHouse.getPersons().add(dialog.getText());
				membersList.getItems().add(dialog.getText());
			}
		});

		editMember.setOnMouseClicked(e -> {
			String s = membersList.getSelectionModel().getSelectedItem();
			DialogWindow dialog = new DialogWindow(new String[] { "Ok",
					"Cancel" }, "Edit House Member", s, true, 350, 100);
			dialog.showAndWait();
			int option = dialog.getOption();
			if (option == 1) {
				currentHouse.getPersons().set(
						membersList.getSelectionModel().getSelectedIndex(),
						dialog.getText());
				membersList.getItems().set(
						membersList.getSelectionModel().getSelectedIndex(),
						dialog.getText());
			}
		});

		deleteMember.setOnMouseClicked(e -> {
			currentHouse.getPersons().remove(
					membersList.getSelectionModel().getSelectedIndex());
			membersList.getItems().remove(
					membersList.getSelectionModel().getSelectedIndex());
		});

		editEmblem.setOnMouseClicked(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open image");
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				Image image = new Image(file.toURI().toString());
				emblemImageView.setImage(image);
			}
		});

		editFlag.setOnMouseClicked(e -> {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("fxml/selectFlag.fxml"));
			Parent window = null;

			try {
				window = loader.load();
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
			Stage stage = new Stage();
			stage.setTitle("Information");
			stage.setScene(new Scene(window));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			SelectFlag selectFlag = loader.getController();

			if (selectFlag.getFlag().size() != 0) {
				flagBox.getChildren().clear();
				for (Color c : selectFlag.getFlag()) {
					Rectangle r = new Rectangle(50, 20);
					r.setFill(c);
					flagBox.getChildren().add(r);
				}
			}
		});
	}

	@FXML
	private void runSlideshow() {
		delay.setDisable(true);
		delaySec = Integer.parseInt(delay.getText()) * 1000;	// установливаем задержку из поля delay			
		runSlideshowButton.setDisable(true);
		stopSlideshowButton.setDisable(false);
		timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						int next = (pagination.getCurrentPageIndex() + 1)
								% pagination.getPageCount();
						pagination.setCurrentPageIndex(next);
					}
				});
			}
		}, delaySec, delaySec);
	}

	@FXML
	private void save() throws SQLException, IOException {
		currentHouse.setName(nameHouse.getText());
		currentHouse.setEmblem(emblemImageView.getImage());
		currentHouse.setWords(words.getText());
		currentHouse.setSeat(seat.getText());

		currentHouse.setRegion(regionBox.getSelectionModel().getSelectedItem());
		ArrayList<Color> flag = new ArrayList<>();
		for (Node node : flagBox.getChildren()) {
			Rectangle rect = (Rectangle) node;
			Color color = (Color) rect.getFill();
			flag.add(color);
		}
		currentHouse.setFlag(flag);
		ArrayList<String> persons = new ArrayList<>();
		for (String s : membersList.getItems()) {
			persons.add(s);
		}
		currentHouse.setPersons(persons);
		houseList.getList().put(currentHouse.getName(), currentHouse);
		new DialogWindow(new String[] { "Yes" }, "Information", "Дом "
				+ currentHouse.getName() + " успешно создан!", false, 350, 100)
				.showAndWait();

		// -----------------insert-house---------------------------------------
		connection = DriverManager.getConnection(Setting.url, Setting.username,
				Setting.password);
		sql = "INSERT INTO house (name, words, seat, x, y, region, emblem) VALUES(? , ?, ?, ?, ?, ?, ?);";
		// готовим prepareStatement с генерацией id, которые были вставлены
		pStatement = connection.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS);
		pStatement.setString(1, currentHouse.getName());
		pStatement.setString(2, currentHouse.getWords());
		pStatement.setString(3, currentHouse.getSeat());
		pStatement.setInt(4, currentHouse.getX());
		pStatement.setInt(5, currentHouse.getY());
		pStatement.setString(6, currentHouse.getRegion());
		// заносим эмблему в InputStream
		RenderedImage renderedImage = SwingFXUtils.fromFXImage(
				currentHouse.getEmblem(), null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(renderedImage, "png", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		pStatement.setBinaryStream(7, is);
		pStatement.execute();
		// ищем последний вставленный id
		int lastInsertedId = 0;
		ResultSet rs = pStatement.getGeneratedKeys(); // вот все вставленные id строк, но поскольку он один
		if (rs.next())           // то мы его сразу получим
		{
			lastInsertedId = rs.getInt(1); // вот он
		}
		pStatement.close();

		// -----------------insert-person---------------------------------------
		sql = "INSERT INTO person (id_house, name) VALUES(? ,?);";
		pStatement = connection.prepareStatement(sql);
		for (String person : currentHouse.getPersons()) {
			pStatement.setInt(1, lastInsertedId);
			pStatement.setString(2, person);
			pStatement.execute();
		}
		pStatement.close();
		// -----------------insert-flag---------------------------------------
		sql = "INSERT INTO flag (id_house, color) VALUES(? ,?);";
		pStatement = connection.prepareStatement(sql);
		for (Color color : currentHouse.getFlag()) {
			pStatement.setInt(1, lastInsertedId);
			pStatement.setString(2, color.toString());
			pStatement.execute();
		}
		pStatement.close();
		connection.close();
		//----------------------------------------------------------------	
		myroot.sceneProperty().get().getWindow().hide();
	}

	public void setHouse(House house) {
		currentHouse = house;
		//если мы создаем новый дом, то у него нет имени 
		if (currentHouse.getName().equals("")) {
			runSlideshowButton.setDisable(true);   // запуск слайдшоу невозможен
			pagination.setDisable(true);
			saveButton.setDisable(false);
			coordX.setText(String.valueOf(house.getX()));
			coordY.setText(String.valueOf(house.getY()));
		} else {   // иначе мы заполняем поля формы из текущего дома			
			membersList.getItems().clear();
			flagBox.getChildren().clear();

			nameHouse.setText(house.getName());
			words.setText(house.getWords());
			seat.setText(house.getSeat());
			coordX.setText(String.valueOf(house.getX()));
			coordY.setText(String.valueOf(house.getY()));
			regionBox.getSelectionModel().select(house.getRegion());

			for (String person : house.getPersons()) {
				membersList.getItems().add(person);
			}
			emblemImageView.setImage(house.getEmblem());
			for (Color c : house.getFlag()) {
				Rectangle r = new Rectangle(50, 20);
				r.setFill(c);
				flagBox.getChildren().add(r);
			}

			// ищем индекс House в массиве по названию дома   
			int i = 0;
			for (String s : houseList.getList().keySet()) {
				if (s.equals(house.getName()))
					break;
				i++;
			}
			pagination.currentPageIndexProperty().set(i);
		}
	}

	public void setHouseList(HouseList houseList) {
		this.houseList = houseList;
		pagination.pageCountProperty().set(houseList.getList().size());
	}

	public void setRegionList(RegionList regionList) {
		this.regionList = regionList;
		for (Region region : this.regionList.getRegions()) {
			regionBox.getItems().add(region.getFullName());
		}
	}

	@FXML
	private void stopSlideshow() {
		delay.setDisable(false);
		runSlideshowButton.setDisable(false);
		stopSlideshowButton.setDisable(true);
		if (timer != null) {
			timer.cancel();
		}
	}

	@FXML
	private void windowClose() {
		if (timer != null)
			timer.cancel();
		myroot.sceneProperty().get().getWindow().hide();
	}

}
