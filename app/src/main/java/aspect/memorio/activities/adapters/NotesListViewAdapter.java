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
import aspect.memorio.fragments.ListRemindersFragment;
import aspect.memorio.models.Reminder;
import aspect.memorio.utils.Utils;

public class NotesListViewAdapter extends ArrayAdapter<Reminder> {

    private static final float MIN_OPACITY = 0.3f;

    private final ListRemindersFragment remindersFragment;

    public NotesListViewAdapter(Context context, List<Reminder> items, ListRemindersFragment remindersFragment) {
        super(context, R.layout.note_item, items);
        this.remindersFragment = remindersFragment;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO: refactor
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.note_item, null);
        }

        Reminder reminder = getItem(position);

        if (reminder != null) {
            TextView textView = view.findViewById(R.id.note_text);
            textView.setText(reminder.getText().isEmpty()? "<empty>" : reminder.getText());

            textView = view.findViewById(R.id.note_remaining_time);
            if (reminder.getDate() == null) {
                textView.setText("Not specified");
            } else {
                textView.setText(Utils.getTimeRemainingFromNowText(reminder.getDate()));
            }

            Button removeButton = view.findViewById(R.id.button_delete_reminder);
            removeButton.setAlpha(0.5f);
            setRemoveButtonAction(removeButton, reminder);
            setOnClickListener(view, reminder);

            if (reminder.getPriority() == Reminder.PRIORITY_HIGH) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.high_priority_reminder));
            } else if (reminder.getPriority() == Reminder.PRIORITY_NORMAL) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.normal_priority_reminder));
            } else if (reminder.getPriority() == Reminder.PRIORITY_LOW) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.low_priority_reminder));
            }

            this.setOpacity(view, reminder);
        }

        return view;
    }

    private void setRemoveButtonAction(Button button, final Reminder reminder) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                remindersFragment.removeReminder(reminder);
            }
        });
    }

    private void setOnClickListener(View itemView, final Reminder reminder) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remindersFragment.editReminder(reminder);
            }
        });
    }

    private void setOpacity(View view, Reminder reminder) {
        long minutesDiff = Utils.getTimeRemainingFromNowInMinutes(reminder.getDate());
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
        view.findViewById(R.id.note_text).setAlpha(opacity);
        view.findViewById(R.id.note_remaining_time).setAlpha(opacity);
    }

}
