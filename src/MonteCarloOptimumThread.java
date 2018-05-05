import java.util.ArrayList;

class MonteCarloOptimumThread extends Thread {
    private final ArrayList<Double[]> listOfValues;
    private final ArrayList<Double[]> resultListOfValues;
    private final String objectiveStr;
    private final ObjectiveFunction objectiveFunction;
    private final Integer indexStart;
    private final Integer indexStop;

    MonteCarloOptimumThread(
                            ArrayList<Double[]> listOfValues,
                            ArrayList<Double[]> resultListOfValues,
                            String objectiveStr,
                            ObjectiveFunction objectiveFunction,
                            Integer indexStart,
                            Integer indexStop) {
        this.listOfValues = listOfValues;
        this.resultListOfValues = resultListOfValues;
        this.objectiveStr = objectiveStr;
        this.objectiveFunction = objectiveFunction;
        this.indexStart = indexStart;
        this.indexStop = indexStop;
    }

    @Override
    public void run() {
        findOptimum();
    }


    private void findOptimum() {
        Double optimum = objectiveFunction.evaluate(listOfValues.get(indexStart));
        Double[] optimizes = listOfValues.get(indexStart);

        for (int i = indexStart; i < indexStop; i++) {
            Double objective = objectiveFunction.evaluate(listOfValues.get(i));
            if(objectiveStr.equals("max")) {
                if(objective > optimum){
                    optimizes = listOfValues.get(i);
                    optimum = objective;
                }
            }
            else if(objectiveStr.equals("min")) {
                if(objective < optimum){
                    optimizes = listOfValues.get(i);
                    optimum = objective;
                }
            }
        }

        resultListOfValues.add(optimizes);
    }
}
