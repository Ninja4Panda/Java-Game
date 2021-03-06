package unsw.gloriaromanus.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.faction.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.scenes.GameScreen;
import unsw.gloriaromanus.scenes.StartScreen;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;


public class LoadSaveMenuController {
    @FXML
    private Button backBtn;
    @FXML
    private ScrollPane scrollPane;
    private StartScreen startScreen;
    private GameScreen gameScreen;
    private GridPane gridPane;

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @FXML
    void initialize() {
        gridPane = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        gridPane.getColumnConstraints().add(column);
        scrollPane.setContent(gridPane);

        //Load saves from save files
        File dir = new File(".","saves/");
        dir.mkdir();
        File[] saves = dir.listFiles();
        for (File save: saves) {
            String filename = save.getName();
            String splitName = filename.substring(0, filename.length()-5);
            try {
                String name = splitName;
                String content = Files.readString(save.toPath());
                addSave(save, content, name);
            } catch (JSONException e) {
                //Skipp corrupted file
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add a save
     * @param save the file object
     * @param content content of the file
     * @param name save filename
     */
    private void addSave(File save, String content, String name) {
        //Index for the players
        int size = gridPane.getChildren().size();

        //Hbox container
        HBox box = new HBox();
        GridPane.setRowIndex(box, size);
        box.setOnMouseClicked(e-> handleSaveClick(save.getAbsolutePath()));
        box.setStyle("-fx-cursor: hand;");
        if((size+1)%2==0) box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        //Add name of the save
        Label nameLabel = new Label(name);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        HBox.setMargin(nameLabel, new Insets(20, 20, 20, 20));
        box.getChildren().add(nameLabel);

        //Get the last played time
        JSONObject config = new JSONObject(content);
        JSONObject game = config.getJSONObject("Game");
        String time = game.getString("SaveTime");
        //Add last played
        Label timeLabel = new Label("Save Time: "+time);
        HBox.setMargin(timeLabel, new Insets(20, 20, 20, 20));
        box.getChildren().add(timeLabel);

        //Set up the players & turn
        JSONArray players = config.getJSONArray("Players");
        for(int i = 0; i<players.length(); i++) {
            //Get the faction name
            JSONObject playerJson = players.getJSONObject(i);
            String factionName = playerJson.getString("Faction");
            Faction faction = Faction.find(factionName);
            String pathname = faction.getFlagPath();

            //Add the flag image
            ImageView image = new ImageView();
            Image flag = new Image(pathname);
            image.setImage(flag);
            box.getChildren().add(image);
        }

        //Delete save button
        Button close = new Button("Delete Save");
        close.setOnAction(e-> {
            save.delete();
            //Remove target row
            int target = GridPane.getRowIndex(box);
            gridPane.getChildren().remove(target);
            //Reset the row index
            for (int i = target; i<gridPane.getChildren().size(); i++) {
                HBox child = (HBox) gridPane.getChildren().get(i);
                GridPane.setRowIndex(child, i);
                if((i+1)%2==0) {
                    child.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                } else {
                    child.setBackground(null);
                }
            }
        });
        HBox.setMargin(close, new Insets(20, 20, 20, 20));
        box.getChildren().add(close);

        gridPane.getChildren().add(box);
    }

    /**
     * Handles when a save is clicked
     * @param configFile path to config file
     */
    private void handleSaveClick(String configFile) {
        try {
            Game game = new Game(configFile);
            gameScreen.start(game);
        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Cannot load save!", ButtonType.CLOSE);
            a.show();
        } catch (JSONException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR, "Corrupted save file!", ButtonType.CLOSE);
            a.show();
        }
    }

    @FXML
    void handleBackBtn() {
        try {
            startScreen.start();
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Fxml loading Error! Please restart program");
            a.show();
        }
    }

}
