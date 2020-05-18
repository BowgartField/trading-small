package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {
	
	@FXML
	public TextFlow exception;
	
	/**
	 * correspond a l'exception renvoyé
	 */
	public static Exception exc;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//on récupére l'exception et on l'ajouter a la fenêtre
		exception.getChildren().add(new Text(this.getStackTrace(exc)));
		
		//on affiche l'exception dans la fentere
		exc.printStackTrace();

	}
	
	private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }
	
	
	
	
	

}
