package aspect.memorio.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import aspect.memorio.activities.HomeActivity;
import aspect.memorio.R;
import aspect.memorio.models.Reminder;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationsManager {

    static final int DAILY_REQUEST_CODE = 1;
    private static final int REMINDER_REQUEST_CODE = 2;

    private final HomeActivity homeActivity;

    public NotificationsManager(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        new TodayTodoNotifications(this);
    }

    public HomeActivity getHomeActivity() {
        return this.homeActivity;
    }

    public static void showNotification(final Context context, final String title, final String text) {
        Notification notification = createNotification(context, title, text);
        showNotification(context, notification);
    }

    public void showNotification(final String title, final String text) {
        showNotification(this.homeActivity, title, text);
    }

    public void addReminderNotification(Reminder reminder) {
        if (reminder.getDate() == null) {
            return;
        }

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(reminder.getDate().getTime());
        time.add(Calendar.HOUR, -1);

        Intent intent = new Intent(this.homeActivity, AlarmReceiver.class);
        intent.putExtra("type", "reminder");
        intent.putExtra("text", reminder.getText());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.homeActivity, REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        }
    }

    private static Notification createNotification(final Context context, final String title, final String text) {
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification notification = new NotificationCompat.Builder(context, "channel")
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(pendingIntent)
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    private static void showNotification(final Context context, final Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

}
