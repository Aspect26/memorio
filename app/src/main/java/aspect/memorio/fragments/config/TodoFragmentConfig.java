package aspect.memorio.fragments.config;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Comparator;

import aspect.memorio.R;
import aspect.memorio.activities.AddItemActivity;
import aspect.memorio.activities.AddTodoActivity;
import aspect.memorio.activities.adapters.TodosListViewAdapter;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.fragments.ListTodosFragment;
import aspect.memorio.models.Todo;

public class TodoFragmentConfig extends ListFragmentConfig<Todo> {

    private TodoFragmentConfig(int layout, int title, int list_view, int add_button, int stringItemRemoved,
                               AdapterCreator<Todo> itemsViewAdapterCreator, Comparator<Todo> itemsComparator,
                               Class<? extends AddItemActivity> addItemActivity) {
        super(layout, title, list_view, add_button, stringItemRemoved, itemsViewAdapterCreator, itemsComparator, addItemActivity);
    }

    public static TodoFragmentConfig get() {
        return new TodoFragmentConfig(
                R.layout.fragment_list_todos,
                R.string.toolbar_title_list_todos,
                R.id.list_todos,
                R.id.button_add_todo,
                R.string.snackbar_todo_removed,
                new AdapterCreator<Todo>() {
                    @Override
                    public ArrayAdapter<Todo> createAdapter(ListFragment<Todo> fragment) {
                        return new TodosListViewAdapter(fragment.getActivity(), new ArrayList<Todo>(), (ListTodosFragment) fragment);
                    }
                },
                new Comparator<Todo>() {
                    @Override
                    public int compare(Todo left, Todo right) {
                        int comparisonByPriority = right.getPriority().value - left.getPriority().value;
                        if (comparisonByPriority != 0) {
                            return comparisonByPriority;
                        }

                        if (left.getDate() == null && right.getDate() == null) {
                            return 0;
                        } else if (left.getDate() == null) {
                            return -1;
                        } else if (right.getDate() == null) {
                            return 1;
                        }
                        return (int) (left.getDate().getTime() / 60000) - (int) (right.getDate().getTime() / 60000);
                    }
                },
                AddTodoActivity.class
        );
    }

}
