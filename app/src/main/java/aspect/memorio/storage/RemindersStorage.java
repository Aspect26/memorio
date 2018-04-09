package aspect.memorio.storage;

import java.util.List;

import aspect.memorio.models.Reminder;

public interface RemindersStorage extends ItemsStorage<Reminder> {

    /**
     * Loads all the data
     * @return true if successful, false otherwise
     */
    boolean loadAll();

    /**
     * Gets all stored reminders
     */
    List<Reminder> getAll();

    /**
     * Gets all reminders for today
     */
    List<Reminder> getAllToday();

    /**
     * Removes all reminders of the user
     */
    void removeAllReminders();

    /**
     * Flushes all data
     * @return true if successful, false otherwise
     */
    void flushAll();

}
