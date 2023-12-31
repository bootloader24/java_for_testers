package ru.training.geometry.figures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TriangleTests {

    @Test
    void canCalculateArea() {
        /* так как в результате вычисления по формуле Герона можем получить вещественное число с длинной дробной частью,
        то тут целесообразно проверять работу метода с какой-то заданной точностью, например 0.01 (через параметр delta)
         */
        Assertions.assertEquals(2.90, new Triangle(2.0, 3.0, 4.0).area(), 0.01);
        Assertions.assertEquals(0.96, new Triangle(2.0, 2.0, 1.0).area(), 0.01);
        Assertions.assertEquals(10.06, new Triangle(7.8, 4.25, 5.12).area(), 0.01);
    }

    @Test
    void canCalculatePerimeter() {
        Assertions.assertEquals(9.0, new Triangle(2.0, 3.0, 4.0).perimeter());
        Assertions.assertEquals(5.0, new Triangle(2.0, 2.0, 1.0).perimeter());
        Assertions.assertEquals(17.17, new Triangle(7.8, 4.25, 5.12).perimeter());
    }

    @Test
    void cannotCreateTriangleWithNegativeSide() {
        try {
            new Triangle(-5.0, 3.0, 7.0);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            // ok
        }
    }

    @Test
    void cannotCreateTriangleWithBrokenInequality() {
        try {
            new Triangle(2.0, 1.0, 7.0);
            Assertions.fail();
        } catch (IllegalArgumentException exception) {
            // ok
        }
    }

    @Test
    void testEquality() {
        var t1 = new Triangle(2.0, 3.0, 4.0);
        var t2 = new Triangle(3.0, 4.0, 2.0);
        Assertions.assertEquals(t1, t2);
    }

    @Test
    void testEquality2() {
        var t1 = new Triangle(3.0, 8.0, 7.0);
        var t2 = new Triangle(7.0, 3.0, 8.0);
        Assertions.assertEquals(t1, t2);
    }
}
