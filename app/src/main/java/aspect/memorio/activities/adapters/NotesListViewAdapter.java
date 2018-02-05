package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: refactor
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.note_item, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView textView = view.findViewById(R.id.note_text);
            textView.setText(note.getText().isEmpty()? "Empty" : note.getText());

            textView = view.findViewById(R.id.note_remaining_time);
            textView.setText(this.getNoteRemainingTimeText(note));
        }

        return view;
    }

    private String getNoteRemainingTimeText(Note note) {
        Date noteDate = note.getDate();
        if (noteDate == null) {
            return "Not specified";
        }

        long diffInMinutes = (noteDate.getTime() - new Date().getTime()) / (1000 * 60);
        if (diffInMinutes <= 0) {
            return "Expired";
        }

        // TODO: refactor to some array or something
        if (diffInMinutes >= MINUTES_OF_YEAR) {
            return diffInMinutes / MINUTES_OF_YEAR + " year(s)";
        } else if (diffInMinutes >= MINUTES_OF_MONTH) {
            return diffInMinutes / MINUTES_OF_MONTH + " month(s)";
        } else if (diffInMinutes >= MINUTES_OF_WEEK) {
            return diffInMinutes / MINUTES_OF_WEEK + " week(s)";
        } else if (diffInMinutes >= MINUTES_OF_DAY) {
            return diffInMinutes / MINUTES_OF_DAY + " day(s)";
        } else if (diffInMinutes >= MINUTES_OF_HOUR) {
            return diffInMinutes / MINUTES_OF_HOUR + " hour(s)";
        } else {
            return diffInMinutes + " minute(s)";
        }

    }


}
