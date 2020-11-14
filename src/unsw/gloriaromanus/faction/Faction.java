package unsw.gloriaromanus.faction;

public interface Faction {
    static Faction find(String factionName) {
        Faction target = null;
        switch (factionName) {
            case "Carthage":
                target = Carthage.getINSTANCE();
                break;
            case "Celts":
                target = Celts.getINSTANCE();
                break;
            case "Egypt":
                target = Egypt.getINSTANCE();
                break;
            case "Gaul":
                target = Gaul.getINSTANCE();
                break;
            case "Rome":
                target = Rome.getINSTANCE();
                break;
            case "Spain":
                target = Spain.getINSTANCE();
                break;
        }
        return target;
    }

    String getFlagPath();
}
