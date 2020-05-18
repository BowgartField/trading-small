package controllers;

import Model.databaseModels.Risk;
import application.Configuration;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.StageOpener;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsControllers implements Initializable {

	@FXML
	CheckBox DefaultRiskEnabledCheckbox;

	@FXML
	ComboBox<Risk> DefaultRiskChosenComboBox;

	/**
	 * Initialise la fenétre de paramétre
	 * @param arg0 ?
	 * @param arg1 ?
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initialise_DefaultRiskChosenComboBox();

	}

	/**
	 * Initialise la comboBox: % de risque par défaut
	 */
	private void initialise_DefaultRiskChosenComboBox(){

		ObservableList<Risk> risks = Main.configuration.getAvailableRisksFromDataBase();

		//HERE IS THE PROBLEME
		System.out.println("size of observable: " + risks.size());

		//TRY TO DELETE THIS LINE => no items in ObservableList
		//SO I CHOOSE THE RIGHT ComboBox
		DefaultRiskChosenComboBox.setItems(risks);
		DefaultRiskChosenComboBox.setDisable(false);

		//print nothing but Obervable not empty
		for(Risk risk: risks){

			System.out.println(risk.getRiskValue());

		}

		/*
			Configuration.preferences.initRiskComboBox(
					"DefaultRiskChosenComboBox",
					DefaultRiskChosenComboBox,
					risks
			);

			DefaultRiskChosenComboBox.disableProperty().bind(DefaultRiskEnabledCheckbox.selectedProperty().not());
		*/
	}

}
