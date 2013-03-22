import javax.swing.JButton;


public class MenuButton extends JButton {
	public String target;
	public MenuButton(String text,String targetCard) {
		super(text);
		target = targetCard;
	}
}
