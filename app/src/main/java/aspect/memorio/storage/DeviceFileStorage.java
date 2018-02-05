package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import aspect.memorio.models.Note;

public class DeviceFileStorage implements Storage {

    private static final String FILE_NAME = "data.dat";
    private final List<Note> data;
    private final Context context;

    public DeviceFileStorage(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public boolean loadAll() {
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = context.openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            this.data.clear();

            while (line != null) {
                Note note = Note.createFromString(line);
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
                writer.write(note.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
        }
    }
}
