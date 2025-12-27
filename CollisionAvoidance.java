package dronesimulator;
import java.util.*;
public class CollisionAvoidance {
	private double safedistance=1.5;
	private double krep=2;
	
	public Vector3 calculateCollisionForce(Drone me,ArrayList<Drone> drones) {
		Vector3 rf= new Vector3(0,0,0);
		for(Drone d: drones) {
			if(d==me)
				continue;
			
			Vector3 difference= me.getPosition().subtract(d.getPosition());
			double distanceSquared= difference.magnitudeSq();
			if(distanceSquared<safedistance*safedistance) {
				rf=rf.add(difference.divide(distanceSquared).multiply(krep));
				
			}
		}
		return rf;
	}
}
