package aspect.memorio.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;

import aspect.memorio.activities.HomeActivity;

import static android.content.Context.ALARM_SERVICE;
import static aspect.memorio.notifications.NotificationsManager.DAILY_REQUEST_CODE;

public class TodayTodoNotifications {

    private final NotificationsManager notificationsManager;

    public TodayTodoNotifications(NotificationsManager notificationsManager) {
        this.notificationsManager = notificationsManager;
        this.initialize();
    }

    private void initialize() {
        this.setAlarmToNextDay();
    }

    private void setAlarmToNextDay() {
        HomeActivity homeActivity = this.notificationsManager.getHomeActivity();

        this.cancelReminder(homeActivity, HomeActivity.class);
        Calendar notificationTime = this.getNextDayNotificationTime();
        this.enableReceiver(homeActivity);
        PendingIntent pendingIntent = this.createPendingIntent(homeActivity);
        this.setAlarm(homeActivity, notificationTime, pendingIntent);
    }

    private void cancelReminder(Context context, Class<?> cls)
    {
        this.disableReceiver(context, cls);
        PendingIntent pendingIntent = this.createPendingIntent(context);
        this.cancelAlarm(context, pendingIntent);
    }

    private Calendar getNextDayNotificationTime() {
        Calendar now = Calendar.getInstance();
        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(Calendar.HOUR_OF_DAY, 4);
        notificationTime.set(Calendar.MINUTE, 0);
        notificationTime.set(Calendar.SECOND, 0);
        if (notificationTime.before(now)) {
            notificationTime.add(Calendar.DATE, 1);
        }

        return notificationTime;
    }

    private void enableReceiver(HomeActivity homeActivity) {
        ComponentName receiver = new ComponentName(homeActivity, HomeActivity.class);
        PackageManager packageManager = homeActivity.getPackageManager();
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void disableReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("type", "daily");
        return PendingIntent.getBroadcast(context, DAILY_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setAlarm(HomeActivity homeActivity, Calendar time, PendingIntent intent) {
        AlarmManager alarmManager = (AlarmManager) homeActivity.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC, time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, intent);
        }
    }

    private void cancelAlarm(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        pendingIntent.cancel();

    }

}
