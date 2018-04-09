package aspect.memorio.fragments;

import android.view.View;

import aspect.memorio.R;
import aspect.memorio.fragments.config.TodoFragmentConfig;
import aspect.memorio.models.Todo;
import aspect.memorio.storage.ItemsStorage;
import aspect.memorio.utils.SnackbarUtils;

public class ListTodosFragment extends ListFragment<Todo> {

    public ListTodosFragment() {
        super(TodoFragmentConfig.get());
    }

    public void completeTodo(final Todo todo) {
        todo.setDone(true);
        this.storage.updateOrAdd(todo);
        this.reinitializeItemsView();

        if (getView() != null) {
            SnackbarUtils.showUndoSnackbar(getView(), R.string.snackbar_todo_completed, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todo.setDone(false);
                    storage.updateOrAdd(todo);
                    reinitializeItemsView();
                }
            });
        }
    }

    @Override
    protected ItemsStorage<Todo> getStorage() {
        return this.homeActivity.getTodosStorage();
    }
}