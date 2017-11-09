/*
CLASS:      Face
PURPOSE:    Represents a face of a polyhedra. Holds original coordinates from given file. Performs
            operations on a face like rotation, projection, finding centre.
*/

import java.util.ArrayList;

class Face
{
    private ArrayList<HCoord> coordinates;
    
    public Face()
    {
        coordinates = new ArrayList<HCoord>();    
    }
    
    //Takes the average of x,y,z values
    public HCoord findCentre()
    {
        int size = coordinates.size();
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        
        for (int i = 0; i < size; i++)
        {
            HCoord current = coordinates.get(i);
            x += current.getX();
            y += current.getY();
            z += current.getZ();
        }
        
        x /= size;
        y /= size;
        z /= size;
        
        HCoord centre = new HCoord(x, y, z, 1.0);
        
        return centre;
    }
    
    //Takes first two edges in face, calculates cross product to find a normal vector
    public HCoord findSurfaceNormal()
    {
        HCoord vector1 = (coordinates.get(1)).subtract(coordinates.get(0));
        HCoord vector2 = (coordinates.get(2)).subtract(coordinates.get(1));
        
        return vector2.cross(vector1);
    }
    
    public Face rotateX(double radX)
    {
        Face rotated = new Face();
        
        for (int i = 0; i < coordinates.size(); i++)
        {
            HCoord newCoord = coordinates.get(i).rotateX(radX);
            rotated.addCoordinate(newCoord);
        }
        
        return rotated;
    }
    
    public Face rotateY(double radY)
    {
        Face rotated = new Face();
        
        for (int i = 0; i < coordinates.size(); i++)
        {
            HCoord newCoord = coordinates.get(i).rotateY(radY);
            rotated.addCoordinate(newCoord);
        }
        
        return rotated;
    }
    
    public Face rotateZ(double radZ)
    {
        Face rotated = new Face();
        
        for (int i = 0; i < coordinates.size(); i++)
        {
            HCoord newCoord = coordinates.get(i).rotateZ(radZ);
            rotated.addCoordinate(newCoord);
        }
        
        return rotated;
    }
    
    //Projects each point of the face
    public ArrayList<PCoord> project(HCoord screen, HCoord view)
    {
        ArrayList<PCoord> projected = new ArrayList<PCoord>();
        
        for (int i = 0; i < coordinates.size(); i++)
        {
            PCoord projectedPoint = projectPoint(coordinates.get(i), screen, view);
            projected.add(projectedPoint);
        }
        
        return projected;
    }
    
    //Does the math to project a point onto screen, from class notess
    public PCoord projectPoint(HCoord point, HCoord screen, HCoord view)
    {
        double numerator = point.dot(screen);
        double denominator = view.dot(screen);
        HCoord term2 = view.mult((numerator/denominator));
        HCoord result = point.subtract(term2);
        
        //y, z will be used to display
        result = result.mult(1.0/result.getW()); 
        
        return new PCoord(result.getY(), result.getZ());
    }

    //Getters and other helpers------------------------------------------------------------------//

    public void addCoordinate(HCoord coord)
    {
        coordinates.add(coord);
    }
    
    public ArrayList<HCoord> getCoordinates()
    {
        return coordinates;
    }
    
    public String toString()
    {
        String retValue = "";
        for (int i = 0; i < coordinates.size(); i++)
        {
            retValue += coordinates.get(i) + " ";
        }
        return retValue;
    }
}