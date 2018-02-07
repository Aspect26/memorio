package aspect.memorio.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import aspect.memorio.R;
import aspect.memorio.fragments.managers.HomeActivityFragmentManager;
import aspect.memorio.models.Reminder;
import aspect.memorio.notifications.NotificationsManager;
import aspect.memorio.storage.DeviceFileStorage;
import aspect.memorio.storage.Storage;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Storage storage;
    private NotificationsManager notificationsManager;
    private HomeActivityFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: refactor
        super.onCreate(savedInstanceState);
        this.storage = new DeviceFileStorage(this);
        this.storage.loadAll();

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.notificationsManager = new NotificationsManager(this);
        this.fragmentManager = new HomeActivityFragmentManager(this);
        this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.REMINDERS_LIST);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_reminders) {
            this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.REMINDERS_LIST);
        } else if (id == R.id.nav_calendar) {
            this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.CALENDAR_VIEW);
        } else if (id == R.id.nav_settings) {
            this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.PREFERENCES);
        } else if (id == R.id.nav_about) {
            this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.ABOUT);
        } else if (id == R.id.nav_help) {
            this.fragmentManager.showFragment(HomeActivityFragmentManager.FragmentType.HELP);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addOrUpdateReminderNotification(Reminder reminder) {
        this.notificationsManager.addOrUpdateReminderNotification(reminder);
    }

    public Storage getStorage() {
        return this.storage;
    }
}
