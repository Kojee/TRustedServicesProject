package utils;

public abstract class Observer {
    public abstract void updateSelected(Subject s);
    public abstract void updateSelectable(Subject s);
}
