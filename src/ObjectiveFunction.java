class ObjectiveFunction {
    private Double[] coefficients;
    private String objective;

    ObjectiveFunction(Integer numberOfDecisionVariables) {
        this.coefficients = new Double[numberOfDecisionVariables];
    }

    Double evaluate(Double[] values) {
        Double result = 0.0;
        for(int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * values[i];
        }
        return result;
    }

    void setCoefficient(Integer index, Double value) {
        coefficients[index] = value;
    }

    void setObjective(String objective) throws Exception {
        if(objective.equals("min") || objective.equals("max")) {
            this.objective = objective;
        }
        else{
            throw new Exception("Bad objective");
        }
    }

    String getObjective() {
        return objective;
    }
}
