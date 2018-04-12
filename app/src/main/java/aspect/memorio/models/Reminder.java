package aspect.memorio.models;

import java.util.Date;
import java.util.UUID;

public class Reminder {

    private String id;
    private String text;
    private Date date;
    private Date notificationDate;
    private Priority priority;

    public Reminder(final String id, final String text, final Date date, final Date notificationDate, final Priority priority) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.notificationDate = notificationDate;
        this.priority = priority;
    }

    public Reminder(String text, Date date, Date notificationDate, Priority priority) {
        this(UUID.randomUUID().toString(), text, date, notificationDate, priority);
    }

    public Reminder() {
        this("", null, null, Priority.MEDIUM);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
