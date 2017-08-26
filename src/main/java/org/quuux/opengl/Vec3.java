package org.quuux.opengl;

public class Vec3 {
    public double x, y, z;

    public Vec3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(final Vec3 vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3() {
        this(0, 0, 0);
    }

    public void add(final Vec3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void sub(final Vec3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void scale(final double scale) {
        x *= scale;
        y *= scale;
        z *= scale;
    }

    public void copy(final Vec3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void zero() {
        this.x = this.y = this.z = 0;
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public void normalize() {
        final double length = length();
        if (length > 0) {
            x /= length;
            y /= length;
            z /= length;
        } else {
            x = y = z = 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Vec3(%s, %s, %s)", x, y, z);
    }
}
