package unsw.gloriaromanus.ConfigMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.json.JSONArray;
import org.json.JSONObject;
import unsw.gloriaromanus.Faction.*;
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

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    @FXML
    void initialize() {
        gridPane = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        gridPane.getColumnConstraints().add(column);
        addPlayer();
        addPlayer();
        scrollPane.setContent(gridPane);
    }

    /**
     * Add a new player
     */
    private void addPlayer() {
        //Index for the players
        int size = gridPane.getChildren().size();

        //Hbox container
        HBox box = new HBox();
        GridPane.setRowIndex(box, size);
        if((size+1)%2==0) box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        //Player name
        Label name = new Label("Player "+(size+1));
        name.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);
        HBox.setMargin(name, new Insets(20, 20, 20, 20));
        box.getChildren().add(name);

        //Add the factions comboBox
        ComboBox<Faction> factionsDropdown = new ComboBox<>();
        factionsDropdown.setPromptText("Faction");
        factionsDropdown.getItems().addAll(Rome.getINSTANCE(), Carthage.getINSTANCE(), Celts.getINSTANCE(), Egypt.getINSTANCE(), Gaul.getINSTANCE(), Spain.getINSTANCE());
        HBox.setMargin(factionsDropdown, new Insets(20, 20, 20, 20));
        box.getChildren().add(factionsDropdown);

        //Add image container
        ImageView image = new ImageView();
        factionsDropdown.setOnAction(e-> {
            Image flag = new Image(factionsDropdown.getValue().getFlagPath());
            image.setImage(flag);
        });
        HBox.setMargin(image, new Insets(0, 20, 20, 20));
        box.getChildren().add(image);

        //Add remove button
        if(size>1) {
            Button close = new Button("X");
            close.setOnAction(e-> {
                //Remove target row
                int target = GridPane.getRowIndex(box);
                gridPane.getChildren().remove(target);
                //Reset the row index & name label
                for (int i = target; i<gridPane.getChildren().size(); i++) {
                    HBox child = (HBox) gridPane.getChildren().get(i);
                    GridPane.setRowIndex(child, i);
                    Label nameLabel = (Label) child.getChildren().get(0);
                    nameLabel.setText("Player "+(i+1));
                    if((i+1)%2==0) {
                        child.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                    } else {
                        child.setBackground(null);
                    }
                }
            });
            box.getChildren().add(close);
        }

        //Add the box into the pane
        gridPane.getChildren().add(box);
    }

    @FXML
    void handleBackBtn(ActionEvent e) throws IOException {
        startScreen.start();
    }

    @FXML
    void handleStartBtn(ActionEvent e) throws IOException {
        //Make faction list
        List<String> factions = new ArrayList<>();
        for (Node row : gridPane.getChildren()) {
            HBox box = (HBox)row;
            ComboBox factionBox = (ComboBox) box.getChildren().get(1);
            String faction = factionBox.getValue().toString();

            //Check if two players has the same faction
            if(faction == null) {
                Alert a = new Alert(Alert.AlertType.NONE, "All players must choose a faction!", ButtonType.CLOSE);
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
        System.out.println(div);
        System.out.println(reminder);

        //Create the ownership json base on number of players
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/provinces_label.geojson"));
        JSONObject obj = new JSONObject(content);
        JSONArray provinces = obj.getJSONArray("features");

        for(int i = 0; i<provinces.length(); i++) {
            String province = provinces.getJSONObject(i).getJSONObject("properties").getString("name");
            Random rand = new Random();
            int num = rand.nextInt();
//            switch() {
//
//            }
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
}
