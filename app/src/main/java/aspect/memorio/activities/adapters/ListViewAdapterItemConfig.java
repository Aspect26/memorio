package aspect.memorio.activities.adapters;


public class ListViewAdapterItemConfig {

    public final int layout;
    public final int text;
    public final int additionalText;
    public final int removeButton;

    public ListViewAdapterItemConfig(int layout, int text, int additionalText, int removeButton) {
        this.layout = layout;
        this.text = text;
        this.additionalText = additionalText;
        this.removeButton = removeButton;
    }

}
