package aspect.memorio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import aspect.memorio.R;
import aspect.memorio.activities.HomeActivity;
import aspect.memorio.fragments.utils.ListFragmentConfig;
import aspect.memorio.storage.ItemsStorage;
import aspect.memorio.utils.SnackbarUtils;

import static android.app.Activity.RESULT_OK;

public abstract class ListFragment<T> extends Fragment {

    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    public static final String INTENT_ITEM_INPUT = "item";

    private static final int AUTOMATIC_UPDATE_INTERVAL_IN_MILIS = 30 * 1000;

    private final ListFragmentConfig<T> fragmentConfig;
    private final ItemsStorage<T> storage;
    private ArrayAdapter<T> itemsViewAdapter;
    private HomeActivity homeActivity;

    public ListFragment(ListFragmentConfig<T> fragmentConfig, ItemsStorage<T> storage) {
        this.fragmentConfig = fragmentConfig;
        this.storage = storage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(this.fragmentConfig.layout, container, false);
        ((Toolbar) getActivity().findViewById(R.id.toolbar)).setTitle(this.fragmentConfig.title);

        this.homeActivity = (HomeActivity) getActivity();
        this.itemsViewAdapter = this.fragmentConfig.itemsViewAdapter;

        FloatingActionButton fab = view.findViewById(this.fragmentConfig.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewItemActivity();
            }
        });

        ListView itemsListView = view.findViewById(this.fragmentConfig.list_view);
        itemsListView.setAdapter(this.fragmentConfig.itemsViewAdapter);

        this.setAutomaticUpdate();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reinitializeItemsView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isCorrectActivityResult(requestCode, resultCode, data.getExtras())) {
            try {
                this.addItemFromActivityResult(requestCode, data.getExtras());
            } catch (ParseException e) {
                Log.w("[ListFragment]", String.format("Could not instantiate item from %s", data.getExtras().getString(INTENT_ITEM_INPUT)));
            }
        }
    }

    public void editItem(T item) {
        this.gotoEditItemActivity(item);
    }

    public void removeItem(final T item) {
        this.storage.remove(item);
        this.reinitializeItemsView();

        if (getView() != null) {
            SnackbarUtils.showUndoSnackbar(getView(), this.fragmentConfig.stringItemRemoved, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storage.add(item);
                    reinitializeItemsView();
                }
            });
        }
    }

    private boolean isCorrectActivityResult(int requestCode, int resultCode, Bundle bundle) {
        return resultCode == RESULT_OK
                && (requestCode == REQUEST_ADD || requestCode == REQUEST_EDIT)
                && (bundle != null && bundle.getString(INTENT_ITEM_INPUT) == null);
    }

    private void addItemFromActivityResult(int requestCode, Bundle bundle) throws ParseException {
        String dataString = bundle.getString(INTENT_ITEM_INPUT);
        T item = this.storage.createNewItemFromString(dataString);
        if (requestCode == REQUEST_ADD) {
            this.addItem(item);
        } else {
            this.updateItem(item);
        }
    }

    private void setAutomaticUpdate() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                homeActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reinitializeItemsView();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, AUTOMATIC_UPDATE_INTERVAL_IN_MILIS, AUTOMATIC_UPDATE_INTERVAL_IN_MILIS);
    }

    private void reinitializeItemsView() {
        if (this.storage == null) {
            return;
        }

        List<T> activeItems = this.storage.getAllActive();
        Collections.sort(this.storage.getAllActive(), this.fragmentConfig.itemsComparator);

        this.itemsViewAdapter.clear();
        this.itemsViewAdapter.addAll(activeItems);
        this.itemsViewAdapter.notifyDataSetChanged();
    }

    private void addItem(T item) {
        this.storage.add(item);
        this.reinitializeItemsView();
    }

    private void updateItem(T item) {
        this.storage.updateOrAdd(item);
        this.reinitializeItemsView();
    }

    private void gotoEditItemActivity(T item) {
        Intent intent = new Intent(homeActivity, this.fragmentConfig.addItemActivity);
        intent.putExtra(INTENT_ITEM_INPUT, item.toString());
        startActivityForResult(intent, REQUEST_EDIT);
    }

    private void goToNewItemActivity() {
        Intent intent = new Intent(homeActivity, this.fragmentConfig.addItemActivity);
        startActivityForResult(intent, REQUEST_ADD);
    }

}
