package leader;

import java.sql.*;
import java.util.ArrayList;

public class LeaderBoard {

	private final ArrayList<Leader> leaders;
	private static final int SIZE = 10;
	private static Statement stmt;
	private static Connection conn;

	public LeaderBoard() {
		try {
			// 加载数据库驱动类
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 获取数据库连接对象
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/TankWar?serverTimezone=Hongkong&useUnicode=true&characterEncoding=utf8&useSSL=true",
					"root", "wang1234");
			stmt = conn.createStatement();
		} catch (ClassNotFoundException | SQLException cnfe) {
			cnfe.printStackTrace();
		}
		ArrayList<Leader> lds = read();
		if (lds != null) {
			leaders = lds;
		}
		else {
			leaders = new ArrayList<>();
		}
	}

	public boolean canGetOn(int score) {
		if (leaders.size() < SIZE) {
			return true;
		}
		return score > leaders.get(leaders.size() - 1).getScore();
	}

	public void putOn(Leader l) {
		leaders.add(l);
		leaders.sort(null);
		//删除多余的leaders
		for (int i = SIZE; i < leaders.size(); i++) {
			leaders.remove(i--);
		}
	}

	public ArrayList<Leader> getLeaders() {
		int currentSize = leaders.size();
		ArrayList<Leader> present = new ArrayList<>(leaders);
		for (int i = 0; i < SIZE - currentSize; i++) {
			present.add(new Leader("-", 0));
		}
		return present;
	}

	public void save() {
		try {
			/*写入数据库数据*/
			for (Leader leader : leaders) {
				String Sname = leader.getName();
				int score = leader.getScore();
				String queryString = String.format("insert into simple values(0,'%s','%d');", Sname, score);
				stmt.execute(queryString);

			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private ArrayList<Leader> read() {
		try {
			/*读取数据库数据并返回*/
			String queryString = "select * from simple order by sgrade desc,sname desc;";
			ArrayList<Leader> past =new ArrayList<>(leaders);
			PreparedStatement stmtRead = conn.prepareStatement(queryString);
			ResultSet result = stmtRead.executeQuery();  //查询数据库，并返回查询结果
			while (result.next()) {
				String firstName =  result.getString("Sname");
				int score = result.getInt(3);
				past.add(new Leader(firstName,score));
			}
			return past;
		} catch(Exception ex) {
			return null;
		}
	}
}

/*
package leader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LeaderBoard {

	private static final String LEADERS_FILE = "leaders.save";
	private final ArrayList<Leader> leaders;
	private static final int SIZE = 10;

	public LeaderBoard() {
		ArrayList<Leader> lds = read();
		if (lds != null) {
			leaders = lds;
		}
		else {
			leaders = new ArrayList<>();
		}
	}

	public boolean canGetOn(int score) {
		if (leaders.size() < SIZE) {
			return true;
		}
		return score > leaders.get(leaders.size() - 1).getScore();
	}

	public void putOn(Leader l) {
		leaders.add(l);
		leaders.sort(null);
		//删除多余的leaders
		for (int i = SIZE; i < leaders.size(); i++) {
			leaders.remove(i--);
		}
	}

	public ArrayList<Leader> getLeaders() {
		int currentSize = leaders.size();
		ArrayList<Leader> present = new ArrayList<>(leaders);
		for (int i = 0; i < SIZE - currentSize; i++) {
			present.add(new Leader("-", 0));
		}
		return present;
	}

	public void save() {
		try {
			FileOutputStream fout = new FileOutputStream(LEADERS_FILE);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(leaders);
			oos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private ArrayList<Leader> read() {
		try {
			FileInputStream fin = new FileInputStream(LEADERS_FILE);
			ObjectInputStream ois = new ObjectInputStream(fin);
			@SuppressWarnings("unchecked")
			ArrayList<Leader> lds = (ArrayList<Leader>) ois.readObject();
			ois.close();
			return lds;
		} catch(Exception ex) {
			return null;
		}
	}
}
*/