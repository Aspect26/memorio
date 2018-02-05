package aspect.memorio.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import aspect.memorio.R;
import aspect.memorio.models.Reminder;

public class AddNoteActivity extends AppCompatActivity {

    public static final String INTENT_NOTE = "note";

    private static final int DIALOG_CODE_DATE = 1;
    private static final int DIALOG_CODE_TIME = 2;

    private final Reminder note;

    public AddNoteActivity() {
        note = new Reminder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button addButton = findViewById(R.id.button_done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteAndExit();
            }
        });

        Button dateButton = findViewById(R.id.button_add_note_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_CODE_DATE);
            }
        });

        Button timeButton = findViewById(R.id.button_add_note_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_CODE_TIME);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();

        if (id == DIALOG_CODE_DATE) {
            return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar noteDate = Calendar.getInstance();
                    noteDate.setTimeInMillis((note.getDate() != null)? note.getDate().getTime() : System.currentTimeMillis());

                    noteDate.set(Calendar.YEAR, year);
                    noteDate.set(Calendar.MONTH, month);
                    noteDate.set(Calendar.DAY_OF_MONTH, day);
                    note.setDate(noteDate.getTime());
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } else if (id == DIALOG_CODE_TIME) {
            return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    Calendar noteTime = Calendar.getInstance();
                    noteTime.setTimeInMillis((note.getDate() != null)? note.getDate().getTime() : System.currentTimeMillis());

                    noteTime.set(Calendar.HOUR, hour);
                    noteTime.set(Calendar.MINUTE, minute);
                    note.setDate(noteTime.getTime());
                }
            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        } else {
            return null;
        }
    }

    private void saveNoteAndExit() {
        final String text = ((EditText) findViewById(R.id.edit_text_new_note_text)).getText().toString();
        note.setText(text);

        Intent intent = new Intent();
        intent.putExtra(INTENT_NOTE, note.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
