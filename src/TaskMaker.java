import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

class TaskMaker {
    private final double FIRST_UPPER_LIMIT;

    private final ArrayList<ConstraintFunction> constraintFunctions;
    private ObjectiveFunction objectiveFunction;
    private Integer dimension;
    private final BufferedReader br;

    TaskMaker(Double firstUpperLimit) {
        br = new BufferedReader(new InputStreamReader(System.in));
        constraintFunctions = new ArrayList<>();
        this.FIRST_UPPER_LIMIT = firstUpperLimit;
    }

    Double[] solve() {
        MonteCarloSolver solver = new MonteCarloSolver(constraintFunctions, objectiveFunction, dimension, FIRST_UPPER_LIMIT);
        return solver.solve();
    }

    void askForTask() {
        askForDimension();
        askForObjectiveFunction();
        askForConstraintFunctions();
    }

    private void askForDimension() {
        System.out.println("What is the dimension of problem (number of decision variables)?");
        try {
            String in = br.readLine();
            dimension = Integer.parseInt(in);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void askForObjectiveFunction() {
        objectiveFunction = new ObjectiveFunction(dimension);
        System.out.println("\nWhat is the objective function?");
        try {
            parseObjectiveFunction();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void parseObjectiveFunction() throws Exception {
        String in = br.readLine();
        String[] objectiveFunctionToParse = in.split(" ");
        for(int i = 0; i < dimension; i++) {
            objectiveFunction.setCoefficient(i, Double.parseDouble(objectiveFunctionToParse[i]));
        }
        objectiveFunction.setObjective(objectiveFunctionToParse[dimension]);
    }

    private void askForConstraintFunctions() {

        try {
            System.out.println("\nHow many constraint functions?");
            String in = br.readLine();
            for(int i = 0; i < Integer.parseInt(in); i++) {
                System.out.println("\nWhat is the constraint function?");
                parseConstraintFunction();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void parseConstraintFunction() throws Exception {
        String in = br.readLine();
        String[] constraintFunctionToParse = in.split(" ");

        Double[] coefficients = new Double[dimension];
        for(int i = 0; i < dimension; i++) {
            coefficients[i] = Double.parseDouble(constraintFunctionToParse[i]);
        }

        String sign = constraintFunctionToParse[dimension];

        Double rightSideValue = Double.parseDouble(constraintFunctionToParse[dimension+1]);

        ConstraintFunction constraintFunction = new ConstraintFunction(coefficients, sign, rightSideValue);
        constraintFunctions.add(constraintFunction);
    }

    ObjectiveFunction getObjectiveFunction() {
        return objectiveFunction;
    }
}
