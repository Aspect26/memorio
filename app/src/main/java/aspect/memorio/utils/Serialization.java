package aspect.memorio.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import aspect.memorio.models.Priority;
import aspect.memorio.models.Purchase;
import aspect.memorio.models.Reminder;
import aspect.memorio.models.Todo;

public class Serialization {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("y-MMMM-d-H-m-s");

    public static String serializePurchase(Purchase purchase) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(purchase.getId()).append(";");
        stringBuilder.append(purchase.getLabel()).append(";");
        stringBuilder.append(purchase.getCost()).append(";");
        stringBuilder.append(serializeBool(purchase.isBought())).append(";");
        stringBuilder.append(purchase.getPriority().value);

        return stringBuilder.toString();
    }

    public static String serializeReminder(Reminder reminder) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(reminder.getText()).append(";");
        stringBuilder.append((reminder.getDate() != null)? DATE_FORMAT.format(reminder.getDate()) : "").append(";");
        stringBuilder.append((reminder.getNotificationDate() != null)? DATE_FORMAT.format(reminder.getNotificationDate()) : "").append(";");
        stringBuilder.append(reminder.getId()).append(";");
        stringBuilder.append(reminder.getPriority().value);

        return stringBuilder.toString();
    }

    public static String serializeTodo(Todo todo) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(todo.getId()).append(";");
        stringBuilder.append(todo.getLabel()).append(";");
        stringBuilder.append((todo.getDate() != null)? DATE_FORMAT.format(todo.getDate()) : "").append(";");
        stringBuilder.append(todo.isDone()? "1" : "0").append(";");
        stringBuilder.append(todo.getPriority().value);

        return stringBuilder.toString();
    }

    public static Purchase deserializePurchase(String dataString) {
        String[] data = dataString.split(";", -1);
        String id = data[0];
        String label = data[1];
        int cost = Integer.parseInt(data[2]);
        boolean bought = deserializeBool(data[3]);
        int priority = Integer.parseInt(data[4]);

        return new Purchase(id, label, bought, cost, Priority.get(priority));
    }

    public static Reminder deserializeReminder(String dataString) throws ParseException {
        String[] data = dataString.split(";", -1);
        String text = data[0];
        String dateText = data[1];
        String notificationDateText = data[2];
        String id = (data.length > 3)? data[3] : "";
        String priority = (data.length > 4)? data[4] : "";

        Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
        Date notificationDate = (notificationDateText != null && notificationDateText.length() > 0)? DATE_FORMAT.parse(notificationDateText) : null;
        id = id.isEmpty()? UUID.randomUUID().toString() : id;
        int priorityValue = priority.isEmpty()? Priority.MEDIUM.value : Integer.parseInt(priority);

        return new Reminder(id, text, date, notificationDate, Priority.get(priorityValue));
    }

    public static Todo deserializeTodo(String dataString) throws ParseException {
        String[] data = dataString.split(";", -1);
        String id = data[0];
        String label = data[1];
        String dateText = data[2];
        String doneText = data[3];
        String priorityText = data[4];

        java.util.Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
        boolean done = doneText.equals("1");
        int priority = Integer.parseInt(priorityText);

        return new Todo(id, label, date, done, Priority.get(priority));
    }

    private static String serializeBool(boolean value) {
        return value? "1" : "0";
    }

    private static boolean deserializeBool(String data) {
        return data.equals("1");
    }
}
