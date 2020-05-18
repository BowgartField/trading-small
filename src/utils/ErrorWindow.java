package utils;


import controllers.ErrorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorWindow {

	public ErrorWindow(Exception exc, Stage parentStage) {
		
		ErrorController.exc = exc;
		
		//on charge la view associé a la scene principale
		Parent root;
		try {
			
			root = FXMLLoader.load(getClass().getResource("/views/Error.fxml"));
			
			//on créé une nouvelle avec notre view
			Scene scene = new Scene(root);
			
			//on définit le composant racine à partir de notre view
			scene.setRoot(root);
			
			Stage errorStage = new Stage();
			
			errorStage.setScene(scene);
	        
	        //On défini le titre de la fenêtre
			errorStage.setTitle("Erreur");
		
			//on défini la scene sur sur la scene principale
			errorStage.setScene(scene);
			
			//on affiche la scene principale
			errorStage.show();
			
			//On défini la fenetre comme non redimensionnable
			errorStage.setResizable(false);
			
			errorStage.initOwner(parentStage);
			
			errorStage.initModality(Modality.APPLICATION_MODAL);
			
			errorStage.show();
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	

}
