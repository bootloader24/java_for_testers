public class Geometry {
    public static void main(String[] args) {
        printSquareArea(5);
        printSquareArea(7);
        printSquareArea(9);
        printRectangleArea(4.0, 5.0);
        printRectangleArea(12,55);
    }

    static void printRectangleArea(double a, double b) {
        System.out.println("Площадь прямоугольника со сторонами " + a + " и " + b + " равна " + rectangleArea(a,b));
    }

    private static double rectangleArea(double a, double b) {
        return a * b;
    }

    static void printSquareArea(double side) {
        System.out.println("Площадь квадрата со стороной " + side + " равна " + squareArea(side));
    }

    private static double squareArea(double a) {
        return a * a;
    }
}
