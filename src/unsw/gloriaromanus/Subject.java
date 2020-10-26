package unsw.gloriaromanus;

public interface Subject {
    /**
     * Attach an observer to a subject
     * @param obs observer
     */
    void attach(Observer obs);

    /**
     * Detach an observer from a subject
     * @param obs observer
     */
    void detach(Observer obs);

    /**
     * NotifyObservers that there is change in the subject
     */
    void notifyObservers();
}
