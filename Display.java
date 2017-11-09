/*
CLASS:      Display
PURPOSE:    Responsible for displaying the window and drawing the polyhedra. Also responsible
            for handling controls to change the pictures. Holds information about the state of the 
            picture, like colours and angles. 
*/

import java.util.*;
import java.util.ArrayList;
import java.lang.Math;
import javax.swing.*; //for JFrame window
import java.awt.event.*; //for KeyListener
import javax.media.opengl.*; //for GLProfile, GLCapbilities
import javax.media.opengl.awt.*; //for GLCanvas
import javax.media.opengl.glu.GLU; //for GLU

class Display implements GLEventListener, KeyListener
{
    //Polyhedra and projection info
    private HCoord projScreen;
    private HCoord viewPoint;
    private Polyhedra drawSubject;
    private double projDistance;
    
    //Light source
    private HCoord lightSource;
    
    //Size of drawing area
    private int screenSize;
    
    //Colour values
	private float red;
	private float green;
	private float blue;
    
    //Angles for rotation
    private double radX;
    private double radY;
    private double radZ;
    
    //Interval to change angles by
    private double angle;
    
    //Window and drawing areas
	private JFrame frame;
	private GLProfile profile;
	private GLCapabilities capabilities;
	private GLCanvas canvas;
	
	public Display(Polyhedra poly)
	{	
        screenSize = 700;
        
		red = 0.1f;
        green = 0.7f;
        blue = 0.75f;

        radX = 0.0;
        radY = 0.0;
        radZ = 0.0;
        angle = 2.0 * Math.PI / 60.0;
        
        drawSubject = poly;
        projDistance = -40.0;
        projScreen = new HCoord(1.0, 0.0, 0.0, projDistance);
        viewPoint = new HCoord(100.0, 0.0, 0.0, 1.0);
        
        lightSource = new HCoord(100.0, 0.0, 0.0, 1.0);       
         
		//Canvas to draw in...
		
		//Note: Without including the "natives" jar files, this was giving errors
        
        //what version of OpenGL we're using
		profile = GLProfile.get(GLProfile.GL2);
        
        //capabilities of the OpenGL version we use
		capabilities = new GLCapabilities(profile); 
        
        //new canvas with those capabilities
		canvas = new GLCanvas(capabilities); 
		
		//We need to add the implementing class of GLEventListener, KeyListener 
		//(a.k.a this class) to the canvas so we can draw
		canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
		canvas.setSize(screenSize, screenSize);
		
		//Window to put the canvas in...
		
		//Create a window using JFrame
		frame = new JFrame(drawSubject.getName());
		
		//Terminates the program when window is closed
		frame.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.out.println(drawSubject.getName() + " window closed.");
				System.exit(0);
			}
		});
		
		//Add the drawing area to the JFrame and display it
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
	}
	
	//GLEventListener Methods--------------------------------------------------------------------//
	
	@Override
	public void init(GLAutoDrawable drawable) 
	{
        //Just setup of drawing area, specifies the coordinate system and background colour
        
		//We get the GL2, which provides access to the OpenGL API
		final GL2 gl = drawable.getGL().getGL2();
        
        //Normally, OpenGl coordinates go from -1.0 to +1.0, this specifies a new coordinate system
        //for the 700 x 700 window, meaning the new coordinates will go from -350 to +350.
        //GLU is the OpenGl Utility Library, we only need it for specifying this.
        int limit = screenSize/2;
        final GLU glu = new GLU();
		glu.gluOrtho2D(-limit, limit, limit, -limit);
        
		//Specifies the background color displayed when you call glClear, in this case, black
		gl.glClearColor(0f, 0f, 0f, 1f);
	}

	@Override
	public void display(GLAutoDrawable drawable) 
	{	
		final GL2 gl = drawable.getGL().getGL2();
		
		//glClearColor only applies when this is called
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		//Drawing code goes here	
		painter(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) 
	{
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) 
	{
	}

    //Drawing Methods----------------------------------------------------------------------------//
    
    //Painter's Algorithm
    public void painter(GL2 gl)
    {
        ArrayList<DisplayInfo> displayThis = new ArrayList<DisplayInfo>();
        ArrayList<Face> polyFaces = drawSubject.getFaces();
        int numFaces = polyFaces.size();

        for (int i = 0; i < numFaces; i++)
        {
            Face currentFace = polyFaces.get(i);
            
            //rotate face
            /*
            NTS Nov 9, 2017: I somewhat remember that there was a reason why
            you did the rotations in Z,Y,X specifically. 

            ...on the other hand, it may also have just been the convention. Review
            the math from the class notes.
            */
            Face rotatedFace = currentFace.rotateZ(radZ);
            rotatedFace = rotatedFace.rotateY(radY);
            rotatedFace = rotatedFace.rotateX(radX);
            
            
            //find centre
            HCoord centre = rotatedFace.findCentre();
            
            //find unit vector normal to surface
            HCoord normalUnit = rotatedFace.findSurfaceNormal();
            normalUnit = normalUnit.findUnit();
            
            //find distance
            double distance = centre.findDistance(lightSource);
            
            //find light intensity
            float intensity = (float)((normalUnit.dot(lightSource.subtract(projScreen)))/distance);
            
            //project face
            ArrayList<PCoord> projectedFace = rotatedFace.project(projScreen, viewPoint);
            
            //save face
            displayThis.add(new DisplayInfo(projectedFace, distance, intensity));
        }
        
        Collections.sort(displayThis);
        
        //Draw from furthest to closest
        for (int j = 0; j < numFaces; j++)
        {
            DisplayInfo currFace = displayThis.get(j);
            float currIntensity = currFace.getIntensity();
            ArrayList<PCoord> faceCoords = currFace.getFace();
            
            gl.glBegin(GL2.GL_POLYGON);
            
            if (currIntensity > 0)
            {
                //Colored
                gl.glColor3f(red * currIntensity, green * currIntensity, blue * currIntensity);
            }
            else
            {
                //Black
                gl.glColor3f(0f, 0f, 0f);
            }
            
            for (int k = 0; k < faceCoords.size(); k++)
            {
                PCoord currCoord = faceCoords.get(k);
                
                float x = (float)currCoord.getX();
                float y = (float)currCoord.getY();
                
                //Remember, regularly OpenGl coordinates go from -1.0 to +1.0, this converts
                //it to int values to be actually displayed
                int displayX = (int)(screenSize/2 * x);
                int displayY = (int)(screenSize/2 * y);
                
                gl.glVertex2i(displayX, displayY);
            }
            
            gl.glEnd();
        }
    }

	//Helper Methods-----------------------------------------------------------------------------//
    
    public void further(double num)
    {
        projScreen.updateW(num);
    }
    
    public void closer(double num)
    {
        projScreen.updateW(num);
    }
    
    public void updateLightSourceX(double num)
    {
        lightSource.updateX(num);
    }
    
    public void updateLightSourceY(double num)
    {
        lightSource.updateY(num);
    }
    
    public void updateLightSourceZ(double num)
    {
        lightSource.updateZ(num);
    }
    
    //KeyListener Methods------------------------------------------------------------------------//
    
    @Override
    public void keyTyped(KeyEvent e)
    {
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        //X,Y,Z control rotations
        //R,G,B control colours
        //UP, DOWN control zoom
        //I,J,K control light source coordinates
        
        //For some reason, key presses don't work until you click into the screen first
        int key = e.getKeyCode();
        
        switch (key)
        {
            case KeyEvent.VK_X :
                if (e.isShiftDown())
                {
                    radX -= angle;
                    canvas.display();
                }
                else
                {
                    radX += angle;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_Y :
                if (e.isShiftDown())
                {
                    radY -= angle;
                    canvas.display();
                }
                else
                {
                    radY += angle;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_Z :
                if (e.isShiftDown())
                {
                    radZ -= angle;
                    canvas.display();
                }
                else
                {
                    radZ += angle;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_R :
                if (e.isShiftDown())
                {
                    red -= 0.05f;
                    canvas.display();
                }
                else
                {
                    red += 0.05f;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_G :
                if (e.isShiftDown())
                {
                    green -= 0.05f;
                    canvas.display();
                }
                else
                {
                    green += 0.05f;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_B :
                if (e.isShiftDown())
                {
                    blue -= 0.05f;
                    canvas.display();
                }
                else
                {
                    blue += 0.05f;
                    canvas.display();
                }
                break;
            case KeyEvent.VK_UP :
                closer(0.5);
                canvas.display();
                break;
            case KeyEvent.VK_DOWN :
                further(-0.5);
                canvas.display();
                break;
            case KeyEvent.VK_I :
                if (e.isShiftDown())
                {
                    updateLightSourceX(-1.0);
                    canvas.display();
                }
                else
                {
                    updateLightSourceX(1.0);
                    canvas.display();
                }
                break;
            case KeyEvent.VK_J :
                if (e.isShiftDown())
                {
                    updateLightSourceY(-1.0);
                    canvas.display();
                }
                else
                {
                    updateLightSourceY(1.0);
                    canvas.display();
                }
                break;
            case KeyEvent.VK_K :
                //For some reason, z is upside down so we give 1.0 to move down and -1.0 to move up
                if (e.isShiftDown())
                {
                    updateLightSourceZ(1.0);
                    canvas.display();
                }
                else
                {
                    updateLightSourceZ(-1.0);
                    canvas.display();
                }
                break;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
    }
}