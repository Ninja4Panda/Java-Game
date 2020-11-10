package unsw.gloriaromanus.ConfigMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.StartUpMenu.StartScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigMenuController {
    @FXML
    private Button backBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button addPlayerBtn;
    @FXML
    private ScrollPane scrollPane;
    private GridPane gridPane;
    private StartScreen startScreen;
    private final int MAX_PLAYERS = 6;
    private final int MAX_PROVINCES = 53;

    public ConfigMenuController() {
        gridPane = new GridPane();
        addPlayer();
        addPlayer();
    }

    /**
     * Add a new player
     */
    private void addPlayer() {
        HBox box = new HBox();

        //Index for the players
        int index = gridPane.getChildren().size()+1;
        Label name = new Label("Player "+index);
        box.setMargin(name, new Insets(20, 20, 20, 20));
        box.getChildren().add(name);

        //Add the factions comboBox
        ComboBox<String> factionsDropdown = new ComboBox<>();
        factionsDropdown.setPromptText("Faction");
        factionsDropdown.getItems().addAll("Carthage","Celts","Egypt","Gauls","Rome","Spain");
        box.setMargin(factionsDropdown, new Insets(20, 20, 20, 20));
        box.getChildren().add(factionsDropdown);

        //Add image container
        ImageView image = new ImageView();
        factionsDropdown.setOnAction(e-> updateFlag(image, factionsDropdown.getValue()));
        box.setMargin(image, new Insets(0, 20, 20, 20));
        box.getChildren().add(image);

        //Add remove button
        if(index>2) {
            Button close = new Button("X");
            close.setOnAction(e->gridPane.getChildren().remove(index));
            box.getChildren().add(close);
        }

        //Add the box into the pane
        gridPane.addRow(index, box);
    }

    /**
     * Update flag according to the user's choice
     * @param image image container box
     * @param faction user's selection
     */
    private void updateFlag(ImageView image, String faction) {
        Image flag = null;
        switch (faction) {
            case "Carthage":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Carthage/CarthageFlag.png")).toURI().toString());
                break;
            case "Celts":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString());
                break;
            case "Egypt":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Egyptian/EgyptianFlag.png")).toURI().toString());
                break;
            case "Gauls":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Gallic/GallicFlag.png")).toURI().toString());
                break;
            case "Rome":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png")).toURI().toString());
                break;
            case "Spain":
                flag = new Image((new File("images/CS2511Sprites_No_Background/Flags/Spanish/SpanishFlag.png")).toURI().toString());
                break;
        }
        image.setImage(flag);
    }

    @FXML
    void initialize() {
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(gridPane);
    }

    @FXML
    void handleBackBtn(ActionEvent e) throws IOException {
        startScreen.start();
    }

    @FXML
    void handleStartBtn(ActionEvent e) throws IOException {
        //Check if two players has the same faction
        List<String> factions = new ArrayList<>();
        for (Node row : gridPane.getChildren()) {
            HBox box = (HBox)row;
            ComboBox factionBox = (ComboBox) box.getChildren().get(1);
            String faction = (String) factionBox.getValue();
            if(faction == null) {
                Alert a = new Alert(Alert.AlertType.NONE, "A player must have a faction!", ButtonType.CLOSE);
                a.show();
                return;
            } else if(factions.contains(faction)) {
                Alert a = new Alert(Alert.AlertType.NONE, "Two players cannot have the same factions!", ButtonType.CLOSE);
                a.show();
                return;
            }
            factions.add(faction);
        }
        generateOwnership(factions);
    }

    /**
     * Generate initial_province_ownership.json
     * @throws IOException reads in provinces_label.geojson
     */
    private void generateOwnership(List<String> factions) throws IOException {
        int div = MAX_PROVINCES/factions.size();
        int reminder = MAX_PROVINCES%factions.size();
        //Create the ownership json base on number of players
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/provinces_label.geojson"));
        JSONObject obj = new JSONObject(content);
        JSONArray provinces = obj.getJSONArray("features");
        for(int i = 0; i<provinces.length(); i++) {
            String province = provinces.getJSONObject(i).getJSONObject("properties").getString("name");
//            Random rand = new Random();
//            rand.nextInt()
//            System.out.println(provinces.length());
        }
    }

    @FXML
    void handleAddBtn(ActionEvent e) {
        if(gridPane.getChildren().size()==MAX_PLAYERS) {
            Alert a = new Alert(Alert.AlertType.NONE, "Too many players!", ButtonType.CLOSE);
            a.show();
        } else {
            addPlayer();
        }
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }
}
