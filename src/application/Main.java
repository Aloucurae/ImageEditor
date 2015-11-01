package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

public class Main extends Application {
	    
	@Override
	public void start(Stage primaryStage) {
		// Setter opp root, grid og scene
		Group root = new Group();
		GridPane grid = new GridPane();
		Scene scene = new Scene(root, 750, 800);
		FileChooser fileChooser = new FileChooser();

		
		
		grid.setVgap(10);
		grid.setHgap(5);
		grid.setPadding(new Insets(20, 20, 20, 20));
		grid.setGridLinesVisible(true);
		// Lager save/load knapper
		Button loadImage = new Button("Load image");
		Button saveImage = new Button("Save image");
		// Legger knappene i en verticalbox
		VBox buttons = new VBox();
		buttons.getChildren().add(loadImage);
		buttons.getChildren().add(saveImage);
		buttons.setSpacing(10);
		// Lager sliders og text
		Slider sepia = new Slider(0, 1, 0);
		Slider bloom = new Slider(0, 1, 1);
		Slider glow = new Slider(0, 1, 0);

		Glow glowTone = new Glow();
		Bloom bloomTone = new Bloom();
		SepiaTone sepiaTone = new SepiaTone();
		Label sepiaValue = new Label(Double.toString(sepia.getValue()));
		Label bloomValue = new Label(Double.toString(bloom.getValue()));
		Label glowValue = new Label(Double.toString(glow.getValue()));


		

		Text glowtext = new Text("Glow");
		Text sepiatext = new Text("Sepia");
		Text bloomtext = new Text("Bloom");

		// Legger sliders og text i en vbox
		VBox sliders = new VBox();
		sliders.setAlignment(Pos.CENTER);
		sliders.getChildren().add(sepia);
		sliders.getChildren().add(sepiatext);
		sliders.getChildren().add(sepiaValue);
		sliders.getChildren().add(bloom);
		sliders.getChildren().add(bloomtext);
		sliders.getChildren().add(bloomValue);
		sliders.getChildren().add(glow);
		sliders.getChildren().add(glowtext);
		sliders.getChildren().add(glowValue);

		// Henter ett image fra en URL, setter bredden til 600px og beholder
		// ratio.
		//Image img = new Image("http://www.hercampus.com/sites/default/files/2015/09/23/emma%20watson.jpg");
		File file = fileChooser.showOpenDialog(primaryStage);
		Image img = new Image(file.toURI().toString());
		ImageView image = new ImageView(img);
		image.setFitWidth(600);
		image.setPreserveRatio(true);
		
		// Legger image i en hbox
		HBox box = new HBox();
		box.getChildren().add(image);
		// legger v/hbox inn i grid.
		grid.add(box, 0, 0);
		grid.add(sliders, 0, 1);
		grid.add(buttons, 1, 1);
		// Oppretteer scenen.
		scene.setRoot(grid);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		primaryStage.setTitle("Imageshop");
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(400);
		primaryStage.show();
		
		sepia.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				sepiaTone.setLevel(new_val.doubleValue());
				sepiaValue.setText(String.format("%.2f", new_val));
				image.setEffect(sepiaTone);
			}
		});
		bloom.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				bloomTone.setThreshold(new_val.doubleValue());
				bloomValue.setText(String.format("%.2f", new_val));
				image.setEffect(bloomTone);
				
			}
		});
		glow.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				glowTone.setLevel(new_val.doubleValue());
				glowValue.setText(String.format("%.2f", new_val));
				image.setEffect(glowTone);
			}
		});
		loadImage.setOnAction((event) -> {
			File fileL = fileChooser.showOpenDialog(primaryStage);
			Image img2 = new Image(fileL.toURI().toString());
			 image.setImage(img2);

          
		});
		saveImage.setOnAction((event) -> {
			{
	            FileChooser fileSaver = new FileChooser();
	            fileChooser.setTitle("Save Image");
	            System.out.println(image.getId());
	            File fileS = fileChooser.showSaveDialog(primaryStage);
	            if (file != null) {
	                try {
	                    ImageIO.write(SwingFXUtils.fromFXImage(image.getImage(),
	                        null), "png", fileS);
	                } catch (IOException ex) {
	                    System.out.println(ex.getMessage());
	                }
	            }
	        }
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
