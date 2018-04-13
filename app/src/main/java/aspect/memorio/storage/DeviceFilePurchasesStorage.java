package aspect.memorio.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import aspect.memorio.models.Purchase;
import aspect.memorio.utils.Serialization;

public class DeviceFilePurchasesStorage implements PurchasesStorage {

    private static final String FILE_NAME = "data_purchases.dat";
    private final List<Purchase> data;
    private final Context context;

    public DeviceFilePurchasesStorage(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public boolean loadAll() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        FileInputStream inputStream;
        BufferedReader reader;

        try {
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            this.data.clear();

            while (line != null) {
                Purchase purchase = Serialization.deserializePurchase(line);
                this.data.add(purchase);
                line = reader.readLine();
            }

            inputStream.close();
            return true;
        } catch (Exception e) {
            Log.d("[DeviceFilePurchasesStorage]", e.getMessage());
            return false;
        }
    }

    @Override
    public void flushAll() {
        FileOutputStream outputStream;

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            outputStream = new FileOutputStream(file);

            for (Purchase purchase : this.data) {
                outputStream.write(Serialization.serializePurchase(purchase).getBytes());
                outputStream.write("\n".getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            Log.d("[DeviceFilePurchasesStorage]", e.getMessage());
        }
    }

    @Override
    public Purchase createNewItemFromString(String str) throws ParseException {
        return Serialization.deserializePurchase(str);
    }

    @Override
    public List<Purchase> getAllActive() {
        List<Purchase> activePurchases = new ArrayList<>();
        for (Purchase purchase : this.data) {
            if (!purchase.isBought()) {
                activePurchases.add(purchase);
            }
        }

        return activePurchases;
    }

    @Override
    public void add(Purchase item) {
        this.data.add(item);
        this.flushAll();
    }

    @Override
    public void updateOrAdd(Purchase updatedPurchase) {
        Purchase oldPurchase = this.findOne(updatedPurchase.getId());
        if (oldPurchase == null) {
            this.add(updatedPurchase);
        } else {
            oldPurchase.setLabel(updatedPurchase.getLabel());
            oldPurchase.setBought(updatedPurchase.isBought());
            oldPurchase.setCost(updatedPurchase.getCost());
        }
        this.flushAll();
    }

    @Override
    public void remove(Purchase purchase) {
        for (int index = 0; index < this.data.size(); ++index) {
            if (this.data.get(index).getId().equals(purchase.getId())) {
                this.data.remove(index);
                break;
            }
        }
        this.flushAll();
    }

    @Override
    public int getPriceOfAllActiveItems() {
        List<Purchase> activeItems = this.getAllActive();

        int totalPrice = 0;
        for (Purchase purchase : activeItems) {
            totalPrice += purchase.getCost();
        }

        return totalPrice;
    }

    private Purchase findOne(String id) {
        for (Purchase purchase : this.data) {
            if (purchase.getId().equals(id)) {
                return purchase;
            }
        }

        return null;
    }

}
