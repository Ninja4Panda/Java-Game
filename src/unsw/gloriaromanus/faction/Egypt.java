package unsw.gloriaromanus.faction;

import java.io.File;

public class Egypt implements Faction {
    private String name = "Egypt";
    private static final Egypt INSTANCE = new Egypt();

    public static Egypt getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Egyptian/EgyptianFlag.png")).toURI().toString();
    }
}
