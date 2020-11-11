package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unsw.gloriaromanus.ConfigMenu.ConfigScreen;
import unsw.gloriaromanus.LoadSaveMenu.LoadSaveScreen;
import unsw.gloriaromanus.StartUpMenu.StartScreen;

public class GloriaRomanusApplication extends Application {

  private static GloriaRomanusController controller;

  @Override
  public void start(Stage stage) throws IOException {
    //Set up all screens
    StartScreen startScreen = new StartScreen(stage);
    ConfigScreen configScreen = new ConfigScreen(stage);
    LoadSaveScreen loadSaveScreen = new LoadSaveScreen(stage);

    //Set up the switching between screens
    startScreen.getController().setConfigScreen(configScreen);
    startScreen.getController().setLoadSaveScreen(loadSaveScreen);
    configScreen.getController().setStartScreen(startScreen);
    loadSaveScreen.getController().setStartScreen(startScreen);

    //Start the app
    startScreen.start();

//    // set up the scene
//    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("main.fxml"));
//    Parent root = mainLoader.load();
//    controller = mainLoader.getController();
//    Scene scene = new Scene(root);
//
//    // set up the stage
//    stage.setTitle("Gloria Romanus");
//    stage.setWidth(800);
//    stage.setHeight(700);
//    stage.setScene(scene);
//    stage.show();

  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    controller.terminate();
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {
    Application.launch(args);
  }
}