import java.util.ArrayList;

class MonteCarloSolver {
    private final double INITIAL_LOWER_LIMIT = 0.0;
    private final int NUMBER_OF_SAMPLES_ON_ITERATION = 10000000;
    private final double EPSILON = 0.001;
    private final double LIMIT_CHANGES = 0.2;

    private ArrayList<ConstraintFunction> constraintFunctions;
    private ObjectiveFunction objectiveFunction;
    private Integer dimension;
    private Double[] upperLimitOfValues;
    private Double[] lowerLimitOfValues;

    MonteCarloSolver(
            ArrayList<ConstraintFunction> constraintFunctions,
            ObjectiveFunction objectiveFunction,
            Integer dimension,
            Double upperLimitOfValues) {
        this.constraintFunctions = constraintFunctions;
        this.objectiveFunction = objectiveFunction;
        this.dimension = dimension;
        this.upperLimitOfValues = new Double[dimension];
        this.lowerLimitOfValues = new Double[dimension];
        for(int i = 0; i < dimension; i++) {
            this.lowerLimitOfValues[i] = INITIAL_LOWER_LIMIT;
            this.upperLimitOfValues[i] = upperLimitOfValues;
        }
    }

    Double[] solve() {
        Double[] values = findValuesThatMinimalizesObjective();
        Double previousObjectiveFunctionValue = objectiveFunction.evaluate(values);

        changeLimits(values);
        values = findValuesThatMinimalizesObjective();
        Double currentObjectiveFunctionValue = objectiveFunction.evaluate(values);

        while( Math.abs(previousObjectiveFunctionValue - currentObjectiveFunctionValue) > EPSILON ) {
            previousObjectiveFunctionValue = currentObjectiveFunctionValue;
            changeLimits(values);
            values = findValuesThatMinimalizesObjective();
            currentObjectiveFunctionValue = objectiveFunction.evaluate(values);
        }

        return values;
    }

    private Double[] findValuesThatMinimalizesObjective() {
        ArrayList<Double[]> listOfValues = findRandomValuesThatFulfillsConstraints();
        Double minimum = objectiveFunction.evaluate(listOfValues.get(0));
        Double[] minimalizes = listOfValues.get(0);

        for (Double[] values : listOfValues) {
            Double objective = objectiveFunction.evaluate(values);
            if(objective < minimum){
                minimalizes = values;
                minimum = objective;
            }
        }

        return minimalizes;
    }

    private ArrayList<Double[]> findRandomValuesThatFulfillsConstraints() {
        RandomValuesGenerator randomGenerator = new RandomValuesGenerator(dimension, lowerLimitOfValues, upperLimitOfValues);
        ArrayList<Double[]> listOfRandomValues = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_SAMPLES_ON_ITERATION; i++) {
            Double[] randomValues = randomGenerator.generate();

            if(valuesFulfillsEveryConstraint(randomValues)) {
                listOfRandomValues.add(randomValues);
            }
        }

        return listOfRandomValues;
    }

    private boolean valuesFulfillsEveryConstraint(Double[] values) {
        for(ConstraintFunction function : constraintFunctions) {
            try{
                if(!function.valuesFulfillsFunction(values)) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private void changeLimits(Double[] values) {
        Double newDistanceBetweenUpperAndLower = Math.abs(upperLimitOfValues[0] - lowerLimitOfValues[0]) * LIMIT_CHANGES;

        for(int i = 0; i < dimension; i++) {
            upperLimitOfValues[i] = values[i] + newDistanceBetweenUpperAndLower/2;
            if(values[i] - newDistanceBetweenUpperAndLower/2 < 0) {
                lowerLimitOfValues[i] = 0.0;
            }
            else {
                lowerLimitOfValues[i] = values[i] - newDistanceBetweenUpperAndLower/2;
            }
        }
    }
}
