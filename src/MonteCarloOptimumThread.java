import java.util.ArrayList;

public class MonteCarloOptimumThread extends Thread {
    private ArrayList<Double[]> listOfValues;
    private ArrayList<Double[]> resultListOfValues;
    private String objectiveStr;
    private ObjectiveFunction objectiveFunction;
    private Integer indexStart;
    private Integer indexStop;

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
        Double[] optimalizes = listOfValues.get(indexStart);

        for (int i = indexStart; i < indexStop; i++) {
            Double objective = objectiveFunction.evaluate(listOfValues.get(i));
            if(objectiveStr.equals("max")) {
                if(objective > optimum){
                    optimalizes = listOfValues.get(i);
                    optimum = objective;
                }
            }
            else if(objectiveStr.equals("min")) {
                if(objective < optimum){
                    optimalizes = listOfValues.get(i);
                    optimum = objective;
                }
            }
        }

        resultListOfValues.add(optimalizes);
    }
}
