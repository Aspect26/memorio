package aspect.memorio.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import aspect.memorio.models.Reminder;
import aspect.memorio.storage.DeviceFileStorage;
import aspect.memorio.storage.Storage;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("type") != null) {
            switch (intent.getStringExtra("type")) {
                case "daily":
                    this.showDailyNotification(context); break;
                case "reminder":
                    this.showReminderNotification(context, intent); break;
            }
        }

    }

    private void showDailyNotification(Context context) {
        final Storage storage = new DeviceFileStorage(context);
        storage.loadAll();
        final List<Reminder> todayReminders = storage.getAllToday();
        if (todayReminders.size() == 0) {
            return;
        }
        final String text = this.getDailyNotificationText(todayReminders);
        NotificationsManager.showNotification(context, "You have " + todayReminders.size() + " reminders today", text);
    }

    private void showReminderNotification(Context context, Intent intent) {
        String notificationText = intent.getStringExtra("text");
        NotificationsManager.showNotification(context, "Reminder!", notificationText);
    }

    private String getDailyNotificationText(List<Reminder> reminders) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Reminder note : reminders) {
            stringBuilder.append(String.format("REMINDER: %s\n", note.getText()));
        }
        return stringBuilder.toString();
    }
}