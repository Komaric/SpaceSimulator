package ru.komaric.spacesimulator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Сам вектор хранится нормализованным, а его длина отдельно,
// чтобы поменьше терять точность
public class Vector {

    private double x;
    private double y;
    private double length;

    public final static Vector Zero = new Vector(0, 0, 0);

    public Vector(double x, double y) {
        if (x == 0 && y == 0) {
            this.length = 0;
            this.x = x;
            this.y = y;
        } else {
            this.length = Math.sqrt(x * x + y * y);
            this.x = x / length;
            this.y = y / length;
        }
    }

    private Vector(double x, double y, double length) {
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    public double getX() {
        return x * length;
    }

    public double getY() {
        return y * length;
    }

    public Vector normalize() {
        return new Vector(x, y, 1);
    }

    public Vector add(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("\"vector\" can't be null");
        }
        if (this.length == 0) {
            return vector;
        }
        if (vector.length == 0) {
            return this;
        }
        double length = Math.sqrt(
                this.length * this.length
                + vector.length * vector.length
                + 2 * this.length * vector.length * this.cosOfAngel(vector));
        if (length == 0) {
            return Zero;
        }
        double x = (this.length * this.x + vector.length * vector.x) / length;
        double y = (this.length * this.y + vector.length * vector.y) / length;
        return new Vector(x, y, length);
    }

    public Vector sub(Vector vector) {
        return this.add(vector.multiply(-1));
    }

    public Vector multiply(double k) {
        if (k == 0) {
            return Zero;
        }
        return k > 0
                ? new Vector(x, y , k * length)
                : new Vector(-x, -y, -k * length);
    }

    public Vector divide(double k) {
        return this.multiply(1 / k);
    }

    public double dotProduct(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("\"vector\" can't be null");
        }
        return this.length * vector.length * (this.x * vector.x + this.y * vector.y);
    }

    private double cosOfAngel(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("\"vector\" can't be null");
        }
        return this.dotProduct(vector) / this.length / vector.length;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", Double.toString(x * length), Double.toString(y * length));
    }

    public static Vector fromString(String string) {
        if (string == null) {
            throw new IllegalArgumentException("\"string\" can't be null");
        }
        String d = "[-+]?(\\d*\\.?\\d+|\\d+\\.)([eE][-+]?\\d+)?";
        Matcher matcher = Pattern.compile(String.format("^ *\\( *(%s) *, *(%s) *\\) *$", d, d)).matcher(string);
        if (!matcher.matches()) {
            throw new NumberFormatException();
        }
        double x = Double.valueOf(matcher.group(1));
        double y = Double.valueOf(matcher.group(4));
        return new Vector(x, y);
    }
}
