package aspect.memorio.activities.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.fragments.ListTodosFragment;
import aspect.memorio.models.Todo;
import aspect.memorio.utils.Utils;

public class TodosListViewAdapter extends ListViewAdapterTemplate<Todo> {

    public TodosListViewAdapter(Context context, List<Todo> items, ListFragment<Todo> fragment) {
        super(context, items, fragment, ListViewAdapterItemConfig.todo());
    }

    @Override
    protected void setAdditional(View view, Todo item) {
        Button completeButton = view.findViewById(R.id.button_complete_todo);
        completeButton.setAlpha(0.5f);
        setCompleteButtonAction(completeButton, item);
    }

    @Override
    protected String getItemText(Todo item) {
        return item.getLabel();
    }

    @Override
    protected String getItemAdditionalText(Todo item) {
        if (item.getDate() == null) {
            return getContext().getText(R.string.not_specified_generic).toString();
        } else {
            return Utils.getTimeRemainingFromNowText(item.getDate());
        }
    }

    @Override
    protected int getItemBackgroundColorResource(Todo item) {
        return this.getPriorityColor(item.getPriority());
    }

    @Override
    protected float getTextOpacity(Todo item) {
        return this.getDateOpacity(item.getDate());
    }

    private void setCompleteButtonAction(Button button, final Todo todo) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                ((ListTodosFragment) fragment).completeTodo(todo);
            }
        });
    }
}
