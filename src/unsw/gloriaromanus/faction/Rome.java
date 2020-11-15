package unsw.gloriaromanus.faction;

import java.io.File;

public class Rome implements Faction {
    private String name = "Rome";
    private static final Rome INSTANCE = new Rome();

    public static Rome getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png")).toURI().toString();
    }
}
