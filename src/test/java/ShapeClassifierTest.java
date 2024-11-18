import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ShapeClassifierTest {
    private ShapeClassifier classifier;

    @BeforeEach
    public void setUp() {
        classifier = new ShapeClassifier();
    }

    @Test
    public void testLineShape() {
        String input = "Line,Small,Yes,50";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testCircleShape() {
        String input = "Circle,Large,Yes,50,50";
        String result = classifier.evaluateGuess(input);
        assertEquals("Wrong Size,Wrong Even/Odd", result);
    }

    @Test
    public void testEllipseShape() {
        String input = "Ellipse,Large,No,40,50";
        String result = classifier.evaluateGuess(input);
        assertEquals("No: Suggestion=Ellipse", result);
    }

    @Test
    public void testEquilateralTriangle() {
        String input = "Equilateral,Small,Yes,5,5,5";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testIsoscelesTriangle() {
        String input = "Isosceles,Small,Yes,5,5,6";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testScaleneTriangle() {
        String input = "Scalene,Small,No,3,4,5";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: ", result);
    }

    @Test
    public void testRectangle() {
        String input = "Rectangle,Large,Yes,10,20,10,20";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testSquare() {
        String input = "Square,Small,Yes,5,5,5,5";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testIncorrectShapeGuess() {
        String input = "Circle,Large,Yes,10,20";
        String result = classifier.evaluateGuess(input);
        assertEquals("No: Suggestion=Ellipse", result);
    }

    @Test
    public void testWrongSizeGuess() {
        String input = "Circle,Small,Yes,150,150";
        String result = classifier.evaluateGuess(input);
        assertEquals("Wrong Size,Wrong Even/Odd", result);
    }

    @Test
    public void testPartialCorrectGuess() {
        String input = "Circle,Large,No,50,50";
        String result = classifier.evaluateGuess(input);
        assertEquals("Wrong Size,Wrong Even/Odd", result);
    }

    @Test
    public void testBadGuessLimit() {
        assertThrows(IllegalStateException.class, () -> {
            classifier.evaluateGuess("Triangle,Small,Yes,1,2,3");
            classifier.evaluateGuess("Triangle,Small,Yes,1,2,3");
            classifier.evaluateGuess("Triangle,Small,Yes,1,2,3");
        });
    }

    @Test
    public void testInvalidInput() {
        String input = "Invalid,Input";
        String result = classifier.evaluateGuess(input);
        assertEquals("No", result);
    }

    @Test
    public void testPerimeterBoundaries() {
        String smallCircle = "Circle,Small,Yes,3,3";
        String largeCircle = "Circle,Large,Yes,200,200";

        String smallResult = classifier.evaluateGuess(smallCircle);
        assertEquals("Wrong Size,Wrong Even/Odd", smallResult);

        String largeResult = classifier.evaluateGuess(largeCircle);
        assertEquals("Wrong Size,Wrong Even/Odd", largeResult);
    }

    @Test
    public void testInvalidTriangle() {
        String input = "Triangle,Small,Yes,1,2,10"; // Invalid triangle
        String result = classifier.evaluateGuess(input);
        assertTrue(result.startsWith("No: Suggestion=Not A Triangle"));
    }

    @Test
    public void testExtremeParameterValues() {
        String hugeInput = "Rectangle,Large,Yes,4095,4095,4095,4095";
        String negativeInput = "Rectangle,Small,Yes,-1,-1,-1,-1";

        String hugeResult = classifier.evaluateGuess(hugeInput);
        assertEquals("No: Suggestion=Square", hugeResult);

        String negativeResult = classifier.evaluateGuess(negativeInput);
        assertEquals("No: Suggestion=Square", negativeResult);
    }

    @Test
    public void testMakeSuggestionForLine() {
        String input = "Line,Small,Yes,10";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testClassifyTwoParametersForEllipse() {
        String input = "Ellipse,Large,No,40,50";
        String result = classifier.evaluateGuess(input);
        assertEquals("No: Suggestion=Ellipse", result);
    }

    @Test
    public void testClassifyFourParametersForSquare() {
        String input = "Square,Small,Yes,5,5,5,5";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testClassifyFourParametersForRectangle() {
        String input = "Rectangle,Large,Yes,10,20,10,20";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testSquarePerimeter() {
        String input = "Square,Small,Yes,5,5,5,5";
        String result = classifier.evaluateGuess(input);
        assertEquals("Yes: Wrong Even/Odd", result);
    }

    @Test
    public void testWrongSizeAndEvenOddGuess() {
        String input = "Circle,Small,No,150,150";
        String result = classifier.evaluateGuess(input);
        assertEquals("Wrong Size,Wrong Even/Odd", result);
    }
}
