package aspect.memorio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aspect.memorio.R;
import aspect.memorio.activities.AddTodoActivity;
import aspect.memorio.activities.HomeActivity;
import aspect.memorio.activities.adapters.TodosListViewAdapter;
import aspect.memorio.models.Todo;
import aspect.memorio.storage.TodosStorage;

import static android.app.Activity.RESULT_OK;

// TODO: Strategy pattern for this  fragment and reminders fragment
public class ListTodosFragment extends Fragment {

    public static final int REQUEST_ADD_TODO = 1;
    public static final int REQUEST_EDIT_TODO = 2;

    private ArrayAdapter<Todo> todosViewAdapter;
    private HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: refactor
        View view = inflater.inflate(R.layout.fragment_list_todos, container, false);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(R.string.toolbar_title_list_todos);

        this.homeActivity = (HomeActivity) getActivity();

        FloatingActionButton fab = view.findViewById(R.id.button_add_todo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewTodoActivity();
            }
        });

        ListView todosListView = view.findViewById(R.id.list_todos);
        this.todosViewAdapter = new TodosListViewAdapter(getActivity(), this.getStorage().getAll(), this);
        todosListView.setAdapter(this.todosViewAdapter);

        this.setAutomaticUpdate();

        return view;
    }

    private void reinitializeTodosView() {
        if (this.getStorage() == null) {
            return;
        }
        List<Todo> todos = this.getStorage().getAllActive();

        Collections.sort(todos, new Comparator<Todo>() {
            @Override
            public int compare(Todo left, Todo right) {
                int comparisonByPriority = right.getPriority() - left.getPriority();
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
        });

        this.todosViewAdapter.clear();
        this.todosViewAdapter.addAll(todos);
        this.todosViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reinitializeTodosView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: refactor this method
        if (requestCode == REQUEST_ADD_TODO || requestCode == REQUEST_EDIT_TODO) {
            try {
                if (resultCode != RESULT_OK || data.getExtras() == null || data.getExtras().getString(AddTodoActivity.RESULT_INTENT_TODO) == null) {
                    return;
                }
                Todo todo = Todo.createFromString(data.getExtras().getString(AddTodoActivity.RESULT_INTENT_TODO));
                if (requestCode == REQUEST_ADD_TODO) {
                    this.addTodo(todo);
                } else {
                    this.updateTodo(todo);
                }
            } catch (ParseException e) {
                return;
            }
        }
    }

    public void editTodo(Todo todo) {
        this.gotoEditTodoActivity(todo);
    }

    public void removeTodo(final Todo todo) {
        this.getStorage().remove(todo);
        this.reinitializeTodosView();

        Snackbar undoSnackBar = Snackbar.make(getView(), R.string.snackbar_todo_removed, Snackbar.LENGTH_LONG);
        undoSnackBar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStorage().add(todo);
                reinitializeTodosView();
            }
        });
        undoSnackBar.setActionTextColor(getResources().getColor(R.color.colorPrimaryLight));
        undoSnackBar.show();
    }

    public void completeTodo(final Todo todo) {
        // TODO: implement
    }

    private void setAutomaticUpdate() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reinitializeTodosView();
                    }
                });
            }
        };
        // TODO: magic constants?
        timer.scheduleAtFixedRate(task, 1000 * 30, 1000 * 30);
    }

    private TodosStorage getStorage() {
        return this.homeActivity.getTodosStorage();
    }

    private void addTodo(Todo todo) {
        this.getStorage().add(todo);
        this.reinitializeTodosView();
    }

    private void updateTodo(Todo todo) {
        this.getStorage().updateOrAdd(todo);
        this.reinitializeTodosView();
    }

    private void gotoEditTodoActivity(Todo todo) {
        Intent intent = new Intent(homeActivity, AddTodoActivity.class);
        intent.putExtra(AddTodoActivity.INPUT_INTENT_TODO, todo.toString());
        startActivityForResult(intent, REQUEST_EDIT_TODO);
    }

    private void goToNewTodoActivity() {
        Intent intent = new Intent(homeActivity, AddTodoActivity.class);
        startActivityForResult(intent, REQUEST_ADD_TODO);
    }

}
