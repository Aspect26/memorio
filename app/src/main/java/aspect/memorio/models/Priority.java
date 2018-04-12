package aspect.memorio.models;

import android.util.Log;

public enum Priority {

    LOW(10),
    MEDIUM(20),
    HIGH(30);

    public final int value;

    Priority(int value) {
        this.value = value;
        Log.w("Priority", "Applying unknown priority value: " + value);
    }

    public static Priority get(int value) {
        for (Priority priority : Priority.values()) {
            if (priority.value == value) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Unknown priority value " + value + ".");
    }

}
