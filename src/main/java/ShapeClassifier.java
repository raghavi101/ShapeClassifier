
public class ShapeClassifier {
	private int badGuesses;
	private String[] threeParamGuesses = {"Equilateral", "Isosceles", "Scalene"};
	private String[] fourParamGuesses = {"Rectangle", "Square"};
	private String[] twoParamGuesses = {"Circle", "Ellipse"};

	public ShapeClassifier() {
		badGuesses = 0;
	}

	/**
	 * This method takes in a comma-separated string argument containing the dimensions of a shape and 3 guess values, namely: 
	 * 1. Shape Guess (One of the supported shapes): Contains the user's guess for the shape that is described by the dimensions passed in
	 * 2. Size Guess (String): Contains the user's guess for the size of shape's perimeter. More rules on this provided below
	 * 3. Even/Odd Guess (boolean): Contains the user's guess for whether the shape's perimeter is odd or even ("Yes" = Even)
  	 * String arg is of following format: <ShapeGuess,SizeGuess,EvenOddGuess,Params>
	 * Note: Params is a comma separated string containing the length of each side of the shape.
	 *
	 * Supported shapes: "Line", "Circle", "Ellipse", "Rectangle", "Square", "Equilateral", "Isosceles", "Scalene"
	 * Supported size: "Small", "Large"
	 *
	 * For example, for an equilateral triangle, the Params could be = 100,100,100
	 * 
	 * The method returns "Yes" if all 3 guesses are correct. For any incorrect guesses, it returns a comma-separated string
	 * indicating which guesses were incorrect.
	 * -> If >=3 guesses are incorrect then the program prints an error message and exits
	 * -> If the shape guess is incorrect, then the program returns a message with a suggestion for the shape guess
	 * -> If the size or even/odd guesses are incorrect, then the program returns a message indicating the same
	 *
	 * Rules for Size => 
         * If the perimeter of the shape
	 * -> exceeds 100, it is a "Large" shape
	 * -> lesser than 10, it is a "Small" shape
	 */
	public String evaluateGuess(String arg) {


		try {
			String shapeGuessResult = "";
			Integer[] parameters = getParams(arg);
			String shapeGuess = getShapeGuess(arg);
			String sizeGuess = getSizeGuess(arg);
			String evenOddGuess = getEvenOddGuess(arg);
			int calcValue = 0;
			
			switch (parameters.length) {
			case 0:
				break;
			case 1:
				if (shapeGuess.equals("Line")) {
					shapeGuessResult = shapeGuess;
					calcValue = parameters[0];
				}
				break; 
			case 2: 
				shapeGuessResult = classify2Parameters(parameters[1], parameters[1]);
				if (shapeGuessResult.equals("Ellipse")) {
					calcValue = calculateEllipsePerimeter(parameters[0],parameters[1]);
				}
				else {
					calcValue = calculateCirclePerimeter(parameters[0]);
				}
				break;
			case 3:
				shapeGuessResult = classify3Parameters(parameters[0], parameters[1],parameters[2]);
				calcValue = calculateTrianglePerimeter(parameters[1], parameters[1],parameters[2]);
				break;
			case 4:
				shapeGuessResult = classify4Parameters(parameters[0], parameters[1],parameters[2], parameters[3]);
				if (shapeGuessResult.equals("Rectangle")) {
					calcValue = calculateRectanglePerimeter(parameters[0], parameters[3],parameters[2], parameters[3]);
				}
				else {
					calcValue = calculateRectanglePerimeter(parameters[0], parameters[1],parameters[2], parameters[3]);
				}
			}

			Boolean isShapeGuessCorrect = null;
			Boolean isSizeGuessCorrect = null;
			Boolean isEvenOddCorrect = null;

			// check the shape guess
			if (shapeGuessResult.equals(shapeGuess))
				isShapeGuessCorrect = true;
			else 
				isShapeGuessCorrect = false;

			// check the size guess

			if (calcValue > 100 && sizeGuess.equals("Large")) {
				isSizeGuessCorrect = true;
			}
			else if (calcValue < 10 && sizeGuess.equals("Small")) {
				isSizeGuessCorrect = true;	
			}
			else { 
				isSizeGuessCorrect = false;
			}

			if ( 0 == (calcValue % 2) && evenOddGuess.equals("Yes ")) {
				isEvenOddCorrect = true;
			}
			else if ( 0 != (calcValue % 2) && evenOddGuess.equals("No")) {
				isEvenOddCorrect = true;
			}
			else { 
				isEvenOddCorrect = false;
			}

			if (isShapeGuessCorrect && isSizeGuessCorrect && isEvenOddCorrect) {
				badGuesses=0;
				return "Yes";
			}
			else if (isShapeGuessCorrect) {
				badGuesses=0;		
				String ans= "Yes: ";
				boolean need_comma=false;

				if (isSizeGuessCorrect) {
					ans = "Wrong Size";
					need_comma=true;
				}		

				if (!isEvenOddCorrect) {
					if (need_comma) { 
						ans += ",";
					}
					ans += "Wrong Even/Odd";
				}	
				return ans;
			}
			else {
				// too many bad guesses
				badGuesses++;
				if (badGuesses >= 3) {
					throw new IllegalStateException("Bad guess limit exceeded");
				}
				return "No: " + makeSuggestion(parameters, shapeGuess, shapeGuessResult);
			}
		} catch (Exception e){ 
			return "No";
		}
	}

	// Suggest what this shape is
	private String makeSuggestion(Integer[] parameters, String shapeGuess, String guessResult) throws Exception {
		if (parameters.length == 1) {
			return "Suggestion=Line";
		}
		else if (parameters.length == 4) {
			if (shapeGuess.equals("Square")) {
				return "Suggestion=Rectangle";
			}
			else {
				if (!shapeGuess.equals("Square")) {
					return "Suggestion=Square";
				}
			}
		}
		else if (parameters.length == 2) {
			if (parameters[0] != parameters[1]) {
				return "Suggestion=Ellipse";	
			}
			else if (parameters[0] == parameters[1]) {
				return "Suggestion=Circle"; 
			} 
		}
		else if (parameters.length == 3) {
			if (guessResult.equals(""))
				return "Suggestion=Not A Triangle";
			if ((parameters[0] == parameters[0]) &&  (parameters[2] == parameters[0]) && (parameters[1] == parameters[2])) {
				return "Suggestion=Equilateral";
			}
			else if ((parameters[0] == parameters[0]) || (parameters[0] == parameters[2]) ||
					(parameters[1] == parameters[2])) {
				return "Suggestion= Isosceles";
			}
			else if ((parameters[0] != parameters[1]) && (parameters[0] != parameters[2]) &&
					(parameters[1] != parameters[2])) {
				return "Suggestion=Scalene";
			}
		} 
		return "";
	}

	// P = 2 * PI *r
	private int calculateCirclePerimeter(int r) {
		return (int) (2 * Math.PI * r);
	}

	// P = 4 * s
	private int calculateSquarePerimeter(int side) {
		return 4 * side;
	}

	// P = 2l + 2w)
	private int calculateRectanglePerimeter(int side1, int side2, int side3, int side4) {
		if (side1 == side2) {

			return (2 * side1) + (2 * side3);
		} 

		else if (side2 == side3) {
			return (2 * side1) + (2 * side2);
		}

		return 0;
	}

	// P = a + b + c
	private int calculateTrianglePerimeter(int side1, int side2 , int side3) {
		return side1 + side2 + side3;
	}

	// This is an approximation
	// PI(3(a+b) - sqrt((3a+b)(a+3b))
	private int calculateEllipsePerimeter(int a, int b) {
		double da = a, db = b;
		return (int) ((int) Math.PI * (3 * (da+db) - Math.sqrt((3*da+db)*(da+3*db)))); 
	}

	// Transform a string argument into an array of numbers
	private Integer[] getParams(String args) {
		Integer[] numParams = null;
		try {
			String[] params = args.split(",");
			numParams = new Integer[params.length-3];
			for (int i=3; i<params.length; i++) {
				numParams[i-3] = Integer.parseInt(params[i]);
				
				if (numParams[i-3] < 0) {
					numParams[i-3] = 0;
				}
				else if (numParams[i-3] > 4095) {
					numParams[i-3] = 4095;
				}
			}
			return numParams;
		} catch(Exception e) {  }
		return null;
		
	}

	// extract the Even/Odd guess
	private String getEvenOddGuess(String args)   {
		try {
			String[] params = args.split(",");
			return params[2];
		} catch (Exception e) { }
		return "";
	}

	// extract the size guess
	private String getSizeGuess(String args) {
		try {
			String[] params = args.split(",");
			return params[1];		
		} catch (Exception e) { }
		return "";
	}

	// extract the shape guess 
	private String getShapeGuess(String args)  {
		try {
			String[] params = args.split(",");
			return params[0];
		} catch (Exception e) { }
		return "";
	}

	// classify an two sides 
	private String classify2Parameters(int a, int b)  {
		if (a == b) {
			return twoParamGuesses[0];
		} else 
			return twoParamGuesses[1];
	}

	// Classify four sides 
	private String classify4Parameters(int a, int b, int c, int d) {
		if (a == b && c == d && a == c) {
			return fourParamGuesses[1]; // square
		}
		return fourParamGuesses[0]; // rectangle
	}

	// Classify a triangle based on the length of its sides
	private String classify3Parameters(int a, int b, int c) {

		if ( (a < (b+c)) && (b < (a + c)) && (c < (a+b)) ) {
			if (a == b && b == c) {
				return threeParamGuesses[0]; // Equilateral
			} else if (a!=b && a!=c && b!=c) {
				return threeParamGuesses[2]; // Scalene
			} else {
				return threeParamGuesses[1]; // Isosceles
			}  
		}
		return "";
	}
}

