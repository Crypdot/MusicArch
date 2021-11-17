package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import view.View;


public class HelpController {
	
	private Controller controller;

	
	@FXML
	private AnchorPane helpPage;
	
	@FXML
	private Text helptext;

	public HelpController(Controller controller) {
		this.controller = controller;
	}

	@FXML
	protected void initialize() throws FileNotFoundException {
		
		try {
			File file = new File("MusicArch/src/main/resources/helpfin.txt");
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();					
				helptext.setText(data);
//				System.out.println("heres some data " + data);
			}
			myReader.close();
		} catch(FileNotFoundException e) {
		    e.getMessage();
		}
	}
}


