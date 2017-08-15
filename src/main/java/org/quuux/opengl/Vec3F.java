package org.quuux.opengl;

public class Vec3F {
    public float x, y, z;

    public Vec3F(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3F(final Vec3F vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3F() {
        this(0, 0, 0);
    }

    public void add(final Vec3F other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void sub(final Vec3F other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void scale(final float scale) {
        x *= scale;
        y *= scale;
        z *= scale;
    }

    public void copy(final Vec3F other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void zero() {
        this.x = this.y = this.z = 0;
    }

    public float length() {
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public void normalize() {
        final float length = length();
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
        return String.format("Vec3F(%s, %s, %s)", x, y, z);
    }
}
