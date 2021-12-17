package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Language;
import view.View;


public class HelpController {
	
	private Controller controller;

	
	@FXML
	private AnchorPane helpPage;
	
	@FXML
	private Text HelpText;

    @FXML
    private GridPane HelpGrid;

    @FXML
    private ScrollPane HelpScrollPane;
	private DoubleProperty fontSize = new SimpleDoubleProperty(20);
	
	public HelpController(Controller controller) {
		this.controller = controller;
	}

	
	
	@FXML
	protected void initialize() throws FileNotFoundException {
	Properties properties = new Properties();
	
	String helpLanguage = new String();
	
	try {
		properties.load(new FileInputStream("help.properties"));
		helpLanguage = properties.getProperty(Locale.getDefault().toString());
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	char[] buffer = null;
	File file = new File(helpLanguage);
	try(FileReader reader = new FileReader(file)){
		buffer = new char[(int) file.length()];
		reader.read(buffer);
		HelpText.setText(new String(buffer));
	}catch(IOException e) {
		System.out.println(e.getMessage());
	}

	fontSize.bind(HelpScrollPane.widthProperty().add(HelpScrollPane.heightProperty()).divide(100));

	HelpText.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
	}
}


