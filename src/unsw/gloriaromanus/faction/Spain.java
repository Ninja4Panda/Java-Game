package unsw.gloriaromanus.faction;

import java.io.File;

public class Spain implements Faction {
    private String name = "Spain";
    private static final Spain INSTANCE = new Spain();

    public static Spain getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getFlagPath() {
        return (new File("images/CS2511Sprites_No_Background/Flags/Spanish/SpanishFlag.png")).toURI().toString();
    }
}
