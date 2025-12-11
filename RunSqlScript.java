import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RunSqlScript {
    public static void main(String[] args) {
        String url = "jdbc:mysql://101.126.23.137:3306/eladmin?serverTimezone=Asia/Shanghai&useSSL=false";
        String user = "root";
        String password = "Jimu131207!";
        String sqlFile = "d:\\work\\test\\el\\b3\\eladmin\\sql\\limiter.sql";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             BufferedReader br = new BufferedReader(new FileReader(sqlFile));
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                sql.append(line);
                if (line.endsWith(";")) {
                    stmt.executeUpdate(sql.toString());
                    sql = new StringBuilder();
                }
            }

            conn.commit();
            System.out.println("SQL script executed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
