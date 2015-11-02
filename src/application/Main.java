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
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Group;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		// Setter opp root, grid og scene
		Group root = new Group();
		GridPane grid = new GridPane();
		Scene scene = new Scene(root, 850, 800);
		Rectangle background = new Rectangle(600,600, 	Color.WHITE);
		// FileChooser for save/load, legger til type imagefiles.
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select an image");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));

		// settings for gridpane
		grid.setVgap(10);
		grid.setHgap(5);
		grid.setPadding(new Insets(20, 20, 20, 20));
		//Brukes til å sette opp
		grid.setGridLinesVisible(false);

		//Crop/resize buttons
		Button cropImage = new Button("Crop image");
		Button resizeImage = new Button("Resize image");
		cropImage.setPrefWidth(108.0);
		resizeImage.setPrefWidth(108.0);

		
		TextField xValue = new TextField();
		TextField yValue = new TextField();
		xValue.setPrefWidth(45);
		yValue.setPrefWidth(45);
		Label xLabel = new Label("X");
		Label yLabel = new Label("Y");
		
		TextField resizeValue = new TextField("100");
		resizeValue.setPrefWidth(45);
		Label reLabel = new Label("%");

		
		HBox XY = new HBox();
		XY.getChildren().addAll(xLabel, xValue, yLabel, yValue);
		XY.setSpacing(5);
		HBox resize = new HBox();
		resize.getChildren().addAll(resizeValue, reLabel);
		resize.setSpacing(5);
		resize.setPadding(new Insets(0,0,0,14));
		


		VBox upperRight = new VBox();
		upperRight.getChildren().add(cropImage);
		upperRight.getChildren().add(XY);
		upperRight.getChildren().add(resizeImage);
		upperRight.getChildren().add(resize);
		upperRight.setSpacing(10);

		// Lager save/load knapper
		Button loadImage = new Button("Load image");
		Button saveImage = new Button("Save image");
		loadImage.setPrefWidth(108.0);
		saveImage.setPrefWidth(108.0);


		// Legger knappene i en verticalbox + mellomrom mellom knappene
		VBox buttons = new VBox();
		buttons.getChildren().add(loadImage);
		buttons.getChildren().add(saveImage);
		buttons.setSpacing(10);

		// Lager sliders og text
		Slider sepia = new Slider(0, 1, 0);
		Slider bloom = new Slider(0, 1, 1);
		Slider glow = new Slider(0, 1, 0);

		// Oppretter effektene, labels og tekst.
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

		// Henter ett image fra en filechooser, setter bredden til 600px og
		// beholder
		// ratio.
		File file = fileChooser.showOpenDialog(primaryStage);
		Image img = new Image(file.toURI().toString());
		ImageView image = new ImageView(img);
		image.setFitWidth(600);
		image.setPreserveRatio(true);

		// Henter bredden og høyden til det resizede imaget.
		int boundWidth = (int) image.getBoundsInParent().getWidth();
		int boundHeight = (int) image.getBoundsInParent().getHeight();

		// Legger image i en hbox
		StackPane imageBox = new StackPane();
		imageBox.getChildren().addAll(background, image);
		//box.getChildren().add(image);
		// legger v/hbox inn i grid.
		grid.add(imageBox, 0, 0);
		grid.add(upperRight, 1, 0);
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

		// Listener for, bruker de tidligere opprettede objektet sepiaTone og
		// den innebygde metoden setLevel();
		sepia.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				sepiaTone.setLevel(new_val.doubleValue());
				sepiaValue.setText(String.format("%.2f", new_val));
				image.setEffect(sepiaTone);
			}
		});

		// se sepiaslider
		bloom.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				bloomTone.setThreshold(new_val.doubleValue());
				bloomValue.setText(String.format("%.2f", new_val));
				image.setEffect(bloomTone);

			}
		});
		// se sepiaslider
		glow.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {

				glowTone.setLevel(new_val.doubleValue());
				glowValue.setText(String.format("%.2f", new_val));
				image.setEffect(glowTone);

			}
		});
		// Listener for å laste inn ett bilde, bruker fileChooser, setter tittel
		// på nytt(i tilfelle et bilde har blitt lagret i mellomtiden
		// og setter imageview image lik img2
		loadImage.setOnAction((event) -> {
			fileChooser.setTitle("Select an image");
			File fileL = fileChooser.showOpenDialog(primaryStage);
			Image img2 = new Image(fileL.toURI().toString());
			image.setImage(img2);

		});
		// Save image listener, oppretter et writable image wim med bredden og
		// høyden lik image, deretter snapshooter vi image og sender til wim
		// Deretter har vi selve lagringen, med evt feilmelding i catch.
		saveImage.setOnAction((event) -> {
			{

				System.out.println(image.getId());
				fileChooser.setTitle("Save as");
				File fileS = fileChooser.showSaveDialog(primaryStage);
				WritableImage wim = new WritableImage((boundWidth*Integer.parseInt(resizeValue.getText()))/100, (boundHeight*Integer.parseInt(resizeValue.getText())/100));
				image.snapshot(null, wim);
				if (fileS != null) {
					try {
						ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", fileS);
					} catch (IOException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});
		
		//Listener for resize. MANGLER varsel for annet enn tall i textfield, varsel hvis tallet er for stort.
		resizeImage.setOnAction((event) -> {
			int resizingValue = Integer.parseInt(resizeValue.getText());
			image.setFitWidth((resizingValue*boundWidth)/100);
			image.setPreserveRatio(true);
		});
		
		//Fungerer dårlig atm, usikker på om det fins noen bedre måte.
		cropImage.setOnAction((event) -> {
			image.setViewport(new Rectangle2D(0,0,Integer.parseInt(xValue.getText()), Integer.parseInt(yValue.getText())));
		});

	}

	public static void main(String[] args) {
		launch(args);
		
	}

}
