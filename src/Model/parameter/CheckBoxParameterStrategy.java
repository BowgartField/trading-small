package Model.parameter;

import application.Configuration;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;

public class CheckBoxParameterStrategy implements ParameterStrategy {

    private final BooleanProperty booleanProperty;
    private final String PropertyName;
    private final String TextFieldId;

    public CheckBoxParameterStrategy(CheckBox checkBox, String PropertyName) {

        //on initialise nos données membres
        booleanProperty = checkBox.selectedProperty();
        this.PropertyName = PropertyName;
        this.TextFieldId = checkBox.getId();

        //on ajoute un listener
        booleanProperty.addListener((observableValue, oldValue, newValue) -> {

            Configuration.preferences.setPreferenceValue(this.PropertyName, booleanProperty.getValue().toString());
            //System.out.println(this.TextFieldId + " égale à: " + newValue);

        });

    }

    @Override
    public String getValue() {
        return booleanProperty.getValue().toString();
    }

    @Override
    public void setValue(String value) {
        boolean selected = Boolean.parseBoolean(value);
        booleanProperty.setValue(selected);
    }

}
