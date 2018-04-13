package aspect.memorio.activities.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.fragments.ListPurchasesFragment;
import aspect.memorio.models.Purchase;

public class PurchasesListViewAdapter extends ListViewAdapterTemplate<Purchase> {

    public PurchasesListViewAdapter(Context context, List<Purchase> items, ListFragment<Purchase> fragment) {
        super(context, items, fragment, ListViewAdapterItemConfig.purchase());
    }

    @Override
    protected String getItemText(Purchase item) {
        return item.getLabel();
    }

    @Override
    protected String getItemAdditionalText(Purchase item) {
        return String.valueOf(item.getCost());
    }

    @Override
    protected int getItemBackgroundColorResource(Purchase item) {
        return this.getPriorityColor(item.getPriority());
    }

    @Override
    protected float getTextOpacity(Purchase item) {
        return 1.0f;
    }

    @Override
    public void setAdditional(View view, Purchase item) {
        Button buyButton = view.findViewById(R.id.button_buy_purchase);
        buyButton.setAlpha(0.5f);
        setBuyButtonAction(buyButton, item);
    }

    private void setBuyButtonAction(Button button, final Purchase purchase) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                ((ListPurchasesFragment) fragment).buyPurchase(purchase);
            }
        });
    }

}
