package aspect.memorio.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import aspect.memorio.activities.HomeActivity;
import aspect.memorio.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationsManager {

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
