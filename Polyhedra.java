/*
CLASS:      Polyhedra
PURPOSE:    Represents a polyhedra. Responsible for reading in the data files, and constructing
            the polyhedra. Holds the rotation system, original coordinates from the data file, and
            faces. 
*/

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ListIterator;

class Polyhedra
{
    private int numVerts;
    private String polyName;
    private ArrayList<LinkedList<Integer>> rotationSystem;
    private ArrayList<HCoord> coordinates;
    private ArrayList<Face> faces;
    private HashSet<Edge> alreadyVisited;
    
	public Polyhedra(String filename)
	{
        rotationSystem = new ArrayList<LinkedList<Integer>>();
        faces = new ArrayList<Face>();
        coordinates = new ArrayList<HCoord>();
        alreadyVisited = new HashSet<Edge>();
        
        try
        {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            
            //first line always the name
            String line = file.readLine(); 
            polyName = line;
            
            //second line always the number of vertices
            line = file.readLine(); 
            numVerts = Integer.parseInt(line);
            
            //next few lines are the rotation list
            int count = 0;
            
            while (count < numVerts)
            {
                line = file.readLine();
                String[] tokens = line.split("\\s+"); //whitespace regex
                LinkedList<Integer> currRotation = new LinkedList<Integer>();
                
                for (int i = 1; i < tokens.length; i++)
                {
                    int vert = Integer.parseInt(tokens[i]);
                    currRotation.add(vert);
                }

                rotationSystem.add(currRotation);
                count++;
            }
            
            //next line is a 0
            line = file.readLine(); 
            
            //last lines are coordinates
            line = file.readLine();
            int count2 = 0;

            while (count2 < numVerts)
            {
                //I think empty spaces at beginning and ends of these lines are causing
                //empty string errors when trying to use parseDouble, so use trim()
                line = line.trim();
                String[] tokens = line.split("\\s+");
                
                double x = Double.parseDouble(tokens[0]);
                double y = Double.parseDouble(tokens[1]);
                double z = Double.parseDouble(tokens[2]);

                HCoord newCoord = new HCoord(x, y, z, 1.0);
                coordinates.add(newCoord);
                
                line = file.readLine();
                count2++;
            }
            
            file.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException occured.");
        }
	}
    
    //Construct face algorithm from class notes
    public void constructFace(int startVertex, int lookingAt)
    {
        Face newFace = new Face();
        //Remember: Vertex 1 is stored in ArrayList[0], and so on.
        LinkedList<Integer> currentRotation = rotationSystem.get(startVertex - 1);
        
        int x = startVertex;
        int y = lookingAt;
        
        do
        {
            //System.out.println("Start: " + x + "Looking At: " + y);
            Edge newEdge = new Edge(x,y);
            alreadyVisited.add(newEdge);
            newFace.addCoordinate(coordinates.get(x - 1));
            
            //find x in the rotation of y
            currentRotation = rotationSystem.get(y - 1); 
            int locationOfX = currentRotation.indexOf(x);
            
            //z = vertex before x
            int z = 0;
            
            if (locationOfX - 1 == -1)
            {
                z = currentRotation.getLast(); //locationOfX is 0 so it goes around back to the end
            }
            else
            {
                z = currentRotation.get(locationOfX - 1);
            }
            
            //update
            x = y;
            y = z;

        } while(x != startVertex);
        
        faces.add(newFace);
    }
    
    //We construct all faces by visiting every unvisited edge
    public void constructPolyhedra()
    {
        int numFaces = 0;
        
        for (int i = 0; i < rotationSystem.size(); i++)
        {
            ListIterator<Integer> currRotation = rotationSystem.get(i).listIterator(0);
            int startAt = i + 1; //Vertices in the text file are numbered 1 - n, not 0 - n
            while (currRotation.hasNext())
            {
                int lookingAt = currRotation.next();
                
                if (!alreadyVisited.contains(new Edge(startAt, lookingAt)))
                {
                    constructFace(startAt, lookingAt);
                    numFaces++;
                }
            }
        }
        
        //clear after finished, in case we need to reconstruct
        alreadyVisited.clear();
    }
    
    //Getters and other helpers------------------------------------------------------------------//
    
    public ArrayList<Face> getFaces()
    {
        return faces;
    }
    
    public String getName()
    {
        return polyName;
    }
    
    public String toString()
    {
        String retValue = "\nPolyhedra: " + polyName;
        retValue += "\nNumber of Faces: " + faces.size();
        return retValue;
    }
}