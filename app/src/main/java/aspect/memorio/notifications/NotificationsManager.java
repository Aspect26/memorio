package aspect.memorio.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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

    public static void showNotification(final Context context, final String title, final String text, final boolean vibrate, final int number) {
        Notification notification = createNotification(context, title, text, vibrate, number);
        showNotification(context, notification);
    }

    public static void showNotification(final Context context, final String title, final String text, final boolean vibrate) {
        showNotification(context, title, text, vibrate, 0);
    }

    public void showNotification(final String title, final String text, final boolean vibrate, final int number) {
        showNotification(this.homeActivity, title, text, vibrate, number);
    }

    public void addOrUpdateReminderNotification(Reminder reminder) {
        if (reminder.getDate() == null) {
            return;
        }

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(reminder.getDate().getTime());
        time.add(Calendar.HOUR, -1);

        Intent intent = new Intent(this.homeActivity, AlarmReceiver.class);
        intent.putExtra("type", "reminder");
        intent.putExtra("text", reminder.getText());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.homeActivity, reminder.getId().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        }
    }

    private static Notification createNotification(final Context context, final String title, final String text, final boolean vibrate, final int number) {
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "channel")
                .setContentTitle(title)
                .setContentText(text.isEmpty()? "<empty>" : text)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_notification_large))
                .setContentIntent(pendingIntent)
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                        .bigText(text))
                // TODO: get this from colors.xml
                .setLights(4128899, 3000, 3000);

        if (vibrate) {
            notificationBuilder.setVibrate(new long[] { 1000, 1000, 1000 });
        }
        if (number > 0) {
            notificationBuilder.setNumber(number);
        }

        Notification notification = notificationBuilder.build();
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
