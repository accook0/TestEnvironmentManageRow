import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;





public class BackendTest extends Application {



    public void drawFour(GraphicsContext gc){
        //four coxed
        gc.translate(0, 150);
        gc.rotate(-45);
        gc.strokeOval(10, 10, 200, 30); //boat

        gc.setStroke(Color.MAROON);
        gc.setFill(Color.MAROON);
        gc.strokeOval(55, 15, 20, 20); //stroke
        gc.fillOval(115, 15, 20, 20); //2

        gc.setStroke(Color.GREEN);
        gc.strokeOval(85, 15, 20, 20); //3
        gc.strokeOval(145, 15, 20, 20); //1

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        gc.strokeOval(175, 17.5, 15, 15);
        gc.fillOval(177.5, 20, 10, 10); //cox
        gc.translate(0, -150);
        gc.rotate(45);
    }

    // Draw on the canvas
    public void saveImg(Canvas canvas, String name){
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(Transform.scale(1.5, 1.5)); // double the scale factor
        canvas.snapshot(params, writableImage);
        // = "test3";
        String test = name.concat(".png");
        System.out.println(test);
        File file = new File(test);

        try{
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } 
        catch (IOException e){
            System.out.println("didn't save");
        }
    }

    

    @Override
    public void start(Stage stage){
        Canvas canvas = new Canvas(400, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawFour(gc);
        saveImg(canvas, "testimg4");

        Button b = new Button();
        Image img = new Image("test3.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);

        b.setGraphic(view);

        Pane p = new Pane();
        p.getChildren().addAll(canvas, b);
        Scene scene = new Scene(p);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
