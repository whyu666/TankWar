package leader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * leader board functionality: Saves and read an ArrayList to file. (permanent storage)
 */
public class LeaderBoard {
	private static final String LEADERS_FILE = "leaders.ser";
	private final ArrayList<Leader> leaders;
	private static final int SIZE = 10;

	/**
	 * Read the leaders' data from file or create a new one.
	 */
	public LeaderBoard() {
		ArrayList<Leader> lds = read();
		leaders = lds != null ? lds : new ArrayList<>();
	}

	/**
	 * @param score player's score
	 * @return if the player can get on the board
	 */
	public boolean canGetOn(int score) {
		if (leaders.size() < SIZE) return true;
		return score > leaders.get(leaders.size() - 1).getScore();
	}

	/**
	 * @param l Leader object that represents the player
	 * Put the player on the leader board.
	 */
	public void putOn(Leader l) {
		leaders.add(l);
		leaders.sort(null);
		//truncate leaders
		for (int i = SIZE; i < leaders.size(); i++) {
			leaders.remove(i);
		}
	}
	
	/**
	 * @return leaders as an ArrayList
	 */
	public ArrayList<Leader> getLeaders() {
		int currentSize = leaders.size();
		ArrayList<Leader> present = new ArrayList<>(leaders);
		for (int i = 0; i < SIZE - currentSize; i++) {
			present.add(new Leader("-", 0));
		}
		return present;
	}

	/**
	 * Save the leaders' data to file.
	 */
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

	/**
	 * @return ArrayList of leaders
	 * Read leaders data from file.
	 */
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