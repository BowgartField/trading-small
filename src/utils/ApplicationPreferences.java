package utils;

import Model.databaseModels.Risk;
import Model.parameter.*;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ApplicationPreferences {

    /**
     * Donnée membre contenant l'objet préférence
     */
    private Preferences preferences = null;

    /**
     * Donnée membre contenant les paramétres de l'application
     */
    private final static Map<String, ParameterStrategy> parametersSynchronous = new HashMap<>();

    /**
     * Constructeur par défaut
     */
    public ApplicationPreferences(){

        String APPLICATION_PROPERTIES_NODE_NAME = "fr/tradingjournal/preferences";

        try{

            //on regarde si les préférences ont déjà été initialiser
            if(Preferences.userRoot().nodeExists(APPLICATION_PROPERTIES_NODE_NAME)){

                //on récupére les préférences
                preferences = Preferences.userRoot().node(APPLICATION_PROPERTIES_NODE_NAME);

                initializeDefaultPreferences();

            }else{
                preferences = Preferences.userRoot().node(APPLICATION_PROPERTIES_NODE_NAME);

                initializeDefaultPreferences();

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        this.printAllPreferences();

    }

    /**
     * Initialise les préférence par défaut à partir d'un patron
     * -> le patron se situe dans INSEREZ ICI LE CHEMIN VERS UN FICHIER
     */
    private void initializeDefaultPreferences() {

        preferences.put("language","fr_FR");
        preferences.put("staticTradingFeesLockedCheckbox","true");

    }

    /**
     * Permet de récupérer la valeur d'une préférence
     * @param preferenceName le nom de la préference dont ont veut récupérer la valeur
     * @return la valeur de la préférence
     */
    public String getPreferenceValue(String preferenceName){
        return preferences.get(preferenceName,null);
    }

    /**
     * Permet de changer la valeur d'une préférence
     * @param preferenceName le nom de la préference dont ont veut changer la valeur
     * @param preferenceValue La valeur de la préférence
     */
    public void setPreferenceValue(String preferenceName, String preferenceValue){

        preferences.put(preferenceName,preferenceValue);

    }

    /* Initialisation des Controleurs */

    /**
     * Permet d'initialiser la CheckBox passé en paramétre avec sa valeur
     * @param key {String} Clé permettant de récupérer l'élément dans la liste des controls
     * @param checkBox CheckBox à initialiser
     */
    public void initControl(String key, CheckBox checkBox){
        initImpl(key,new CheckBoxParameterStrategy(checkBox,key));
    }

    /**
     * Permet d'initialiser la comboBox passé en paramétre avec sa valeur
     * @param key {String} Clé permettant de récupérer l'élément dans la liste des controls
     * @param comboBox comboBox à initialiser
     * @param items {ObservableList<String>} les items à affiché dans la comboBox
     */
    public void initControl(String key, ComboBox<String> comboBox, ObservableList<String> items){
        initImpl(key,new ComboBoxParameterStrategy(comboBox,key,items));
    }

    /**
     * Permet d'initialiser la TextField passé en paramétre avec sa valeur
     * @param key {String} Clé permettant de récupérer l'élément dans la liste des controls
     * @param textField comboBox à initialiser
     */
    public void initControl(String key, TextField textField){
        initImpl(key,new TextFieldParameterStrategy(textField,key));
    }

    /**
     * Permet d'initialiser une Combox avec un type de donnée spéciale
     * Ex: ComboBox<Risk> ou ComboBox<Market>
     *
     * @param key {String}
     * @param comboBox
     * @param items
     */
    public void initRiskComboBox(String key, ComboBox<Risk> comboBox, ObservableList<Risk> items){
        initImpl(key, new RiskComboBoxStrategy(comboBox, key, items));
    }

    /**
     * Permet d'initialiser les différents control représenter par la classe ParameterStrategy
     * @param key {String} clé correspondant au nom du paramétre que modifie le control
     * @param parameter {ParameterStrategy} control à initialiser avec sa valeur de configuration
     */
    private void initImpl(String key, ParameterStrategy parameter){
        parameter.setValue(this.getPreferenceValue(key));
        parametersSynchronous.put(key,parameter);
    }


    /* DEBUG */
    /**
     * Permet d'afficher toutes les préférences
     */
    public void printAllPreferences(){

        System.out.println("//-----------------------------------------------//");

        try {
            for(String preference : preferences.keys()){

                System.out.println(preference + ":" + this.getPreferenceValue(preference));

            }
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        System.out.println("//-----------------------------------------------//");

    }

}
