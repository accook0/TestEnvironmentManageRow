import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.Optional;

import javax.swing.plaf.metal.MetalCheckBoxIcon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView; 
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javax.imageio.ImageIO;
import javafx.scene.transform.Transform;
import java.io.FileWriter;


public class ManageRow extends Application{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String[] POSITIONS = {"Port", "Starboard", "Both", "Coxswain"};
    private static final Integer[] BOATS = {1, 2, 4, 8};
    private static final String[] RIGS = {"Port", "Starboard"};
    private Button newBoatButton = new Button("New Boat");

    private TextField boatNameField =  new TextField();
    private TextField boatSizeField =  new TextField();
    private TextField rowerNameField =  new TextField();
    private TextField rowerSideField =  new TextField();
    
    private Canvas boatImg = new Canvas();

    private BorderPane root = new BorderPane();
    private FlowPane boatInfo = new FlowPane();
    private GridPane boatThumbnails = new GridPane();
    private ScrollPane allThumbnails = new ScrollPane();


    private ComboBox boatSizes = new ComboBox(FXCollections.observableArrayList(BOATS));
    private ComboBox rowerPosition = new ComboBox();
    private ComboBox rigOptions = new ComboBox(FXCollections.observableArrayList(RIGS));
    
    private TableView<Rower> table;

    private TextArea displayArea =  new TextArea();

    private static ArrayList<Boat> fleet = new ArrayList<Boat>();
    private static ArrayList<Rower> teamRoster = new ArrayList<Rower>();

    private Tab boatsTab = new Tab();
    private Tab lineupsTab = new Tab();
    private Tab rosterTab = new Tab();
    private Tab learnMore = new Tab();

    public void start(Stage stage){
        //create tabs
        TabPane tabPane = new TabPane();
        boatsTab.setText("Boats");
        boatsTab.setClosable(false);
        Canvas c = new Canvas();
        setBoatTab(boatsTab);



        lineupsTab.setText("Lineups");
        lineupsTab.setClosable(false);


        rosterTab.setText("Roster");
        rosterTab.setClosable(false);


        learnMore.setText("LearnMore");
        learnMore.setClosable(false);

        tabPane.getTabs().addAll(boatsTab, lineupsTab, rosterTab, learnMore);
        //create all handlers
        setHandlers(c);

        //create the scene
        Scene scene = new Scene(tabPane);

        stage.setScene(scene);
        stage.setTitle("Row Manager");
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        //scene.getStylesheets().add("cssFile.css");
        stage.show(); 


    }

    public void setBoatTab(Tab boats){
        HBox boatInfo = new HBox(50);
        VBox name = new VBox(10);
        VBox size = new VBox(10);
        VBox rig = new VBox(10);

        Label boatNameLabel = new Label("Boat Name:    ");
        Label boatSizeLabel = new Label("Boat Size:   ");
        Label boatRigLabel = new Label("Boat Rig:   ");

        name.getChildren().addAll(boatNameLabel, boatNameField);
        size.getChildren().addAll(boatSizeLabel, boatSizes);
        rig.getChildren().addAll(boatRigLabel, rigOptions);
        boatInfo.getChildren().addAll(name, size, rig, newBoatButton);

        BorderPane page = new BorderPane();
        Pane wrapperPane = new Pane();
        
        page.setCenter(wrapperPane);
        
        // Put canvas in the center of the window
        wrapperPane.getChildren().add(boatImg);
        // Bind the width/height property to the wrapper Pane
        boatImg.widthProperty().bind(wrapperPane.widthProperty());
        boatImg.heightProperty().bind(wrapperPane.heightProperty());

        page.setPadding(new Insets(10));
        page.setTop(boatInfo);
        allThumbnails.setContent(boatThumbnails);
        page.setLeft(allThumbnails);
        boats.setContent(page);
    }

    private void setHandlers(Canvas c){
        newBoatButton.setOnAction(e-> addBoat(c));
    }

    //********************** Helpers **********************/

    public void saveImg(Canvas canvas, String name){
        WritableImage writableImage = new WritableImage(400, 400);
        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(Transform.scale(1, 1)); // double the scale factor
        canvas.snapshot(params, writableImage);
        // = "test3";
        String test = name.concat(".png");
        System.out.println(test);
        File file = new File(test);
        System.out.println("in save img");

        try{
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } 
        catch (IOException e){
            System.out.println("didn't save");
        }
        System.out.println("exiting save img");

    }

    //********************** Handlers **********************/
    public void addBoat(Canvas c){
        //check rig to draw boat
        int rigin = 0;
        if(String.valueOf(rigOptions.getValue()) == RIGS[1]){
            rigin = 1;
        }
        //for the CSV
        String boatName = boatNameField.getText();


        Boat b = new Boat(Integer.valueOf(String.valueOf((boatSizes.getValue()))), boatNameField.getText(), rigin);
        fleet.add(b);
        csvWriterBoat(fleet); //ideally this would only do new ones but we dont have a save button
        
        //draw the boat
        GraphicsContext gc = boatImg.getGraphicsContext2D();
        gc.clearRect(0, 0, boatImg.getWidth(), boatImg.getHeight());
        b.drawBoat(gc);

        //reset the page
        boatSizeField.setText("");
        boatNameField.setText("");

        saveImg(c, boatName);
           
    }

    //********************** CSV Writers **********************/
    public void csvWriterRower(ArrayList<Rower> data) {
        //ArrayList<String> data = new ArrayList<String>();
        
        String csvFilePath = "roster.csv";
        FileWriter csvWriter = null;
        
        try {
            csvWriter = new FileWriter(csvFilePath);
            for (Rower line : data) {
                csvWriter.append(line.getName());
                csvWriter.append("|");
                csvWriter.append(line.getSide());
                csvWriter.append("|");
                csvWriter.append(line.getErgScore());
                csvWriter.append("|");
                csvWriter.append(String.valueOf(line.getWeight()));
                csvWriter.append("\n");

            }
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public void csvWriterBoat(ArrayList<Boat> data) {
            //ArrayList<String> data = new ArrayList<String>();
            
            String csvFilePath = "boats.csv";
            FileWriter csvWriter = null;
            
            try {
                csvWriter = new FileWriter(csvFilePath);
                for (Boat line : data) {
                    csvWriter.append(line.getName());
                    csvWriter.append("|");
                    csvWriter.append(String.valueOf(line.getSize()));
                    csvWriter.append("|");
                    csvWriter.append(String.valueOf(line.getRig()));
                    csvWriter.append("\n");
    
                }
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
