package unsw.gloriaromanus.Faction;

import java.io.File;

public class Celts implements Faction {
    private String name = "Celts";
    private static final Celts INSTANCE = new Celts();

    public static Celts getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Celts";
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString();
    }
}
