package application;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectFlag {

	@FXML
	private BorderPane root;
	@FXML
	private ColorPicker color;
	@FXML
	private Button add, delete, save, close;
	@FXML
	private ListView<Rectangle> flag;

	private ArrayList<Color> list;        // array colors

	public SelectFlag() {

	}

	public ArrayList<Color> getFlag() {
		return list;
	}

	@FXML
	private void initialize() {
		list = new ArrayList<>();

		add.setOnMouseClicked(e -> {             // adding colour
			Rectangle rect = new Rectangle(flag.getWidth() - 20, 10);
			rect.setFill(color.getValue());
			flag.getItems().add(rect);
		});

		delete.setOnMouseClicked(e -> {
			if (flag.getSelectionModel().getSelectedIndex() >= 0) {   // if  selected some colour to remove
				flag.getItems().remove(
						flag.getSelectionModel().getSelectedIndex());
			}
		});

		save.setOnMouseClicked(e -> {
			if (!flag.getItems().isEmpty()) {  // if colour list is not empty			
				for (Rectangle c : flag.getItems()) {
					list.add((Color) c.getFill());
				}
			}
		});

		close.setOnMouseClicked(e -> {          // close windows
			root.sceneProperty().get().getWindow().hide();
		});
	}

}
