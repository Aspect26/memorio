package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aspect.memorio.models.Reminder;
import aspect.memorio.utils.Serialization;

public class DeviceFileRemindersStorage implements RemindersStorage {

    private static final String FILE_NAME = "data.dat";
    private final List<Reminder> data;
    private final Context context;

    public DeviceFileRemindersStorage(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public Reminder createNewItemFromString(String dataString) throws ParseException {
        return Serialization.deserializeReminder(dataString);
    }

    @Override
    public boolean loadAll() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            this.data.clear();

            while (line != null) {
                Reminder note = Serialization.deserializeReminder(line);
                if (note != null) {
                    this.data.add(note);
                }
                line = reader.readLine();
            }

            inputStream.close();
            return true;
        } catch (Exception e) {
            Log.d("[DeviceFileRemindersStorage]", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reminder> getAll() {
        return this.data;
    }

    @Override
    public List<Reminder> getAllToday() {
        List<Reminder> todayReminders = new ArrayList<>();
        for (Reminder reminder : this.data) {
            if (reminder.getDate() == null) {
                continue;
            }

            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());
            Calendar noteCalendarDate = Calendar.getInstance();
            noteCalendarDate.setTimeInMillis(reminder.getDate().getTime());

            if (now.get(Calendar.DAY_OF_MONTH) == noteCalendarDate.get(Calendar.DAY_OF_MONTH)) {
                todayReminders.add(reminder);
            }
        }

        return todayReminders;
    }

    @Override
    public List<Reminder> getAllActive() {
        List<Reminder> nonExpiredReminders = new ArrayList<>();
        for (Reminder reminder : this.data) {
            if (reminder.getDate() == null) {
                nonExpiredReminders.add(reminder);
                continue;
            }

            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());

            Calendar noteCalendarDate = Calendar.getInstance();
            noteCalendarDate.setTimeInMillis(reminder.getDate().getTime());

            if (noteCalendarDate.after(now)) {
                nonExpiredReminders.add(reminder);
            }
        }

        return nonExpiredReminders;
    }

    @Override
    public void add(Reminder reminder) {
        this.data.add(reminder);
        this.flushAll();
    }

    @Override
    public void updateOrAdd(Reminder updatedReminder) {
        Reminder oldReminder = this.findReminder(updatedReminder.getId());
        if (oldReminder == null) {
            this.add(updatedReminder);
        } else {
            oldReminder.setDate(updatedReminder.getDate());
            oldReminder.setNotificationDate(updatedReminder.getNotificationDate());
            oldReminder.setText(updatedReminder.getText());
            oldReminder.setPriority(updatedReminder.getPriority());
        }
        this.flushAll();
    }

    @Override
    public void remove(Reminder reminder) {
        for (int index = 0; index < this.data.size(); ++index) {
            if (this.data.get(index).getId().equals(reminder.getId())) {
                this.data.remove(index);
                break;
            }
        }
        this.flushAll();
    }

    @Override
    public void removeAllReminders() {
        this.data.clear();
        this.flushAll();
    }

    @Override
    public void flushAll() {
        FileOutputStream outputStream;

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            outputStream = new FileOutputStream(file);

            for (Reminder reminder : this.data) {
                outputStream.write(Serialization.serializeReminder(reminder).getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            Log.d("[DeviceFileRemindersStorage]", e.getMessage());
        }
    }

    private Reminder findReminder(String id) {
        for (Reminder reminder : this.data) {
            if (reminder.getId().equals(id)) {
                return reminder;
            }
        }

        return null;
    }
}
