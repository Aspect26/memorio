package aspect.memorio.models;

import java.util.Date;

public class Note {

    private String text;

    private Date date;

    private Date notificationDate;

    public Note(String text, Date date, Date notificationDate) {
        this.text = text;
        this.date = date;
        this.notificationDate = notificationDate;
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

}
