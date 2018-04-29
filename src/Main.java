import java.io.FileInputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setIn(new FileInputStream("./src/testingValues"));
        TaskMaker taskMaker = new TaskMaker();
        taskMaker.askForTask();
        Double[] result = taskMaker.solve();
        for(Double value : result) {
            System.out.print(value);
            System.out.print("  ");
        }
    }
}
