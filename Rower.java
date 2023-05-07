

public class Rower {
    private String name;
    private double height;
    private double weight;
    private String side;
    private String ergScore;
    private int classYear;

    public static final String[] SIDE = {"Port", "Starboard", "Both"};
    public Rower(String nameIn, String sideIn, String ergScoreIn, Double weightIn)
    {
        name = nameIn;
        ergScore = ergScoreIn;
        weight = weightIn;
        side = sideIn;
    }

    public Rower(String nameIn, int classYearIn)
    {
        name = nameIn;
        classYear = classYearIn;
        side = "Coxswain";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSide(String side)
    {
        this.side = side;
    }

    public void setClassYear(int classYearIn)
    {
        classYear = classYearIn;
    }

    

    public int getClassYear()
    {
        return classYear;
    }

    public void changeSide(String newSide)
    {
        side = newSide;
    }

    public void changeWeight(double newWeight)
    {
        weight = newWeight;
    }

    public void setErgScore(String newErgScore)
    {
        ergScore = newErgScore;
    }

    public String getErgScore()
    {
        return ergScore;
    }

    public String getName()
    {
        return name;
    }

    public double getHeight()
    {
        return height;
    }

    public double getWeight()
    {
        return weight;
    }

    public String getSide()
    {
        return side;
    }
    
    @Override
    public String toString()
    {
        return name + ": " + side + ", " + height + ", " + weight; 
    }




}