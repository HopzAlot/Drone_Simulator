package dronesimulator;
public class Environment {

    private double width;
    private double height;
    private double depth;

    public Environment(double width, double height,double depth) {
        this.width = width;
        this.height = height;
        this.depth=depth;
    }

    public void enforceBounds(Drone d) {

        Vector3 p = d.getPosition();
        Vector3 v = d.getVelocity();

        
        if (p.x < 0) {
            p.x = 0;
            v.x = -v.x;
        } else if (p.x > width) {
            p.x = width;
            v.x = -v.x;
        }

       
        if (p.y < 0) {
            p.y = 0;
            v.y = -v.y;
        } else if (p.y > height) {
            p.y = height;
            v.y = -v.y;
        }

      
        if (p.z < 0) {
            p.z = 0;
            v.z = -v.z;
        } else if (p.z > depth) {
            p.z = depth;
            v.z = -v.z;
        }

        d.setPosition(p);
        d.setVelocity(v);
    }
}
