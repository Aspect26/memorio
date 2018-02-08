package aspect.memorio.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Reminder {

    public static final int PRIORITY_LOW = 10;
    public static final int PRIORITY_NORMAL = 20;
    public static final int PRIORITY_HIGH = 30;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("y-MMMM-d-H-m-s");

    private String id;
    private String text;
    private Date date;
    private Date notificationDate;
    private int priority;

    private Reminder(final String id, final String text, final Date date, final Date notificationDate, final int priority) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.notificationDate = notificationDate;
        this.priority = priority;
    }

    public Reminder(String text, Date date, Date notificationDate, int priority) {
        this(UUID.randomUUID().toString(), text, date, notificationDate, priority);
    }

    public Reminder() {
        this("", null, null, PRIORITY_NORMAL);
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getText()).append(";");
        stringBuilder.append((this.getDate() != null)? DATE_FORMAT.format(this.getDate()) : "").append(";");
        stringBuilder.append((this.getNotificationDate() != null)? DATE_FORMAT.format(this.getNotificationDate()) : "").append(";");
        stringBuilder.append(this.getId()).append(";");
        stringBuilder.append(this.getPriority());

        return stringBuilder.toString();
    }

    public static Reminder createFromString(String line) throws ParseException {
        String[] data = line.split(";", -1);
        String text = data[0];
        String dateText = data[1];
        String notificationDateText = data[2];
        String id = (data.length > 3)? data[3] : "";
        String priority = (data.length > 4)? data[4] : "";

        Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
        Date notificationDate = (notificationDateText != null && notificationDateText.length() > 0)? DATE_FORMAT.parse(notificationDateText) : null;
        id = id.isEmpty()? UUID.randomUUID().toString() : id;
        int priorityValue = priority.isEmpty()? PRIORITY_NORMAL : Integer.parseInt(priority);

        return new Reminder(id, text, date, notificationDate, priorityValue);
    }

}
