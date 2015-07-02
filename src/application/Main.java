package application;

import java.io.IOException;
import java.sql.SQLException;

import entity.House;
import entity.Region;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	@FXML
	private BorderPane root;
	@FXML
	private StackPane stackPane;
	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Pane polygonPane;
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuRegion;

	@FXML
	private MenuItem regionAddHouseItem, regionHouseMembers;
	private Image map;
	private ImageView mapView;
	private HouseList houseList;

	private RegionList regionList;
	private Menu menu;

	private MenuItem itemHouse;

	//------------------ event для пунктов меню, выбора Дома
	EventHandler<ActionEvent> ev = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			MenuItem item = (MenuItem) event.getSource();
			showInfo(houseList.getList().get(item.getText()));  // отображаем информацию о Доме
		}
	};

	@FXML
	private void initialize() {
		houseList = new HouseList();
		regionList = new RegionList();
		// пробегаем циклом по регионам
		for (Region region : regionList.getRegions()) {
			// добавляем контуры регионов
			polygonPane.getChildren().add(region.getPolygon());

			// формируем меню
			menu = new Menu(region.getFullName());
			menuRegion.getItems().add(menu);
			for (House house : houseList.getList().values()) {
				if (house.getRegion().equals(region.getFullName())) {
					itemHouse = new MenuItem(house.getName());
					menu.getItems().add(itemHouse);
					itemHouse.setOnAction(ev);
				}
			}
		}

		map = new Image(getClass().getResourceAsStream("map.jpg"));
		mapView = new ImageView();
		mapView.setImage(map);
		mapView.setOpacity(0.8);
		polygonPane.getChildren().add(mapView);
		scrollPane.setContent(polygonPane);
		scrollPane.setMaxSize(map.getWidth(), map.getHeight());

		mapView.setOnMouseMoved(e -> {   // движение мыши по карте     	
			for (Region region : regionList.getRegions()) {
				if (region.getPolygon().contains(e.getX(), e.getY())) {
					region.getPolygon().setOpacity(1); // если попали на полигон, подсвечиваем облать
				} else {
					region.getPolygon().setOpacity(0); // иначе прячем область, делая ее непрозрачной
				}
			}

		});

		// обработка щелчка мыши по карте
		mapView.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {   // нажата правая кнопка мыши
				int option = 0;
				DialogWindow dialog = new DialogWindow(
						new String[] { "Yes", "No", "I`m not sure:)" },
						"Question",
						"You clicked on the map. Would you like to add house?",
						false, 350, 100);
				dialog.showAndWait();
				option = dialog.getOption();          // если соглашаемся с созданием Дома
				if (option == 1) {
					House h = new House();            // создаем новый Дом
					h.setX((int) e.getX());            // присваиваем ему координаты 
					h.setY((int) e.getY());
					int countHouse = houseList.getList().size(); // количество домов
					showInfo(h);                      // отображаем окно 
					if (countHouse < houseList.getList().size()) {  // если добавили дом			
						//тут добавим меню
						House newHouse = houseList.getElementAtIndex(houseList
								.getList().size() - 1);  // это наш добавленный дом
						for (MenuItem item : menuRegion.getItems()) {   // пробежит по пунктам меню (регионам)
							if (newHouse.getRegion().equals(item.getText())) { // если регион дома совпадает в меню
								MenuItem newItem = new MenuItem(newHouse
										.getName());  // создаем новый пункт
								((Menu) item).getItems().add(newItem);                 // добавляем его в меню  
								newItem.setOnAction(ev);	                          // вешаем на него action  					
							}
						}
					}
				}
				return;
			}

			if (e.getButton() == MouseButton.PRIMARY) {   // нажата левая кнопка мыши		
				// а если нажата левая кнопка, пробегаем по списку описанных Домов 
				for (House house : houseList.getList().values()) {
					Circle c = new Circle(house.getX(), house.getY(), 5);
					if (c.contains(e.getX(), e.getY())) {
						showInfo(house);         // и показываем инфу						
						return;                  // возврат из функции чтобы дальше не искать регионы
						// либо сделать переменную boolean и проверять ее дальше

					}
				}

				// если мы дом не нашли, пробегаем по списку регионов
				for (Region region : regionList.getRegions()) {
					if (region.getPolygon().contains(e.getX(), e.getY())) { // если попали на полигон
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource(
								"fxml/regionInformation.fxml"));
						Parent infoWindow = null;
						try {
							infoWindow = loader.load();
						} catch (Exception ex) {
							System.out.println(ex.getMessage());
						}
						RegionInformation regionInformation = loader
								.getController();
						regionInformation.setRegion(region);

						Stage stage = new Stage();
						stage.setTitle("Information about Region");
						stage.setScene(new Scene(infoWindow));
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.showAndWait();
					}
				}

			}
		});

		// пункт меню для добавления Дома
		regionAddHouseItem.setOnAction(e -> {
			new DialogWindow(new String[] { "Yes" }, "Information",
					"Right-click on the map to select a location.", false, 350,
					100).showAndWait();
		});

		// обработка меню для House Members  
		regionHouseMembers
				.setOnAction(e -> {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource(
							"fxml/houseMembers.fxml"));
					Parent infoWindow = null;
					try {
						infoWindow = loader.load();
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					HouseMembers members = loader.getController();
					members.setHouseList(houseList);

					Stage stage = new Stage();
					stage.setTitle("Information about the members of the house");
					stage.setScene(new Scene(infoWindow, 600, 450));
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();
				});

	}

	private void showInfo(House house) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/houseInformation.fxml"));
		Parent infoWindow = null;

		try {
			infoWindow = loader.load();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		HouseInformation houseInfo = loader.getController();
		houseInfo.setRegionList(regionList);
		houseInfo.setHouseList(houseList);

		houseInfo.setHouse(house);

		Stage stage = new Stage();
		if (house.getName().equals(""))
			stage.setTitle("Input information about House");
		else
			stage.setTitle("Information");
		stage.setScene(new Scene(infoWindow, 600, 450));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		FXMLLoader loaderMain = new FXMLLoader();
		loaderMain.setLocation(getClass().getResource("fxml/mainForm.fxml"));
		root = loaderMain.load();
		Scene scene = new Scene(root, 500, 700);
		primaryStage.setTitle("Game of Thrones");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
