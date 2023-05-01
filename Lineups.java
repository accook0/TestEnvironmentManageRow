//This will contain the graphics element/front end
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import java.text.NumberFormat;
import java.util.Optional;
import javafx.scene.layout.TilePane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;

public class Lineups extends Application
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final int MIN_WIDTH = 600;
    private static final int MIN_HEIGHT = 500;

    private static final String[] POSITIONS = {"Port", "Starboard", "Coxswain"};
    private static final int[] BOATS = {1, 2, 4, 8};
    //private Button newBoatButton = new Button("New Boat");

    private TextField boatNameField =  new TextField();
    private TextField boatSizeField =  new TextField();
    private TextField rowerNameField =  new TextField();
    private TextField rowerSideField =  new TextField();

    private BorderPane root = new BorderPane();
    private FlowPane boatInfo = new FlowPane();

    private ComboBox boatSizes = new ComboBox();
    private ComboBox rowerPosition = new ComboBox();
    
    private TableView<Rower> table;

    private TextArea displayArea =  new TextArea();

    private static ArrayList<Boat> fleet = new ArrayList<Boat>();
    private static ArrayList<Rower> teamRoster = new ArrayList<Rower>();

    public void start(Stage stage) {     
        
        //fleet = new Fleet();
        displayArea.setEditable(false);
        //BorderPane root = new BorderPane();
        
        HBox boatInputs = new HBox(10);
        HBox rowerInputs = new HBox(10);

        Label boatNameLabel = new Label("Boat Name:    ");
        Label boatSizeLabel = new Label("Boat Size:   ");
        Label rowerNameLabel = new Label("Rower Name: ");
        Label rowerSideLabel = new Label("Rower Position: ");

        Button addBoatButton = new Button("Add Boat");
        addBoatButton.setOnAction( e -> addBoat());
        Button addRowerButton = new Button("Add Rower");
        addRowerButton.setOnAction( e -> addRowertoRoster());

        for(int i = 0; i < POSITIONS.length; i++)
        {
            rowerPosition.getItems().add(POSITIONS[i]);
        }
        for(int i = 0; i < BOATS.length; i++)
        {
            boatSizes.getItems().add(BOATS[i]);
        }
        


        boatInputs.getChildren().addAll(boatNameLabel, boatNameField, boatSizeLabel, boatSizes, addBoatButton);
        rowerInputs.getChildren().addAll(rowerNameLabel, rowerNameField, rowerSideLabel, rowerPosition, addRowerButton);

        boatInputs.setPadding(new Insets(0,0,0,10));
        rowerInputs.setPadding(new Insets(0,0,0,10));

        VBox inputs = new VBox(10);
        //inputs.getChildren().addAll(boatInputs,rowerInputs);
                                        
        //buttons.getChildren().addAll(newBoatButton);
        
        Menu boatMenu = createBoatMenu();
        Menu rowerMenu = createRowerMenu();
        Menu rosterMenu = createRosterMenu();
        MenuBar menubar = new MenuBar(boatMenu, rowerMenu, rosterMenu);
        //buttons.getChildren().addAll(menubar);
        inputs.getChildren().addAll(boatInputs,rowerInputs);
        root.setTop(inputs);
        root.setBottom(displayArea);
        
        updateTable();
        /* TableView<Rower> table = createRosterView();
        root.setRight(table); */
        Scene scene = new Scene(root, Color.LIGHTBLUE);
        
        
        
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);    
      
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        


        // configure the stage and make the stage visible
        stage.setScene(scene);
        stage.setTitle("Lineups");

        stage.show(); 
    }

    public void updateTable()
    {
        table = createRosterView();
        root.setRight(table);
    }

    public Menu createBoatMenu() {
        Menu boatMenu = new Menu("Fleet");
        

        MenuItem item = new MenuItem("Add Boat");
        item.setOnAction( e -> addBoat());
        boatMenu.getItems().add(item);
        
        item = new MenuItem("Remove Boat");
        //item.setOnAction( e -> currentColor = Color.GREEN );
        boatMenu.getItems().add(item);
          
        
        
         
        
        return boatMenu;
    }

    public void addBoat()
    {
        displayArea.setText("");
        if(boatSizes.getValue() == null || boatNameField.getText().equals(""))
        {
            displayArea.setText("Please enter a Boat name and select a size");
        }
        else
        {
        Boat b = new Boat(Integer.parseInt(boatSizes.getValue().toString()), boatNameField.getText(),0);
        fleet.add(b);
        boatSizeField.setText("");
        boatNameField.setText("");
        System.out.println(fleet);
        updateBoatDisplay();
        /* TilePane tile = new TilePane(Orientation.VERTICAL);
        for(int i = 0; i < b.SIZEOFSHELL; i++)
        {
            Rower temp = b.getLineup()[i];
            Label rowerLabel;
            if(temp != null)
            {
                rowerLabel = new Label(temp.toString());
            }
            else
            {
                rowerLabel = new Label("Empty Seat");
            }
            tile.getChildren().add(rowerLabel);
        }
        tilePaneHolder.add(tile); */
        //System.out.println(fleet);
        }
    }

    public void updateBoatDisplay()
    {
        Collections.sort(fleet, Comparator.comparingInt(Boat ::getSize));
        boatInfo.getChildren().clear();
        for(Boat b : fleet)
        {
            ArrayList<String> tempArray = new ArrayList<String>();
            Rower[] rowers = b.getLineup();
            
            for(int i = 0; i < rowers.length; i++)
            {
                if(rowers[i] == null)
                {
                    if(i == rowers.length - 1 && i > 1)
                    {
                        tempArray.add("C: Empty Seat");
                    }
                    else
                    {
                        tempArray.add("" + (i + 1) + ": Empty Seat");
                    }
                }
                else
                {
                    if(i == rowers.length - 1 && i > 1)
                    {
                        tempArray.add("C: " + rowers[i].getName());
                    }
                    else
                    {
                    tempArray.add("" + (i + 1) + ": " + rowers[i].getName());
                    }
                }
            }
            System.out.println(tempArray);
            ListView<String> boatList = new ListView<String>();

            ObservableList<String> boats = FXCollections.observableArrayList(tempArray);
            boatList.setItems(boats);
            boatList.prefHeightProperty().bind(Bindings.size(boats).multiply(24));
            VBox test = new VBox(10);
            HBox boatHeader = new HBox(10);
            Button addRowerToBoat = new Button("Add Rower");
            addRowerToBoat.setOnAction( e -> addRowerToBoat(b));
            Button removeRowerFromBoat = new Button("Remove Rower");
            removeRowerFromBoat.setOnAction( e -> removeRowerFromBoat(b, boatList));
            Label boatNameLabel = new Label(b.getName());
            boatHeader.getChildren().addAll(boatNameLabel, addRowerToBoat, removeRowerFromBoat);
            test.getChildren().addAll(boatHeader, boatList);
            boatInfo.getChildren().add(test);
            root.setCenter(boatInfo);
        }
        
        
    }

    public void removeRowerFromBoat(Boat b, ListView<String> list)
    {
        displayArea.setText("");
        String name = list.getSelectionModel().getSelectedItem();
        name = name.substring(3);
        System.out.println(name);
        Rower[] lineup = b.getLineup();
        Rower tempRower = null;
        for(int i = 0; i < lineup.length; i ++)
        {
            if(lineup[i] != null)
            {
                if(lineup[i].getName().equals(name))
                {
                    tempRower = lineup[i];
                    lineup[i] = null;
                }
            }
        }
        if(tempRower == null)
        {
            displayArea.setText("No rower selected");
        }
        else{
        teamRoster.add(tempRower);
        updateBoatDisplay();
        updateTable();
        }

    }

    public Menu createRowerMenu() {
        Menu rowerMenu = new Menu("Boats");
        

        
          
        
        MenuItem item = new MenuItem("Add Rower");
        //item.setOnAction( e -> addRowerToBoat());
        rowerMenu.getItems().add(item);

        item = new MenuItem("Remove Rower");
        //item.setOnAction( e -> currentColor = Color.BLUE );
        rowerMenu.getItems().add(item);
         
        
        return rowerMenu;
    }

    public Menu createRosterMenu() {
        Menu rosterMenu = new Menu("Roster");
        

        MenuItem item = new MenuItem("Add Rower");
        item.setOnAction( e -> addRowertoRoster());
        rosterMenu.getItems().add(item);
        
        item = new MenuItem("Remove Rower");
        //item.setOnAction( e -> currentColor = Color.GREEN );
        rosterMenu.getItems().add(item);
          
        
         
        
        return rosterMenu;
    }

    public void addRowertoRoster()
    {
        displayArea.setText("");
        if(rowerNameField.getText().equals("") || rowerPosition.getValue() == null)
        {
            displayArea.setText("Please enter a Rower's name and select a Position");
        }
        else
        {
        Rower r = new Rower(rowerNameField.getText(), rowerPosition.getValue().toString(), "", 200.0);
        teamRoster.add(r);
        rowerNameField.setText("");
        rowerSideField.setText("");
        updateTable();
        //System.out.println(teamRoster);
        }
    }

    public void addRowerToBoat(Boat bo)
    {
        displayArea.setText("");
        Rower r = table.getSelectionModel().getSelectedItem();
        if(r == null)
        {
            displayArea.setText("Please select a rower on the roster");
        }
        else
        {
        int seat = getSeat();
        
        if(seat % 2 == 1 && seat == bo.getSize() && r.getSide().equals("Coxswain") && bo.getSize() != 1)
        {
            bo.addRower(r, seat);
            teamRoster.remove(r);
            updateTable();
            updateBoatDisplay();
        }
        else if(seat % 2 == 1 && r.getSide().equals("Starboard") && seat == bo.getSize() && bo.getSize() != 1)
        {
            System.out.println("You must select a coxswain for this seat");
            displayArea.setText("You must select a coxswain for this seat");
        }
        else if(seat % 2 == 1 && !r.getSide().equals("Starboard"))
        {
            if(seat == bo.getSize() && bo.getSize() != 1)
            {
                System.out.println("You must select a Coxswain for this seat");
                displayArea.setText("You must select a Coxswain for this seat");
            }
            else
            {
                System.out.println("You must select a Starboard for this seat");
                displayArea.setText("You must select a Starboard for this seat");
            }
        }
        else if(seat % 2 == 0 && !r.getSide().equals("Port"))
        {
            System.out.println("You must select a Port for this seat");
            displayArea.setText("You must select a Port for this seat");
        }
        else
        {

        
        /* if(isValidRower(table.getSelectionModel().getSelectedItem()))
        {
            Rower temp = null;
            for(Rower r: teamRoster)
            {
                if(r.getName().equals(rowerNameField.getText()))
                {
                    temp = r;
                }
            }
            for(Boat b: fleet)
            {
                if(b.getName().equals(boat))
                {
                    b.addRower(temp, 1);
                    System.out.println(b);
                }
            }
        } */
        bo.addRower(r, seat);
        teamRoster.remove(r);
        updateTable();
        updateBoatDisplay();
        }
    }



    }

    public int getSeat() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Which seat would you like to add this rower to?");
        dialog.setTitle("Boat Information Request");
        
        Optional<String> result = dialog.showAndWait();
        
        return Integer.parseInt(result.get());
    }

    public boolean isValidBoat(String name)
    {
        for(Boat b: fleet)
        {
            if(b.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }
    public boolean isValidRower(String name)
    {
        for(Rower r : teamRoster)
        {
            if(r.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public TableView<Rower> createRosterView(){
        ObservableList<Rower> observable_roster = FXCollections.observableArrayList(teamRoster);
        ListView<Rower> roster_view = new ListView<Rower>();
        roster_view.setItems(observable_roster);
        TableView<Rower> table = new TableView<Rower>();
        table.setItems(observable_roster);
        TableColumn<Rower, String> nameCol = new TableColumn<Rower, String>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Rower, String> sideCol = new TableColumn<Rower, String>("Side");
        sideCol.setCellValueFactory(new PropertyValueFactory("side"));
        table.getColumns().setAll(nameCol, sideCol);
        
        return table;
    }

    

    //Use tilepane or Grid Pane


}