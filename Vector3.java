package dronesimulator;

public class Vector3 {
    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public Vector3 multiply(double k) {
        return new Vector3(x * k, y * k, z * k);
    }

    public Vector3 divide(double k) {
        return new Vector3(x / k, y / k, z / k);
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(
            this.y * v.z - this.z * v.y,
            this.z * v.x - this.x * v.z,
            this.x * v.y - this.y * v.x
        );
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double magnitudeSq() {
        return x*x + y*y + z*z;
    }

    @Override
    public String toString() {
        return String.format("%.3f,%.3f,%.3f", x, y, z);
    }
}
