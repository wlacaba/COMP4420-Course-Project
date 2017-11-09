PROJECT INFORMATION--------------------------------------------------------------------------------

Course:         COMP4420
Assignment:     Course Project
Author:         Warren Lacaba
Student Number: 7713922
Source Files:   WLacabaCourseProject.java, Polyhedra.java, Edge.java, Face.java, HCoord.java, 
		PCoord.java, DisplayInfo.java, Display.java

SYSTEM SPECIFICATIONS------------------------------------------------------------------------------

The following are the specifications of the computer I used to write the program:

Computer Model: ASUS G46VW
CPU: Intel Core i7-3630QM @ 2.4 GHz
Memory: 8 GB
OS: Windows 10, 64-bit

DEVELOPMENT TOOLS USED-----------------------------------------------------------------------------

This project uses JOGL (Java Binding for OpenGL) for the drawing component. Appropriate jar files
have been included in the jar folder. Note that the following are required for any platform:

gluegen-rt.jar
jogl-all.jar

The other files in the jar folder are the native jar files for specific systems. As I did not know
the specific system this project will be graded on, I have included all of them. Information on 
this can be found at http://jogamp.org/wiki/index.php/Downloading_and_installing_JOGL.

In particular, the platform I developed this on required the follow jar files:
    
gluegen-rt.jar
jogl-all.jar
gluegen-rt-natives-windows-amd64.jar
jogl-all-natives-windows-amd64.jar

With the included jar files, this should work on these operating systems:

Windows 64/32 bit
Linux 64/32 bit
MacOSX 64/32 bit

***REMINDER: The jar folder should be in the classpath.***

CONTORLS-------------------------------------------------------------------------------------------

***NOTE: Key controls don't respond unless you first click into the window, for some reason.***

R               : Increase red colour value
G               : Increase green colour value
B               : Increase blue colour value
SHIFT + R       : Decrease red colour value
SHIFT + G       : Decrease green colour value
SHIFT + B       : Decrease blue colour value

X               : Increase x angle
Y               : Increase y angle
Z               : Increase z angle
SHIFT + X       : Decrease x angle
SHIFT + Y       : Decrease y angle
SHIFT + Z       : Decrease z angle

I               : Move light source closer
J               : Move light source right
K               : Move light source up
SHIFT + I       : Move light source further
SHIFT + J       : Move light source left
SHIFT + K       : Move light source down

UP              : Zoom in
DOWN            : Zoom out

---------------------------------------------------------------------------------------------------

