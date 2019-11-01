package Utils;

import java.awt.geom.Point2D;

/**
 * Represents a 2D vector with needed methods to manipulate it.
 */
public class Vector2D extends Point2D.Double {

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds vector from objects vector
     * @param v Vector to add
     * @return Result vector
     */
    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }


    /**
     * Subtract vector from objects vector
     * @param v Vector to subtract
     * @return Result vector
     */
    public Vector2D subtract(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    /**
     * Multiply vector and objects vector
     * @param v Vector to multiply with
     * @return Result vector
     */
    public Vector2D mult(Vector2D v) {
        return new Vector2D(this.x * v.x, this.y * v.y);
    }


    /**
     * Normalize vector
     */
    public void normalize() {
        double val = Math.sqrt(x * x + y * y);
        x = x / val;
        y = y / val;
    }

    /**
     * Multiply vector by scalar n
     * @param n Scalar to multiply with
     */
    public void mult(double n) {
        x = x * n;
        y = y * n;
    }


    /**
     * Dot product of vector and objects vector
     * @param v Vector to dot product with
     * @return Resulting vector
     */
    public double dot(Vector2D v) {
        return x * v.x + y * v.y;
    }

    /**
     * Adds two vectors together
     * @param v1 Vector 1
     * @param v2 Vector 2
     * @return Result vector
     */
    public static Vector2D add(Vector2D v1, Vector2D v2){
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2D getNormalPoint(Vector2D p, Vector2D a, Vector2D b) {
        Vector2D ap = p.subtract(a);
        Vector2D ab = b.subtract(a);

        ab.normalize();
        ab.mult(ap.dot(ab));
        Vector2D normalPoint = Vector2D.add(a, ab);
        return normalPoint;
    }
}
