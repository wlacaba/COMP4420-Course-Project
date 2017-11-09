/*
CLASS:      PCoordinate
PURPOSE:    Represents the projected coordinate after the homogenous coorindate. Will be held in
            a DisplayInfo object.
*/

class PCoord 
{
    private double x;
    private double y;
    
    public PCoord(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    
    //Getters and other helpers------------------------------------------------------------------//
    
    public double getX()
    {
        return this.x;
    }
    
    public double getY()
    {
        return this.y;
    }
    
    public String toString()
    {
        return "{" + this.x + " " + this.y + "}";
    }
}