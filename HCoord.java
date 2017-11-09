/*
CLASS:      HCoord
PURPOSE:    Class to represent a homogenous coordinate. Also responsible for operations on
            homogenous coordinates, like finding distances, dot and cross products, etc.
*/

import java.lang.Math;

class HCoord
{
    private double x;
    private double y;
    private double z;
    private double w;
    
    public HCoord(double x, double y, double z, double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public double findMagnitude()
    {
        double term1 = Math.pow(this.x, 2.0);
        double term2 = Math.pow(this.y, 2.0);
        double term3 = Math.pow(this.z, 2.0);
        double magnitude = Math.sqrt(term1 + term2 + term3);
        
        return magnitude;
    }
    
    //Distance formula
    public double findDistance(HCoord coord)
    {
        double term1 = Math.pow((this.x - coord.getX()), 2.0);
        double term2 = Math.pow((this.y - coord.getY()), 2.0);
        double term3 = Math.pow((this.z - coord.getZ()), 2.0);
        double distance = Math.sqrt(term1 + term2 + term3);
        
        return distance;
    }
    
    //Dot product
    public double dot(HCoord coord)
    {
        double sum = 0.0;
        sum += this.x * coord.getX();
        sum += this.y * coord.getY();
        sum += this.z * coord.getZ();
        sum += this.w * coord.getW();
        
        return sum;
    }
    
    //Cross product
    public HCoord cross(HCoord coord)
    {
        double x = this.y * coord.getZ() - this.z * coord.getY();
        double y = -(this.x * coord.getZ() - this.z * coord.getX());
        double z = this.x * coord.getY() - this.y * coord.getX();
        
        return new HCoord(x, y, z, 1.0);
    }
    
    //divides the coordinates by the magnitude
    public HCoord findUnit()
    {
        double mag = this.findMagnitude();
        double x = this.x/mag;
        double y = this.y/mag;
        double z = this.z/mag;
        
        return new HCoord(x, y, z, 1.0);
    }
    
    //Multiply a constant
    public HCoord mult(double value)
    {
        return new HCoord(this.x * value, this.y * value, this.z * value, this.w * value);   
    }
    
    //Subtract two coordinates
    public HCoord subtract(HCoord coord)
    {
        double x = this.x - coord.getX();
        double y = this.y - coord.getY();
        double z = this.z - coord.getZ();
        double w = this.w - coord.getW();
        
        return new HCoord(x, y, z, w);
    }
    
    //Rotations, math taken from class notes
    
    public HCoord rotateZ(double rad)
    {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
                          
        double newX = (cos * this.x) + (-sin * this.y) + 0.0;
        double newY = (sin * this.x) + (cos * this.y) + 0.0;
        
        return new HCoord(newX, newY, this.z, this.w);
    }
    
    public HCoord rotateY(double rad)
    {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double newX = (cos * this.x) + 0.0 + (-sin * this.z);
        double newZ = (sin * this.x) + 0.0 + (cos * this.z);
        
        return new HCoord(newX, this.y, newZ, this.w);
    }
    
    public HCoord rotateX(double rad)
    {
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);

        double newY = 0.0 + (cos * this.y) + (-sin * this.z);
        double newZ = 0.0 + (sin * this.y) + (cos * this.z);
        
        return new HCoord(this.x, newY, newZ, this.w);
    }
    
    //Getters and other helpers------------------------------------------------------------------//
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public double getZ()
    {
        return z;
    }
    
    public double getW()
    {
        return w;
    }
    
    public void updateX(double add)
    {
        this.x = this.x + add;
    }
    
    public void updateY(double add)
    {
        this.y = this.y + add;
    }
    
    public void updateZ(double add)
    {
        this.z = this.z + add;
    }
    
    public void updateW(double add)
    {
        this.w = this.w + add;
    }
    
    public String toString()
    {
        String retValue = "{";
        retValue += this.x + " ";
        retValue += this.y + " ";
        retValue += this.z + " ";
        retValue += this.w + "}";
        return retValue;
    }
}