public class WLacabaCourseProject
{
	public static void main(String[] args)
	{
        
                int index = 0;

                String shapes[] = 
                {
                "Cube",
                "Tetrahedron",
                "Octahedron",
                "Dodecahedron",
                "Golfball",
                "StellatedDodecahedron",
                "StellatedOctahedron"
                };
                
                try
                {
                        index = Integer.parseInt(args[0]);
                }
                catch (NumberFormatException e)
                {
                        System.err.println("Must enter an integer between 0 and 6.");
                        System.exit(1);
                }
                
                String toDraw = shapes[index] + ".txt";

                Polyhedra poly = new Polyhedra(toDraw);
                poly.constructPolyhedra();
                Display newDrawing = new Display(poly);
	}
}