package aspect.memorio.fragments.config;

import android.widget.ArrayAdapter;

import java.util.Comparator;

import aspect.memorio.activities.AddItemActivity;
import aspect.memorio.fragments.ListFragment;

public class ListFragmentConfig<T> {

    public final int layout;
    public final int title;

    public final int list_view;
    public final int add_button;

    public final int stringItemRemoved;

    public final AdapterCreator<T> itemsViewAdapterCreator;
    public final Comparator<T> itemsComparator;
    public final Class<? extends AddItemActivity> addItemActivity;

    ListFragmentConfig(int layout, int title, int list_view, int add_button, int stringItemRemoved,
                       AdapterCreator<T> itemsViewAdapterCreator, Comparator<T> itemsComparator,
                       Class<? extends AddItemActivity> addItemActivity) {
        this.layout = layout;
        this.title = title;
        this.list_view = list_view;
        this.add_button = add_button;
        this.stringItemRemoved = stringItemRemoved;
        this.itemsViewAdapterCreator = itemsViewAdapterCreator;
        this.itemsComparator = itemsComparator;
        this.addItemActivity = addItemActivity;
    }

    public interface AdapterCreator<S> {

        ArrayAdapter<S> createAdapter(ListFragment<S> fragment);

    }

}
