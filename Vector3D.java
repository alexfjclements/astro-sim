/**
 * @author A. Clements
 * @author S. Macdonald
 * @version "10/2015"
 *
 */

public class Vector3D {

    /*
     * Properties
     */

    private double xx;
    private double yy;
    private double zz;

    /*
     *Constructors
     */

    /** Default constructor. Constructs a new vector, with uninitialised 
     * elements (x,y,z).
     */

    public Vector3D () {
	// Set to 'not a number' to indicate uninitialised.
	this.setVector3D(0 ,0 ,0);
    }

    /** Copy constructor. Constructs a new vector by copying the elements
     * of another vector.
     *
     * @param original vector to be copied.
     */

    public Vector3D (Vector3D original) {
	this.setVector3D(original.getX(), original.getY(), original.getZ());
    }

    /** Explicit constructor. Constructs a new vector from explicitly given
     * elements (x,y,z).
     *
     * @param x a double giving the x-coordinate.
     * @param y a double giving the y-coordinate.
     * @param z a double giving the z-coordinate.
     */ 

    public Vector3D(double x, double y, double z) {
	this.setVector3D(x,y,z);
    }

    /*
     *Setters and Getters
     */

    /** Convinient set method to set vector elements (x,y,z).
     *
     * @param x a double to set the x-coordinate.
     * @param y a double to set the y-coordinate.
     * @param z a double to set the z coordinate.
     */

    public void setVector3D(double x, double y, double z) {
	this.setX(x);
	this.setY(y);
	this.setZ(z);
    }

    /** Set x coordinate
     *
     * @param x a double to set the x-coordinate.
     */

    public void setX(double x) {this.xx = x; }

    /** Set y coordinate
     *
     * @param y a double to set the y-coordinate.
     */

    public void setY(double y) {this.yy = y; }

    /** Set z coordinate
     *
     * @param z a double to set the z-coordinate.
     */

    public void setZ(double z) {this.zz = z; }

    // Getters provide access to private internal variables.

    /**
     * Gets x-coordinate.
     * @return a double instance representing the x-coordinate.
     */

    public double getX() {return this.xx; }

    /**
     * Gets y-coordinate.
     * @return a double instance representing the y-coordinate.
     */

    public double getY() {return this.yy; }

    /**
     * Gets z-coordinate.
     * @return a double instance representing the z-coordinate.
     */

    public double getZ() {return this.zz; }

    /**
     *  Returns the square of the vector magnetude.
     *
     * @return the square of the magnetude of the vector.
     */
   
    public double mag2() {
	double result = xx*xx + yy*yy + zz*zz;
	return result;   
    }

    /**
     * Returns the magnetude of the vector.
     *
     * @return the magnetude of the vector.
     */

    public double mag() {
	double result = Math.sqrt(xx*xx + yy*yy + zz*zz);
	return result;
    }

    /** Returns a string representation of the vector components.
     *
     * @return a string representation of the vector components.
     */

    public String toString() {
	double x = this.getX();
	double y = this.getY();
	double z = this.getZ();
	return ("x:"+x+" y:"+y+" z:"+z);
    } 

    /** Instance methods for scalar multiplication of a vector by a double.
     *
     * @param A Method to update vector by taking product of vector elements with a double.
     */

    public void vectMultip(double A) {
	this.xx = this.xx*A;
	this.yy = this.yy*A;
	this.zz = this.zz*A;
    }

    public static Vector3D vectMultip(double A, Vector3D V) {

	return new Vector3D(V.getX()*A, V.getY()*A, V.getZ()*A);
    }

    /** Divides vector by a double.
     *
     * @param B Method to update vector by dividing vector elements by a double.
     */

    public void vectDiv(double B) {
	this.xx = this.xx/B;
	this.yy = this.yy/B;
	this.zz = this.zz/B;
    }

    /** Method for vector addition.
     *
     * @return Sets values of a new vector to the sum of two previously defined vectors.
     */

    public static Vector3D add(Vector3D A, Vector3D B) {
	Vector3D result = new Vector3D(A.xx+B.xx, A.yy+B.yy, A.zz+B.zz);
	return result;
    }

    /** Method for vector subtraction.
     *
     * @return Sets values of a new vector to the result of the elements of one
     * previously defined vector minus another previously defined vector.
     */

    public static Vector3D sub(Vector3D A, Vector3D B) {
	Vector3D result = new Vector3D(A.xx-B.xx, A.yy-B.yy, A.zz-B.zz);
	return result;
    }

    /** Method for dot product.
     *
     * @return Returns the dot product of two given vectors.
     */

    public static double dot(Vector3D A, Vector3D B) {
	double result = A.xx*B.xx + A.yy*B.yy + A.zz*B.zz;
	return result;
    }

    /** Method for cross product.
     *
     * @return Sets values of a new vector to the results of the cross product of two
     * previously defined vectors.
     */

    public static Vector3D cross(Vector3D A, Vector3D B) {
	Vector3D result = new Vector3D(A.yy*B.zz - B.yy*A.zz, A.zz*B.xx - A.xx*B.zz, A.xx*B.yy - B.xx*A.yy);
	return result;
    }
}
