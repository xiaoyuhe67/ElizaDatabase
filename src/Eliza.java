import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Eliza {

	private static Map<Integer, String> replacementMap = new HashMap<Integer, String>();

	private static Scanner sc = new Scanner(System.in);

	private static boolean contain = false;

	private static String response;

	public static void main(String[] args) {
		Connection con = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet hedgequestionset = null;
		ResultSet qualifyquestionset = null;
		String hedgequestion = "SELECT * FROM (SELECT hedge_question FROM   hedge ORDER BY DBMS_RANDOM.RANDOM) WHERE  rownum=1";
		String qualifyquestion = "SELECT * FROM (SELECT qualify_question FROM  qualify ORDER BY DBMS_RANDOM.RANDOM) WHERE  rownum=1";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// con = DriverManager.getConnection("jdbc:oracle:thin:sys as
			// sysdba/oracle@localhost:1521:orcl");
			con = DriverManager.getConnection("jdbc:oracle:thin:ora1/ora1@localhost:1521:orcl");
			stmt1 = con.createStatement();
			stmt2 = con.createStatement();
			System.out.println("Good day. What is your problem?");
			System.out.println("Enter your response here or Q to quit: ");

			while (1 > 0) {

				response = sc.nextLine();

				if (response.equals("Q")) {
					// Set<String> Set = new HashSet<String>();
					break;
				}

				String[] changeInput = response.split(" ");

				String content = Quilify(changeInput);

				if (contain == true) {
					replacementMap = Replacement(changeInput);
					qualifyquestionset = stmt1.executeQuery(qualifyquestion);
					while (qualifyquestionset.next()) {
						String value = qualifyquestionset.getString(1) + " " + content;
						System.out.println(value);
					}

				} else if (contain == false) {

					hedgequestionset = stmt2.executeQuery(hedgequestion);
					while (hedgequestionset.next()) {
						System.out.println(hedgequestionset.getString(1) + "\t");
					}

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (hedgequestionset != null) {
					hedgequestionset.close();
				}
				if (qualifyquestionset != null) {
					qualifyquestionset.close();
				}
				stmt1.close();
				stmt2.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static String Quilify(String[] str) {
		String Content = "";
		Set<String> Set = new HashSet<String>();
		Map<Integer, String> replacementMap = new HashMap<Integer, String>();
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("I") || str[i].equals("i") || str[i].equals("me") || str[i].equals("my")
					|| str[i].equals("My") || str[i].equals("am")) {
				contain = true;
				replacementMap = Replacement(str);

				for (Integer key : replacementMap.keySet()) {

					Content += replacementMap.get(key) + " ";
				}

				break;
			}
			contain = false;
		}

		return Content;

	}

	public static Map<Integer, String> Replacement(String[] str) {
		int length = str.length;
		Map<Integer, String> map = new HashMap<Integer, String>();

		for (int i = 0; i < length; i++) {
			if (str[i].equals("my")) {
				map.put(i, "your");
			} else if (str[i].equals("My")) {
				map.put(i, "your");
			} else if (str[i].equals("i")) {
				map.put(i, "you");
			} else if (str[i].equals("I")) {
				map.put(i, "you");
			} else if (str[i].equals("am")) {
				map.put(i, "are");
			} else if (str[i].equals("me")) {
				map.put(i, "you");
			} else {
				map.put(i, str[i]);
			}

		}
		return map;
	}

}
