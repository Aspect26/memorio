package aspect.memorio.fragments.config;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Comparator;

import aspect.memorio.R;
import aspect.memorio.activities.AddItemActivity;
import aspect.memorio.activities.AddReminderActivity;
import aspect.memorio.activities.adapters.RemindersListViewAdapter;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.fragments.ListRemindersFragment;
import aspect.memorio.models.Reminder;

public class RemindersFragmentConfig extends ListFragmentConfig<Reminder> {

    private RemindersFragmentConfig(int layout, int title, int list_view, int add_button, int stringItemRemoved,
                                    AdapterCreator<Reminder> itemsViewAdapterCreator, Comparator<Reminder> itemsComparator,
                                    Class<? extends AddItemActivity> addItemActivity) {
        super(layout, title, list_view, add_button, stringItemRemoved, itemsViewAdapterCreator, itemsComparator, addItemActivity);
    }

    public static RemindersFragmentConfig get() {
        return new RemindersFragmentConfig(
                R.layout.fragment_list_reminders,
                R.string.toolbar_title_list_reminders,
                R.id.list_reminders,
                R.id.button_add_reminder,
                R.string.snackbar_reminder_removed,
                new AdapterCreator<Reminder>() {
                    @Override
                    public ArrayAdapter<Reminder> createAdapter(ListFragment<Reminder> fragment) {
                        return new RemindersListViewAdapter(fragment.getActivity(), new ArrayList<Reminder>(), (ListRemindersFragment) fragment);
                    }
                },
                new Comparator<Reminder>() {
                    @Override
                    public int compare(Reminder left, Reminder right) {
                        int comparisonByPriority = right.getPriority().value - left.getPriority().value;
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
                },
                AddReminderActivity.class
        );
    }

}
