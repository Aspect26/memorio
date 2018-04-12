package aspect.memorio.fragments;

import aspect.memorio.fragments.config.RemindersFragmentConfig;
import aspect.memorio.models.Reminder;
import aspect.memorio.storage.RemindersStorage;
import aspect.memorio.utils.Serialization;

public class ListRemindersFragment extends ListFragment<Reminder> {

    public ListRemindersFragment() {
        super(RemindersFragmentConfig.get());
    }

    protected RemindersStorage getStorage() {
        return this.homeActivity.getRemindersStorage();
    }

    @Override
    protected String serializeItem(Reminder item) {
        return Serialization.serializeReminder(item);
    }

}
