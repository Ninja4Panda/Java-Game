package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;
import unsw.gloriaromanus.Controllers.PhaseMenuController;
import unsw.gloriaromanus.Controllers.PlayerMenuController;
import unsw.gloriaromanus.Controllers.RegionMenuController;
import unsw.gloriaromanus.Faction.Faction;
import unsw.gloriaromanus.Game.Game;
import unsw.gloriaromanus.Game.Player;
import unsw.gloriaromanus.Phase.GamePhase;
import unsw.gloriaromanus.Phase.MovePhase;
import unsw.gloriaromanus.Phase.PreparationPhase;
import unsw.gloriaromanus.units.Unit;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  // could use ControllerFactory?
  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  // private String humanFaction;

  private Feature currentlySelectedLeftProvince;
  private Feature currentlySelectedRightProvince;

  private FeatureLayer featureLayer_provinces;

  private Game game;

  public Feature getCurrentlySelectedLeftProvince() {
    return currentlySelectedLeftProvince;
  }

  public Feature getCurrentlySelectedRightProvince() {
    return currentlySelectedRightProvince;
  }

  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    // TODO = you should rely on an object oriented design to determine ownership
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    Random r = new Random();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    // humanFaction = "Rome";

    currentlySelectedLeftProvince = null;
    currentlySelectedRightProvince = null;

    String []menus = {"scenes/phaseMenu.fxml", "scenes/regionMenu.fxml", "scenes/playerMenu.fxml"};
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName: menus){
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }
  

    
    stackPaneMain.getChildren().add(controllerParentPairs.get(1).getValue());
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    stackPaneMain.getChildren().add(controllerParentPairs.get(2).getValue());

    if(controllerParentPairs.get(2).getKey() instanceof PlayerMenuController) {
      ((PlayerMenuController)controllerParentPairs.get(2).getKey()).updatePlayer(game.getCurPlayer());
    }

    initializeProvinceLayers();
  }

  public void endPhase() {
    resetSelections();

    //Display message
    String msg = game.endPhase();
    showSummary(msg);

    if(controllerParentPairs.get(0).getKey() instanceof PhaseMenuController) {
      ((PhaseMenuController) controllerParentPairs.get(0).getKey()).update(game.getCurPhase().toString());
    }
    if(controllerParentPairs.get(1).getKey() instanceof RegionMenuController) {
      RegionMenuController regionControl = ((RegionMenuController) controllerParentPairs.get(1).getKey());
      regionControl.reset();
      if(game.getCurPhase() instanceof PreparationPhase) {
        regionControl.clearLog();
        regionControl.addLog(msg);
      }
    }
    if(controllerParentPairs.get(2).getKey() instanceof PlayerMenuController) {
      ((PlayerMenuController)controllerParentPairs.get(2).getKey()).updatePlayer(game.getCurPlayer());
    }
  }

  /**
   * Shows the summary for a phase
   * @param msg msg to display
   */
  private void showSummary(String msg) {
    //Popup summary
    Stage stage = (Stage)stackPaneMain.getScene().getWindow();
    Stage popupStage = new Stage();
    popupStage.initStyle(StageStyle.UNDECORATED);
    popupStage.initModality(Modality.APPLICATION_MODAL);
    popupStage.initOwner(stage);
    //Container
    VBox row = new VBox();
    row.setSpacing(15);
    row.setAlignment(Pos.CENTER);
    //msg
    Label summary = new Label();
    summary.setText(msg);
    summary.setTextFill(Paint.valueOf("red"));
    summary.setFont(Font.font("",30));
    row.getChildren().add(summary);
    //Set the popup size
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    double width = primaryScreenBounds.getWidth();
    double height = primaryScreenBounds.getHeight();
    popupStage.setScene(new Scene(row, width*0.5, height*0.5));
    popupStage.show();
    //Auto close after 1sec
    PauseTransition delay = new PauseTransition(Duration.seconds(1));
    delay.setOnFinished(e->popupStage.hide());
    delay.play();
  }

  public GamePhase getCurPhase() {
   return game.getCurPhase();
 }


  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());

        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        Faction a = Faction.find(faction);
        Image image = new Image(a.getFlagPath(),50,50,false,false);
        PictureMarkerSymbol s = new PictureMarkerSymbol(image);

        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);
    
    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);
        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (game.getCurPlayer().getRegion(province)!=null){
                  // province owned by human
                  if (currentlySelectedLeftProvince != null){
                    featureLayer.unselectFeature(currentlySelectedLeftProvince);
                  }
                  currentlySelectedLeftProvince = f;
                  if (controllerParentPairs.get(1).getKey() instanceof RegionMenuController){
                    ((RegionMenuController)controllerParentPairs.get(1).getKey()).handleLeftClick(game.getCurPlayer().getRegion(province));
                  }
                  featureLayer.selectFeature(f);                

                }
                // Have an Else clause to show the player that it's not his region that's why he cant select
                

              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      } else if (e.getButton() == MouseButton.SECONDARY && Objects.equals(game.getCurPhase().toString(), "Move")) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");
                if (currentlySelectedRightProvince != null){
                  featureLayer.unselectFeature(currentlySelectedRightProvince);
                }
                currentlySelectedRightProvince = f;

                if (game.getCurPlayer().getRegion(province)!=null){
                  // province owned by human
                  if (controllerParentPairs.get(1).getKey() instanceof RegionMenuController){
                    ((RegionMenuController)controllerParentPairs.get(1).getKey()).handleRightClick(province, game.displayRegion(province), false);
                  }

                  
                } else {
                  if (controllerParentPairs.get(1).getKey() instanceof RegionMenuController){
                    ((RegionMenuController)controllerParentPairs.get(1).getKey()).handleRightClick(province, game.displayRegion(province), true);
                    
                  }
                }
                featureLayer.selectFeature(f);
              
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      } 
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(game.getCurFaction()));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  public void resetSelections(){
    if(currentlySelectedLeftProvince!=null) {
      featureLayer_provinces.unselectFeature(currentlySelectedLeftProvince);
    }
    if(currentlySelectedRightProvince!=null) {
      featureLayer_provinces.unselectFeature(currentlySelectedRightProvince);
    }
    currentlySelectedRightProvince = null;
    currentlySelectedLeftProvince = null;
    if(controllerParentPairs.get(1).getKey() instanceof RegionMenuController) {
      RegionMenuController regionControl = ((RegionMenuController) controllerParentPairs.get(1).getKey());
      regionControl.reset();
    }
  }

  private void printMessageToTerminal(String message){
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController){
      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void switchMenu() throws JsonParseException, JsonMappingException, IOException {
    System.out.println("trying to switch menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    Collections.reverse(controllerParentPairs);
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public String regionConTrainRequest(List<String> train, String region) {
    String msg = game.train(region, train);
    if(controllerParentPairs.get(2).getKey() instanceof PlayerMenuController) {
      ((PlayerMenuController)controllerParentPairs.get(2).getKey()).updatePlayer(game.getCurPlayer());
    }
    return msg;
  }

  public void regionConTaxReform(int newTax, String region) {
    game.getCurPlayer().getRegion(region).setTax(newTax);
  }

  public String regionMoveRequest(String origin, String target, List<String> units) throws IOException {
      return game.getCurPhase().move(origin, units,  target);
  }

  public String regionAttackRequest(String origin, String target, List<String> units ) throws IOException {
      return game.invade(origin, units, target);
  }

}
