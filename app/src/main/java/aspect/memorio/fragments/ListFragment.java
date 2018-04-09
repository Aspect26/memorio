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
import aspect.memorio.fragments.config.ListFragmentConfig;
import aspect.memorio.storage.ItemsStorage;
import aspect.memorio.utils.SnackbarUtils;

import static android.app.Activity.RESULT_OK;

public abstract class ListFragment<T> extends Fragment {

    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    public static final String INTENT_ITEM = "item";

    private static final int AUTOMATIC_UPDATE_INTERVAL_IN_MILLIS = 30 * 1000;

    protected ItemsStorage<T> storage;
    protected HomeActivity homeActivity;
    private final ListFragmentConfig<T> fragmentConfig;
    private ArrayAdapter<T> itemsViewAdapter;

    public ListFragment(ListFragmentConfig<T> fragmentConfig) {
        this.fragmentConfig = fragmentConfig;
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
        this.storage = this.getStorage();
        this.itemsViewAdapter = this.fragmentConfig.itemsViewAdapterCreator.createAdapter(this);

        FloatingActionButton fab = view.findViewById(this.fragmentConfig.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNewItemActivity();
            }
        });

        ListView itemsListView = view.findViewById(this.fragmentConfig.list_view);
        itemsListView.setAdapter(this.itemsViewAdapter);

        this.setAutomaticUpdate();
        this.reinitializeItemsView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.reinitializeItemsView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isCorrectActivityResult(requestCode, resultCode, data)) {
            try {
                this.addItemFromActivityResult(requestCode, data.getExtras());
            } catch (ParseException e) {
                Log.w("[ListFragment]", String.format("Could not instantiate item from %s", data.getExtras().getString(INTENT_ITEM)));
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

    protected abstract ItemsStorage<T> getStorage();

    protected void reinitializeItemsView() {
        if (this.storage == null) {
            return;
        }

        List<T> activeItems = this.storage.getAllActive();
        Collections.sort(activeItems, this.fragmentConfig.itemsComparator);

        this.itemsViewAdapter.clear();
        this.itemsViewAdapter.addAll(activeItems);
        this.itemsViewAdapter.notifyDataSetChanged();
    }

    private boolean isCorrectActivityResult(int requestCode, int resultCode, Intent intent) {
        return resultCode == RESULT_OK
                && (requestCode == REQUEST_ADD || requestCode == REQUEST_EDIT)
                && (intent != null && intent.getExtras() != null && intent.getExtras().getString(INTENT_ITEM) != null);
    }

    private void addItemFromActivityResult(int requestCode, Bundle bundle) throws ParseException {
        String dataString = bundle.getString(INTENT_ITEM);
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
        timer.scheduleAtFixedRate(task, AUTOMATIC_UPDATE_INTERVAL_IN_MILLIS, AUTOMATIC_UPDATE_INTERVAL_IN_MILLIS);
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
        intent.putExtra(INTENT_ITEM, item.toString());
        startActivityForResult(intent, REQUEST_EDIT);
    }

    private void goToNewItemActivity() {
        Intent intent = new Intent(homeActivity, this.fragmentConfig.addItemActivity);
        startActivityForResult(intent, REQUEST_ADD);
    }

}
