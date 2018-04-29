import java.util.ArrayList;

public class MonteCarloThread extends Thread {
    private ArrayList<Double[]> listOfRandomValues;
    private ArrayList<Double[]> resultListOfRandomValues;
    private ArrayList<ConstraintFunction> constraintFunctions;
    private Integer indexStart;
    private Integer indexStop;

    MonteCarloThread(ArrayList<Double[]> randomValues,
                     ArrayList<Double[]> resultListOfRandomValues,
                     ArrayList<ConstraintFunction> constraintFunctions,
                     Integer indexStart,
                     Integer indexStop) {
        this.listOfRandomValues = randomValues;
        this.resultListOfRandomValues = resultListOfRandomValues;
        this.constraintFunctions = constraintFunctions;
        this.indexStart = indexStart;
        this.indexStop = indexStop;
    }

    @Override
    public void run() {
        for(int i = indexStart; i < indexStop; i++) {
            if(valuesFulfillsEveryConstraint(listOfRandomValues.get(i))) {
                resultListOfRandomValues.add(listOfRandomValues.get(i));
            }
        }
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

}
