/*
CLASS:      Edge
PURPOSE:    Represents an edge of a polyhedron. Used by the polyhedra class to construct each face
            and check already visited edges.
*/

import java.util.Objects;

class Edge
{
    private int startAt;
    private int lookingAt;
    
    public Edge(int u, int v)
    {
        startAt = u;
        lookingAt = v;
    }
    
    //Overridden from Objects class--------------------------------------------------------------//
    
    //We override equals() so that the HashSet class can check for conatins() with Edge objects
    @Override
    public boolean equals(Object o)
    {
        boolean returnValue = false;
        
        if (o instanceof Edge)
        {
            returnValue = (((Edge)o).getStart() == startAt) && (((Edge)o).getEnd() == lookingAt);
        }
        
        return returnValue;
    }
    
    //Whenever you override equals(), you must also override hashCode()
    @Override
    public int hashCode()
    {
        return Objects.hash(startAt, lookingAt);
    }
    
    //Getters
    
    public int getStart()
    {
        return startAt;
    }
    
    public int getEnd()
    {
        return lookingAt;
    }
    
    public String toString()
    {
        return "{" + startAt + "," + lookingAt + "}";
    }
}