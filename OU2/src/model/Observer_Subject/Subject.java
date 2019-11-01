package model.Observer_Subject;

/**
 * Subject in observer-subject design pattern
 */
public interface Subject {

    void addObserver(Observer o);
    void deleteObserver(Observer o);
    void notifyObs();

}
