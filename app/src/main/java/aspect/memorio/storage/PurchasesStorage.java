package aspect.memorio.storage;

import aspect.memorio.models.Purchase;

public interface PurchasesStorage extends ItemsStorage<Purchase> {

    boolean loadAll();

    void flushAll();

}
