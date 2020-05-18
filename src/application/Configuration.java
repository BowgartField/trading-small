package application;

import Model.databaseModels.Risk;
import javafx.collections.ObservableList;
import utils.ApplicationPreferences;

import java.util.Locale;
import java.util.ResourceBundle;

public class Configuration {
	
	/**
	 * Donnée membre égale au chemin vers la base de donnée
	 */
	private static String PATH_TO_DATABASE;

	/**
	 * Donnée membre égale au marché disponible
	 */
	private ObservableList<Market> AvailableMarkets;

	/**
	 * Donné membre contenant la liste des risques disponible
	 */
	private ObservableList<Risk> AvailableRisks;

	/**
	 * Donnée membre contenant la liste des périodes disponibles
	 */
	private ObservableList<String> AvailablePeriods;

	/**
	 * Donnée membre correspondant a la langue
	 */
	public String lang;
	
	/**
	 * Donnée membre contenant le binding des noms de fenêtres
	 */
	public static ResourceBundle WindowTitleRessourceBundle;
	
	/**
	 * Donnée membre contenant le binding des messages pour les alertes
	 */
	public static ResourceBundle AlertMessageRessourceBundle;

	/**
	 * Donnée membre permettant d'accéder aux préférence de l'application
	 */
	public static ApplicationPreferences preferences = new ApplicationPreferences();


	/**
	 * Constructeur par défaut
	 */
	public Configuration() {

		//on récupére les périodes
		this.retreiveAvailablePeriodFromDatabase();
		
		//on récupére les configurations des titres des fenêtres et les messages d'alerte
		this.getPropertiesFiles();

	}

	/**
	 * On récupére les configurations des titres des fenêtres et les messages d'alerte
	 */
	private void getPropertiesFiles() {

		//récupére les titre des fenêtres
		this.getWindowTitleFromPropertiesFile();

		//récupére les message relatif aux fenêtres d'alerte
		this.getMessagesAlertFromPropertiesFile();
		
	}


	/*-------------------------------------  RECUPERE LES DONNES DEPUIS LA BASE   ------------------------------------*/

	/**
	 * Récupére les marché disponible à partir de la base de donnée
	 */
	public ObservableList<Market> getAvailableMarketsFromDatabase() {
		
		return Main.database.GetAvailableMarkets();
	}

	/**
	 * Getter pour la donnée membre AvailableRisks
	 * @return liste observable contenant tou les risques disponible lors de l'ouverture d'un trade
	 */
	public ObservableList<Risk> getAvailableRisksFromDataBase(){

		return Main.database.GetAvalaibleRisks();

	}

	/**
	 *
	 */
	private void retreiveAvailablePeriodFromDatabase(){

		this.AvailablePeriods = Main.database.GetAvailablePeriodRisks();

	}


	/*-----------------------------------------------------  GETTERS  ------------------------------------------------*/

	/**
	 * Récupére les période disponibles traduite dans la langue choisie par l'utilisateur
	 */
	public ObservableList<String> getAvailablePeriod(){

		return this.AvailablePeriods;

	}

	/*-------------------------------  RECUPERATION LES TRADUCTIONS POUR LES ALERTES  --------------------------------*/

	/**
	 * Récupére tous les noms des fenêtres à partir du fichier de configuration properties
	 * -> fichier: "WindowTitle_fr.properties"
	 */
	private void getWindowTitleFromPropertiesFile() {
		
		try {

			//on créé la langue actuelle
			Locale locale = new Locale("fr_FR");
			
			//on créé la langue actuelle
			WindowTitleRessourceBundle = ResourceBundle.getBundle("ressources.fr_FR.WindowTitle",locale);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}
	
	/**
	 * Récupére toute les messages pour les alertes
	 */
	private void getMessagesAlertFromPropertiesFile() {

		try {
			
			//on créé la langue actuelle
			Locale locale = new Locale("fr_FR");
			
			//on créé la langue actuelle
			AlertMessageRessourceBundle = ResourceBundle.getBundle("ressources.fr_FR.MessagesAlert",locale);
			
		}catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}

}
