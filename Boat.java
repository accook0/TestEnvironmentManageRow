import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.font.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Boat
{
    private  Rower[] lineup;
    private  String name;
    private  int size;
    private int rig;
    private static final Color PORT = Color.MAROON;
    private static final Color STARBOARD = Color.GREEN;
    private static final int PORT_RIGGED = 0;
    private static final int STARBOARD_RIGGED = 1;
    private ArrayList<Integer> filledSeats = new ArrayList<Integer>();




    
    public Boat(int sizeOfShell, String nameIn, int rigin)
    {
        if(sizeOfShell > 3)
        {
            lineup = new Rower[sizeOfShell + 1];
            size = sizeOfShell + 1;
        }
        else
        {
            lineup = new Rower[sizeOfShell];
            size = sizeOfShell;
        }
        rig = rigin;
        name = nameIn;

    }

    public int getRig(){
        return rig;
    }
    
    public int getSize()
    {
        return size;
    }

    public int getSizeForWriting()
    {
        if(size > 3)
        {
            return size-1;
        }
        else
        {
            return size;
        }
    }

    public String getName()
    {
        return name;
    }

    public boolean addRower(Rower r, int seatNum)
    {
        if(seatNum > size)
        {
            return false;
        }
        lineup[seatNum-1] = r;
        filledSeats.add(this.getSize() - seatNum -1);
        return true;
    }

    public boolean removeRowerinSeat(int seatNum)
    {
        if(seatNum > size || lineup[seatNum] == null)
        {
            return false;
        }
        lineup[seatNum] = null;
        filledSeats.remove(this.getSize() - seatNum);
        return true;
    }

    public Rower[] getLineup()
    {
        return lineup;
    }
    
    public Rower getRower(int seatNum)
    {
        if(seatNum > size || lineup[seatNum-1] == null)
        {
            return null;
        }
        return lineup[seatNum-1];
    }

    public double getAverageWeight()
    {
        double total = 0;
        int count = 0;
        for(int i = 0; i < size; i ++)
        {
            if(lineup[i] != null)
            {
                total += lineup[i].getWeight();
                count++;
            }
        }
        if(count == 0)
        {
            return 0;
        }
        else
        {
            return total/count;
        }
    }

    public static Boat getBoat(String boatName, ArrayList<Boat> fleet){
        for (Boat b: fleet){
            if(b.getName().equals(boatName)){
                return b;
            }
        }
        return null;
    }


    // //start
    //     // third param for main canvas or not
    //     public void drawBoat(GraphicsContext gc, int seatNum, boolean thumbnail){
        
    //         // gc.clearRect(0, 0, 400, 400);
    //          gc.save();
    //          gc.setStroke(Color.BLACK);
     
    //          if (!thumbnail) {     
    //              gc.setFill(Color.GREEN);
    //              gc.fillRect(0,0,500,500);
    //          } else {
                  
    //  gc.setFill(Color.YELLOW);
     
    //              gc.fillRect(0, 0,   gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
     
    //          }
     
     
    //          int coordx = 0;
    //          int coordy = 0;
    //          //System.out.println("here");
    //          coordx = 45;
    //          coordy = 15;
    //          int translatey = 0;
    //          double scale = 1;
     
    //          if (this.getSize() == 5){
    //              translatey = 150;
    //              scale = .5;
    //              //gc.scale(.5,.5);//elodie
     
    //          }
    //          else if (this.getSize() == 9){
    //              translatey = 250;
    //              scale = .35;
    //              //gc.scale(.35,.35);//elodie
     
    //          }
    //          else if (this.getSize() == 2){
    //              translatey = 150;
    //              scale = .5;
    //              //gc.scale(.5,.5);//elodie
     
    //          }
    //          else if (this.getSize() == 1){
    //              translatey = 150;
    //              scale = .5;
    //              //gc.scale(.5,.5);//elodie
    //          }
     
    //          if (thumbnail) 
    //              gc.scale(scale, scale);
     
    //          gc.translate(0, translatey);
    //          gc.rotate(-45);
    //          System.out.println("rotate");
     
             
    //         // gc.setTransform(coordy, coordy, coordy, coordy, coordx, coordy);
    //          if(this.getSize() == 1){
    //              gc.strokeOval(10, 10, 150, 30); //boat
    //              gc.strokeOval(75, 15.5, 20, 20);
    //          }
     
    //          else if (this.getSize() == 2){ //THIS A LITTLE BUGGY BUT ITS LIKE FINE
    //              gc.strokeOval(10, 10, 150, 30); //boat
    //              gc.strokeOval(55, 15.5, 20, 20);
    //              gc.strokeOval(90, 15.5, 20, 20);
     
    //          }
    //          else if(this.getSize() == 5){
     
    //              gc.strokeOval(10, 10, 200, 30); //boat
    //              gc.fillOval(175, 17.5, 15, 15);
    //          }
    //          else if (this.getSize() == 9){
                  
    //              // coordy = 55;
     
    //              gc.strokeOval(10, coordy-5, 350, 30); //boat
    //              //gc.strokeOval(55, 57.5, 15, 15);
    //              gc.setStroke(Color.BLACK);
    //              gc.setFill(Color.BLACK);
    //              gc.fillOval(coordx + 2.5, coordy +5, 10, 10);
    //            //  coordx += 30;
    //            //  translatey = 250;
    //              //gc.fillOval(57.5, 60, 10, 10); //cox
    //          }
     
     
    //          if(this.getSize() ==5 || this.getSize() ==9){
    //              for(int i = 0; i < this.getSize() - 1; i ++){
    //                  gc.setStroke(Color.BLACK);
    //                  gc.setFill(Color.BLACK);
    //                  if(i%2 == this.getRig()){
    //                      gc.setStroke(PORT);
    //                      gc.setFill(PORT);
    //                      //System.out.println("in if");
    //                  }
    //                  else{
    //                      gc.setStroke(STARBOARD);
    //                      gc.setFill(STARBOARD); //this is my branch
     
    //                  }
    //                  if(i == seatNum){
    //                      gc.fillOval(coordx, coordy, 20, 20);
    //                  }
    //                  else{
    //                      gc.strokeOval(coordx, coordy, 20, 20);
    //                  }
    //             //     coordx += 30;
    //              }
    //          }
     
    //          gc.setFont(new Font(20));
    //          gc.fillText(name, 50, 60);
     
    //          //put back gc 
    //      //    gc.rotate(45);
    //      //    gc.translate(0, -translatey);
    //          System.out.println("rotate");
    //          gc.restore(); //elodie
    //      }
    // //end

    public void drawBoat(GraphicsContext gc, double scale){        
        //GraphicsContext gc = c.getGraphicsContext2D();
        //gc.clearRect(0, 0, 400, 400);
        System.out.println(this.filledSeats);
        gc.save();
        gc.setStroke(Color.BLACK);
        int coordx = 0;
        int coordy = 0;
        //System.out.println("here");
        coordx = 45;
        coordy = 15;
        int translatey = 0;

        if (this.getSize() == 5){
            translatey = 150;
            gc.scale(scale,scale);//elodie

        }
        else if (this.getSize() == 9){
            translatey = 250;
            gc.scale(scale,scale);//elodie


        }
        else if (this.getSize() == 2){
            translatey = 150;
            gc.scale(scale,scale);//elodie


        }
        else if (this.getSize() == 1){
            translatey = 150;
            gc.scale(scale,scale);//elodie


        }
        gc.translate(0, translatey);
        gc.rotate(-45);
        //System.out.println("rotate");

        
       // gc.setTransform(coordy, coordy, coordy, coordy, coordx, coordy);
        if(this.getSize() == 1){
            gc.strokeOval(10, 10, 150, 30); //boat
            if(this.filledSeats.contains(0))
                gc.fillOval(75, 15.5, 20, 20);
            else{
                gc.strokeOval(75, 15.5, 20, 20);
            }
        }

        else if (this.getSize() == 2){ //THIS A LITTLE BUGGY BUT ITS LIKE FINE
            gc.strokeOval(10, 10, 150, 30); //boat
            if(this.filledSeats.contains(0))
                gc.fillOval(55, 15.5, 20, 20);
            if(this.filledSeats.contains(1))
                gc.fillOval(90, 15.5, 20, 20);
                
            gc.strokeOval(55, 15.5, 20, 20);
            gc.strokeOval(90, 15.5, 20, 20);
        }
        else if(this.getSize() == 5){
            gc.strokeOval(10, 10, 200, 30); //boat
            if(this.filledSeats.contains(-1)){
                gc.strokeOval(172.5, 15.5, 19, 19);
            }
            gc.fillOval(175, 17.5, 15, 15);
        }

        else if (this.getSize() == 9){
            // coordy = 55;
            gc.strokeOval(10, coordy-5, 350, 30); //boat
            //gc.strokeOval(55, 57.5, 15, 15);
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.BLACK);
            if(this.filledSeats.contains(-1)){
                gc.strokeOval(coordx, coordy +3, 15, 15);
                gc.fillOval(coordx + 2.5, coordy +5, 10, 10);
            }
            else{
                gc.fillOval(coordx + 2.5, coordy +5, 10, 10);
            }
            coordx += 30;
            translatey = 250;
            //gc.fillOval(57.5, 60, 10, 10); //cox
        }


        if(this.getSize() ==5 || this.getSize() ==9){
            for(int i = 0; i < this.getSize() - 1; i ++){
                //System.out.println("drawing seat");

                gc.setStroke(Color.BLACK);
                gc.setFill(Color.BLACK);
                if(i%2 == this.getRig()){
                    gc.setStroke(PORT);
                    gc.setFill(PORT);
                    //System.out.println("in if");
                }
                else{
                    gc.setStroke(STARBOARD);
                    gc.setFill(STARBOARD); //this is my branch

                }
                if(this.filledSeats.contains(i)){
                    gc.fillOval(coordx, coordy, 20, 20);
                    //System.out.println("filled seat");

                    }
                else{
                    gc.strokeOval(coordx, coordy, 20, 20);
                }
                coordx += 30;
            }
        }
        //put back gc 
        gc.setFont(new Font(20));
        gc.fillText(name, 50, 60);

        gc.rotate(45);
        gc.translate(0, -translatey);
        gc.restore(); //elodie
    }

    @Override
    public String toString()
    {
        return name + ": " + size + ", " + Arrays.toString(lineup); 
    }




}