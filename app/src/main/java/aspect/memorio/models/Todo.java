package aspect.memorio.models;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Todo {

    public static final int PRIORITY_LOW = 10;
    public static final int PRIORITY_NORMAL = 20;
    public static final int PRIORITY_HIGH = 30;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("y-MMMM-d-H-m-s");

    private String id;
    private String label;
    private Date date;
    private boolean done;
    private int priority;

    public Todo(String id, String label, Date date, boolean done, int priority) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.done = done;
        this.priority = priority;
    }

    public Todo() {
        this(UUID.randomUUID().toString(), "", null, false, PRIORITY_NORMAL);
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.getId()).append(";");
        stringBuilder.append(this.getLabel()).append(";");
        stringBuilder.append((this.getDate() != null)? DATE_FORMAT.format(this.getDate()) : "").append(";");
        stringBuilder.append(this.isDone()? "1" : "0").append(";");
        stringBuilder.append(this.getPriority());

        return stringBuilder.toString();
    }

    public static Todo createFromString(String line) throws ParseException {
        String[] data = line.split(";", -1);
        String id = data[0];
        String label = data[1];
        String dateText = data[2];
        String doneText = data[3];
        String priorityText = data[4];

        java.util.Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
        boolean done = doneText.equals("1");
        int priority = Integer.parseInt(priorityText);

        return new Todo(id, label, date, done, priority);
    }

}
