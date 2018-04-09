package aspect.memorio.storage;

import java.text.ParseException;
import java.util.List;

public interface ItemsStorage<T> {

    /**
     * Instantiates empty item.
     */
    T createNewItemFromString(String str) throws ParseException;

    /**
     * Gets all active items that shall be displayed in the list fragment.
     */
    List<T> getAllActive();

    /**
     * Adds the specified item to the storage.
     */
    void add(T item);

    /**
     * Updates the specified item. If the item is not found, then adds new.
     */
    void updateOrAdd(T item);

    /**
     * Removes the specified item from the storage.
     */
    void remove(T item);

}
