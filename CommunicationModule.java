package dronesimulator;
import java.util.*;
public class CommunicationModule {
	private double range=5;
	private double loss=0.2;
	private Random rand= new Random();
	
	public ArrayList<SharedData> exchange(Drone me, ArrayList<Drone> drones){
		ArrayList<SharedData> shareddata= new ArrayList<>();
		
		for(Drone d: drones) {
			
			if(d==me)
				continue;
			Vector3 diff= me.getPosition().subtract(d.getPosition());
			
			if(diff.magnitude()>range)
				continue;
			if(rand.nextDouble()>loss) {
				shareddata.add(new SharedData(d.getPosition(),d.getVelocity()));
			}
		}
		return shareddata;
	}
			
	
}
