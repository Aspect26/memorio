package aspect.memorio.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import aspect.memorio.R;
import aspect.memorio.models.Todo;
import aspect.memorio.utils.Utils;

public class AddTodoActivity extends AppCompatActivity {

    public static final String RESULT_INTENT_TODO = "todo";
    public static final String INPUT_INTENT_TODO = "input_todo";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("d.MM.y H:m");

    private Todo todo;

    public AddTodoActivity() {
        todo = new Todo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: refactor this method
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_todo);
        Toolbar toolbar = findViewById(R.id.add_todo_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            String inputReminderString = getIntent().getStringExtra(INPUT_INTENT_TODO);
            this.setTodo(inputReminderString);
        }

        Button addButton = findViewById(R.id.button_todo_done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodoAndExit();
            }
        });

        Button dateButton = findViewById(R.id.button_add_todo_date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        Button timeButton = findViewById(R.id.button_add_todo_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        EditText textEditor = findViewById(R.id.edit_text_new_todo_label);
        textEditor.setText(this.todo != null? this.todo.getLabel() : "");

        RadioButton highPriorityButton = findViewById(R.id.radio_todo_priority_high);
        highPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    todo.setPriority(Todo.PRIORITY_HIGH);
                }
            }
        });

        RadioButton normalPriorityButton = findViewById(R.id.radio_todo_priority_normal);
        normalPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    todo.setPriority(Todo.PRIORITY_NORMAL);
                }
            }
        });

        RadioButton lowPriorityButton = findViewById(R.id.radio_todo_priority_low);
        lowPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    todo.setPriority(Todo.PRIORITY_LOW);
                }
            }
        });

        this.setDefaultPriorityValue();
        this.refreshDateTimeTexts();
    }

    // TODO: duplicity with the add note activity
    private void showDateDialog() {
        Calendar defaultTime = getDefaultTimeForDateTimeDialog();
        hideKeyboard();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar noteDate = Calendar.getInstance();
                noteDate.setTimeInMillis((todo.getDate() != null)? todo.getDate().getTime() : System.currentTimeMillis());

                noteDate.set(Calendar.YEAR, year);
                noteDate.set(Calendar.MONTH, month);
                noteDate.set(Calendar.DAY_OF_MONTH, day);
                todo.setDate(noteDate.getTime());
                refreshDateTimeTexts();
            }
        }, defaultTime.get(Calendar.YEAR), defaultTime.get(Calendar.MONTH), defaultTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        Calendar defaultTime = getDefaultTimeForDateTimeDialog();
        hideKeyboard();

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar noteTime = Calendar.getInstance();
                noteTime.setTimeInMillis((todo.getDate() != null)? todo.getDate().getTime() : System.currentTimeMillis());

                noteTime.set(Calendar.HOUR_OF_DAY, hour);
                noteTime.set(Calendar.MINUTE, minute);
                todo.setDate(noteTime.getTime());
                refreshDateTimeTexts();
            }
        }, defaultTime.get(Calendar.HOUR_OF_DAY), defaultTime.get(Calendar.MINUTE), true).show();
    }

    private Calendar getDefaultTimeForDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        if (todo.getDate() != null) {
            calendar.setTimeInMillis(todo.getDate().getTime());
        }

        return calendar;
    }

    private void hideKeyboard() {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View contentView = findViewById(R.id.content_add_todo);
        if (imm != null && contentView != null) {
            imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
        }
    }

    private void setDefaultPriorityValue() {
        if (this.todo != null) {
            switch (this.todo.getPriority()) {
                case Todo.PRIORITY_HIGH:
                    ((RadioButton) findViewById(R.id.radio_todo_priority_high)).setChecked(true); break;
                case Todo.PRIORITY_NORMAL:
                    ((RadioButton) findViewById(R.id.radio_todo_priority_normal)).setChecked(true); break;
                case Todo.PRIORITY_LOW:
                    ((RadioButton) findViewById(R.id.radio_todo_priority_low)).setChecked(true); break;
            }
        }
    }

    private void setTodo(String todoString) {
        if (todoString != null) {
            try {
                this.todo = Todo.createFromString(todoString);
            } catch (ParseException ignored) {

            }
        }
    }

    private void refreshDateTimeTexts() {
        ((TextView) findViewById(R.id.text_datetime_todo)).setText(
                this.todo.getDate() != null? DATE_FORMAT.format(this.todo.getDate()) : getResources().getText(R.string.not_specified_date)
        );

        ((TextView) findViewById(R.id.text_todo_remaining_time)).setText(
                this.todo.getDate() != null? Utils.getTimeRemainingFromNowText(this.todo.getDate()) : ""
        );
    }

    private void saveTodoAndExit() {
        final String text = ((EditText) findViewById(R.id.edit_text_new_todo_label)).getText().toString();
        todo.setLabel(text);

        Intent intent = new Intent();
        intent.putExtra(RESULT_INTENT_TODO, todo.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
