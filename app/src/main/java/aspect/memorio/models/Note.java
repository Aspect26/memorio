package aspect.memorio.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMMM-d-H-m-s");

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getText()).append(";");
        stringBuilder.append((this.getDate() != null)? DATE_FORMAT.format(this.getDate()) : "").append(";");
        stringBuilder.append((this.getNotificationDate() != null)? DATE_FORMAT.format(this.getNotificationDate()) : "").append(";");

        return stringBuilder.toString();
    }

    public static Note createFromString(String line) throws ParseException {
        String[] data = line.split(";");
        if (data.length != 3) {
            return null;
        } else {
            String text = data[0];
            String dateText = data[1];
            String notificationDateText = data[2];

            Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
            Date notificationDate = (notificationDateText != null && notificationDateText.length() > 0)? DATE_FORMAT.parse(notificationDateText) : null;

            return new Note(text, date, notificationDate);
        }
    }

}
