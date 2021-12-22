package entities;

import java.util.Set;

public class UpdateReport {
    private Title title;
    private Set<String> subscribers;
    private String message;
    private boolean updated = true;

    public UpdateReport(Title title, Set<String> subscribers, String message) {
        this.title = title;
        this.subscribers = subscribers;
        this.message = message;
    }
    public UpdateReport() {
        updated = false;
    }

    public Title getTitle() {
        return title;
    }

    public Set<String> getSubscribers() {
        return subscribers;
    }

    public String getMessage() {
        return message;
    }

    public boolean wasUpdated() {
        return updated;
    }
}
