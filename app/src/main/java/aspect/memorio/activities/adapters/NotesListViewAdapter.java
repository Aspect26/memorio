package aspect.memorio.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.models.Note;

public class NotesListViewAdapter extends ArrayAdapter<Note> {

    public NotesListViewAdapter(Context context, List<Note> items) {
        super(context, R.layout.note_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: refactor
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.note_item, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView text = v.findViewById(R.id.text_note_text);
            text.setText(note.getText());
        }

        return v;
    }


}
