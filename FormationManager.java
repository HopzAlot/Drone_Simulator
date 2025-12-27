package dronesimulator;
import java.util.*;
public class FormationManager {
	private double kp=0.4;
	private double kv=0.2;
	
	public Vector3 calculateFormationForce(Drone me,ArrayList<Drone> drones) {
		Vector3 ff=new Vector3(0,0,0);
		
		for(Drone d: drones) {
			if(d==me)
				continue;
			Vector3 dp=me.getPosition().subtract(d.getPosition());
			Vector3 dv= me.getVelocity().subtract(d.getVelocity());
			
			ff=ff.add(dp.multiply(kp).add(dv.multiply(kv)));
		}
		return ff.multiply(-1);
	}
}
