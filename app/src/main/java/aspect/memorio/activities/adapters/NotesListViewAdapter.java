package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import aspect.memorio.R;
import aspect.memorio.models.Note;

public class NotesListViewAdapter extends ArrayAdapter<Note> {

    private static final int MINUTES_OF_YEAR = 60 * 24 * 30 * 365;
    private static final int MINUTES_OF_MONTH = 60 * 24 * 30;
    private static final int MINUTES_OF_WEEK = 60 * 24 * 7;
    private static final int MINUTES_OF_DAY = 60 * 24;
    private static final int MINUTES_OF_HOUR = 60;

    public NotesListViewAdapter(Context context, List<Note> items) {
        super(context, R.layout.note_item, items);
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

        view.setVisibility(View.VISIBLE);
        Note note = getItem(position);

        if (note != null) {
            TextView textView = view.findViewById(R.id.note_text);
            textView.setText(note.getText().isEmpty()? "Empty" : note.getText());

            textView = view.findViewById(R.id.note_remaining_time);
            if (note.getDate() == null) {
                textView.setText("Not specified");
            } else {
                long minutesRemaining = this.getNoteRemainingTime(note);
                if (minutesRemaining < 0) {
                    view.setVisibility(View.GONE);
                }
                textView.setText(this.getNoteRemainingTimeText(minutesRemaining));
            }
        }

        return view;
    }

    private long getNoteRemainingTime(Note note) {
        Date noteDate = note.getDate();
        return (noteDate.getTime() - new Date().getTime()) / (1000 * 60);
    }

    private String getNoteRemainingTimeText(long minutesRemaining) {
        if (minutesRemaining == 0) {
            return "Now";
        }

        if (minutesRemaining >= MINUTES_OF_YEAR) {
            return minutesRemaining / MINUTES_OF_YEAR + " year(s)";
        } else if (minutesRemaining >= MINUTES_OF_MONTH) {
            return minutesRemaining / MINUTES_OF_MONTH + " month(s)";
        } else if (minutesRemaining >= MINUTES_OF_WEEK) {
            return minutesRemaining / MINUTES_OF_WEEK + " week(s)";
        } else if (minutesRemaining >= MINUTES_OF_DAY) {
            return minutesRemaining / MINUTES_OF_DAY + " day(s)";
        } else if (minutesRemaining >= MINUTES_OF_HOUR) {
            return minutesRemaining / MINUTES_OF_HOUR + " hour(s)";
        } else {
            return minutesRemaining + " minute(s)";
        }
    }

}
