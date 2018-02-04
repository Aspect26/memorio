package aspect.memorio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import aspect.memorio.R;
import aspect.memorio.activities.HomeActivity;
import aspect.memorio.models.Note;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button addButton = findViewById(R.id.button_save_new_note);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteAndExit();
            }
        });
    }

    private void saveNoteAndExit() {
        final String text = ((EditText) findViewById(R.id.edit_text_new_note_text)).getText().toString();
        Note newNote = new Note(text, null, null);

        Intent intent = new Intent();
        // TODO: use constant omg
        intent.putExtra("note", newNote.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
