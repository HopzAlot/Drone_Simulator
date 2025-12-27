package dronesimulator;
public class Controller {
	private Vector3 target;
	
	//this is for translational motion.
	private double kp=2;
	private double kd=1.4;
	
	//this is for rotational motion.
	private double kr=4;
	private double komega=3;
	
	public Controller(Vector3 target) {
		this.target=target;
	}
	
	
	
	public Vector3 calculateThrustInitial(Drone d) {
			Vector3 ep= target.subtract(d.getPosition());
			Vector3 ev= d.getVelocity().multiply(-1);
			
			Vector3 aCMD= ep.multiply(kp).add(ev.multiply(kd)).add(new Vector3(0,0,9.81));

			return aCMD.multiply(d.getMass());	
	}
	
	public Vector3 calculateTargetOrientation(Drone d) {
	  
	    Vector3 aCMD = calculateThrustInitial(d).divide(d.getMass());
	    double desiredPitch = Math.atan2(aCMD.x, aCMD.z); 
	    double desiredRoll = Math.atan2(-aCMD.y, Math.sqrt(aCMD.x * aCMD.x + aCMD.z * aCMD.z));

	    Vector3 dir = target.subtract(d.getPosition());
	    double desiredYaw = Math.atan2(dir.y, dir.x);

	    return new Vector3(desiredRoll, desiredPitch, desiredYaw);
	}
	
	public Vector3 calculateTorque(Drone d) {
		Vector3 desiredorien= calculateTargetOrientation(d);
		Vector3 orientationerror=desiredorien.subtract(d.getEulerAngles());
		Vector3 angularvelocityerror=d.getAngularVelocity().multiply(-1);
		
		return orientationerror.multiply(kr).add((angularvelocityerror).multiply(komega));
	}
	
	public Vector3 calculateBodyThrust(Drone d) {
		Vector3 bodythrust= calculateThrustInitial(d);

		double[][] orientation= d.getOrientation();
		int rows= orientation.length;
		int columns= orientation[0].length;
		double[][] transorientation= new double[rows][columns];
		
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				transorientation[i][j]=orientation[j][i];
			}
		}
		 double x = transorientation[0][0]*bodythrust.x + transorientation[0][1]*bodythrust.y + transorientation[0][2]*bodythrust.z;
		    double y = transorientation[1][0]*bodythrust.x + transorientation[1][1]*bodythrust.y + transorientation[1][2]*bodythrust.z;
		    double z = transorientation[2][0]*bodythrust.x + transorientation[2][1]*bodythrust.y + transorientation[2][2]*bodythrust.z;

		return new Vector3(x,y,z);
		
	}
	public double getBodyThrustZ(Drone d) {
		return calculateBodyThrust(d).z;
	}
	public Vector3 getTarget() {
		return target;
	}
	
	
}
