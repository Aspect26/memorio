package aspect.memorio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aspect.memorio.R;
import aspect.memorio.activities.adapters.NotesListViewAdapter;
import aspect.memorio.models.Reminder;
import aspect.memorio.notifications.NotificationsManager;
import aspect.memorio.storage.DeviceFileStorage;
import aspect.memorio.storage.Storage;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_ADD_NOTE = 1;

    private Storage storage;
    private NotificationsManager notificationsManager;
    private ArrayAdapter<Reminder> notesViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.storage = new DeviceFileStorage(this);
        this.storage.loadAll();

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.button_add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewNoteActivity();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView notesListView = findViewById(R.id.list_notes);
        this.notesViewAdapter = new NotesListViewAdapter(this, this.storage.getAll());
        notesListView.setAdapter(this.notesViewAdapter);

        this.setAutomaticUpdate();

        this.notificationsManager = new NotificationsManager(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reinitializeRemindersView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_NOTE) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void setAutomaticUpdate() {
        final HomeActivity thisActivity = this;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                thisActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reinitializeRemindersView();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000 * 30, 1000 * 30);
    }

    private void addReminder(Reminder reminder) {
        this.storage.addReminder(reminder);
        this.storage.flushAll();
        this.notesViewAdapter.notifyDataSetChanged();
        this.notificationsManager.addReminderNotification(reminder);
    }

    private void goToNewNoteActivity() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }
    
}
