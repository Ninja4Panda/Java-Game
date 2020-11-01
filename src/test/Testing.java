package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import unsw.gloriaromanus.winCond.Check;

public class Testing {
    public static void main(String[] args) {
   

        try {
            String content = Files.readString(Paths.get("saveCheck.json"));
            JSONObject game = new JSONObject(content);
            Check loaded = new Check(game.getString("Goal"), game.getString("Junction"), game.getJSONObject("SubCheck"));

            JSONObject loadedCheck = loaded.getSave();
            String filename = "loadedSave.json";
            File file = new File( filename);
            FileWriter writer = new FileWriter(file);
            writer.write(loadedCheck.toString(2));
            writer.close();

        } catch (IOException e) {
        }


       
    }
}
