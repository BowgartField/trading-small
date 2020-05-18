package application;

import Model.databaseModels.Risk;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Callback;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.Pragma;
import utils.AlertWindow;
import utils.ErrorWindow;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TradingDatabase{
	
	/**
	 * Donnée membre contenant le stade de chargement de la base de donnée
	 */
	String LoadingStatus;
	
	/**
	 * Donnée membre contenant le nom du fichier
	 */
	private String database_file_name = "trading.db";
	
	/**
	 * Donnée membre contenant la connection a la base de donnée
	 */
	public Connection databaseConnection;
	
	/**
	 * Donnée membre permettant de savoir l'avancement du chargement de la base de donnée
	 * @return true/false
	 */
	public boolean Loading = false;
	
	/**
	 * Constructeur par défaut
	 * Initialiase la connection à la base de donnée
	 */
	public TradingDatabase() {}
	
	/**
	 * Créé la base de donnée sur l'ordinateur de l'utilisateur
	 * Remplie la base de donnée avec les différentes tables
	 */
	public void CreateDatabase() {
		
		//on vérifie si le fichier de base de donnée existe
		if(!new File(database_file_name).exists()) {
			
			//on créé l'objet représentant le fichier de base de donnée
			File DatabaseFile = new File(this.database_file_name);

			try {
				DatabaseFile.createNewFile();
				
				//on vérifie si la base contient des tables
				try {
					
					//on se connecte à la base récement créé
					this.ConnectDataBase();
						
					//on prépare la requete de création des différentes tables
					PreparedStatement req = this.databaseConnection.prepareStatement(
							"CREATE TABLE Trades(" +
								"id integer primary key autoincrement," + 
								"Nom varchar(255) ," +
								"Ticker varchar(255) ,"+
								"Marché varchar(255) ,"+
								"Nombre int ,"+
								"Entrée int ,"+
								"Sortie int ,"+
								"Date_entrée Date,"+
								"Date_sortie Date,"+
								"Stop_loss int,"+
								"Frais_de_courtage int,"+
								"Pv_Mv int ,"+
								"Closed boolean,"+
								"Neutralised boolean,"+
								"Neutral boolean,"+
								"Neutral_date Date,"+
								"Risk int,"+
								"Maximum_risk int,"+
								"R2 boolean,"+
								"R2_date Date"+ 
							")"
						);
					
						//on éxecute la requete
						req.execute();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	/**
	 * Charge la base de donnée,
	 * Elle est executé dans un autre Thread
	 * Si elle n'existe pas, alors elle est créé
	 * 
	 * @return {Connection} retourne un objet de type Conneection
	 */
	public Connection ConnectDataBase(){
		
		//on créé un object 
		Connection c = null;
		
		File database = new File(this.database_file_name);
		
		//tant que la base n'existe pas on réexecute le code
		do {
			
			if(database.exists()) {
				
				//on tente une connexion à la base de donnée
				//si elle n'existe elle sera crée
				try {
					
					//on créé une configuration SQL
					SQLiteConfig sqLiteConfig = new SQLiteConfig();
					Properties properties = sqLiteConfig.toProperties();
					
					//on défini le pattern des dates utilisée
					properties.setProperty(Pragma.DATE_STRING_FORMAT.pragmaName, "dd/MM/yyyy");
					
					//on instancie une connexion à la base de donnée
					c = DriverManager.getConnection("jdbc:sqlite:" + database_file_name,properties);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				
				//on créé la base de donnée
				this.CreateDatabase();
				
			}
			
		}while(!database.exists());
		
		return c;
		
	}
	
	/**
	 * Récupére tout les trades neutralisé dans la base de donnée
	 * @return Service<ResultSet>
	 */
	public Service<ResultSet> getAllFreeTrades() {
		
		//on récupére le service de connection à la base de donnée
		Connection connection = this.ConnectDataBase();
			
			//on créé un service pour éviter le chargement de la base dans le thread principal
			Service<ResultSet > getFreeTrades = new Service<ResultSet>() {
				
				@Override
				protected Task<ResultSet> createTask() {
					
					return new Task<ResultSet>() {
						
						@Override
						protected ResultSet call() {
							
							ResultSet result = null;
							
							try {
								
								PreparedStatement req = connection.prepareStatement("SELECT "
										+ "id, Nom, Ticker, Marché, Nombre, Entrée, Date_entrée, "
										+ "Stop_loss, Sortie, Frais_de_courtage, Pv_Mv, Closed,"
										+ "Neutral, Neutral_date, Neutralised, Risk, Maximum_risk, R2, R2_date "
										+ "FROM Trades WHERE Neutralised = 'true' ORDER BY id asc");

								result = req.executeQuery();
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							return result;
						}
						
					};
						
				};
				
			};
			
			//on démarre la tâche
			getFreeTrades.start();
		
			return getFreeTrades;
	
	}
	
	/**
	 * Récupére tout les trades non neutralisé dans la base de données
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<ResultSet> getAllRiskyTrades(){
		
		//on récupére le service de connection à la base de donnée
		Connection connection = this.ConnectDataBase();
		
		//on créé un service pour éviter le chargement de la base dans le thread principal
		Service <ResultSet> getRiskyTrades = new Service<ResultSet>() {

			@Override
			protected Task<ResultSet> createTask() {
				
				return new Task<ResultSet>() {

					@Override
					protected ResultSet call() throws Exception {
						
						ResultSet result = null;
						
						try {
							
//							PreparedStatement req = database.prepareStatement("SELECT "
//									+ "id, Nom, Ticker, March�, Nombre, Entrée, Date_entrée, "
//									+ "Stop_loss, Sortie, Frais_de_courtage, Neutral_date, Neutralised, Risk,"
//									+ "Maximum_risk, R2, R2_date "
//									+ "FROM Trades WHERE Neutralised = 'false' ORDER BY id asc");
							
							PreparedStatement req = connection.prepareStatement("SELECT * "
									+ "FROM Trades WHERE Neutralised = 'false' ORDER BY id asc");

							result = req.executeQuery();
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						return result;
					}
					
				};
				
			};
			

		};
		
		//on démarre la tâche
		getRiskyTrades.start();
		
		return getRiskyTrades;
		
	}


	/*-------------------------------------------  RECUPERATION BASE DE DONNES  --------------------------------------*/
	
	/**
	 * Récupére les noms des marchés disponible dans la base de données
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public ObservableList<Market> GetAvailableMarkets(){

		//On créé une observableList<Market> contenant des objets Market
		//On ajoute un callback qui permet d'ajouter un Listener sur les propriétes des objets stockés à l'intérieur
		ObservableList<Market> observableList = FXCollections.observableArrayList(new Callback<Market, Observable[]>() {
			@Override
			public Observable[] call(Market market) {
				return new Observable[]{market.checkProperty()};
			}
		});

		//on créé une liste
		List<Market> marketList = new ArrayList<>();
		
		//on récupére le service de connection à la base de
		Connection connection = this.ConnectDataBase();
			
			Service <ResultSet> getavailablemarkets = new Service<ResultSet>() {
				
				@Override
				protected Task<ResultSet> createTask() {
					
					return new Task<ResultSet>() {
						
						@Override
						protected ResultSet call() throws Exception {
							
							ResultSet result = null;
							
							try {
								
								PreparedStatement req = connection.prepareStatement(
										"SELECT id , marché, pays FROM Markets ORDER BY marché ASC"
								);
							
								result = req.executeQuery();
							
							}catch (SQLException e) {
								System.out.println(e.getMessage());
								e.printStackTrace();
								
							}
						
						return result;
						
					}
					
				};
				
			};
			
		};
		
		//on démarre la tâche
		getavailablemarkets.start();

		//L'opération à réussie
		getavailablemarkets.setOnSucceeded((WorkerStateEvent) -> {

			ResultSet markets = getavailablemarkets.getValue();

			try {

				while(markets.next()) {

					//on vérifie que le tableau de résultat n'est pas vide
					if(markets.getString("marché") != null) {
						//on ajoute les marchés disponible dans la liste observable
						marketList.add(new Market(
								Integer.parseInt(markets.getString("id")),
								markets.getString("marché"),
								markets.getString("pays")
						));

					}

				}

				observableList.addAll(marketList);

			}catch(Exception exec) {
				exec.printStackTrace();
			}

		});

		//L'opération à échoué
		getavailablemarkets.setOnFailed((WorkerStateEvent) -> {

			AlertWindow error = new AlertWindow(
					"RETRIEVE_MARKET_IMPOSSIBLE",
					"RETRIEVE_MARKET_IMPOSSIBLE",
					null
			);

			error.show();

		});

		System.out.println(observableList);

		return observableList;
		
	}

	/**
	 * Récupére les différents risques disponible depuis la base de données
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public ObservableList<Risk> GetAvalaibleRisks() {

		//On créé une observableList<Market> contenant des objets Market
		//On ajoute un callback qui permet d'ajouter un Listener sur les propriétes des objets stockés à l'intérieur
		ObservableList<Risk> observableList = FXCollections.observableArrayList(new Callback<Risk, Observable[]>() {
			@Override
			public Observable[] call(Risk risk) {
				return new Observable[]{risk.checkProperty()};
			}
		});

		//on récupére le service de connection à la base de
		Connection connection = this.ConnectDataBase();

		Service<ResultSet> getavailablerisks = new Service<ResultSet>() {

			@Override
			protected Task<ResultSet> createTask() {

				return new Task<ResultSet>() {

					@Override
					protected ResultSet call(){

						ResultSet result = null;

						try {

							PreparedStatement req = connection.prepareStatement("SELECT id, risk_value FROM Risks ORDER BY risk_value ASC");

							result = req.executeQuery();

						} catch (SQLException e) {
							e.printStackTrace();

						}

						return result;

					}

				};

			}

		};

		//on démarre la tâche
		getavailablerisks.start();

		//L'opération à réussie
		getavailablerisks.setOnSucceeded((WorkerStateEvent) -> {

			ResultSet riskAvailableResultSet = getavailablerisks.getValue();

			try{

				while(riskAvailableResultSet.next()){

					String value = riskAvailableResultSet.getString("risk_value");
					String id = riskAvailableResultSet.getString("id");

					if(value != null){

						observableList.add(
								new Risk(
										Integer.parseInt(id),
										Double.parseDouble(value)
								)
						);

					}

				}


			}catch(Exception exec){

				exec.printStackTrace();

			}

		});

		//L'opération à échoué
		getavailablerisks.setOnFailed((WorkerStateEvent) -> {

			AlertWindow error = new AlertWindow(
					"RETRIEVE_RISK_IMPOSSIBLE",
					"RETRIEVE_RISK_IMPOSSIBLE",
					null
			);

			error.show();

		});

		return observableList;

	}

	/**
	 * Récupére les périodes disponibles depuis la base de données et les mets en forme
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public ObservableList<String> GetAvailablePeriodRisks(){

		//on créé une observableList<String>
		ObservableList<String> observableList = FXCollections.observableArrayList();

		//on récupére le service de connection à la base de
		Connection connection = this.ConnectDataBase();

		Service<ResultSet> getavailablePeriodRisks = new Service<ResultSet>() {

			@Override
			protected Task<ResultSet> createTask() {

				return new Task<ResultSet>(){

					@Override
					protected ResultSet call(){

						ResultSet result = null;

						try{

							PreparedStatement req = connection.prepareStatement("SELECT period FROM PeriodicRisks");
							result = req.executeQuery();

						}catch (SQLException e){
							e.printStackTrace();
						}

						return result;
					}

				};

			}

		};

		//on démarre la tâche
		getavailablePeriodRisks.start();

		//L'opération à réussie
		getavailablePeriodRisks.setOnSucceeded((WorkerStateEvent) -> {

			//On récupére le
			ResultSet resultSet = getavailablePeriodRisks.getValue();

			try {

				while(resultSet.next()) {

					//on vérifie que le tableau de résultat n'est pas vide
					if(resultSet.getString("period") != null) {

						//on ajoute les marchés disponible dans la liste observable
						observableList.add(resultSet.getString("period"));

					}

				}

			}catch(Exception exec) {
				exec.printStackTrace();
			}

		});

		//L'opération à échoué
		getavailablePeriodRisks.setOnFailed((WorkerStateEvent) -> {

			AlertWindow error = new AlertWindow(
					"RETRIEVE_PERIODIC_IMPOSSIBLE",
					"RETRIEVE_PERIODIC_IMPOSSIBLE",
					null
			);

			error.show();

		});

		return observableList;

	}


	/*-----------------------------------------------  AJOUT BASE DE DONNES  -----------------------------------------*/

	/**
	 * Permet d'ajouter dans la base de données un trade
	 * @param trade {Trade} trade à jouter à la base de donnée
	 * @return un service
	 */
	public Service<String> addTrade(Trade trade){

		//on ajoute le trade dans la ListView
		Main.RiskyTrades.add(trade);

		//on récupére le service de connection à la base de donnée
		Connection connection = this.ConnectDataBase();

		Service <String> addTrade = new Service<String>() {

			@Override
			protected Task<String> createTask() {

				return new Task<String>() {

					@Override
					protected String call(){

						String result = null;

						try {

							String request = "INSERT INTO Trades("
									+ "Nom,Ticker,Marché,Nombre,Entrée,Sortie,Date_Entrée,Stop_loss,Closed,Neutral," +
									"Neutralised,Risk,Maximum_risk) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";


							//on créé la requete préparé
							PreparedStatement req = connection.prepareStatement(request);

							req.setString(1,trade.getNom());
							req.setString(2,trade.getTicker());
							req.setString(3,trade.getMarket());
							req.setFloat(4,trade.getNombre());
							req.setFloat(5,trade.getEntrée());
							req.setFloat(6, trade.getSortie());
							req.setDate(7,Date.valueOf(trade.getDate_entrée()));
							req.setFloat(8,trade.getStop_loss());
							req.setBoolean(9,trade.getCloturé());
							req.setFloat(10,trade.getNeutral());
							req.setBoolean(11,trade.getNeutralisé());
							req.setFloat(12,trade.getRisque());
							req.setFloat(13,trade.getRisque_maximum());

							//on execute la requete
							req.execute();

							//on retourne True, tout s'est bien passé
							result = "true";

						}catch (SQLException e) {
							result = "false";
							new ErrorWindow(e,Main.Stages.get("OpenSettingsStage"));
						}

						return result;

					}

				};

			};

		};

		//on démarre la tâche
		addTrade.start();

		return addTrade;

	}

	/**
	 * Permet d'ajouter un niveau risque dans la liste des risques disponibles
	 * @param newRiskValue nouvelle valeur du risque
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
    public Service<String> addNewRisk(Risk newRiskValue) {

		Connection connection = this.ConnectDataBase();

		Service<String> addnewrisk = new Service<String>() {

			@Override
			protected Task<String> createTask() {

				return new Task<>() {

					@Override
					protected String call(){

						String result = null;

						try {

							String request = "INSERT INTO Risks(risk_value) VALUES (?)";

							//on créé la requete préparé
							PreparedStatement req = connection.prepareStatement(request);

							req.setString(1, String.valueOf(newRiskValue.getRiskValue()));

							//on execute la requete
							req.execute();

							//on retourne True, tout s'est bien passé
							result = "true";

						}catch (SQLException e) {
							// TODO Auto-generated catch block
							result = "false";
							e.printStackTrace();
						}

						return result;

					}

				};

			}

		};

		//on démarre la tâche
		addnewrisk.start();


		addnewrisk.setOnSucceeded((WorkerStateEvent) -> {

		});

		addnewrisk.setOnFailed((WorkerStateEvent) -> {

		});



		return addnewrisk;

    }

	/**
	 * Permet d'ajouter une nouvelle place de marché dans la base de donnée
	 * @param market {Market} objet Market
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> addNewMarket(Market market){

		Connection connection = this.ConnectDataBase();

		Service<String> addnewmarket = new Service<>() {
			@Override
			protected Task<String> createTask() {

				return new Task<>(){

					@Override
					protected String call(){

						String result;

						try{

							String sql = "INSERT INTO Markets(marché,pays) VALUES (?,?)";

							PreparedStatement req =  connection.prepareStatement(sql);

							req.setString(1, market.getMarket());
							req.setString(2, market.getCountry());

							req.execute();

							result = "true";

						}catch (SQLException e){

							new ErrorWindow(e,Main.Stages.get("OpenSettingsStage"));

							result = "false";

						}

						return result;
					}

				};

			}
		};

		addnewmarket.start();
		return addnewmarket;

	}


	/*--------------------------------------------  MODIFICATION BASE DE DONNEES  ------------------------------------*/

	/**
	 * Permet de change le risque attaché à la période
	 * @param periods ObservableList contenant les différentes périodes de risques et leurs valeurs
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> changePeriodicRisk(ObservableList<PeriodicRisk> periods){

		Connection connection = this.ConnectDataBase();

		Service<String> changePeriodicRisk = new Service<String>() {
			@Override
			protected Task<String> createTask() {

				return new Task<>(){

					@Override
					protected String call(){

						String result = "false";

						try{

							for(PeriodicRisk periodicRisk : periods){

								//on prépare la requête
								String request = "UPDATE PeriodicRisks SET value = ? WHERE id = ?";

								//on créé l'objet qui contiendra la requête
								PreparedStatement req = connection.prepareStatement(request);

								//on bind les paramétres
								req.setString(1, periodicRisk.getPeriodValue());
								req.setString(2, String.valueOf(periodicRisk.getId()));

								//on execute la requête
								req.execute();

								result = "true";

							}

						}catch(SQLException e){

							new ErrorWindow(e,Main.Stages.get("OpenSettingsStage"));

							result = "false";

						}

						return result;

					}

				};

			}
		};

		changePeriodicRisk.start();

		return changePeriodicRisk;

	}

	/**
	 * Permet de modifier les informations des place de marché modifiées
	 * @param markets marché à modifier
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> changeMarket(ObservableList<Market> markets){

		Connection connection = this.ConnectDataBase();

		Service<String> changemarkets = new Service<>() {
			@Override
			protected Task<String> createTask() {

				return new Task<>(){

					@Override
					protected String call(){

						String result = "false";

						try{

							for(Market market: markets){

								//on prépare la requête
								String request = "UPDATE Markets SET marché = ?, pays = ? WHERE id = ?";

								//on créé l'objet qui contiendra la requête
								PreparedStatement req = connection.prepareStatement(request);

								//on bind les paramétres
								req.setString(1, market.getMarket());
								req.setString(2, market.getCountry());
								req.setString(3, String.valueOf(market.getId()));

								//on execute la requête
								req.execute();

							}

							result = "true";

						}catch(SQLException e){

							new ErrorWindow(e,Main.Stages.get("OpenSettingsStage"));

							result = "false";

						}

						return result;

					}

				};

			}
		};

		changemarkets.start();

		return changemarkets;

	}

	/**
	 * Permet de modifier les informations de risque
	 *
	 * @param risks {List<String>} risques à modifier
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> changeRisk(ObservableList<Risk> risks){

		Connection connection = this.ConnectDataBase();

		Service<String> changeRisk = new Service<String>() {
			@Override
			protected Task<String> createTask() {

				return new Task<>(){

					@Override
					protected String call(){

						String result;

						try{

							for(Risk risk : risks){

								//on prépare la requête
								String request = "UPDATE Risks SET risk_value = ? WHERE id = ?";

								//on créé l'objet qui contiendra la requête
								PreparedStatement req = connection.prepareStatement(request);

								//on bind les paramétres
								req.setString(1, String.valueOf(risk.getRiskValue()));
								req.setString(3, String.valueOf(risk.getId()));

								//on execute la requête
								req.execute();

							}

							result = "true";

						}catch (SQLException e){

							result = "false";
							e.printStackTrace();

						}


						return result;

					}

				};

			}

		};

		changeRisk.start();
		return changeRisk;

	}

	/*--------------------------------------------  SUPPRESSION BASE DE DONNES  --------------------------------------*/

	/**
	 * Permet de supprimer les places de marchés disponible
	 * @param deletedMarkets {List<String>} liste contenant tous les ids des marché à supprimer
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> deleteMarket(List<String> deletedMarkets){

		//on récupérer la connection à la base de donnée
		Connection connection = this.ConnectDataBase();

		//on créé un service
		Service<String> deletemarket = new Service<>() {

			@Override
			protected Task<String> createTask() {

				return new Task<>() {

					@Override
					protected String call(){

						String result;

						try{

							for (String market: deletedMarkets){

								String sql = "DELETE FROM 'Markets' WHERE id = ?";

								PreparedStatement req = connection.prepareStatement(sql);

								req.setString(1, market);
								req.execute();

							}

							result = "true";

						}catch(Exception e){
							result = "false";
							e.printStackTrace();
						}

						return result;

					}

				};

			}

		};

		deletemarket.start();
		return deletemarket;

	}

	/**
	 * Permet de supprimer les risques disponibles
	 * @param deleteRisks {List<String>} liste contenant tous les ids des risques à supprimer
	 * @return {Service<String>} un service permettant de récupérer le status de la tâche
	 */
	public Service<String> deleteRisk(List<String> deleteRisks){

		//on récupérer la connection à la base de donnée
		Connection connection = this.ConnectDataBase();

		Service<String> deleterisks = new Service<>() {
			@Override
			protected Task<String> createTask() {

				return new Task<>() {

					@Override
					protected String call(){

						String result;

						try{

							for(String risk : deleteRisks){

								String sql = "DELETE FROM 'Risks' WHERE id = ?";

								PreparedStatement req = connection.prepareStatement(sql);

								req.setString(1, risk);
								req.execute();

							}

							result = "true";

						}catch(SQLException e){
							e.printStackTrace();
							result = "false";
						}

						return result;

					}

				};

			}
		};

		deleterisks.start();
		return deleterisks;

	}
}
