package ru.training.geometry.figures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SquareTests {

    @Test
    void canCalculateArea () {
        double result = Square.area(5.0);
        Assertions.assertEquals(25.0, result);
    }

    @Test
    void canCalculatePerimeter () {
        var result = Square.perimeter(7.0);
        Assertions.assertEquals(28.0, result);
    }
}
