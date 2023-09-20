package ru.training.geometry.figures;

public record Rectangle(double a, double b) {

    public static void printRectangleArea(Rectangle r) {
        String text = String.format("Площадь прямоугольника со сторонами %f и %f = %f", r.a, r.b, r.area());
        System.out.println(text);
    }

    public double area() {
        return this.a * this.b;
    }

    public double perimeter() {
        return this.a * 2 + this.b * 2;
    }
}