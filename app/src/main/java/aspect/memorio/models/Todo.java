package aspect.memorio.models;

import java.util.Date;
import java.util.UUID;

public class Todo {

    private String id;
    private String label;
    private Date date;
    private boolean done;
    private Priority priority;

    public Todo(String id, String label, Date date, boolean done, Priority priority) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.done = done;
        this.priority = priority;
    }

    public Todo() {
        this(UUID.randomUUID().toString(), "", null, false, Priority.MEDIUM);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
