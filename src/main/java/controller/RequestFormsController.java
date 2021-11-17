package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.jcg.hibernate.maven.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import view.View;

public class RequestFormsController {
	private Controller controller;
	private View view;

	@FXML
	private AnchorPane RequestCategories;
	@FXML
	private TitledPane ArtistDrop;
	@FXML
	private TitledPane GenreDrop;
	@FXML
	private TitledPane AlbumDrop;
	@FXML
	private TextField GenreAddTxtField;
	@FXML
	private Label GenreAddLabel;
	@FXML
	private Button SendGenre;
	@FXML
	private Label GenreAddTitle;
	@FXML
	private TextField ArtistsName;
	@FXML
	private TextArea Biografia;
	@FXML
	private TextField Released;
	@FXML
	private Label Year;
	@FXML
	private Label AlbumTitle;
	@FXML
	private TextField AlbumName;
	@FXML
	private TextField ArtistName;
	@FXML
	private TextField Songs;
	@FXML
	private TextField GenreName;
	@FXML
	private VBox root;
	@FXML
	private VBox root1;
	@FXML
	private VBox root2;
	
	@FXML
	protected void initialize() {

	}
	
	public RequestFormsController(Controller controller) {
		this.controller = controller;
	}
	
	//Dropdownlists mouseevents
	@FXML
	void Album(MouseEvent event) {
	}

	@FXML
	void Artist(MouseEvent event) {
	}

	@FXML
	void Genre(MouseEvent event) {
	}

	// GenreFormButton, AlbumFromButton and ArtistFromButton sends request to view to change forms in requests.fxml
	@FXML
	void GenreFormButton(ActionEvent event) throws IOException {
		view.showGenreForm();
	}

	@FXML
	void AlbumFormButton(ActionEvent event) throws IOException {
		view.showAlbumForm();
	}

	@FXML
	void ArtistFormButton(ActionEvent event) throws IOException {
		view.showArtistForm();
	}

	
	//The Newgenre button, NewSong button, and NewArtist button create new textfields in the album form.
	List<Artist> ArtistList = new ArrayList<Artist>();
	ArrayList<TextField> artistList = new ArrayList<TextField>();
	int counter = 0;

	@FXML
	void NewArtist(ActionEvent event) {
		TextField field = new TextField();
		final HBox parent = new HBox(5.0);
		field.setId(String.valueOf(counter++));
		System.out.print(field.getId());
		artistList.add(field);
		System.out.println(field.getId());
		Button button = new Button("-");
		field.setAlignment(Pos.CENTER_LEFT);
		button.setAlignment(Pos.CENTER_RIGHT);
		button.setOnAction((e) -> parent.getChildren().clear());
		HBox.setHgrow(field, Priority.ALWAYS);
		HBox.setHgrow(button, Priority.NEVER);
		parent.getChildren().setAll(field, button);
		root.getChildren().add(parent);

	}

	int counter2 = 0;
	ArrayList<TextField> genreList = new ArrayList<TextField>();

	@FXML
	void NewGenre(ActionEvent event) {

		TextField field = new TextField();
		final HBox parent = new HBox(5.0);
		field.setId(String.valueOf(counter2++));
		System.out.print(field.getId());
		genreList.add(field);
		System.out.println(field.getId());
		Button button = new Button("-");
		field.setAlignment(Pos.CENTER_LEFT);
		button.setAlignment(Pos.CENTER_RIGHT);
		button.setOnAction((e) -> parent.getChildren().clear());
		HBox.setHgrow(field, Priority.ALWAYS);
		HBox.setHgrow(button, Priority.NEVER);
		parent.getChildren().setAll(field, button);
		root2.getChildren().add(parent);
	}

	int counter3 = 0;
	ArrayList<TextField> songList = new ArrayList<TextField>();

	@FXML
	void NewSong(ActionEvent event) {

		TextField field = new TextField();
		final HBox parent = new HBox(5.0);
		field.setId(String.valueOf(counter3++));
		System.out.print(field.getId());
		songList.add(field);
		System.out.println(field.getId());
		// ArtistList.add(field.getId());
		Button button = new Button("-");
		field.setAlignment(Pos.CENTER_LEFT);
		button.setAlignment(Pos.CENTER_RIGHT);
		button.setOnAction((e) -> parent.getChildren().clear());
		HBox.setHgrow(field, Priority.ALWAYS);
		HBox.setHgrow(button, Priority.NEVER);
		parent.getChildren().setAll(field, button);
		root1.getChildren().add(parent);
	}
	
	// SendGenreButton sends textfield data to controller
	//The Error window pops up if GenreAddTxtField textfield is empty.

	@FXML
	void SendGenreButton(ActionEvent event) throws IOException {
		String genreName = GenreAddTxtField.getText();
		if (genreName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Virhe!");
			alert.setHeaderText("Tarkista genren nimi. Nimi ei voi olla tyhjä.");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Vahistuspyyntö");
			alert.setHeaderText("Vahvista genrepyyntö:");
			alert.setContentText("Haluatko lähettää pyynnön lisätä genren " + genreName + "?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				controller.createGenre(genreName);
				GenreAddTxtField.clear();

			}
		}
	}

	// SendArtistButton sends textfield data to controller
	//The Error window pops up if ArtistsName textfield is empty.
	@FXML
	void SendArtistButton(ActionEvent event) throws IOException {
		System.out.print(" Artistin nimi: " + ArtistsName.getText() + " Artistin bio: " + Biografia.getText() + " ");
		String artistName = ArtistsName.getText();
		String artistBio = Biografia.getText();

		if (ArtistsName.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Virhe!");
			alert.setHeaderText("Tarkista artistin nimi. Nimeä ei voi jättää tyhjäksi!");
			alert.showAndWait();

		} else {

			Alert alert = new Alert(AlertType.CONFIRMATION);

			alert.setTitle("Vahvistuspyyntö");
			alert.setHeaderText("Vahvista artistipyyntö:");
			alert.setContentText("Haluatko lähettää pyynnön lisätä artistin " + artistName
					+ " ja artistille biografian: " + artistBio + "?");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				controller.createArtist(artistName, artistBio);
				ArtistsName.clear();
				Biografia.clear();
			}
		}

	};
	
	// SendAlbumButton sends textfield data to controller
	//The Error window pops up if AlbumName textfield is empty.

	@FXML
	void SendAlbumButton(ActionEvent event) {

		if (AlbumName.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Virhe!");
			alert.setHeaderText("Tarkista artistin nimi. Nimeä ei voi jättää tyhjäksi!");
			alert.showAndWait();

		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);

			alert.setTitle("Vahvistuspyyntö");
			alert.setHeaderText("Vahvista albumin lisäyspyyntö:");
			alert.setContentText("Haluatko lähettää pyynnön lisätä albumin " + AlbumName.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {

				String[] genreListGiven = new String[genreList.size()];
				int k = 0;
				for (TextField textfield : genreList) {
					System.out.println("Testataan listaa " + textfield.getId());
					System.out.println(textfield.getText());
					genreListGiven[k] = textfield.getText();
					k++;
				}

				String[] artistName = new String[artistList.size()];
				int j = 0;

				for (TextField textfield : artistList) {
					System.out.println("Testataan listaa " + textfield.getId());
					System.out.println(textfield.getText());
					artistName[j] = textfield.getText();
					j++;
				}

				String[] songListGiven = new String[songList.size()];

				int h = 0;
				for (TextField textfield : songList) {
					System.out.println("Testataan listaa " + textfield.getId());
					System.out.println(textfield.getText());
					songListGiven[h] = textfield.getText();
					h++;
				}

				controller.createAlbum(AlbumName.getText(), Integer.parseInt(Released.getText()), genreListGiven,
						artistName, songListGiven);
			}
		}
	}
}
