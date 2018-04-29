class ConstraintFunction {
    private Double[] coefficients;
    private String sign;
    private Double rightSideValue;

    ConstraintFunction(Integer numberOfDecisionVariables, String sign, Double rightSideValue) throws Exception {
        if (sign.equals("<=") || sign.equals("==") || sign.equals(">=") )
            this.sign = sign;
        else
            throw new Exception("Bad sign of ConstraintFunction");
        this.rightSideValue = rightSideValue;
        this.coefficients = new Double[numberOfDecisionVariables];
    }

    void setCoefficient(Integer index, Double value) {
        coefficients[index] = value;
    }

    boolean valuesFulfillsFunction(Double[] values) throws Exception {
        if(values.length != coefficients.length)
            throw new Exception("Length of values and constrains are different");
        else {
            Double leftSideValue = evaluateLeftSide(values);
            return checkBothSides(this.sign, leftSideValue, rightSideValue);
        }
    }

    private Double evaluateLeftSide(Double[] values) {
        Double result = 0.0;
        for(int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * values[i];
        }
        return result;
    }

    private boolean checkBothSides(String sign, double left, double right) {
        if( sign.equals("==")) {
            return left == right;
        }
        else if( sign.equals(">=") ) {
            return left >= right;
        }
        else {
            return left <= right;
        }
    }

}
