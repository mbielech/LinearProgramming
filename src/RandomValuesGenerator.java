import java.util.Random;

public class RandomValuesGenerator {
    private Integer dimension;
    private Double[] upperLimitOfValues;
    private Double[] lowerLimitOfValues;

    public RandomValuesGenerator(Integer dimension, Double[] lowerLimitOfValues, Double[] upperLimitOfValues) {
        this.dimension = dimension;
        this.lowerLimitOfValues = lowerLimitOfValues;
        this.upperLimitOfValues = upperLimitOfValues;
    }

    public Double[] generate() {
        Double[] result = new Double[dimension];
        Random random = new Random();

        for (int i = 0; i < dimension; i++) {
            result[i] = random.nextDouble() * ( upperLimitOfValues[i] - lowerLimitOfValues[i] ) + lowerLimitOfValues[i];
        }

        return result;
    }
}
