package aspect.memorio.models;

import java.util.UUID;

public class Purchase {

    private String id;
    private String label;
    private boolean bought;
    private int cost;
    private Priority priority;

    public Purchase(String id, String label, boolean bought, int cost, Priority priority) {
        this.id = id;
        this.label = label;
        this.bought = bought;
        this.cost = cost;
        this.priority = priority;
    }

    public Purchase(){
        this(UUID.randomUUID().toString(), "", false, 0, Priority.MEDIUM);
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
