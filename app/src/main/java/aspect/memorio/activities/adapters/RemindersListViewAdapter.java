package aspect.memorio.activities.adapters;

import android.content.Context;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListRemindersFragment;
import aspect.memorio.models.Reminder;
import aspect.memorio.utils.Utils;

public class RemindersListViewAdapter extends ListViewAdapterTemplate<Reminder> {

    public RemindersListViewAdapter(Context context, List<Reminder> items, ListRemindersFragment remindersFragment) {
        super(context, items, remindersFragment, ListViewAdapterItemConfig.reminder());
    }

    @Override
    protected String getItemText(Reminder item) {
        return item.getText();
    }

    @Override
    protected String getItemAdditionalText(Reminder item) {
        if (item.getDate() == null) {
            return getContext().getText(R.string.not_specified_generic).toString();
        } else {
            return Utils.getTimeRemainingFromNowText(item.getDate());
        }
    }

    @Override
    protected int getItemBackgroundColorResource(Reminder item) {
        return this.getPriorityColor(item.getPriority());
    }

    @Override
    protected float getTextOpacity(Reminder item) {
        return this.getDateOpacity(item.getDate());
    }
}
