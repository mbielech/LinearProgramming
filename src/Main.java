class Main {
    private static final double FIRST_UPPER_LIMIT = 50000.0;

    public static void main(String[] args) {
        TaskMaker taskMaker = new TaskMaker(FIRST_UPPER_LIMIT);
        taskMaker.askForTask();
        Double[] result = taskMaker.solve();

        ObjectiveFunction objectiveFunction = taskMaker.getObjectiveFunction();
        System.out.println("\n Coefficients:");
        for(Double value : result) {
            System.out.print(value);
            System.out.print("  ");
        }

        System.out.println("\nObjective Function:");
        Double objective = objectiveFunction.evaluate(result);
        System.out.println(objective);
    }


}
