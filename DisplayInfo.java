/*
CLASS:      DisplayInfo
PURPOSE:    Holds the projected coordinates, distance to the viewing point and light intensity. 
            This will be what we use to display to the screen.
*/

import java.util.ArrayList;

class DisplayInfo implements Comparable<DisplayInfo>
{
    private ArrayList<PCoord> projectedFace;
    private double distance;
    private float intensity;
    
    public DisplayInfo(ArrayList<PCoord> face, double dist, float in)
    {
        projectedFace = face;
        distance = dist;
        intensity = in;
    }
    
    @Override
    public int compareTo(DisplayInfo other)
    {
        //implement compareTo so we can call Collections.sort on an ArrayList of DisplayInfo
        int retValue = 0;
        
        if (this.distance > other.getDistance())
        {
            retValue = -1;
        }
        else if (this.distance < other.getDistance())
        {
            retValue = 1;
        }
        
        return retValue;
    }
    
    //Getters and other helpers------------------------------------------------------------------//
    
    public ArrayList<PCoord> getFace()
    {
        return projectedFace;
    }
    
    public double getDistance()
    {
        return distance;
    }
    
    public float getIntensity()
    {
        return intensity;
    }
}