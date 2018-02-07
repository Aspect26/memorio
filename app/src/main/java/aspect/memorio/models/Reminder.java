package aspect.memorio.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Reminder {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("y-MMMM-d-H-m-s");

    private String id;

    private String text;

    private Date date;

    private Date notificationDate;

    private Reminder(final String id, final String text, final Date date, final Date notificationDate) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.notificationDate = notificationDate;
    }

    public Reminder(String text, Date date, Date notificationDate) {
        this(UUID.randomUUID().toString(), text, date, notificationDate);
    }

    public Reminder() {
        this("", null, null);
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getText()).append(";");
        stringBuilder.append((this.getDate() != null)? DATE_FORMAT.format(this.getDate()) : "").append(";");
        stringBuilder.append((this.getNotificationDate() != null)? DATE_FORMAT.format(this.getNotificationDate()) : "").append(";");
        stringBuilder.append(this.getId());

        return stringBuilder.toString();
    }

    public static Reminder createFromString(String line) throws ParseException {
        String[] data = line.split(";", -1);
        String text = data[0];
        String dateText = data[1];
        String notificationDateText = data[2];
        String id = (data.length > 3)? data[3] : "";

        Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
        Date notificationDate = (notificationDateText != null && notificationDateText.length() > 0)? DATE_FORMAT.parse(notificationDateText) : null;
        if (id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }

        return new Reminder(id, text, date, notificationDate);
    }

}
