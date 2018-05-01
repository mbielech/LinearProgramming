import java.io.FileInputStream;

class Tests {
    private final double FIRST_UPPER_LIMIT = 2000000.0;

    void firstTest(){
        try{
            test("./src/testingValues");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    void secondTest(){ //Fails, because of == constraint
        try{
            test("./src/testingValues2");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    void thirdTest(){
        try{
            test("./src/testingValues3");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    void fourthTest(){ //Fails, because of == constraint
        try{
            test("./src/testingValues4");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void test(String file) throws Exception {
        System.setIn(new FileInputStream(file));

        TaskMaker taskMaker = new TaskMaker(FIRST_UPPER_LIMIT);
        taskMaker.askForTask();
        Double[] result = taskMaker.solve();

        ObjectiveFunction objectiveFunction = taskMaker.getObjectiveFunction();
        System.out.println("\nCoefficients:");
        for(Double value : result) {
            System.out.print(value);
            System.out.print("  ");
        }

        Double objective = objectiveFunction.evaluate(result);
        System.out.println("\n\nObjective Function:");
        System.out.println(objective);
    }
}
