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

import aspect.memorio.models.Todo;

public class DeviceFileTODOsStorage implements TodosStorage {

    private static final String FILE_NAME = "data_todo.dat";
    private final List<Todo> data;
    private final Context context;

    public DeviceFileTODOsStorage(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    // TODO: duplicity with the other storage
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
                Todo todo = Todo.createFromString(line);
                if (todo != null) {
                    this.data.add(todo);
                }
                line = reader.readLine();
            }

            inputStream.close();
            return true;
        } catch (Exception e) {
            Log.d("[DeviceFileTODOsStorage]", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Todo> getAll() {
        return this.data;
    }

    @Override
    public List<Todo> getAllActive() {
        List<Todo> activeTODOs = new ArrayList<>();
        for (Todo todo : this.data) {
            if (todo.isDone()) {
                continue;
            }

            if (todo.getDate() == null) {
                activeTODOs.add(todo);
                continue;
            }

            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(System.currentTimeMillis());

            Calendar todoDate = Calendar.getInstance();
            todoDate.setTimeInMillis(todo.getDate().getTime());

            if (todoDate.after(now)) {
                activeTODOs.add(todo);
            }
        }

        return activeTODOs;
    }

    @Override
    public void add(Todo todo) {
        this.data.add(todo);
        this.flushAll();
    }

    @Override
    public void updateOrAdd(Todo updatedTodo) {
        Todo oldTodo = this.findTodo(updatedTodo.getId());
        if (oldTodo == null) {
            this.add(updatedTodo);
        } else {
            oldTodo.setLabel(updatedTodo.getLabel());
            oldTodo.setDate(updatedTodo.getDate());
            oldTodo.setDone(updatedTodo.isDone());
            oldTodo.setPriority(updatedTodo.getPriority());
            this.flushAll();
        }
    }

    @Override
    public void remove(Todo todo) {
        for (int index = 0; index < this.data.size(); ++index) {
            if (this.data.get(index).getId().equals(todo.getId())) {
                this.data.remove(index);
                break;
            }
        }
        this.flushAll();
    }

    @Override
    public void flushAll() {
        FileOutputStream outputStream;

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            outputStream = new FileOutputStream(file);

            for (Todo todo : this.data) {
                outputStream.write(todo.toString().getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            Log.d("[DeviceFileTODOsStorage]", e.getMessage());
        }
    }

    private Todo findTodo(String id) {
        for (Todo todo : this.data) {
            if (todo.getId().equals(id)) {
                return todo;
            }
        }

        return null;
    }
}
