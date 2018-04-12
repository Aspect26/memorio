package aspect.memorio.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

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
        this.reinitializeView();
        this.getStorage().flushAll();

        if (getView() != null) {
            SnackbarUtils.showUndoSnackbar(getView(), R.string.snackbar_purchase_bought, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    purchase.setBought(false);
                    storage.updateOrAdd(purchase);
                    reinitializeView();
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.updateStatusBar();
    }

    @Override
    protected void reinitializeView() {
        super.reinitializeView();
        this.updateStatusBar();
    }

    private void updateStatusBar() {
        TextView statusBas = this.getActivity().findViewById(R.id.status_bar_list_purchases);
        if (statusBas == null) {
            return;
        }

        int accentColor = getActivity().getResources().getColor(R.color.colorAccent);

        // TODO: currency name from strings
        String text = String.format("Total cost: <font color='#%s'> %d CZK</font>.", Integer.toHexString(accentColor).substring(2), ((PurchasesStorage) this.storage).getPriceOfAllActiveItems());
        statusBas.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
    }
}
