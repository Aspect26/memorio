package aspect.memorio.storage;

import java.util.List;

import aspect.memorio.models.Reminder;

public interface Storage {

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
     * Gets all reminders that are not expired
     */
    List<Reminder> getAllNonExpired();

    /**
     * Adds the specified note to the storage
     * @return true if successful, false otherwise
     */
    void addReminder(Reminder reminder);

    /**
     * Removes the specified note from the storage
     * @return true if successful, false otherwise
     */
    void removeReminder(Reminder reminder);

    /**
     * Flushes all data
     * @return true if successful, false otherwise
     */
    void flushAll();

}
