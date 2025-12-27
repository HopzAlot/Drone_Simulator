package dronesimulator;

import java.util.*;

public class Drone {

    /* Translational state */
    private Vector3 position;
    private Vector3 velocity;
    

    /* Rotational state */
    private double[][] R;                 // Rotation matrix
    private Vector3 angularVelocity;

    /* Physical parameters */
    private double mass = 1.0;
    private Vector3 inertia =
            new Vector3(0.02, 0.02, 0.04);
    

	private double kd=1.2;
    

    /* Modules */
    private Controller controller;
	private CollisionAvoidance collision= new CollisionAvoidance();
	private FormationManager formation= new FormationManager();

    public Drone(Vector3 startPos, Vector3 target) {

        position = startPos;
        velocity = new Vector3(0, 0, 0);

        angularVelocity = new Vector3(0, 0, 0);
        R = identityMatrix();

        controller = new Controller(target);
    }
    
    /* ================= MAIN STEP ================= */

    public void step(ArrayList<Drone> drones,
                     double dt,
                     Environment env) {

        /* ----- TRANSLATION ----- */
        Vector3 totalforce= totalForces(drones);

        Vector3 accel = totalforce.divide(mass);
        velocity = velocity.add(accel.multiply(dt));
        position = position.add(velocity.multiply(dt));

        /* ----- ROTATION ----- */
        Vector3 torque =
                controller.calculateTorque(this);

        /* I * ω */
        Vector3 Iomega = new Vector3(
                inertia.x * angularVelocity.x,
                inertia.y * angularVelocity.y,
                inertia.z * angularVelocity.z
        );

        /* ω × (Iω) */
        Vector3 gyro =
                angularVelocity.cross(Iomega);

        /* ω̇ = I⁻¹ ( τ − ω × (Iω) ) */
        Vector3 angularAccel = new Vector3(
                (torque.x - gyro.x) / inertia.x,
                (torque.y - gyro.y) / inertia.y,
                (torque.z - gyro.z) / inertia.z
        );

        /* ω(t + Δt) */
        angularVelocity =
                angularVelocity.add(angularAccel.multiply(dt));

        /* R(t + Δt) = R * Exp(ω Δt) */
        double angle =
                angularVelocity.magnitude() * dt;

        if (angle > 1e-6) {
            Vector3 axis =
                    angularVelocity.divide(
                            angularVelocity.magnitude());

            double[][] dR =
                    rodrigues(axis, angle);

            R = multiply(R, dR);
        }
        env.enforceBounds(this);
    }
    public Vector3 totalForces(ArrayList<Drone> drones) {
    	Vector3 gravity= new Vector3(0,0,-9.81);
    	double[][] rotationalMatrix= getOrientation();
    	double Tz= controller.getBodyThrustZ(this);
    	double x= rotationalMatrix[0][2]*Tz;
    	double y= rotationalMatrix[1][2]*Tz;
    	double z= rotationalMatrix[2][2]*Tz;
    	Vector3 RT= new Vector3(x,y,z);
    	
    	Vector3 aeroForce= (getVelocity().multiply(kd)).multiply(-1);
    	Vector3 repForce= collision.calculateCollisionForce(this, drones);
    	Vector3 formForce= formation.calculateFormationForce(this, drones);
    	
    	Vector3 total= ((gravity.multiply(getMass())).add(RT).add(aeroForce).add(repForce).add(formForce));
    	return total;
    } 

    private double[][] rodrigues(Vector3 k, double theta) {

        double c = Math.cos(theta);
        double s = Math.sin(theta);
        double v = 1 - c;

        double x = k.x, y = k.y, z = k.z;

        return new double[][] {
            {x*x*v + c,   x*y*v - z*s, x*z*v + y*s},
            {y*x*v + z*s, y*y*v + c,   y*z*v - x*s},
            {z*x*v - y*s, z*y*v + x*s, z*z*v + c}
        };
    }

    private double[][] multiply(double[][] A, double[][] B) {

        double[][] C = new double[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    C[i][j] += A[i][k] * B[k][j];

        return C;
    }

    private double[][] identityMatrix() {
        return new double[][] {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
    }

    /* ================= GETTERS ================= */

    public Vector3 getPosition() {
        return position;
    }
    public Controller getController() {
    	return controller;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public Vector3 getAngularVelocity() {
        return angularVelocity;
    }

    public double getMass() {
        return mass;
    }

    /* Rotation matrix (canonical orientation) */
    public double[][] getOrientation() {
        return R;
    }

    /* Euler angles (derived view, NOT stored) */
    public Vector3 getEulerAngles() {

        double yaw =
                Math.atan2(R[1][0], R[0][0]);

        double pitch =
                Math.atan2(
                        -R[2][0],
                        Math.sqrt(
                                R[2][1]*R[2][1] +
                                R[2][2]*R[2][2])
                );

        return new Vector3(0, pitch, yaw);
    }

    /* ================= SETTERS (needed by Environment) ================= */

    public void setPosition(Vector3 p) {
        this.position = p;
    }
    
    
    public void setVelocity(Vector3 v) {
        this.velocity = v;
    }
}
