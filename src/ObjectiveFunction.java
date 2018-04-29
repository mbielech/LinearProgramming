public class ObjectiveFunction {
    private Double[] coefficients;
    private String objective;

    public ObjectiveFunction(Integer numberOfDecisionVariables) {
        this.coefficients = new Double[numberOfDecisionVariables];
    }

    public Double evaluate(Double[] values) {
        Double result = 0.0;
        for(int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * values[i];
        }
        return result;
    }

    public void setCoefficient(Integer index, Double value) {
        coefficients[index] = value;
    }

    public void changeCoefficients() {
        for(int i = 0; i < coefficients.length; i++) {
            coefficients[i] = coefficients[i] * (-1);
        }
    }

    public void setObjective(String objective) throws Exception {
        if(objective.equals("min") || objective.equals("max")) {
            this.objective = objective;
        }
        else{
            throw new Exception("Bad objective");
        }
    }

    public String getObjective() {
        return objective;
    }
}
