package utils;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<>();
    public void attach(Observer o){
        observers.add(o);
    }

    public void setSelected(){
        for(Observer o : observers)
            o.updateSelected(this);
    }

    public void setSelectable(){
        for(Observer o: observers)
            o.updateSelectable(this);
    }
}
