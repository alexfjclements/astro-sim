/**
 * Computer Modelling, Exercise 3: Particle3D
 *
 * @author S. Macdonald
 * @author A. Clements
 *@version "11/2016"
 *
 */

import java.io.*;
import java.util.*;

public class Particle3D {


    /*
     * Vector properties, mass property and particle label (Bob).
     */

    private double mass;
    private Vector3D position;
    private Vector3D velocity;
    private String bob;

    // Setters and Getters

    //Getters

    /** Get the position of a particle.
     *
     * @return a vector representing the position.
     */
    public Vector3D getPosition() { return position; }

    /** Get the velocity of a particle
     *
     * @return a vector representing velocity.
     */
    public Vector3D getVelocity() { return velocity; }

    /** Get the mass of a particle.
     *
     * @return a double representing the mass.
     */
    public double getMass() { return mass; }

    /** Get particle label.
     *
     * @return a string representing the particle label.
     */
    public String getBob() {return bob; }


    //Setters

    /** Set the position of a particle.
     *
     * @param P a vector representing the position.
     */
    public void setPosition(Vector3D P) { this.position = P; }

    /** Set the velocity of a particle.
     *
     * @param V a vector representing the velocity. 
     */
    public void setVelocity(Vector3D V) { this.velocity = V; }

    /** Set the mass of a particle.
     *
     * @param m a double representing the velocity.
     */
    public void setMass(double m) { this.mass = m; }

    /** Set the label of a particle.
     *
     * @param B a string representing the particle label.
     */
    public void setBob(String B) { this.bob = B; }


    /*
     * Constructors (default and explicit)
     */

    //First default constructor.

    /** Default constructor which sets mass properties to "not a number"
     * and position and velocity properties to a zero vector to indicate 
     * that they are uninitialised.
     */

    public Particle3D() {
	this.setMass(Double.NaN);
	this.setPosition(new Vector3D(0, 0, 0));
	this.setVelocity(new Vector3D(0, 0, 0));
    }

    // Secondly the explicit constructor.

    /** Explicit constructor which constructs a new Particle3D with
     * explicitly given position, velocity, and mass.
     *
     * @param m a double that defines the mass.
     * @param P a vector that defines the position.
     * @param V a vector that defines the velocity.
     */
    public Particle3D(double m, Vector3D P, Vector3D V) {
	this.setMass(m);
	this.setPosition(P);
	this.setVelocity(V);
    }

    /**
     * toString Method
     */
    public String toString() {
	return getBob() + " " + getPosition().getX() + " " + getPosition().getY() + " " + getPosition().getZ(); }

    /**
     * Method to read particle from Scanner object.
     */

    public Particle3D(Scanner scan) throws IOException {

	this.setMass(scan.nextDouble());

	this.setPosition(new Vector3D(scan.nextDouble(), scan.nextDouble(), scan.nextDouble()));

	this.setVelocity(new Vector3D(scan.nextDouble(), scan.nextDouble(), scan.nextDouble()));

    }

    /*
     * Instance Methods
     */

    /**
     * Returns the kinetic energy of a Particle3D, using expression 0.5*m*v^2
     *
     * @return a double that is kinetic energy.
     */

    public double kineticEnergy() { return Vector3D.dot(velocity,velocity)*0.5*mass; }

    /**
     * method to update particle's velocity according to v(t + dt) = v(t) + dt*f(t)/m.
     *
     * @param dt is a double that represents the timestep.
     * @param force is a vector that is the force on the particle.
     */


    public void updateVelocity(double dt, Vector3D force) {

	Vector3D Fdt = Vector3D.vectMultip(dt/mass, new Vector3D(force));
    velocity = Vector3D.add(velocity, Fdt);
    }

    /** 
     * method to update particle's position according to r(t + dt) = r(t) + dt*v(t)
     *
     * @param dt is a double that is the timestep.
     */

    public void updatePosition(double dt) {

	Vector3D Vdt = Vector3D.vectMultip(dt, new Vector3D(velocity));
	position = Vector3D.add(position, Vdt);
    }

    /**
     * method to update particle's position according to r(t + dt) = r(t) + dt*v(t) + (f(t)/2m)*dt^2.
     *
     * @param dt is a double that is the timestep.
     * @param force is a vector that is the current force on the particle.
     */

    public void updatePosition(double dt, Vector3D force) {

	Vector3D Fdt2 = Vector3D.vectMultip((dt*dt)/(2*mass), force);
	Vector3D Vdt = Vector3D.vectMultip(dt, velocity);
	Vector3D VdtFdt2 = Vector3D.add(Vdt, Fdt2);
	position = Vector3D.add(position, VdtFdt2);
    }
    /** method to calculate gravitational force on a particle due to another particle, returning a Vector3D.
     */

    public static Vector3D gravForce(Particle3D N, Particle3D M, double G){
	Vector3D sep = relativeSeparation(M.getPosition(), N.getPosition());
	double Magnetude = ((G*N.getMass()*M.getMass()/ (sep.mag()*sep.mag()*sep.mag())));
	Vector3D Force = Vector3D.vectMultip(Magnetude, sep);
	return Force;
    }

    /**
     * method to calculate the relative separation of two particles
     *
     * @param D and E are vectors describing the positions of two particles.
     */

    public static Vector3D relativeSeparation(Vector3D D, Vector3D E) {

	return Vector3D.sub(D,E);
    }

    public static Vector3D[] forceArrcalc(Particle3D[] bodyData,double G){

	Vector3D[] bod1For = new Vector3D[bodyData.length];

	Arrays.fill(bod1For,new Vector3D());

	for (int i=0;i<bodyData.length;i++){

	    for (int j=0;j<bodyData.length;j++){
		// Call force on bodies from Particle3D

		if (i!=j){
		    Vector3D bod1Inc = Particle3D.gravForce(bodyData[i],bodyData[j],G);
		    bod1For[i] = Vector3D.add(bod1For[i],bod1Inc);
		}
	    }
	}
	return bod1For;
    }
}





