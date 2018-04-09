package aspect.memorio.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import aspect.memorio.R;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.models.Reminder;
import aspect.memorio.utils.Utils;

public class AddReminderActivity extends AddItemActivity {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("d.MM.y H:m");
    // TODO: this would be better in enum
    private static final int MINUTES_15 = 15;
    private static final int HOUR = 60;
    private static final int HOURS_2 = 2 * 60;
    private static final int DAY = 60 * 24;
    private static final int WEEK = 60 * 24 * 7;

    private Reminder reminder;

    public AddReminderActivity() {
        reminder = new Reminder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null &&  getIntent().getStringExtra(ListFragment.INTENT_ITEM) != null) {
            String inputReminderString = getIntent().getStringExtra(ListFragment.INTENT_ITEM);
            this.setReminder(inputReminderString);
        } else if (savedInstanceState != null && savedInstanceState.getString(ListFragment.INTENT_ITEM) != null) {
            String inputReminderString = savedInstanceState.getString(ListFragment.INTENT_ITEM);
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
                showDateDialog();
            }
        });

        Button timeButton = findViewById(R.id.button_add_note_time);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
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

        ((CheckBox) findViewById(R.id.checkbox_notification)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                setNotificationRadioButtonsEnabled(checked);
            }
        });

        findViewById(R.id.radio_notification_ontime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(0);
            }
        });

        findViewById(R.id.radio_notification_15_minutes_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(MINUTES_15);
            }
        });

        findViewById(R.id.radio_notification_hour_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(HOUR);
            }
        });

        findViewById(R.id.radio_notification_2_hours_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(HOURS_2);
            }
        });

        findViewById(R.id.radio_notification_day_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(DAY);
            }
        });

        findViewById(R.id.radio_notification_week_before).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReminderNotificationTime(WEEK);
            }
        });

        this.setDefaultPriorityValue();
        this.setDefaultNotificationValue();
        this.refreshDateTimeTexts();
        this.setNotificationsEnabled(this.reminder.getDate() != null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ListFragment.INTENT_ITEM, this.reminder.toString());
    }

    private void showDateDialog() {
        Calendar defaultTime = getDefaultTimeForDateTimeDialog();
        hideKeyboard();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar noteDate = Calendar.getInstance();
                noteDate.setTimeInMillis((reminder.getDate() != null)? reminder.getDate().getTime() : System.currentTimeMillis());

                noteDate.set(Calendar.YEAR, year);
                noteDate.set(Calendar.MONTH, month);
                noteDate.set(Calendar.DAY_OF_MONTH, day);
                reminder.setDate(noteDate.getTime());
                setNotificationsEnabled(true);
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
                noteTime.setTimeInMillis((reminder.getDate() != null)? reminder.getDate().getTime() : System.currentTimeMillis());

                noteTime.set(Calendar.HOUR_OF_DAY, hour);
                noteTime.set(Calendar.MINUTE, minute);
                reminder.setDate(noteTime.getTime());
                setNotificationsEnabled(true);
                refreshDateTimeTexts();
            }
        }, defaultTime.get(Calendar.HOUR_OF_DAY), defaultTime.get(Calendar.MINUTE), true).show();
    }

    private Calendar getDefaultTimeForDateTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        if (reminder.getDate() != null) {
            calendar.setTimeInMillis(reminder.getDate().getTime());
        }

        return calendar;
    }

    private void hideKeyboard() {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View contentView = findViewById(R.id.content_add_note);
        if (imm != null && contentView != null) {
            imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
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

    private void setDefaultNotificationValue() {
        if (this.reminder.getNotificationDate() == null) {
            this.setNotificationRadioButtonsEnabled(false);
        } else {
            this.setNotificationRadioButtonsEnabled(true);
            findViewById(R.id.checkbox_notification).setEnabled(true);
            int minutesDiff = (int) (this.reminder.getDate().getTime() - this.reminder.getNotificationDate().getTime()) / (1000 * 60);
            switch (minutesDiff) {
                case MINUTES_15:
                    ((RadioButton) findViewById(R.id.radio_notification_15_minutes_before)).setChecked(true); break;
                case HOUR:
                    ((RadioButton) findViewById(R.id.radio_notification_hour_before)).setChecked(true); break;
                case HOURS_2:
                    ((RadioButton) findViewById(R.id.radio_notification_2_hours_before)).setChecked(true); break;
                case DAY:
                    ((RadioButton) findViewById(R.id.radio_notification_day_before)).setChecked(true); break;
                case WEEK:
                    ((RadioButton) findViewById(R.id.radio_notification_week_before)).setChecked(true); break;
                default:
                    ((RadioButton) findViewById(R.id.radio_notification_ontime)).setChecked(true); break;
            }
        }
    }

    private void setNotificationsEnabled(final boolean enabled) {
        CheckBox notificationCheckbox = findViewById(R.id.checkbox_notification);
        notificationCheckbox.setEnabled(enabled);
        notificationCheckbox.setTextColor(enabled? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.disabled));

        this.setNotificationRadioButtonsEnabled(enabled);
    }

    private void setNotificationRadioButtonsEnabled(final boolean enabled) {
        RadioGroup notificationTimesRadioGroup = findViewById(R.id.radio_group_notification);
        for(int i = 0; i < notificationTimesRadioGroup.getChildCount(); i++){
            RadioButton button = (RadioButton) notificationTimesRadioGroup.getChildAt(i);
            button.setEnabled(enabled);
            button.setTextColor((enabled)? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.disabled));
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

    private void setReminderNotificationTime(final int minutesBefore) {
        if (this.reminder.getDate() == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.reminder.getDate().getTime());
        calendar.add(Calendar.MINUTE, -minutesBefore);

        this.reminder.setNotificationDate(new Date(calendar.getTimeInMillis()));
    }

    private void refreshDateTimeTexts() {
        ((TextView) findViewById(R.id.text_datetime)).setText(
                this.reminder.getDate() != null? DATE_FORMAT.format(this.reminder.getDate()) : getResources().getText(R.string.not_specified_date)
        );

        ((TextView) findViewById(R.id.text_remaining_time)).setText(
                this.reminder.getDate() != null? Utils.getTimeRemainingFromNowText(this.reminder.getDate()) : ""
        );
    }

    private void saveNoteAndExit() {
        final String text = ((EditText) findViewById(R.id.edit_text_new_note_text)).getText().toString();
        reminder.setText(text);

        Intent intent = new Intent();
        intent.putExtra(ListFragment.INTENT_ITEM, reminder.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
