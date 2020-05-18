package application;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import utils.StageOpener;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
	
	/**
	 * Donnée membre contenant notre base de donnée
	 */
	public static TradingDatabase database = new TradingDatabase();
	
	/**
	 * Liste contenant toutes les stages
	 */
	public static Map<String, Stage> Stages = new HashMap();
	 
	 /**  
	  * Contient la liste de tout les trades gratuit -> neutralisé
	  */
	 public static ObservableList<Trade> freeTrades = FXCollections.observableArrayList();
	 
	 /**  
	  * Contient la liste de tout les trades risqués -> non neutralisé
	  */
	 
	 public static ObservableList<Trade> RiskyTrades = FXCollections.observableArrayList();
	 
	 /**
	  * Donnée membre contenant la configuration effectuée par l'utilisateur
	  */
	 public static Configuration configuration;

	public static void main(String[] args) {
		launch(args);
	}

    @Override
	public void start(Stage primaryStage){

    	//on récupére les préférences de l'application
    	InitializeConfiguration();

    	//Ouvre l'écran
		new StageOpener(
				primaryStage,
				"/views/Settings.fxml",
				"Settings",
				"Settings",
				null,
				false
		);
		
	}

	/**
	 * Charge la configuration à partir du fichier de configuration
	 */
	private void InitializeConfiguration() {

		//on initialise la configuration
		configuration = new Configuration();

	}
	
}
