package aspect.memorio.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import aspect.memorio.R;
import aspect.memorio.fragments.ListRemindersFragment;

public class HomeActivityFragmentManager {

    private final HomeActivity homeActivity;

    public HomeActivityFragmentManager(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public enum FragmentType {
        REMINDERS_LIST, CALENDAR_VIEW, PREFERENCES, HELP, ABOUT
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
        // TODO: singletons!!!
        switch (type) {
            case REMINDERS_LIST:
                return new ListRemindersFragment();
            default:
                return new ListRemindersFragment();
        }
    }
}
