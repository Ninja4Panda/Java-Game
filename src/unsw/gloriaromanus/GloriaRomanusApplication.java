package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import unsw.gloriaromanus.scenes.*;

public class GloriaRomanusApplication extends Application {
  private GloriaRomanusController controller;

  @Override
  public void start(Stage stage) throws IOException {
    //Set up all screens
    StartScreen startScreen = new StartScreen(stage);
    ConfigScreen configScreen = new ConfigScreen(stage);
    LoadSaveScreen loadSaveScreen = new LoadSaveScreen(stage);
    GameScreen gameScreen = new GameScreen(stage);
    controller = gameScreen.getController();

    //Set up the switching between screens
    startScreen.getController().setConfigScreen(configScreen);
    startScreen.getController().setLoadSaveScreen(loadSaveScreen);
    configScreen.getController().setStartScreen(startScreen);
    configScreen.getController().setGameScreen(gameScreen);
    loadSaveScreen.getController().setStartScreen(startScreen);
    loadSaveScreen.getController().setGameScreen(gameScreen);

    //Start the app
    startScreen.start();
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