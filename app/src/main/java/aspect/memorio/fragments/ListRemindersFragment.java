package aspect.memorio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aspect.memorio.R;
import aspect.memorio.activities.AddNoteActivity;
import aspect.memorio.activities.HomeActivity;
import aspect.memorio.activities.adapters.NotesListViewAdapter;
import aspect.memorio.models.Reminder;
import aspect.memorio.storage.Storage;

import static android.app.Activity.RESULT_OK;

public class ListRemindersFragment extends Fragment {

    public static final int REQUEST_ADD_NOTE = 1;
    public static final int REQUEST_EDIT_NOTE = 2;

    private ArrayAdapter<Reminder> notesViewAdapter;
    private Storage storage;
    private HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: refactor
        View view = inflater.inflate(R.layout.fragment_list_reminders, container, false);
        FloatingActionButton fab = view.findViewById(R.id.button_add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewNoteActivity();
            }
        });

        ListView notesListView = view.findViewById(R.id.list_notes);
        this.notesViewAdapter = new NotesListViewAdapter(getActivity(), this.storage.getAll(), this.storage, this);
        notesListView.setAdapter(this.notesViewAdapter);

        this.setAutomaticUpdate();

        this.homeActivity = (HomeActivity) getActivity();

        return view;
    }

    private void reinitializeRemindersView() {
        if (this.storage == null) {
            return;
        }
        List<Reminder> reminders = this.storage.getAllNonExpired();

        Collections.sort(reminders, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder left, Reminder right) {
                int comparisonByPriority = right.getPriority() - left.getPriority();
                if (comparisonByPriority != 0) {
                    return comparisonByPriority;
                }

                if (left.getDate() == null && right.getDate() == null) {
                    return 0;
                } else if (left.getDate() == null) {
                    return -1;
                } else if (right.getDate() == null) {
                    return 1;
                }
                return (int) (left.getDate().getTime() / 60000) - (int) (right.getDate().getTime() / 60000);
            }
        });

        this.notesViewAdapter.clear();
        this.notesViewAdapter.addAll(reminders);
        this.notesViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reinitializeRemindersView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: refactor this method
        if (requestCode == REQUEST_ADD_NOTE || requestCode == REQUEST_EDIT_NOTE) {
            try {
                if (resultCode != RESULT_OK || data.getExtras() == null || data.getExtras().getString(AddNoteActivity.RESULT_INTENT_NOTE) == null) {
                    return;
                }
                Reminder reminder = Reminder.createFromString(data.getExtras().getString(AddNoteActivity.RESULT_INTENT_NOTE));
                if (requestCode == REQUEST_ADD_NOTE) {
                    this.addReminder(reminder);
                } else {
                    this.updateReminder(reminder);
                }
            } catch (ParseException e) {
                return;
            }
        }
    }

    public void editReminder(Reminder reminder) {
        this.gotoEditNoteActivity(reminder);
    }

    private void setAutomaticUpdate() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    reinitializeRemindersView();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000 * 30, 1000 * 30);
    }

    public void removeReminder(final Reminder reminder) {
        storage.removeReminder(reminder);
        this.reinitializeRemindersView();

        Snackbar undoSnackBar = Snackbar.make(getView(), R.string.snackbar_reminder_removed, Snackbar.LENGTH_LONG);
        undoSnackBar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storage.addReminder(reminder);
                reinitializeRemindersView();
            }
        });
        undoSnackBar.setActionTextColor(getResources().getColor(R.color.colorPrimaryLight));
        undoSnackBar.show();
    }

    private void addReminder(Reminder reminder) {
        this.storage.addReminder(reminder);
        this.storage.flushAll();
        this.reinitializeRemindersView();
        homeActivity.addOrUpdateReminderNotification(reminder);
    }

    private void updateReminder(Reminder reminder) {
        this.storage.updateOrAddReminder(reminder);
        this.storage.flushAll();
        this.reinitializeRemindersView();
        homeActivity.addOrUpdateReminderNotification(reminder);
    }

    private void gotoEditNoteActivity(Reminder reminder) {
        Intent intent = new Intent(homeActivity, AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.INPUT_INTENT_NOTE, reminder.toString());
        startActivityForResult(intent, REQUEST_EDIT_NOTE);
    }

    private void goToNewNoteActivity() {
        Intent intent = new Intent(homeActivity, AddNoteActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

}
