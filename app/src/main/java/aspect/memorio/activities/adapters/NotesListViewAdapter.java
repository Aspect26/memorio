package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListRemindersFragment;
import aspect.memorio.models.Reminder;
import aspect.memorio.storage.Storage;

public class NotesListViewAdapter extends ArrayAdapter<Reminder> {

    private enum Measurement {
        YEAR(60 * 24 * 30 * 365, "year"),
        MONTH(60 * 24 * 30, "month"),
        WEEK(60 * 24 * 7, "week"),
        DAY(60 * 24, "day"),
        HOUR(60, "hour"),
        MINUTE(1, "minute");

        public final int multiplier;
        public final String label;

        Measurement(int multiplier, String label) {
            this.multiplier = multiplier;
            this.label = label;
        }
    }

    private static final List<Measurement> measurements = new ArrayList<Measurement>() {{
        add(Measurement.YEAR);
        add(Measurement.MONTH);
        add(Measurement.WEEK);
        add(Measurement.DAY);
        add(Measurement.HOUR);
        add(Measurement.MINUTE);
    }};

    private final Storage storage;
    private final ListRemindersFragment remindersFragment;

    public NotesListViewAdapter(Context context, List<Reminder> items, final Storage storage, ListRemindersFragment remindersFragment) {
        super(context, R.layout.note_item, items);
        this.storage = storage;
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
                long minutesRemaining = this.getNoteRemainingTime(reminder);
                textView.setText(this.getNoteRemainingTimeText(minutesRemaining));
            }

            Button removeButton = view.findViewById(R.id.button_delete_reminder);
            removeButton.setAlpha(0.5f);
            setRemoveButtonAction(removeButton, reminder);
            setOnClickListener(view, reminder);
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

    private long getNoteRemainingTime(Reminder note) {
        Date noteDate = note.getDate();
        return (noteDate.getTime() - new Date().getTime()) / (1000 * 60);
    }

    private String getNoteRemainingTimeText(long minutesRemaining) {
        if (minutesRemaining == 0) {
            return "Now";
        }

        for (int i = 0; i < measurements.size() - 1; ++i) {
            Measurement measurement = measurements.get(i);
            Measurement secondaryMeasurement = measurements.get(i + 1);
            if (minutesRemaining >= measurement.multiplier) {
                int primaryCount = (int) minutesRemaining / measurement.multiplier;
                int secondaryCount = (int) (minutesRemaining - primaryCount * measurement.multiplier) / secondaryMeasurement.multiplier;
                String value = primaryCount + " " + measurement.label + "(s) ";
                if (secondaryCount > 0) {
                    value += secondaryCount + " " + secondaryMeasurement.label + "(s)";
                }

                return value;
            }
        }

        return minutesRemaining + " " + measurements.get(measurements.size() - 1).label + "(s)";
    }

}
