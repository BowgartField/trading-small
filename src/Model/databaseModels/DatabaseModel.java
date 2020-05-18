package Model.databaseModels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DatabaseModel {

    protected final IntegerProperty idProperty;
    protected final BooleanProperty checkProperty;

    protected DatabaseModel(int id){
        idProperty = new SimpleIntegerProperty(id);
        checkProperty = new SimpleBooleanProperty(false);
    }

    /*
        Propeties
     */
    public IntegerProperty idProperty(){
        return  idProperty;
    }
    public BooleanProperty checkProperty(){
        return checkProperty;
    }

    /*
        Getters
     */
    public int getId(){
        return idProperty.get();
    }
    public boolean getChecked(){
        return checkProperty.get();
    }



}
