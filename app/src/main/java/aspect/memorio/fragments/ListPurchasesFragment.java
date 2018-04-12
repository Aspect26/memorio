package aspect.memorio.fragments;

import android.view.View;

import aspect.memorio.R;
import aspect.memorio.fragments.config.PurchasesFragmentConfig;
import aspect.memorio.models.Purchase;
import aspect.memorio.storage.PurchasesStorage;
import aspect.memorio.utils.SnackbarUtils;

public class ListPurchasesFragment extends ListFragment<Purchase> {

    public ListPurchasesFragment() {
        super(PurchasesFragmentConfig.get());
    }

    protected PurchasesStorage getStorage() {
        return this.homeActivity.getPurchasesStorage();
    }

    public void buyPurchase(final Purchase purchase) {
        purchase.setBought(true);
        this.reinitializeItemsView();
        this.getStorage().flushAll();

        if (getView() != null) {
            SnackbarUtils.showUndoSnackbar(getView(), R.string.snackbar_purchase_bought, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    purchase.setBought(false);
                    storage.updateOrAdd(purchase);
                    reinitializeItemsView();
                }
            });
        }
    }

}
