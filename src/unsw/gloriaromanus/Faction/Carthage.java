package unsw.gloriaromanus.Faction;

import java.io.File;

public class Carthage implements Faction {
    private String name = "Carthage";
    private static final Carthage INSTANCE = new Carthage();

    public static Carthage getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Carthage";
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Carthage/CarthageFlag.png")).toURI().toString();
    }
}
