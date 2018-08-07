/**
 * Computer Modeling project: Astronomical Simulation
 *
 * The purpose of this program is to generalise the code from Ex.3, so that it can model the motion not
 * of a single body under a central force, but of two or more bodies, all of which are free to move.
 * It should also be able to calculate the perhelion and aperihelion of each body's orbit with respect
 * to a central body, as well as the orbital period and eccentricity. Close to 1 million timesteps will be
 * required to run a full simulation, due to the larger orbits in the provided data files. Gravforce is set
 * up to use the first body entered to calculate the orbital characteristics- in the case of the supplied
 * data this is the Sun. All other bodies can be in any order; the program will proceed regardless.
 * Labels in the paramaterinput.dat file must be in the same order as the orbital data lines in the
 * bodyinput.dat file. The system reads, from the paramaterinput.dat file: number of steps, timestep,
 * gravitational constant, number of bodies, and the body names. From the bodyinput file it reads
 * mass, position (x,y,z), and velocity (x,y,z).
 * 
 * @author A.Clements
 * @author S.Macdonald
 * @version 2/2/16
 */

// IO package for file writing
import java.io.*;
import java.util.*;

public class gravforce {

    /*
     *  As doing file IO, need to throw an exception
     */
    public static void main (String[] argv) throws IOException{

	if (argv.length !=4) {

	    // Prompt to enter filename if none entered.

	    System.out.printf("ERROR: Enter output file names using code:\n");
	    System.out.printf("  java gravforce <Body input filename> <Trajectory output filename> <Paramater filename> <Body properties output filename>\n");

	    System.exit(-4);

	}

	// Open the output files.

	String outT = argv[1];
	PrintWriter TrajectoryOut = new PrintWriter(new FileWriter(outT+".xyz"));
	
	String outP = argv[3];
	PrintWriter PropertiesOut = new PrintWriter(new FileWriter(outP + ".dat"));

	// Read in gravitational constant, number of timesteps, step size, number of particles, and particle names from specified file.
	String param = argv[2];
	Scanner paramScan = new Scanner(new BufferedReader(new FileReader(param + ".dat")));

	// Number of timesteps:
	int numStep = paramScan.nextInt();

	// Time interval dt:
	double dt = paramScan.nextDouble();

	// Initial time
	double t = 0.0;

	// Gravitational Constant
	double G = paramScan.nextDouble();

	// Number of particles
	int numPart = paramScan.nextInt();
	paramScan.useDelimiter("\\n");

	// Particle/Body names
	String[] bodyName = new String[numPart];
	for (int i=0;i<numPart; i++){
	    bodyName[i] = paramScan.next();
	}

	// Set up scanner to aquire data on astronomical bodies.
	String bodies = argv[0];
	Scanner bodyScan = new Scanner(new BufferedReader(new FileReader(bodies + ".dat")));

	// Create array
	Particle3D[] bodyData = new Particle3D[numPart];

	// Set double for new velocity calculation
	double k = 0.5;

	// Set up loop to populate Particle3D array with the required number of particles:
	for (int i=0;i<numPart; i++){

	    // Populate array
	    bodyData[i] = new Particle3D(bodyScan);
	}
	
	// Initial lines: number of points to plot, and point number
	TrajectoryOut.printf("%d\n", numPart);
	TrajectoryOut.printf("Point = 1\n");

	// For loop to allow for the full number of points to be plotted.
	for (int i=0;i<numPart; i++){
	    TrajectoryOut.printf(bodyName[i] + " %10.5f %10.5f %10.5f \n", bodyData[i].getPosition().getX(), bodyData[i].getPosition().getY(), bodyData[i].getPosition().getZ());
	}

	// Total mass of the system
	double totalMass = 0.0;
	for (int i=0;i<numPart;i++){
	    totalMass = totalMass + bodyData[i].getMass();
	}

	// Initialise arrays to hold perihelion, aphelion, eccentricity; and set to initial distances
	double[] perihelion = new double[numPart];
	double[] aphelion = new double[numPart];
	Vector3D[] vectorSeparation = new Vector3D[numPart];
	double[] eccentricity = new double[numPart];
	for (int i=0;i<numPart;i++){
	    vectorSeparation[i] = Particle3D.relativeSeparation(bodyData[0].getPosition(), bodyData[i].getPosition());
	    perihelion[i] = vectorSeparation[i].mag();
	    aphelion[i] = vectorSeparation[i].mag();
	}

	// Initialise array for orbital period
	double[] orbitalPeriod = new double[numPart];

	// Call initial force using Particle3D
	Vector3D[] Force = Particle3D.forceArrcalc(bodyData,G);

	// Start Verlet loop

	for (int i=0;i<numStep;i++){

	    // Update position from force
	    for (int j=0;j<bodyData.length;j++){
		bodyData[j].updatePosition(dt,Force[j]);
	    }

	    // Update vector seperation
	    for (int j=0;j<numPart;j++){
		vectorSeparation[j] =  Particle3D.relativeSeparation(bodyData[0].getPosition(), bodyData[j].getPosition());
	    }
	    // Check magnitude for perihelion/aphelion
	    for (int j=0;j<numPart;j++){
		if (perihelion[j]>vectorSeparation[j].mag()){
		    perihelion[j] = vectorSeparation[j].mag();
		}
		if (aphelion[j]<vectorSeparation[j].mag()){
		    aphelion[j] = vectorSeparation[j].mag();
		}
	    }

	    // Output new datapoint
	    TrajectoryOut.printf("%d\n", numPart);

	    // Define new integer 'b', as initial position datapoint already plotted so point must = i+2 to give an initial point = 2
	    int b = i+2;
	    TrajectoryOut.printf("Point = %d\n", b);

	    // For loop to print out the required particle data as above.
	    for (int x=0;x<numPart; x++){
		TrajectoryOut.printf(bodyName[x] + " %10.5f %10.5f %10.5f\n", bodyData[x].getPosition().getX(), bodyData[x].getPosition().getY(), bodyData[x].getPosition().getZ());
	    }

	    // Update force using new position
	    Vector3D[] newForce = Particle3D.forceArrcalc(bodyData,G);

	    // Sum of new and old forces for velocity update
	    Vector3D[] fSum = new Vector3D[numPart];

	    // Velocity update
	    for (int j=0;j<bodyData.length;j++){
		fSum[j] = Vector3D.add(Force[j],newForce[j]);
		Force[j] = Vector3D.vectMultip(k,fSum[j]);
		bodyData[j].updateVelocity(dt,Force[j]);
	    }	    
		
	    // New force to force
	    for (int j=0;j<bodyData.length;j++){
		Force[j] = newForce[j];
	    }
	    t = t + dt;
    }


    // Orbital period and eccentricities

    for (int i=0;i<numPart;i++){
	eccentricity[i] = (aphelion[i]-perihelion[i])/(aphelion[i]+perihelion[i]);
	double semiMajor = (aphelion[i] + perihelion[i]) / 2.0;
	orbitalPeriod[i] = Math.sqrt((4.0*Math.PI*Math.PI*Math.pow(semiMajor,3))/(G*(bodyData[0].getMass()+bodyData[i].getMass())));
    }
	// Output orbital period and perihelion/aphelion to file
	for (int j=0;j<bodyData.length;j++){
	    PropertiesOut.printf("Orbital period of " + bodyName[j] + " is " + orbitalPeriod[j] + " Earth years.\n");
	    PropertiesOut.println("Orbital eccentricity of " + bodyName[j] + " is " + eccentricity[j]);
	    PropertiesOut.printf("Perihelion of " + bodyName[j] + " is " + perihelion[j] + " AU.\n");
	    PropertiesOut.printf("Aphelion of " + bodyName[j] + " is " + aphelion[j] + " AU.\n" + "\n");
	}


		
	// Close output files
	TrajectoryOut.close();
	PropertiesOut.close();
	System.exit(0); 
   }
}

