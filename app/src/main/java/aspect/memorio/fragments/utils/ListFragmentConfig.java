package aspect.memorio.fragments.utils;

import android.widget.ArrayAdapter;

import java.util.Comparator;

import aspect.memorio.activities.AddItemActivity;

public class ListFragmentConfig<T> {

    public final int layout;
    public final int title;

    public final int list_view;
    public final int add_button;

    public final int stringItemRemoved;

    public final ArrayAdapter<T> itemsViewAdapter;
    public final Comparator<T> itemsComparator;
    public final Class<? extends AddItemActivity> addItemActivity;

    ListFragmentConfig(int layout, int title, int list_view, int add_button, int stringItemRemoved,
                       ArrayAdapter<T> itemsViewAdapter, Comparator<T> itemsComparator,
                       Class<? extends AddItemActivity> addItemActivity) {
        this.layout = layout;
        this.title = title;
        this.list_view = list_view;
        this.add_button = add_button;
        this.stringItemRemoved = stringItemRemoved;
        this.itemsViewAdapter = itemsViewAdapter;
        this.itemsComparator = itemsComparator;
        this.addItemActivity = addItemActivity;
    }

}
