package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = new FileInputStream(file);
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

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            outputStream = new FileOutputStream(file);

            for (Note note : this.data) {
                outputStream.write(note.toString().getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            Log.d("[DeviceFileStorage]", e.getMessage());
        }
    }
}
