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

    private ArrayAdapter<Reminder> notesViewAdapter;
    private Storage storage;
    private HomeActivity homeActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO: refactor
        super.onCreate(savedInstanceState);
        View view = getView();
        if (view == null) {
            return;
        }

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
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_reminders, container, false);
    }

    private void reinitializeRemindersView() {
        List<Reminder> reminders = this.storage.getAllNonExpired();

        Collections.sort(reminders, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder left, Reminder right) {
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
        if (requestCode == HomeActivity.REQUEST_ADD_NOTE) {
            // TODO: refactor
            try {
                if (resultCode != RESULT_OK || data.getExtras() == null || data.getExtras().getString(AddNoteActivity.INTENT_NOTE) == null) {
                    return;
                }
                Reminder reminder = Reminder.createFromString(data.getExtras().getString(AddNoteActivity.INTENT_NOTE));
                if (reminder != null) {
                    this.addReminder(reminder);
                }
            } catch (ParseException e) {
                return;
            }
        }
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

        Snackbar undoSnackBar = Snackbar.make(getView().findViewById(R.id.content_home), R.string.snackbar_reminder_removed, Snackbar.LENGTH_LONG);
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
        this.notesViewAdapter.notifyDataSetChanged();
        homeActivity.addReminderNotification(reminder);
    }

    private void goToNewNoteActivity() {
        Intent intent = new Intent(homeActivity, AddNoteActivity.class);
        startActivityForResult(intent, HomeActivity.REQUEST_ADD_NOTE);
    }

}
