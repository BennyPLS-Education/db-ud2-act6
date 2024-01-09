import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private final static String URL = "jdbc:mysql://localhost:3306/negoci";
    private final static String user = "root";
    private final static String password = "CalaClara21.";

    private static int currentRow = 1;

    public static void main(String[] args) {
        // establish connection
        try (var con = DriverManager.getConnection(URL, user, password)) {
            var statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            var rs = statement.executeQuery("SELECT * FROM CLIENT ORDER BY CLIENT_COD");
            mainLoop(rs);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void print(ResultSet rs) throws SQLException {
        var rsmd = rs.getMetaData();
        var columns = rsmd.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            System.out.print(rs.getObject(i) + " ");
        }
        System.out.println();
    }


    private static void printMenu() {
        System.out.println("1. Primer client");
        System.out.println("2. Següent client");
        System.out.println("3. Anterior client");
        System.out.println("4. Darrer client");
        System.out.println("5. Exit");
    }

    private static void getInput(ResultSet rs) throws SQLException {
        var scanner = new Scanner(System.in);
        var input = scanner.nextInt();
        rs.absolute(currentRow);
        switch (input) {
            case 1:
                rs.first();
                break;
            case 2:
                if (rs.isLast()) {
                    System.out.println("You are already at the last row");
                    return;
                }
                rs.absolute(++currentRow);
                break;
            case 3:
                if (rs.isFirst()) {
                    System.out.println("You are already at the first row");
                    return;
                }
                rs.absolute(--currentRow);
                break;
            case 4:
                rs.last();
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input");
                return;
        }

        print(rs);
    }

    private static void mainLoop(ResultSet rs) throws SQLException {
        while (true) {
            printMenu();
            getInput(rs);
        }
    }
}