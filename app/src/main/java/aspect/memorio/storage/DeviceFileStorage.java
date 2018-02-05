package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import aspect.memorio.models.Reminder;

public class DeviceFileStorage implements Storage {

    private static final String FILE_NAME = "data.dat";
    private final List<Reminder> data;
    private final Context context;

    public DeviceFileStorage(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
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
                Reminder note = Reminder.createFromString(line);
                if (note != null) {
                    this.data.add(note);
                }
                line = reader.readLine();
            }

            inputStream.close();
            return true;
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
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
    public List<Reminder> getAllNonExpired() {
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
    public void addReminder(Reminder reminder) {
        this.data.add(reminder);
    }

    @Override
    public void removeReminder(Reminder reminder) {
        this.data.remove(reminder);
    }

    @Override
    public void flushAll() {
        FileOutputStream outputStream;

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            outputStream = new FileOutputStream(file);

            for (Reminder note : this.data) {
                outputStream.write(note.toString().getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
        }
    }
}
