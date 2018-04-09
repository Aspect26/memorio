package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListTodosFragment;
import aspect.memorio.models.Todo;
import aspect.memorio.utils.Utils;

public class TodosListViewAdapter extends ArrayAdapter<Todo> {

    private static final float MIN_OPACITY = 0.3f;

    private final ListTodosFragment todosFragment;

    public TodosListViewAdapter(Context context, List<Todo> items, ListTodosFragment todosFragment) {
        super(context, R.layout.todo_item, items);
        this.todosFragment = todosFragment;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO: refactor + strategy pattern
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.todo_item, null);
        }

        Todo todo = getItem(position);

        if (todo != null) {
            TextView textView = view.findViewById(R.id.todo_label);
            textView.setText(todo.getLabel().isEmpty()? "<empty>" : todo.getLabel());

            textView = view.findViewById(R.id.todo_remaining_time);
            if (todo.getDate() == null) {
                textView.setText(getContext().getText(R.string.not_specified_generic));
            } else {
                textView.setText(Utils.getTimeRemainingFromNowText(todo.getDate()));
            }

            Button removeButton = view.findViewById(R.id.button_delete_todo);
            removeButton.setAlpha(0.5f);
            setRemoveButtonAction(removeButton, todo);

            Button completeButton = view.findViewById(R.id.button_complete_todo);
            completeButton.setAlpha(0.5f);
            setCompleteButtonAction(completeButton, todo);

            setOnClickListener(view, todo);

            if (todo.getPriority() == Todo.PRIORITY_HIGH) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.high_priority_item));
            } else if (todo.getPriority() == Todo.PRIORITY_NORMAL) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.normal_priority_item));
            } else if (todo.getPriority() == Todo.PRIORITY_LOW) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.low_priority_item));
            }

            this.setOpacity(view, todo);
        }

        return view;
    }

    private void setRemoveButtonAction(Button button, final Todo todo) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                todosFragment.removeItem(todo);
            }
        });
    }

    private void setCompleteButtonAction(Button button, final Todo todo) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                todosFragment.completeTodo(todo);
            }
        });
    }

    private void setOnClickListener(View itemView, final Todo todo) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todosFragment.editItem(todo);
            }
        });
    }

    // TODO: this is duplicity to the other adapter
    private void setOpacity(View view, Todo todo) {
        long minutesDiff = Utils.getTimeRemainingFromNowInMinutes(todo.getDate());
        if (minutesDiff <= 0) {
            return;
        }

        float monthsRemaining = minutesDiff / (60 * 24 * 30.0f);
        float opacity;
        if (monthsRemaining > 1.0) {
            opacity = MIN_OPACITY;
        } else if (monthsRemaining < 0.2f) {
            opacity = 1.0f;
        } else {
            opacity = (1 - monthsRemaining) * MIN_OPACITY + MIN_OPACITY;
        }
        view.findViewById(R.id.todo_label).setAlpha(opacity);
        view.findViewById(R.id.todo_remaining_time).setAlpha(opacity);
    }

}
