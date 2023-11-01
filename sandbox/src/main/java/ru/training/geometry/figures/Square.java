package ru.training.geometry.figures;

public record Square(double side) {

    public Square {
        if (side < 0) {
            throw new IllegalArgumentException("Square side should be non-negative");
        }
    }

    public static void printArea(Square s) {
        String text = String.format("Площадь квадрата со стороной %f = %f", s.side, s.area());
        System.out.println(text);
    }

    public static void printPerimeter(Square s) {
        String text = String.format("Периметр квадрата со стороной %f = %f", s.side, s.perimeter());
        System.out.println(text);
    }


    public double area() {
        return this.side * this.side;
    }

    public double perimeter() {
        return this.side * 4;
    }
}