import java.sql.Time;

/**
 * Created with IntelliJ IDEA.
 * User: asarawgi
 * Date: 10/21/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SudokuDriver {

    private static void helperFunction(String grid) {
        long start = System.currentTimeMillis();
        SudokuSolver solver = new SudokuSolver();
        solver.search(solver.parseString(grid));
        long end = System.currentTimeMillis();
        System.out.println("This sudoku puzzle took " + (end - start) / 1000.0 + " secs");

    }

    public static void main(String args[]) {

        String grid  = "#,#,#,#,8,3,#,#,6,#,7,#,#,4,#,7,#,#,#,#,5,2,#,12,9,#,#,11,#,5,7,#,#,#,10,4,11,7,#,2,#,10,#,#,#,#,#,5,#,#,12,#,#,#,#,#,#,7,#,#,#,#,#,#,12,#,#,5,#,11,#,#,#,#,3,#,9,#,#,1,#,#,#,#,#,#,4,#,#,#,#,#,#,12,#,#,6,#,#,#,#,#,5,#,3,#,8,2,12,2,#,#,#,9,6,#,4,#,#,3,1,#,8,4,#,#,#,#,2,#,12,#,#,9,#,3,#,#,12,2,#,#,#,#";
        helperFunction(grid);

    }
}
