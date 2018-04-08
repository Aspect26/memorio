package aspect.memorio.storage;

import java.util.List;

import aspect.memorio.models.Todo;

public interface TodosStorage {

    boolean loadAll();

    List<Todo> getAll();

    List<Todo> getAllActive();

    void add(Todo todo);

    void updateOrAdd(Todo todo);

    void remove(Todo todo);

    void flushAll();

}
