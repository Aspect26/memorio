package aspect.memorio.fragments;

import aspect.memorio.fragments.config.PurchasesFragmentConfig;
import aspect.memorio.models.Purchase;
import aspect.memorio.storage.PurchasesStorage;

public class ListPurchasesFragment extends ListFragment<Purchase> {

    public ListPurchasesFragment() {
        super(PurchasesFragmentConfig.get());
    }

    protected PurchasesStorage getStorage() {
        return this.homeActivity.getPurchasesStorage();
    }

    public void buyPurchase(Purchase purchase) {
        purchase.setBought(true);
        this.reinitializeItemsView();
        this.getStorage().flushAll();
    }

}
