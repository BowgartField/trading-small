package Model.databaseModels;

import javafx.beans.property.*;

public class Risk extends DatabaseModel{

    final DoubleProperty riskValueProperty;

    public Risk(int id, Double value){

        super(id);

        riskValueProperty = new SimpleDoubleProperty(value);

    }


    /*
        Properties
     */
    public DoubleProperty riskValueProperty(){
        return riskValueProperty;
    }

    /*
        Getter
     */

    public Double getRiskValue(){
        return riskValueProperty.get();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString(){

        return String.valueOf(getRiskValue());

    }

}
