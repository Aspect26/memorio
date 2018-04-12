package aspect.memorio.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import aspect.memorio.R;
import aspect.memorio.fragments.ListPurchasesFragment;
import aspect.memorio.models.Purchase;
import aspect.memorio.models.Todo;
import aspect.memorio.utils.Utils;

public class PurchasesListViewAdapter extends ArrayAdapter<Purchase> {

    private final ListPurchasesFragment fragment;

    public PurchasesListViewAdapter(Context context, List<Purchase>  items, ListPurchasesFragment fragment) {
        super(context, R.layout.todo_item, items);
        this.fragment = fragment;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // TODO: refactor + strategy pattern
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.purchase_item, null);
        }

        Purchase purchase = getItem(position);

        if (purchase != null) {
            TextView textView = view.findViewById(R.id.purchase_label);
            textView.setText(purchase.getLabel().isEmpty()? this.fragment.getText(R.string.empty_string) : purchase.getLabel());

            textView = view.findViewById(R.id.purchase_cost);
            if (purchase.getCost() < 0) {
                textView.setText(getContext().getText(R.string.not_specified_generic));
            } else {
                textView.setText(purchase.getCost() + " " + this.fragment.getText(R.string.currency));
            }

            Button removeButton = view.findViewById(R.id.button_delete_purchase);
            removeButton.setAlpha(0.5f);
            setRemoveButtonAction(removeButton, purchase);

            Button buyButton = view.findViewById(R.id.button_buy_purchase);
            buyButton.setAlpha(0.5f);
            setBuyButtonAction(buyButton, purchase);

            setOnClickListener(view, purchase);

            if (purchase.getPriority() == Todo.PRIORITY_HIGH) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.high_priority_item));
            } else if (purchase.getPriority() == Todo.PRIORITY_NORMAL) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.normal_priority_item));
            } else if (purchase.getPriority() == Todo.PRIORITY_LOW) {
                view.setBackgroundColor(getContext().getResources().getColor(R.color.low_priority_item));
            }
        }

        return view;
    }

    private void setRemoveButtonAction(Button button, final Purchase purchase) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                fragment.removeItem(purchase);
            }
        });
    }

    private void setBuyButtonAction(Button button, final Purchase purchase) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setAlpha(1.0f);
                fragment.buyPurchase(purchase);
            }
        });
    }

    private void setOnClickListener(View itemView, final Purchase purchase) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.editItem(purchase);
            }
        });
    }

}
