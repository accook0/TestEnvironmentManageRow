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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.canvas.*;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;



public class ManageRow2 extends Application{
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static final String[] POSITIONS = {"Port", "Starboard", "Both", "Coxswain"};
    private static final Integer[] BOATS = {1, 2, 4, 8};
    private static final String[] RIGS = {"Port", "Starboard"};

 

    private Button newBoatButton = new Button("New Boat");

    private TextField boatNameField =  new TextField();
    private TextField boatSizeField =  new TextField();
    private TextField rowerNameField =  new TextField();
    private TextField rowerSideField =  new TextField();
    
    private Canvas boatImg; // = new Canvas();
    private Canvas lineupsCanvas = new Canvas();


    private BorderPane root = new BorderPane();
    private FlowPane boatInfo = new FlowPane();
    private GridPane boatThumbnails = new GridPane();
   // private ScrollPane allThumbnails = new ScrollPane();

    private HBox lineupsTable = new HBox(100);

    private ComboBox boatSizes = new ComboBox(FXCollections.observableArrayList(BOATS));
    private ComboBox rowerPosition = new ComboBox();
    private ComboBox rigOptions = new ComboBox(FXCollections.observableArrayList(RIGS));
    private ComboBox boatsDropDown = new ComboBox();
    private ComboBox ports = new ComboBox();
    private ComboBox starboards = new ComboBox();
    private ComboBox coxs = new ComboBox();
    
    private TableView<Rower> table;

    private TextArea displayArea =  new TextArea();

    private ArrayList<Boat> fleet = new ArrayList<Boat>();
    private ArrayList<Rower> teamRoster = new ArrayList<Rower>();

 //   private static int boatAdded = 0;
    //private Boat[] boats = new Boat[30];
    private Canvas[] canvases = new Canvas[16];

    private Tab boatsTab = new Tab();
    private Tab lineupsTab = new Tab();
    private Tab rosterTab = new Tab();
    private Tab learnMore = new Tab();

    public void start(Stage stage){
        //create elements for tabs
        //createRowerCombos("roster.csv");

      

        teamRoster = csvReaderRower("roster.csv");
        //create tabs
        TabPane tabPane = new TabPane();
        boatsTab.setText("Boats");
        boatsTab.setClosable(false);
      
      
        // Canvas c = new Canvas();
        setBoatTab();



        lineupsTab.setText("Lineups");
        lineupsTab.setClosable(false);
        setLineupsTab(lineupsTab);


        rosterTab.setText("Roster");
        rosterTab.setClosable(false);
        setRosterTab(rosterTab);


        learnMore.setText("LearnMore");
        learnMore.setClosable(false);
        setLearnMoreTab(learnMore);

        tabPane.getTabs().addAll(boatsTab, lineupsTab, rosterTab, learnMore);
        //create all handlers
        setHandlers();//c);

        //create the scene
        Scene scene = new Scene(tabPane);

        stage.setScene(scene);
        stage.setTitle("Row Manager");
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        scene.getStylesheets().add("style.css");
        stage.show(); 

 /* 
        int last = fleet.size()-1;
        GraphicsContext smallGC = canvases[last].getGraphicsContext2D();
        fleet.get(last).drawBoat(smallGC, 2, true);
       */
 
        for(int i = 0; i < 8; i ++){
            for(int j = 0; j < 2; j ++){
                int index = i*2+j;

                if (index < fleet.size()) {
                    GraphicsContext gc = canvases[index].getGraphicsContext2D();
                    fleet.get(index).drawBoat(gc, 2, true);
                    System.out.println("redraw small canvas " + index + "/" + fleet.size());
                }
            }
        }
    }

    

    public void setBoatTab(){
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
        page.setPadding(new Insets(10));
        page.setTop(boatInfo);

        fleet = readBoatCsv("boats.csv"); //returns the csv

        GridPane wrapperPane = new GridPane();
        wrapperPane.setGridLinesVisible(true);

        //wrapperPane.setHgap(20);
        page.setCenter(wrapperPane);

        Pane p = new Pane();
        boatImg = new Canvas(wrapperPane.getWidth()/2, wrapperPane.getHeight());
        p.getChildren().add(boatImg);
        wrapperPane.setStyle("-fx-background-color: lightyellow");
      //  wrapperPane.add(boatImg,0, 0);
        // Put canvas in the center of the window
     //   wrapperPane.getChildren().add(boatImg);
        wrapperPane.add(p, 0, 0);

        wrapperPane.add(boatThumbnails, 1, 0);

        boatThumbnails.setGridLinesVisible(true);
        // Bind the width/height property to the wrapper Pane
        boatImg.widthProperty().bind(wrapperPane.widthProperty());
        boatImg.heightProperty().bind(wrapperPane.heightProperty());

 

        // may be drawn too early
        for(int i = 0; i < 8; i ++){
            for(int j = 0; j < 2; j ++){
                int index = i*2+j;
 
                canvases[index] = new Canvas(100, 100);

             //   if (index < fleet.size()) {
             //       GraphicsContext gc = canvases[index].getGraphicsContext2D();
                  //  fleet.get(index).drawBoat(gc, -1, true);
             //       System.out.println("redraw small canvas " + index + "/" + fleet.size());
              //  }
                boatThumbnails.add(canvases[index], j, i);

            }
        }


       // page.setRight(new Label("right"));//boatThumbnails);

        boatThumbnails.setStyle("-fx-background-color: #336699;");
        boatsTab.setContent(page);
      //  popThumbnails();

    }

    public void setLineupsTab(Tab lineups){
        boatsDropDown.setPrefWidth(100);
        //System.out.println(fleet.toString());
        for(Boat b : fleet){ //read the csv here, create combo box
            boatsDropDown.getItems().add(b.getName());
        }

        VBox selectBoat = new VBox(20);
        selectBoat.getChildren().addAll(boatsDropDown);

        //make all canvas things
        BorderPane lineupsPane = new BorderPane();
        Pane wrapperPane = new Pane();
        lineupsPane.setCenter(wrapperPane);

        //bind the canvas
        wrapperPane.getChildren().addAll(lineupsCanvas);
        lineupsCanvas.widthProperty().bind(wrapperPane.widthProperty());
        lineupsCanvas.heightProperty().bind(wrapperPane.heightProperty());

        lineupsPane.setPadding(new Insets(10));
        lineupsPane.setTop(selectBoat);

        TableView<Rower> rosterTable = createCoxRowerRosterView();


        //this is for the proof of consept
        Boat b = new Boat(4, "Conte", 1);
        HBox test = lineupsTable(b);
        lineupsPane.setBottom(test);
        lineupsPane.setRight(rosterTable);

      
        lineups.setContent(lineupsPane);

    }

    private void setRosterTab(Tab rosterTab){
        BorderPane rosterPane = new BorderPane();
        VBox rowers = new VBox(10);
        rosterPane.setPadding(new Insets(10));

        HBox nameAndWeight = new HBox(10);
        Label rowerName = new Label("Name");
        TextField rowerNameField2 = new TextField();
        Label weight = new Label("Lbs");
        TextField weightField = new TextField();
        nameAndWeight.getChildren().addAll(rowerName, rowerNameField2, weight, weightField);

        HBox positionAndRemove = new HBox(10);
        Label positionLabel = new Label("Position");
        ComboBox positionDropDown = new ComboBox();
        for(int i = 0; i < POSITIONS.length-1; i++)
        {
            positionDropDown.getItems().add(POSITIONS[i]);;
        }
        Button removeRowerButton = new Button("Remove");
        positionAndRemove.getChildren().addAll(positionLabel, positionDropDown, removeRowerButton);

        HBox ergScoreAndSave = new HBox(10);
        Label ergLabel = new Label("2k");
        TextField ergScore = new TextField();
        Button saveRowerButton = new Button("Save");
        ergScoreAndSave.getChildren().addAll(ergLabel, ergScore, saveRowerButton);


        rowers.getChildren().addAll(nameAndWeight, positionAndRemove, ergScoreAndSave);
        
        TableView<Rower> rowerTable = createRowerRosterView();
        TableView<Rower> coxTable = createCoxRosterView();

        VBox rowerTableBox = new VBox(20);
        VBox coxTableBox = new VBox(20);
        //Create stuff below cox table
        VBox coxFields = new VBox(20);
        HBox coxNameFields = new HBox(10);
        HBox coxyearFields = new HBox(10);

        Label coxNameLabel = new Label("Name");
        TextField coxNameField = new TextField();
        Button removeCoxButton = new Button("Remove");
        coxNameFields.getChildren().addAll(coxNameLabel, coxNameField, removeCoxButton);

        Label coxYearLabel = new Label("Year");
        ComboBox yearDropDown = new ComboBox();
        for(int i = 2023; i < 2027; i++)
        {
            yearDropDown.getItems().add(i);;
        }
        Button addCoxButton = new Button("Save");
        coxyearFields.getChildren().addAll(coxYearLabel, yearDropDown, addCoxButton);

        coxFields.getChildren().addAll(coxNameFields, coxyearFields);

        rowerTableBox.getChildren().addAll(rowerTable, rowers);
        rosterPane.setLeft(rowerTableBox);

        coxTableBox.getChildren().addAll(coxTable, coxFields);
        rosterPane.setRight(coxTableBox);

        rosterTab.setContent(rosterPane);

        
        saveRowerButton.setOnAction(e-> {
            Rower temp = new Rower(rowerNameField2.getText(), String.valueOf(positionDropDown.getValue()), ergScore.getText(), Double.valueOf(weightField.getText()));
            teamRoster.add(temp);
            System.out.println(teamRoster.toString());

            /* createRowerRosterView();
            createCoxRosterView(); */
            setRosterTab(rosterTab);
            setLineupsTab(lineupsTab);

        }); //working here
    }

    public void setLearnMoreTab(Tab learnMore){
        Image gifImage = new Image("LearnMore.gif");
        
        // Create an ImageView object to display the GIF image
        ImageView gifImageView = new ImageView(gifImage);
        
        // Create a Pane to hold the ImageView
        ScrollPane pane = new ScrollPane(gifImageView);
        learnMore.setContent(pane);
    }


    private void setHandlers() { //Canvas c){
        newBoatButton.setOnAction(e-> 
        {
            addBoat();
        }); //c));
        boatsDropDown.setOnAction(e -> selectBoat()); //make this
        
    }

    //********************** Helpers **********************/

    public HBox lineupsTable(Boat b)
    {
        HBox output = new HBox(0);
        for(int i = 0; i < b.getLineup().length; i++)
        {
            VBox seat = new VBox(0);
            VBox rower = new VBox(0);
            VBox combo = new VBox(10);
            combo.setPrefWidth(100);
            combo.setPrefHeight(100);
            Label seatLabel;
            if(i == 0)
            {
                seatLabel = new Label("Bow");
            }
            else if(i == b.getLineup().length-2)
            {
                seatLabel = new Label("Stroke");
            }
            else if(i == b.getLineup().length-1)
            {
                seatLabel = new Label("Coxswain");
            }
            else
            {
                seatLabel = new Label("" + (i + 1));
            }
            
            Label rowerLabel;
            ComboBox rowerLabel2;
            rowerLabel2 = new ComboBox();
            rowerLabel2.getItems().add("Empty");

            
            for(Rower r : teamRoster){
                
                if(i == b.getLineup().length-1 && r.getSide().equals("Coxswain")){
                    rowerLabel2.getItems().add(r.getName());
                }
                else if((r.getSide().equals("Port") || r.getSide().equals("Both") ) && i % 2 == b.getRig()-1){
                    rowerLabel2.getItems().add(r.getName());
                }
                else if((r.getSide().equals("Starboard") || r.getSide().equals("Both") )&& i % 2 == b.getRig()){
                    rowerLabel2.getItems().add(r.getName());
                }
            }
            if(b.getLineup()[i] != null){
            rowerLabel = new Label(b.getLineup()[i].getName());
                rowerLabel2.setValue(b.getLineup()[i].getName());
            }
            else{
                rowerLabel = new Label("Empty");
                rowerLabel2.setValue("Empty");
            }
            seat.getChildren().add(seatLabel);
            rower.getChildren().add(rowerLabel2);

            combo.getChildren().addAll(seat, rower);

            output.getChildren().add(combo);
        }
        return output;
    }

    public TableView<Rower> createCoxRowerRosterView(){
        
        ObservableList<Rower> observable_roster = FXCollections.observableArrayList(teamRoster);
        ListView<Rower> roster_view = new ListView<Rower>();
        roster_view.setItems(observable_roster);

        TableView<Rower> table = new TableView<Rower>();
        table.setItems(observable_roster);

        TableColumn<Rower, String> nameCol = new TableColumn<Rower, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.setPrefWidth(100);
        
        TableColumn<Rower, String> sideCol = new TableColumn<Rower, String>("Position");
        sideCol.setCellValueFactory(new PropertyValueFactory("side"));
        sideCol.setPrefWidth(100);
        
        TableColumn<Rower, String> ergCol = new TableColumn<Rower, String>("2k");
        ergCol.setCellValueFactory(new PropertyValueFactory("ergScore"));
        ergCol.setPrefWidth(100);


        table.getColumns().setAll(nameCol, sideCol, ergCol);
        
        return table;
    }
    public TableView<Rower> createRowerRosterView(){
        ArrayList<Rower> rowersOnly = new ArrayList<Rower>();
        for(Rower r: teamRoster)
        {
            if(!r.getSide().equals(POSITIONS[3]))
            {
                rowersOnly.add(r);
            }
        }
        System.out.println(rowersOnly);
        ObservableList<Rower> observable_roster = FXCollections.observableArrayList(rowersOnly);
        ListView<Rower> roster_view = new ListView<Rower>();
        roster_view.setItems(observable_roster);
        TableView<Rower> table = new TableView<Rower>();
        table.setItems(observable_roster);

        TableColumn<Rower, String> nameCol = new TableColumn<Rower, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.setPrefWidth(100);

        TableColumn<Rower, String> sideCol = new TableColumn<Rower, String>("Position");
        sideCol.setCellValueFactory(new PropertyValueFactory("side"));
        sideCol.setPrefWidth(100);
        
        TableColumn<Rower, String> ergCol = new TableColumn<Rower, String>("2k");
        ergCol.setCellValueFactory(new PropertyValueFactory("ergScore"));
        ergCol.setPrefWidth(100);
        
        TableColumn<Rower, Double> weightCol = new TableColumn<Rower, Double>("Weight");
        weightCol.setCellValueFactory(new PropertyValueFactory("weight"));
        weightCol.setPrefWidth(100);
                
        table.getColumns().setAll(nameCol, sideCol, ergCol, weightCol);
        
        csvWriterRower(rowersOnly);
        return table;
    }

    public TableView<Rower> createCoxRosterView(){
        ArrayList<Rower> coxesOnly = new ArrayList<Rower>();
        for(Rower r: teamRoster)
        {
            if(r.getSide().equals(POSITIONS[3]))
            {
                coxesOnly.add(r);
            }
        }
        ObservableList<Rower> observable_roster = FXCollections.observableArrayList(coxesOnly);
        ListView<Rower> roster_view = new ListView<Rower>();
        roster_view.setItems(observable_roster);
        
        TableView<Rower> table = new TableView<Rower>();
        table.setItems(observable_roster);
        
        TableColumn<Rower, String> nameCol = new TableColumn<Rower, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Rower, String> classCol = new TableColumn<Rower, String>("Year");
        classCol.setCellValueFactory(new PropertyValueFactory("classYear"));
        classCol.setPrefWidth(150);
        
        table.getColumns().setAll(nameCol, classCol);
        
        
        return table;
    }


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

    //not used rn
    public void createRowerCombos(String filePath){
        teamRoster = csvReaderRower(filePath);
        for(Rower r : teamRoster){    
            if(r.getSide().equals("Coxswain")){
                coxs.getItems().add(r.getName());
            }
            else if((r.getSide().equals("Port") || r.getSide().equals("Both") )){
                ports.getItems().add(r.getName());
            }
            else if((r.getSide().equals("Starboard") || r.getSide().equals("Both"))){
                starboards.getItems().add(r.getName());
            }
        }
    }

    /* 
    public void popThumbnails() { //throws FileNotFoundException{

        System.out.println("in pop");
     //   int toBoatAdded = 0;


        for(int i = 0; i < 2; i ++){
            for(int j = 0; j < 8; j ++){
                int index = i*8+j;
                 System.out.println("Boat Here " + index);

                if (index < fleet.size()) {
                    GraphicsContext gc = canvases[index].getGraphicsContext2D();
                    fleet.get(index).drawBoat(gc, 2);
                    System.out.println("redraw small canvas");
                }

              //  b.setPadding(new Insets(10));
               // boatThumbnails.add(c, i, j);
 
             //   Image img = new Image(new FileInputStream("testimg4.png"), 100, 100, true, false);
             //   ImageView imgIcon = new ImageView(img);
 
            //    b.setGraphic(imgIcon);
            }
        }
    }
    */

    // public HBox lineupsTable(Boat b){
    //     HBox lineup = new HBox(10);
    //     for(int i = 0; i < b.getLineup().length; i++){
    //         int rig = b.getRig();
    //         VBox seat = new VBox(0);
    //         VBox rower = new VBox(0);
    //         VBox combo = new VBox(10);
    //         combo.setPrefWidth(100);
    //         combo.setPrefHeight(100);
    //         Label seatLabel;
    //         if(i == 0){
    //             seatLabel = new Label("Bow");
    //         }
    //         else if(i == b.getLineup().length-2){
    //             seatLabel = new Label("Stroke");
    //         }
    //         else if(2% i == 1){
    //             rower.getChildren().add(starboards);
    //         }
    //         else if(2% i == 0){
    //             rower.getChildren().add(ports);
    //         }
    //         else if(i == b.getLineup().length-1){
    //             seatLabel = new Label("Coxswain");
    //             rower.getChildren().add(coxs);
    //         }
    //         else{
    //             seatLabel = new Label("" + (i + 1));
    //         }
    //         combo.getChildren().addAll(seatLabel, rower);
    //         lineup.getChildren().add(combo);
    //     }
    //     return lineup;      
            
    // }

    //********************** Handlers **********************/
    public void addBoat() { //Canvas c){
        //check rig to draw boat
        int rigin = 0;
        if(String.valueOf(rigOptions.getValue()) == RIGS[1]){
            rigin = 1;
        }
        //for the CSV
        String boatName = boatNameField.getText();


        Boat b = new Boat(Integer.valueOf(String.valueOf((boatSizes.getValue()))), boatNameField.getText(), rigin);
       // boats[boatAdded++] = b;

        fleet.add(b);

        csvWriterBoat(fleet); //ideally this would only do new ones but we dont have a save button
        boatsDropDown.getItems().add(b.getName());
        //draw the boat
        GraphicsContext gc = boatImg.getGraphicsContext2D();
        gc.clearRect(0, 0, boatImg.getWidth(), boatImg.getHeight());
     
     // Elodie
        b.drawBoat(gc, 1, false);

        System.out.println("drawing big version?");
       
        // works by itself, but tempting to fix all of them
           int last = fleet.size()-1;
            GraphicsContext smallGC = canvases[last].getGraphicsContext2D();
            fleet.get(last).drawBoat(smallGC, 2, true);
            System.out.println("drawing new small canvas boat " + last + "/" + fleet.size());
      

        //reset the page
        boatSizeField.setText("");
        boatNameField.setText("");

        //popThumbnails();


        // fixing all the thumbnails
     /*  for(int i = 0; i < 8; i ++){
            for(int j = 0; j < 2; j ++){
                int index = i*2+j;
            //    canvases[index] = new Canvas(100, 100);
                if (index < fleet.size()) {
                    GraphicsContext smallGC = canvases[index].getGraphicsContext2D();
                    fleet.get(index).drawBoat(smallGC, -1, true);
                    System.out.println("redraw small canvas " + index + "/" + fleet.size());
                }

            }
        }
        */
    }

    public void selectBoat(){ //only draws boats that have been pre drawn
       String boatName = String.valueOf(boatsDropDown.getValue());
       GraphicsContext gc = lineupsCanvas.getGraphicsContext2D();
       Boat b =  Boat.getBoat(boatName, fleet);

       // Elodie
        b.drawBoat(gc, 1, false);
        lineupsTable = lineupsTable(b);
    }

    //********************** CSV Tools **********************/
    public void csvWriterRower(ArrayList<Rower> data) {
        //ArrayList<String> data = new ArrayList<String>();
        
        String csvFilePath = "roster.csv";
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter(csvFilePath);

            for (Rower line : data) {
                if(line.getSide() == "Port" || line.getSide() == "Starboard" || line.getSide() == "Both"){
                    csvWriter.append(line.getName());
                    csvWriter.append("|");
                    csvWriter.append(line.getSide());
                    csvWriter.append("|");
                    csvWriter.append(line.getErgScore());
                    csvWriter.append("|");
                    csvWriter.append(String.valueOf(line.getWeight()));
                    csvWriter.append("|");
                }
                else{
                    csvWriter.append(line.getName());
                    csvWriter.append("|");
                    csvWriter.append(line.getSide());
                    csvWriter.append("|");
                    csvWriter.append(String.valueOf(line.getWeight()));
                    csvWriter.append("|");
                    csvWriter.append(String.valueOf(line.getClassYear()));
                    csvWriter.append("\n");
                }
            }
            csvWriter.flush();
        } // changing csv writers to reflect the cox position needs
        
 
        catch (IOException e) {
            e.printStackTrace();
        } 
         try {
            csvWriter.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Rower> csvReaderRower(String filePath) {
        ArrayList<Rower> dataList = new ArrayList<Rower>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Rower data;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] values = line.split("\\|");
                //System.out.println(values[0]);
                if(values[1].equals("Coxswain")){
                    data = new Rower(values[0], Integer.valueOf(values[3]));

                }
                else{
                    data = new Rower(values[0], values[1], values[2], Double.valueOf(values[3])); // assuming the CSV has three columns
                }
                //name, size, rig
                //System.out.println(data);
                dataList.add(data);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
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

        public  ArrayList<Boat> readBoatCsv(String filePath) {
            ArrayList<Boat> dataList = new ArrayList<Boat>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    String[] values = line.split("\\|");
                    //System.out.println(values[0]);
                    Boat data = new Boat(Integer.valueOf(values[1]), values[0], Integer.valueOf(values[2])); // assuming the CSV has three columns
                    //name, size, rig
                    //System.out.println(data);

                    dataList.add(data);
                //    boats[boatAdded++] = dataList.get(dataList.size()-1);
 
                //    System.out.println("total boat " + boatAdded); 

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dataList;
        }
    }
