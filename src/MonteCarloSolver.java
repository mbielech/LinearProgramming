import java.util.ArrayList;

class MonteCarloSolver {
    private final double INITIAL_LOWER_LIMIT = 0.0;
    private final int NUMBER_OF_SAMPLES_ON_ITERATION = 1000;
    private final double EPSILON = 0.00000000001;
    private final double LIMIT_CHANGES = 0.8;
    private final int NUMBER_OF_THREADS = 8;

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
        tryToAdjustLimits();

        Double[] values = findValuesThatOptimalizesObjective(objectiveFunction.getObjective());
        Double previousObjectiveFunctionValue = objectiveFunction.evaluate(values);

        changeLimits(values);
        values = findValuesThatOptimalizesObjective(objectiveFunction.getObjective());
        Double currentObjectiveFunctionValue = objectiveFunction.evaluate(values);

        while( Math.abs(previousObjectiveFunctionValue - currentObjectiveFunctionValue) > EPSILON ) {
            previousObjectiveFunctionValue = currentObjectiveFunctionValue;
            changeLimits(values);
            values = findValuesThatOptimalizesObjective(objectiveFunction.getObjective());
            currentObjectiveFunctionValue = objectiveFunction.evaluate(values);
        }

        return values;
    }

    private Double[] findValuesThatOptimalizesObjective(String objectiveStr) {
        ArrayList<Double[]> listOfValues = findRandomValuesThatFulfillsConstraints();
        return findOptimumValuesByThreads(listOfValues,objectiveStr);
    }


    private ArrayList<Double[]> findRandomValuesThatFulfillsConstraints() {
        RandomValuesGenerator randomGenerator = new RandomValuesGenerator(dimension, lowerLimitOfValues, upperLimitOfValues);
        ArrayList<Double[]> resultListOfRandomValues = new ArrayList<>();

        ArrayList<Double[]> listOfRandomValues = randomGenerator.generateNumberOfRandomValues(NUMBER_OF_SAMPLES_ON_ITERATION);

        MonteCarloThread[] threads = new MonteCarloThread[NUMBER_OF_THREADS];
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            Integer portionForOneThread = NUMBER_OF_SAMPLES_ON_ITERATION/NUMBER_OF_THREADS;
            threads[i] = new MonteCarloThread(listOfRandomValues,
                    resultListOfRandomValues,
                    constraintFunctions,
                    i*portionForOneThread,
                    i*portionForOneThread + portionForOneThread);
            threads[i].run();
        }
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return resultListOfRandomValues;
    }
    private Double[] findOptimumValuesByThreads(ArrayList<Double[]> listOfValues, String objectiveStr) {
        ArrayList<Double[]> listOfOptimumsForEveryThread = findOptimumValueForEveryThread(listOfValues, objectiveStr);

        return findOptimumValues(listOfOptimumsForEveryThread, objectiveStr);
    }

    private Double[] findOptimumValues(ArrayList<Double[]> listOfValues, String objectiveStr) {
        Double optimum = objectiveFunction.evaluate(listOfValues.get(0));
        Double[] optimalizes = listOfValues.get(0);

        for (Double[] values : listOfValues) {
            Double objective = objectiveFunction.evaluate(values);
            if(objectiveStr.equals("max")) {
                if(objective > optimum){
                    optimalizes = values;
                    optimum = objective;
                }
            }
            else if(objectiveStr.equals("min")) {
                if(objective < optimum){
                    optimalizes = values;
                    optimum = objective;
                }
            }
        }

        return optimalizes;
    }

    private ArrayList<Double[]> findOptimumValueForEveryThread(ArrayList<Double[]> listOfValues, String objectiveStr) {
        ArrayList<Double[]> resultListOfValues = new ArrayList<>();

        MonteCarloOptimumThread[] threads = new MonteCarloOptimumThread[NUMBER_OF_THREADS];
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            Integer portionForOneThread = listOfValues.size()/NUMBER_OF_THREADS;
            threads[i] = new MonteCarloOptimumThread(
                                                listOfValues,
                                                resultListOfValues,
                                                objectiveStr,
                                                objectiveFunction,
                                                i*portionForOneThread,
                                                i*portionForOneThread + portionForOneThread);
            threads[i].run();
        }
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return resultListOfValues;
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

    private void tryToAdjustLimits() {
        for(ConstraintFunction constraintFunction : constraintFunctions) {
            if(constraintFunction.getSign().equals("<=")){
                boolean canAdjust = true;
                boolean rightSideGreaterThanZero = constraintFunction.getRightSideValue() > 0;
                for(Double coefficient : constraintFunction.getCoefficients()) {
                    if(coefficient > 0 != rightSideGreaterThanZero) {
                        canAdjust = false;
                    }
                }
                if(canAdjust) {
                    for(int i = 0; i < upperLimitOfValues.length; i++) {
                        Double newLimit = constraintFunction.getRightSideValue()/constraintFunction.getCoefficients()[i];
                        if (upperLimitOfValues[i] > newLimit) {
                            upperLimitOfValues[i] = newLimit;
                        }
                    }
                }
            }
        }
    }
}
