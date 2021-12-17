package controller;

import java.io.IOException;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import model.Language;
import view.View;
/**
 * This controller displays search results and users requests form. 
 * From is used to create and send new requests to admin and search 
 * results are from searchbar on the main
 * window
 * 
 * @author Jemila, Alex
 *
 */
public class SearchController {
	private View view;

	private String search;
	private Controller controller;

	@FXML
	private Label SearchLabel;

	@FXML
	private GridPane SearchGrid;

	@FXML
	private Label ArtistLabel;

	@FXML
	private Label GenreLabel;

	@FXML
	private Label AlbumLabel;

	@FXML
	private Button requestFormButton;
	@FXML
	private Label requestLabel;
	@FXML
	private GridPane requestFormGridpienempi;

	@FXML
	private GridPane artistSearchGrid;

	@FXML
	private GridPane genreSearchGrid;

	@FXML
	private GridPane albumSearchGrid;
	@FXML
	private Label foundGenre;
	@FXML
	private Label notFoundGenre;
	@FXML
	private Label foundArtist;

	@FXML
	private Label notFoundArtist;
	@FXML
	private Label foundAlbum;

	@FXML
	private Label notFoundAlbum;
	private DoubleProperty fontSize = new SimpleDoubleProperty(20);

	public SearchController(String search, Controller controller) {
		this.search = search;
		this.controller = controller;
	}

	/**
	 * This initialize is for setting up the SearchPage with the search results
	 **/
	@FXML
	protected void initialize() {
		try {
			foundGenre.setVisible(true);
			notFoundGenre.setVisible(false);
			Genre genreResults = controller.searchGenre(search);
			SearchGrid.add(new Text(genreResults.getGenreName()), 0, 0);

		} catch (Exception e) {
			foundGenre.setVisible(false);
			notFoundGenre.setVisible(true);
			SearchGrid.add(new Text(this.search), 0, 0);
			System.out.println(e.getMessage());
		}
		try {
			Artist artistResults = controller.searchArtist(search);
			foundArtist.setVisible(true);
			notFoundArtist.setVisible(false);
			SearchGrid.add(new Text(artistResults.getArtistName()), 0, 1);

		} catch (Exception e) {
			foundArtist.setVisible(false);
			notFoundArtist.setVisible(true);
			SearchGrid.add(new Text(this.search), 0, 1);
			System.out.println(e.getMessage());
		}

		try {
			foundAlbum.setVisible(true);
			notFoundAlbum.setVisible(false);
			Album albumResults = controller.searchAlbum(search);
			SearchGrid.add(new Text(albumResults.getAlbumName()), 0, 2);
			System.out.println("Search = " + search);

		} catch (Exception e) {
			foundAlbum.setVisible(false);
			notFoundAlbum.setVisible(true);
			SearchGrid.add(new Text(this.search), 0, 2);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This creates request forms fields and button and contains sending feature
	 **/
	@FXML
	void openRequestForm(ActionEvent event) throws IOException {
		requestFormButton.setVisible(false);
		requestLabel.setVisible(false);

		TextArea text = new TextArea();
		Label title = new Label();
		Label textareatitle = new Label();
		Label countChar = new Label();
		Label countCharWord = new Label();
		VBox charBox = new VBox();
		charBox.setMinWidth(160);
		charBox.getChildren().addAll(countChar, countCharWord);

		countChar.setMinWidth(100);
		title.setText(Language.getInstance().getBundle().getString("RequestTextAreaLabel"));
		textareatitle.setText(Language.getInstance().getBundle().getString("RequestTitleLabel"));
		countCharWord.setText(Language.getInstance().getBundle().getString("CharCountLabel"));
		text.setWrapText(true);

		text.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() >= 250) {
				text.setText(oldValue);
			} else if (newValue.length() > 250) {
				countChar.setTextFill(Color.RED);
			}
			countChar.setTextFill(Color.BLACK);
			System.out.println(newValue.length());
			String num = String.valueOf(newValue.length());
			countChar.setText(num + "/250");
		});

		TextField requestTitle = new TextField();
		requestTitle.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 50)
				requestTitle.setText(oldValue);
		});

		Button sendR = new Button();
		sendR.setText(Language.getInstance().getBundle().getString("sendRequestButton"));

		requestFormGridpienempi.add(title, 0, 1);
		requestFormGridpienempi.add(textareatitle, 0, 2);
		requestFormGridpienempi.add(requestTitle, 1, 1);
		requestFormGridpienempi.add(text, 1, 2);
		requestFormGridpienempi.add(sendR, 1, 3);
		requestFormGridpienempi.add(charBox, 2, 2);

		requestTitle.setPrefWidth(200);
		sendR.setPrefWidth(200);
		text.maxHeight(100);

		for (Node n : requestFormGridpienempi.getChildren()) {
			if (n instanceof Label) {
				((Label) n).setPrefWidth(150);
			}
		}
		GridPane.setMargin(textareatitle, new Insets(5, 0, 10, 5));
		GridPane.setMargin(title, new Insets(5, 0, 40, 5));
		GridPane.setMargin(sendR, new Insets(5, 10, 10, 5));
		GridPane.setMargin(text, new Insets(5, 10, 10, 5));
		GridPane.setMargin(requestTitle, new Insets(5, 10, 40, 5));

		// sendR-button send data to controller (saves data to UserRequests table)
		sendR.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				try {
					controller.createRequest(requestTitle.getText(), text.getText());
					text.clear();
					requestTitle.clear();
					Alert message = new Alert(AlertType.CONFIRMATION);
					message.setTitle(Language.getInstance().getBundle().getString("MessageAlertTitleRequestSend"));
					message.setHeaderText(
							Language.getInstance().getBundle().getString("MessageAlertHeaderRequestSend"));
					message.showAndWait();

				} catch (Exception exception) {
					Alert requestError = new Alert(AlertType.ERROR);
					requestError.setTitle(Language.getInstance().getBundle().getString("ErrorAlertTitleRequest"));
					requestError.setHeaderText(Language.getInstance().getBundle().getString("ErrorAlertHeaderRequest"));
					requestError.showAndWait();

				}

			}
		});
	}

}
