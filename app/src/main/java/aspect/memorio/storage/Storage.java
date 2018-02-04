package aspect.memorio.storage;

import java.util.List;

import aspect.memorio.models.Note;

public interface Storage {

    /**
     * Loads all the data
     * @return true if successful, false otherwise
     */
    boolean loadAll();

    /**
     * Gets all stored notes
     */
    List<Note> getAll();

    /**
     * Adds the specified note to the storage
     * @return true if successful, false otherwise
     */
    void addNote(Note note);

    /**
     * Removes the specified note from the storage
     * @return true if successful, false otherwise
     */
    void removeNote(Note note);

    /**
     * Flushes all data
     * @return true if successful, false otherwise
     */
    void flushAll();

}
