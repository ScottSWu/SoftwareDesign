/**
 * SongButton extends JButton
 * 
 * Custom button for song selection.
 */

import javax.swing.JButton;

public class SongButton extends JButton {
	
	// Song file location
	public String songFolder,songMap;
	
	/**
	 * SongButton constructor
	 * 
	 * @param text	String: Button text
	 * @param sf	String: Song folder
	 * @param sm	String: Song map
	 */
	public SongButton(String text,String sf,String sm) {
		super(text);
		songFolder = sf;
		songMap = sm;
	}
}
