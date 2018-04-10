package aspect.memorio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import aspect.memorio.R;
import aspect.memorio.fragments.ListFragment;
import aspect.memorio.models.Purchase;
import aspect.memorio.utils.Serialization;

public class AddPurchaseActivity extends AddItemActivity {

    private Purchase purchase;

    public AddPurchaseActivity() {
        purchase = new Purchase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_purchase);
        Toolbar toolbar = findViewById(R.id.add_purchase_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null && getIntent().getStringExtra(ListFragment.INTENT_ITEM) != null) {
            String inputPurchaseString = getIntent().getStringExtra(ListFragment.INTENT_ITEM);
            this.setPurchase(inputPurchaseString);
        } else if (savedInstanceState != null && savedInstanceState.getString(ListFragment.INTENT_ITEM) != null) {
            String inputTodoString = savedInstanceState.getString(ListFragment.INTENT_ITEM);
            this.setPurchase(inputTodoString);
        }

        Button addButton = findViewById(R.id.button_purchase_done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePurchaseAndExit();
            }
        });

        EditText labelEditor = findViewById(R.id.edit_text_new_purchase_label);
        labelEditor.setText(this.purchase != null? this.purchase.getLabel() : "");

        EditText costEditor = findViewById(R.id.edit_text_new_purchase_cost);
        costEditor.setText(this.purchase != null? String.valueOf(this.purchase.getCost()) : "");

        RadioButton highPriorityButton = findViewById(R.id.radio_purchase_priority_high);
        highPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    purchase.setPriority(Purchase.PRIORITY_HIGH);
                }
            }
        });

        RadioButton normalPriorityButton = findViewById(R.id.radio_purchase_priority_normal);
        normalPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    purchase.setPriority(Purchase.PRIORITY_NORMAL);
                }
            }
        });

        RadioButton lowPriorityButton = findViewById(R.id.radio_purchase_priority_low);
        lowPriorityButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    purchase.setPriority(Purchase.PRIORITY_LOW);
                }
            }
        });

        this.setDefaultPriorityValue();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ListFragment.INTENT_ITEM, Serialization.serializePurchase(this.purchase));
    }

    private void setDefaultPriorityValue() {
        if (this.purchase != null) {
            switch (this.purchase.getPriority()) {
                case Purchase.PRIORITY_HIGH:
                    ((RadioButton) findViewById(R.id.radio_purchase_priority_high)).setChecked(true); break;
                case Purchase.PRIORITY_NORMAL:
                    ((RadioButton) findViewById(R.id.radio_purchase_priority_normal)).setChecked(true); break;
                case Purchase.PRIORITY_LOW:
                    ((RadioButton) findViewById(R.id.radio_purchase_priority_low)).setChecked(true); break;
            }
        }
    }

    private void setPurchase(String purchaseString) {
        if (purchaseString != null) {
            this.purchase = Serialization.deserializePurchase(purchaseString);
        }
    }

    private void savePurchaseAndExit() {
        final String label = ((EditText) findViewById(R.id.edit_text_new_purchase_label)).getText().toString();
        final int cost = Integer.parseInt(((EditText) findViewById(R.id.edit_text_new_purchase_cost)).getText().toString());

        this.purchase.setLabel(label);
        this.purchase.setCost(cost);

        Intent intent = new Intent();
        intent.putExtra(ListFragment.INTENT_ITEM, Serialization.serializePurchase(this.purchase));
        setResult(RESULT_OK, intent);
        finish();
    }

}
