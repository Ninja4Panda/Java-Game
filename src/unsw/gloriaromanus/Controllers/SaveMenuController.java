package unsw.gloriaromanus.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import unsw.gloriaromanus.Faction.Faction;
import unsw.gloriaromanus.Game.Game;

import javax.swing.text.LabelView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveMenuController {
    @FXML
    private HBox bottomBox;
    @FXML
    private Label gameLabel;
    @FXML
    private Button backBtn;
    @FXML
    private ScrollPane scrollPane;
    private GridPane gridPane;
    private Game game;
    private Stage stage;

    public SaveMenuController(Stage stage, Game game) {
        this.game = game;
        this.stage = stage;
    }

    @FXML
    void initialize() {
        gridPane = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100);
        gridPane.getColumnConstraints().add(column);
        scrollPane.setContent(gridPane);
        gameLabel.setText("Save Game");

        //New save button
        Button newSave = new Button("New Save");
        newSave.setPrefSize(143,45);
        newSave.setOnAction(e->handleNewSave());
        bottomBox.getChildren().add(newSave);

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
                //Corrupted file
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleNewSave() {
        Stage popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);

        VBox row = new VBox();
        row.setSpacing(15);
        row.setAlignment(Pos.CENTER);

        //Title
        Label title = new Label();
        title.setText("New Save");
        title.setStyle("-fx-font-size: 24;");
        row.getChildren().add(title);

        //TextField
        TextField name = new TextField();
        name.setPromptText("Enter save name");
        row.getChildren().add(name);
        name.getParent().requestFocus();

        //Msg to display
        Text msg = new Text();
        msg.setFill(Color.RED);
        msg.setStyle("-fx-font-size: 13;");
        row.getChildren().add(msg);

        HBox col = new HBox();
        row.getChildren().add(col);
        col.setAlignment(Pos.CENTER);


        //Save Btn
        Button save = new Button("Save");
        save.setPrefSize(143,45);
        save.setOnAction(event-> {
            String stripedName = name.getText().replace(".","-");
            String filename = stripedName.concat(".json");
            File dir = new File(".","saves");
            dir.mkdir();
            if(filename.length()>20) {
                msg.setText("Save name is too long!");
                return;
            }

            File file = new File(dir, filename);
            if(file.exists()) {
                msg.setText("Save already exists");
                return;
            }

            popupStage.hide();
            try {
                game.save(stripedName);
                String content = Files.readString(file.toPath());
                addSave(file, content, filename);
            } catch (IOException e) {
                e.printStackTrace();
                msg.setText("Cannot save file");
            }
        });
        HBox.setMargin(save, new Insets(5,20,5,20));
        col.getChildren().add(save);

        //Cancel Btn
        Button cancel = new Button("Cancel");
        cancel.setPrefSize(143,45);
        cancel.setOnAction(e-> {
            popupStage.hide();
        });
        HBox.setMargin(cancel, new Insets(5,20,5,20));
        col.getChildren().add(cancel);

        //Set the popup size
        popupStage.setScene(new Scene(row));
        popupStage.show();
    }

    /**
     * Add a save
     * @param save the file object
     * @param content content of the file
     * @param name save filename
     */
    private void addSave(File save, String content, String name) throws IOException {
        //Index for the players
        int size = gridPane.getChildren().size();

        //Hbox container
        HBox box = new HBox();
        GridPane.setRowIndex(box, size);
        box.setOnMouseClicked(e->handleBoxClick(save.getAbsolutePath()));
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
        Label timeLabel = new Label("Save time: "+time);
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

    private void handleBoxClick(String absolutePath) {

    }

    @FXML
    void handleBackBtn(ActionEvent e) throws IOException {
        stage.hide();
    }
}
