package aspect.memorio.fragments.config;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Comparator;

import aspect.memorio.R;
import aspect.memorio.activities.AddItemActivity;
import aspect.memorio.activities.AddPurchaseActivity;
import aspect.memorio.activities.AddTodoActivity;
import aspect.memorio.activities.adapters.PurchasesListViewAdapter;
import aspect.memorio.activities.adapters.TodosListViewAdapter;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.fragments.ListPurchasesFragment;
import aspect.memorio.fragments.ListTodosFragment;
import aspect.memorio.models.Purchase;
import aspect.memorio.models.Todo;

public class PurchasesFragmentConfig extends ListFragmentConfig<Purchase> {

    private PurchasesFragmentConfig(int layout, int title, int list_view, int add_button, int stringItemRemoved,
                                    AdapterCreator<Purchase> itemsViewAdapterCreator, Comparator<Purchase> itemsComparator,
                                    Class<? extends AddItemActivity> addItemActivity) {
        super(layout, title, list_view, add_button, stringItemRemoved, itemsViewAdapterCreator, itemsComparator, addItemActivity);
    }

    public static PurchasesFragmentConfig get() {
        return new PurchasesFragmentConfig(
                R.layout.fragment_list_purchases,
                R.string.toolbar_title_list_purchases,
                R.id.list_purchases,
                R.id.button_add_purchase,
                R.string.snackbar_purchase_removed,
                new AdapterCreator<Purchase>() {
                    @Override
                    public ArrayAdapter<Purchase> createAdapter(ListFragment<Purchase> fragment) {
                        return new PurchasesListViewAdapter(fragment.getActivity(), new ArrayList<Purchase>(), (ListPurchasesFragment) fragment);
                    }
                },
                new Comparator<Purchase>() {
                    @Override
                    public int compare(Purchase left, Purchase right) {
                        int comparisonByPriority = right.getPriority().value - left.getPriority().value;
                        if (comparisonByPriority != 0) {
                            return comparisonByPriority;
                        }
                        
                        return left.getCost() - right.getCost();
                    }
                },
                AddPurchaseActivity.class
        );
    }

}
