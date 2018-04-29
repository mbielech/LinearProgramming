import java.util.ArrayList;

class MonteCarloSolver {
    private final double INITIAL_LOWER_LIMIT = 0.0;
    private final int NUMBER_OF_SAMPLES_ON_ITERATION = 1000000;
    private final double EPSILON = 0.0001;
    private final double LIMIT_CHANGES = 0.2;
    private final int NUMBER_OF_THREADS = 4;

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


    private ArrayList<Double[]> findRandomValuesThatFulfillsConstraints() {
        RandomValuesGenerator randomGenerator = new RandomValuesGenerator(dimension, lowerLimitOfValues, upperLimitOfValues);
        ArrayList<Double[]> resultListOfRandomValues = new ArrayList<>();
        ArrayList<Double[]> listOfRandomValues = new ArrayList<>();

        for(int i = 0; i < NUMBER_OF_SAMPLES_ON_ITERATION; i++) {
            listOfRandomValues.add(randomGenerator.generate());
        }

        MonteCarloThread[] threads = new MonteCarloThread[NUMBER_OF_THREADS];
        for(int i = 0; i < NUMBER_OF_THREADS; i++) {
            threads[i] = new MonteCarloThread(listOfRandomValues,
                    resultListOfRandomValues,
                    constraintFunctions,
                    i*NUMBER_OF_SAMPLES_ON_ITERATION/NUMBER_OF_THREADS,
                    i*NUMBER_OF_SAMPLES_ON_ITERATION/NUMBER_OF_THREADS + NUMBER_OF_SAMPLES_ON_ITERATION/NUMBER_OF_THREADS);
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
