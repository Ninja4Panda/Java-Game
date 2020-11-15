package unsw.gloriaromanus.faction;

import java.io.File;

public class Gaul implements Faction {
    private String name = "Gaul";
    private static final Gaul INSTANCE = new Gaul();

    public static Gaul getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Gallic/GallicFlag.png")).toURI().toString();
    }
}
