import java.util.ArrayList;
import java.util.Random;

class RandomValuesGenerator {
    private Integer dimension;
    private Double[] upperLimitOfValues;
    private Double[] lowerLimitOfValues;

    RandomValuesGenerator(Integer dimension, Double[] lowerLimitOfValues, Double[] upperLimitOfValues) {
        this.dimension = dimension;
        this.lowerLimitOfValues = lowerLimitOfValues;
        this.upperLimitOfValues = upperLimitOfValues;
    }

    ArrayList<Double[]> generateNumberOfRandomValues(Integer number) {
        ArrayList<Double[]> randomValues = new ArrayList<>();

        for(int i = 0; i < number; i++) {
            randomValues.add(generate());
        }

        return randomValues;
    }

    private Double[] generate() {
        Double[] result = new Double[dimension];
        Random random = new Random();

        for (int i = 0; i < dimension; i++) {
            result[i] = random.nextDouble() * ( upperLimitOfValues[i] - lowerLimitOfValues[i] ) + lowerLimitOfValues[i];
        }

        return result;
    }
}
