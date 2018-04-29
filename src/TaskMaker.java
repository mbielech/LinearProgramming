import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

class TaskMaker {
    private final double FIRST_UPPER_LIMIT;

    private ArrayList<ConstraintFunction> constraintFunctions;
    private ObjectiveFunction objectiveFunction;
    private Integer dimension;
    private BufferedReader br;

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
        System.out.println("What is the dimension of problem (number of decision variables)?: ");
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
        System.out.println("What is the objective function? \n");
        for(int i = 0; i < dimension; i++) {
            System.out.println("Write coefficient of x" + (i+1) + " decision variable:");
            try {
                String in = br.readLine();

                objectiveFunction.setCoefficient(i,Double.parseDouble(in));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("What is the objective: minimalization or maximalization (write min or max):");
        try {
            String in = br.readLine();
            objectiveFunction.setObjective(in);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        if(objectiveFunction.getObjective().equals("max")) {
            changeObjective();
            try {
                objectiveFunction.setObjective("min");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changeObjective() {
        objectiveFunction.changeCoefficients();
    }

    private void askForConstraintFunctions() {
        try {
            while (true) {
                System.out.println("\nWhat is the constraint function? \n");
                System.out.println("What is the sign of constraint function (write <=, ++ or >=):");
                String sign;
                sign = br.readLine();

                System.out.println("What is the right side value of constraint function:");
                Double rightSideValue;
                String in = br.readLine();
                rightSideValue = Double.parseDouble(in);

                ConstraintFunction constraintFunction = new ConstraintFunction(dimension, sign, rightSideValue);

                for(int i = 0; i < dimension; i++) {
                    System.out.println("Write coefficient of x" + (i+1) + " decision variable:");
                    in = br.readLine();
                    constraintFunction.setCoefficient(i,Double.parseDouble(in));
                }

                constraintFunctions.add(constraintFunction);

                System.out.println("Would you like to add another constraint function? (y or n)");
                in = br.readLine();
                if(in.equals("n") || in.equals("N") || in.equals("No") || in.equals("no") || in.equals("NO")) {
                    break;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    ObjectiveFunction getObjectiveFunction() {
        return objectiveFunction;
    }
}
