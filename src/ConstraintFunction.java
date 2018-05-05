class ConstraintFunction {
    private Double[] coefficients;
    private String sign;
    private Double rightSideValue;

    ConstraintFunction(Double[] coefficients, String sign, Double rightSideValue) throws Exception {
        if (sign.equals("<=") || sign.equals("==") || sign.equals(">=") )
            this.sign = sign;
        else
            throw new Exception("Bad sign of ConstraintFunction");
        this.rightSideValue = rightSideValue;
        this.coefficients = coefficients;
    }

    boolean valuesFulfillsFunction(Double[] values) throws Exception {
        if(values.length != coefficients.length)
            throw new Exception("Length of values and constrains are different");
        else {
            Double leftSideValue = evaluateLeftSide(values);
            return checkBothSides(this.sign, leftSideValue, rightSideValue);
        }
    }

    Double[] getCoefficients() {
        return coefficients;
    }

    Double getRightSideValue() {
        return rightSideValue;
    }

    String getSign() {
        return sign;
    }

    private Double evaluateLeftSide(Double[] values) {
        Double result = 0.0;
        for(int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * values[i];
        }
        return result;
    }

    private boolean checkBothSides(String sign, double left, double right) throws Exception {
        switch (sign) {
            case "==":
                return left == right;
            case ">=":
                return left >= right;
            case "<=":
                return left <= right;
        }

        throw new Exception("Bad sign");
    }

}
