import java.io.FileInputStream;

public class Tests {
    public final double FIRST_UPPER_LIMIT = 50000.0;

    public void firstTest(){
        try{
            test("./src/testingValues", 5000.0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void secondTest(){
        try{
            test("./src/testingValues3", 50000.0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void test(String file, Double firstUpperLimit) throws Exception {
        System.setIn(new FileInputStream(file));

        TaskMaker taskMaker = new TaskMaker(firstUpperLimit);
        taskMaker.askForTask();
        Double[] result = taskMaker.solve();

        ObjectiveFunction objectiveFunction = taskMaker.getObjectiveFunction();
        System.out.println("\nCoefficients:");
        for(Double value : result) {
            System.out.print(value);
            System.out.print("  ");
        }

        Double objective = objectiveFunction.evaluate(result);
        System.out.println("\nObjective Function:");
        System.out.println(objective);
    }
}
