package aspect.memorio.models;

import java.util.UUID;

public class Purchase {

    // TODO: move priorities to custom class
    public static final int PRIORITY_LOW = 10;
    public static final int PRIORITY_NORMAL = 20;
    public static final int PRIORITY_HIGH = 30;

    private String id;
    private String label;
    private boolean bought;
    private int cost;
    private int priority;

    public Purchase(String id, String label, boolean bought, int cost, int priority) {
        this.id = id;
        this.label = label;
        this.bought = bought;
        this.cost = cost;
        this.priority = priority;
    }

    public Purchase(){
        this(UUID.randomUUID().toString(), "", false, 0, PRIORITY_NORMAL);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
