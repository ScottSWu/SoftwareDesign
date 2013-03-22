import javax.swing.JButton;


public class SongButton extends JButton {
	public String songFolder,songMap;
	public SongButton(String text,String sf,String sm) {
		super(text);
		songFolder = sf;
		songMap = sm;
	}
}
