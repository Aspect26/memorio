package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import aspect.memorio.models.Note;

public class DeviceFileStorage implements Storage {

    private static final String FILE_NAME = "data.dat";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMMM-d-H-m-s");

    private List<Note> data;
    private final Context context;

    public DeviceFileStorage(Context context) {
        this.context = context;
    }

    @Override
    public boolean loadAll() {
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = context.openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            this.data = new ArrayList<>();

            while (line != null) {
                Note note = this.createNoteFromData(line);
                if (note != null) {
                    this.data.add(note);
                }
            }

            inputStream.close();
            return true;
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Note> getAll() {
        return this.data;
    }

    @Override
    public void addNote(Note note) {
        this.data.add(note);
    }

    @Override
    public void removeNote(Note note) {
        this.data.remove(note);
    }

    @Override
    public void flushAll() {
        FileOutputStream outputStream;
        BufferedWriter writer;

        try {
            outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            for (Note note : this.data) {
                writer.write(this.getNoteStringRepresentation(note));
                writer.newLine();
            }
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
        }
    }

    private Note createNoteFromData(String line) throws ParseException {
        String[] data = line.split(";");
        if (data.length != 3) {
            Log.d("[DeviceFileStorage]", "Could not read note: " + line);
            return null;
        } else {
            String text = data[0];
            String dateText = data[1];
            String notificationDateText = data[2];

            Date date = (dateText != null && dateText.length() > 0)? DATE_FORMAT.parse(dateText) : null;
            Date notificationDate = (notificationDateText != null && notificationDateText.length() > 0)? DATE_FORMAT.parse(notificationDateText) : null;

            return new Note(text, date, notificationDate);
        }
    }

    private String getNoteStringRepresentation(final Note note) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(note.getText()).append(";");
        stringBuilder.append(DATE_FORMAT.format((note.getDate() != null)? note.getDate() : "")).append(";");
        stringBuilder.append(DATE_FORMAT.format((note.getNotificationDate() != null)? note.getNotificationDate() : "")).append(";");

        return stringBuilder.toString();
    }

}
