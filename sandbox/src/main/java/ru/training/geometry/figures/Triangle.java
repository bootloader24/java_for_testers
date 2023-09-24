package ru.training.geometry.figures;

public record Triangle(double a, double b, double c) {

    public Triangle {
        if (a < 0 || b < 0 || c < 0) {
            throw new IllegalArgumentException("Triangle side should be non-negative");
        }
        if (a + b < c || a + c < b || b + c < a) {
            throw new IllegalArgumentException("The sum of any two triangle sides must be no less than the third side");
        }

    }

    public static void printTriangleArea(Triangle t) {
        String text = String.format("Площадь треугольника со сторонами %f, %f и %f = %f", t.a, t.b, t.c, t.area());
        System.out.println(text);
    }

    public double area() {
        var p = this.perimeter() / 2; // вычисление полупериметра
        return Math.sqrt(p * (p - this.a) * (p - this.b) * (p - this.c));
    }

    public double perimeter() {
        return this.a + this.b + this.c;
    }
}
