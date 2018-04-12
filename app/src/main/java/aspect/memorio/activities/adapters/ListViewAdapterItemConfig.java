package aspect.memorio.activities.adapters;


import aspect.memorio.R;

public class ListViewAdapterItemConfig {

    public final int layout;
    public final int text;
    public final int additionalText;
    public final int removeButton;

    private ListViewAdapterItemConfig(int layout, int text, int additionalText, int removeButton) {
        this.layout = layout;
        this.text = text;
        this.additionalText = additionalText;
        this.removeButton = removeButton;
    }

    public static ListViewAdapterItemConfig reminder() {
        return new ListViewAdapterItemConfig(
                R.layout.reminder_item,
                R.id.reminder_text,
                R.id.reminder_remaining_time,
                R.id.button_delete_reminder);
    }

}
