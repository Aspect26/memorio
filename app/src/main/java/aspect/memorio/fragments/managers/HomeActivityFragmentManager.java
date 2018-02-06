package aspect.memorio.fragments.managers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import aspect.memorio.R;
import aspect.memorio.activities.HomeActivity;
import aspect.memorio.fragments.AboutFragment;
import aspect.memorio.fragments.CalendarFragment;
import aspect.memorio.fragments.HelpFragment;
import aspect.memorio.fragments.ListRemindersFragment;
import aspect.memorio.fragments.PreferencesFragment;

public class HomeActivityFragmentManager {

    public enum FragmentType {
        REMINDERS_LIST, CALENDAR_VIEW, PREFERENCES, HELP, ABOUT
    }

    private final HomeActivity homeActivity;
    private final ListRemindersFragment listRemindersFragment;
    private final CalendarFragment calendarFragment;
    private final AboutFragment aboutFragment;
    private final HelpFragment helpFragment;
    private final PreferencesFragment preferencesFragment;

    public HomeActivityFragmentManager(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;

        this.listRemindersFragment = new ListRemindersFragment();
        this.listRemindersFragment.setStorage(homeActivity.getStorage());

        this.aboutFragment = new AboutFragment();
        this.helpFragment = new HelpFragment();
        this.preferencesFragment = new PreferencesFragment();
        this.calendarFragment = new CalendarFragment();
    }

    public void showFragment(FragmentType fragmentType, Bundle args) {
        Fragment fragment = this.getFragmentInstance(fragmentType);
        fragment.setArguments(args);

        FragmentTransaction transaction = homeActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_home, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void showFragment(FragmentType fragmentType) {
        this.showFragment(fragmentType, new Bundle());
    }

    private Fragment getFragmentInstance(FragmentType type) {
        switch (type) {
            case REMINDERS_LIST:
                return this.listRemindersFragment;
            case ABOUT:
                return this.aboutFragment;
            case HELP:
                return this.helpFragment;
            case PREFERENCES:
                return this.preferencesFragment;
            case CALENDAR_VIEW:
                return this.calendarFragment;
            default:
                return this.listRemindersFragment;
        }
    }
}
