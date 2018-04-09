package aspect.memorio.storage;

import java.util.List;

import aspect.memorio.models.Todo;

public interface TodosStorage extends ItemsStorage<Todo> {

    boolean loadAll();

    List<Todo> getAll();

    void remove(Todo todo);

    void flushAll();

}
