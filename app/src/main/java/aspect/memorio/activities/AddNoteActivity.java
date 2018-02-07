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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;

import aspect.memorio.R;
import aspect.memorio.models.Reminder;

public class AddNoteActivity extends AppCompatActivity {

    public static final String RESULT_INTENT_NOTE = "reminder";
    public static final String INPUT_INTENT_NOTE = "input";

    private static final int DIALOG_CODE_DATE = 1;
    private static final int DIALOG_CODE_TIME = 2;

    private Reminder reminder;

    public AddNoteActivity() {
        reminder = new Reminder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: refactor this method
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent()!= null) {
            String inputReminderString = getIntent().getStringExtra(INPUT_INTENT_NOTE);
            this.setReminder(inputReminderString);
        }

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

        EditText textEditor = findViewById(R.id.edit_text_new_note_text);
        textEditor.setText(this.reminder != null? this.reminder.getText() : "");

        RadioButton highPriorityButton = findViewById(R.id.radio_priority_high);
        highPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    reminder.setPriority(Reminder.PRIORITY_HIGH);
                }
            }
        });

        RadioButton normalPriorityButton = findViewById(R.id.radio_priority_normal);
        normalPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    reminder.setPriority(Reminder.PRIORITY_NORMAL);
                }
            }
        });

        RadioButton lowPriorityButton = findViewById(R.id.radio_priority_low);
        lowPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    reminder.setPriority(Reminder.PRIORITY_LOW);
                }
            }
        });

        this.setDefaultPriorityValue();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO: resolve this deprecation
        Calendar defaultTime = Calendar.getInstance();
        if (reminder.getDate() != null) {
            defaultTime.setTimeInMillis(reminder.getDate().getTime());
        }

        if (id == DIALOG_CODE_DATE) {
            return new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar noteDate = Calendar.getInstance();
                    noteDate.setTimeInMillis((reminder.getDate() != null)? reminder.getDate().getTime() : System.currentTimeMillis());

                    noteDate.set(Calendar.YEAR, year);
                    noteDate.set(Calendar.MONTH, month);
                    noteDate.set(Calendar.DAY_OF_MONTH, day);
                    reminder.setDate(noteDate.getTime());
                }
            }, defaultTime.get(Calendar.YEAR), defaultTime.get(Calendar.MONTH), defaultTime.get(Calendar.DAY_OF_MONTH));
        } else if (id == DIALOG_CODE_TIME) {
            return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    Calendar noteTime = Calendar.getInstance();
                    noteTime.setTimeInMillis((reminder.getDate() != null)? reminder.getDate().getTime() : System.currentTimeMillis());

                    noteTime.set(Calendar.HOUR_OF_DAY, hour);
                    noteTime.set(Calendar.MINUTE, minute);
                    reminder.setDate(noteTime.getTime());
                }
            }, defaultTime.get(Calendar.HOUR_OF_DAY), defaultTime.get(Calendar.MINUTE), true);
        } else {
            return null;
        }
    }

    private void setDefaultPriorityValue() {
        if (this.reminder != null) {
            switch (this.reminder.getPriority()) {
                case Reminder.PRIORITY_HIGH:
                    ((RadioButton) findViewById(R.id.radio_priority_high)).setChecked(true); break;
                case Reminder.PRIORITY_NORMAL:
                    ((RadioButton) findViewById(R.id.radio_priority_normal)).setChecked(true); break;
                case Reminder.PRIORITY_LOW:
                    ((RadioButton) findViewById(R.id.radio_priority_low)).setChecked(true); break;
            }
        }
    }

    private void setReminder(String reminderString) {
        if (reminderString != null) {
            try {
                this.reminder = Reminder.createFromString(reminderString);
            } catch (ParseException ignored) {

            }
        }
    }

    private void saveNoteAndExit() {
        final String text = ((EditText) findViewById(R.id.edit_text_new_note_text)).getText().toString();
        reminder.setText(text);

        Intent intent = new Intent();
        intent.putExtra(RESULT_INTENT_NOTE, reminder.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
