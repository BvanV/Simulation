import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class TopScorePanel extends JPanel {

	private static final long serialVersionUID = 8014890380693120422L;
	private static final String TOPSCORE_FILE_NAME = "./Topscores.txt";
	private final JLabel[] topscores = new JLabel[10];
	private final int[] scores = new int[10];

	public TopScorePanel() {
		for(int i=0;i<topscores.length;i++) {
			topscores[i] = new JLabel();
			Font font = new Font("Courier New", Font.PLAIN, 14);
			topscores[i].setFont(font);
		}
		this.setLayout(new BoxLayout(this, WIDTH));
		readTopScores();	
	}
	
	public void saveTopScores() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(TOPSCORE_FILE_NAME, "UTF-8");
		for(int i=0;i<topscores.length;i++) {
			writer.println(topscores[i].getText());
		}
		writer.close();
	}
	
	/**
	 * Add score to topscore list if applicable
	 */
	public void addScore(String name, int score) {
		
	}
	
	public void readTopScores() {
		BufferedReader br = null;
		int index = 0;
		try {
			br = new BufferedReader(new FileReader(TOPSCORE_FILE_NAME));
		    String line = br.readLine();
		    while (line != null && index < topscores.length) {
		    	topscores[index].setText(line);
		        line = br.readLine();
		        index++;
		    }
		    br.close();
		} catch (IOException e) {
			topscores[index].setText("Could not load Topscores");
			System.err.println("Could not load Topscores");
			e.printStackTrace();
		}
		for(int i=0;i<topscores.length;i++) {
			this.add(topscores[i]);
			scores[i] = getScore(topscores[i].getText());
		}
		
	}
	
	/**
	 * Gets the score out of a String with a topscore line
	 * @param s
	 * @return
	 */
	public int getScore(String s) {
		int score = 0;
		int factor = 1;
		int index = s.length() - 1;
		while(index > 0 && s.charAt(index) != ' ') {
			score = score + factor * (int) (s.charAt(index) - '0');
			index--;
			factor *= 10;
		}
		return score;
	}
	
}

