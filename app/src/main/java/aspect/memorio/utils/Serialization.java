package aspect.memorio.utils;


import aspect.memorio.models.Purchase;

public class Serialization {

    public static String serializePurchase(Purchase purchase) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(purchase.getId()).append(";");
        stringBuilder.append(purchase.getLabel()).append(";");
        stringBuilder.append(purchase.getCost()).append(";");
        stringBuilder.append(serializeBool(purchase.isBought())).append(";");
        stringBuilder.append(purchase.getPriority());

        return stringBuilder.toString();
    }

    public static Purchase deserializePurchase(String dataString) {
        String[] data = dataString.split(";", -1);
        String id = data[0];
        String label = data[1];
        int cost = Integer.parseInt(data[2]);
        boolean bought = deserializeBool(data[3]);
        int priority = Integer.parseInt(data[4]);

        return new Purchase(id, label, bought, cost, priority);
    }

    private static String serializeBool(boolean value) {
        return value? "1" : "0";
    }

    private static boolean deserializeBool(String data) {
        return data.equals("1");
    }
}
