package view;

import java.io.IOException;
import java.util.List;
import com.sun.glass.ui.Window;
import controller.Controller;
import controller.FrontPageController;
import controller.GUIController;
import controller.HelpController;
import controller.SearchController;
import controller.UserCollectionController;
import controller.RequestFormsController;
import controller.AlbumPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View extends Application {

	private Stage primaryStage;
	private static BorderPane rootLayout;
	private static BorderPane anotherRoot;
	private static AnchorPane userRoot;
	private static GUIController guiController;
	private static Controller controller;

	public void init() {
		controller = new Controller();
		guiController = new GUIController(this, controller);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("MusicArch");
		showHome();

		showFrontPage();

	}
	
	// Places the rootLayout.fxml file on top of the primaryStage
	public void showHome() throws IOException {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/RootLayout2.fxml"));

		rootLayout = (BorderPane) loader.load();
		Scene scene = new Scene(rootLayout);
		String css = this.getClass().getResource("/view/style.css").toExternalForm();
		scene.getStylesheets().add(css);
		primaryStage.setMinWidth(1200);
		primaryStage.setOnCloseRequest(event -> {
			System.exit(-1);
		});
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	// FrontPage.fxml is placed in the center of rootLayout- BorderPane
	public static void showFrontPage() throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/FrontPage.fxml"));
		loader.setControllerFactory(FrontPageController -> new FrontPageController(controller));
		try {
			System.out.println("lataaja " + loader);
		} catch (Exception e) {
			System.out.println("päädyit tänne " + e.getMessage());
		}
		AnchorPane Frontpage = (AnchorPane) loader.load();
		rootLayout.setCenter(Frontpage);

	}
	//showHelpPage.fxml contains software instructions.
	//showHelpPage.fxml is placed in the center of rootLayout- BorderPane
	public static void showHelpPage() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/helpPage.fxml"));
		loader.setControllerFactory(HelpController -> new HelpController(controller));
		AnchorPane Frontpage = (AnchorPane) loader.load();
		rootLayout.setCenter(Frontpage);

	}
	//The search results are set on the SearchPage.fxml. 
	//showHelpPage.fxml is placed in the center of rootLayout- BorderPane
	public static void showSearchPage(String searchText) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/SearchPage.fxml"));
		loader.setControllerFactory(SearchController -> new SearchController(searchText, controller));
		AnchorPane Frontpage = (AnchorPane) loader.load();
		rootLayout.setCenter(Frontpage);
	}
	//showAlbumPage method gets id from frontPageController
	//showAlbumPage.fxml is placed in the center of rootLayout- BorderPane
	public static void showAlbumPage(int id) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/AlbumPage.fxml"));
		loader.setControllerFactory(AlbumPageController -> new AlbumPageController(controller, id));
		AnchorPane Frontpage = (AnchorPane) loader.load();
		rootLayout.setCenter(Frontpage);
	}

	//UserCollection.fxml is placed in the center of userRoot- AnchorPane
	public static void showUserCollectionPage() throws IOException {
		System.out.println("User collection!!!");
		List<Window> windows = Window.getWindows();
		System.out.println(windows);
		boolean test = true;
		try {
			for (int i = 0; i < windows.size(); i++) {
				System.out.println(windows.get(i).getTitle());
				if (windows.get(i).getTitle().contains("User")) {
					System.out.println("Truee");
					test = false;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (test) {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(View.class.getResource("/view/fxmlFiles/UserCollection.fxml"));
			fxmlLoader.setControllerFactory(UserCollectionController -> new UserCollectionController(controller));
			userRoot = (AnchorPane) fxmlLoader.load();
			Scene scene = new Scene(userRoot);
			scene.getWindow();
			scene.getStylesheets().add("/view/style.css");
			Stage stage2 = new Stage();
			stage2.setMinWidth(1200);
			stage2.setMinHeight(500);
			stage2.setTitle("User");
			stage2.setScene(scene);
			stage2.show();
		}
	}

	// -------------------Requests stage---------------------------------
	//Requests.fxml is placed in the center of anotherRoot- BorderPane
	public static void showRequestsWindow() throws IOException {
		System.out.print(" !!!    täällä ollaan    !!!");
			
			List<Window> windows = Window.getWindows();
			System.out.println(windows);
			boolean test = true;
			try {
				for(int i = 0; i < windows.size(); i++) {
					System.out.println(windows.get(i).getTitle());
					if(windows.get(i).getTitle().contains("Request")) {
						System.out.println("True");
						test = false;
						break;
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			if(test) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(View.class.getResource("/view/fxmlFiles/Requests.fxml"));
				fxmlLoader.setControllerFactory(RequestFormsController -> new RequestFormsController(controller));
				anotherRoot = (BorderPane) fxmlLoader.load();

				Scene scene = new Scene(anotherRoot);
				scene.getStylesheets().add("/view/style.css");
				Stage stage = new Stage();
				stage.setResizable(false);
				stage.setTitle("Request");
				stage.setScene(scene);
				stage.show();
			}

	}

	//showGenreForm, showAlbumForm and showArtistForm changes the 
	//form in the requests.fxml file.
	public static void showGenreForm() throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/GenreForm.fxml"));
		loader.setControllerFactory(RequestFormsController -> new RequestFormsController(controller));
		AnchorPane genre = (AnchorPane) loader.load();
		anotherRoot.setCenter(genre);
	}

	public static void showAlbumForm() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/AlbumForm.fxml"));
		loader.setControllerFactory(RequestFormsController -> new RequestFormsController(controller));
		AnchorPane album = (AnchorPane) loader.load();
		anotherRoot.setCenter(album);
	}

	public static void showArtistForm() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(View.class.getResource("/view/fxmlFiles/ArtistForm.fxml"));
		loader.setControllerFactory(RequestFormsController -> new RequestFormsController(controller));
		AnchorPane artist = (AnchorPane) loader.load();
		anotherRoot.setCenter(artist);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);

	}
}