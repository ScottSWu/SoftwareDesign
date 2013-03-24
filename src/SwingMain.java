/**
 * SwingMain
 * 
 * Runs an instance of the game.
 * FOR NORMAL PURPOSES.
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.jogamp.opengl.util.FPSAnimator;
import funnytrees.JOsu.OsuData.OsuBeatmap;

public class SwingMain implements ActionListener,KeyListener {
	// Options
	private boolean FULLSCREEN = false;
	private int WIDTH = 800;
	private int HEIGHT = 600;
	
	// Variables
	private Dimension screenSize;
	private FPSAnimator animator;
	private JFrame window;
	private JPanel cards;
	private CardLayout deck;
	private JPanel mainMenu;
	private JPanel playMenu;
	private JPanel instructions;
	private JPanel optionsMenu;
	private RenderCanvas canvas;
	
	public static void main(String[] args){
		new SwingMain("SwingMain");
	}
	
	public SwingMain(String title) {
		window = new JFrame(title);
		window.setBackground(Color.WHITE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Adjust window size to true size (including borders)
		if (FULLSCREEN) {
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			window.setResizable(false);
			window.setUndecorated(true);
			window.setSize(screenSize);
		}
		else {
			screenSize = new Dimension(WIDTH,HEIGHT);
			window.setSize(screenSize);
			window.setVisible(true);
			Dimension actualSize = window.getContentPane().getSize();
			Dimension adjustedSize = new Dimension(2*screenSize.width - actualSize.width,2*screenSize.height - actualSize.height);
			window.setSize(adjustedSize);
			window.setVisible(false);
		}
		canvas = new RenderCanvas(window,screenSize);
		animator = new FPSAnimator(canvas,120);
		
		// Create screens
		createMain();
		createPlay();
		createInstructions();
		createOptions();
		
		// Compile the window together
		createCards();
		window.add(cards);
		deck.show(cards,"mainMenu");
		window.setVisible(true);
	}
	
	/**
	 * Create the main menu panel.
	 */
	public void createMain() {
		mainMenu = new JPanel();
		mainMenu.setSize(screenSize);
		mainMenu.setBackground(Color.WHITE);
		mainMenu.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridy = 0;
		MenuButton play = new MenuButton("Play","playMenu");
		play.addActionListener(this);
		mainMenu.add(play, c);
		
		c.gridy = 1;
		MenuButton instructions = new MenuButton("Instructions","instructionMenu");
		instructions.addActionListener(this);
		mainMenu.add(instructions, c);
		
		c.gridy = 2;
		MenuButton options = new MenuButton("Options","optionMenu");
		options.addActionListener(this);
		mainMenu.add(options, c);
		
		c.gridy = 3;
		MenuButton quit = new MenuButton("Quit","quitMenu");
		quit.addActionListener(this);
		mainMenu.add(quit,c);
	}
	
	//creates play menu panel
	public void createPlay() {
		playMenu = new JPanel();
		playMenu.setSize(screenSize);
		playMenu.setLayout(new BoxLayout(playMenu,BoxLayout.Y_AXIS));
		JPanel scrollableSelections = new JPanel();
		scrollableSelections.setLayout(new BoxLayout(scrollableSelections,BoxLayout.Y_AXIS));
		JScrollPane selections = new JScrollPane(scrollableSelections,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		File[] songs = (new File("Songs")).listFiles(),songParts;
		ArrayList<OsuBeatmap> maps = new ArrayList<OsuBeatmap>();
		OsuBeatmap map;
		SongButton songSelection;
		String buttonText;
		
		MenuButton back = new MenuButton("Back","mainMenu");
		back.addActionListener(this);
		playMenu.add(back);
		for (int i=0; i<songs.length; i++) {
			if (songs[i].isDirectory()) {
				songParts = songs[i].listFiles();
				for (int j=0; j<songParts.length; j++) {
					if (songParts[j].getName().endsWith(".osu")) {
						map = new OsuBeatmap();
						map.readMeta(songs[i].getName(),songParts[j].getName());
						maps.add(map);
						buttonText = "(Difficulty " + map.OverallDifficulty + ") " + map.Artist + " - " + map.Title + " [" + map.Version + "]";
						songSelection = new SongButton(buttonText,songs[i].getName(),songParts[j].getName());
						songSelection.addActionListener(this);
						scrollableSelections.add(songSelection);
					}
				}
			}
		}
		playMenu.add(selections);
	}
	
	/**
	 * Create the instructions panel
	 */
	private int currentPage = 0;
	public void createInstructions() {
		instructions = new JPanel() {
			private int totalPages = 3;
			private BufferedImage[] pages = new BufferedImage[totalPages];
			
			public void paint(Graphics g) {
				if (currentPage>=totalPages) currentPage = currentPage - totalPages;
				if (currentPage<0) currentPage = currentPage + totalPages;
				if (pages[currentPage]==null) loadImage(currentPage);
				g.drawImage(pages[currentPage],0,0,null);
				for (int i=0; i<this.getComponentCount(); i++) {
					this.getComponent(i).paint(g);
				}
			}
			
			private void loadImage(int i) {
				try {
					pages[i] = ImageIO.read(new File("images/instructions" + i + ".png"));
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		instructions.setSize(screenSize);
		instructions.setBackground(Color.WHITE);
		instructions.setLayout(new BoxLayout(instructions,BoxLayout.PAGE_AXIS));
		
		MenuButton back = new MenuButton("Back","mainMenu");
		back.addActionListener(this);
		instructions.add(back);
		back.setLocation(0,0);
		instructions.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getX()>720 && e.getY()>150 && e.getY()<450) {
					currentPage++;
					instructions.repaint();
				}
				else if (e.getX()<80 && e.getY()>150 && e.getY()<450) {
					currentPage--;
					instructions.repaint();
				}
			}
		});
	}
	
	/**
	 * Create then options menu panel
	 */
	public void createOptions() {
		optionsMenu = new JPanel();
		optionsMenu.setSize(screenSize);
		optionsMenu.setBackground(Color.WHITE);
		optionsMenu.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		JLabel options = new JLabel("Options");
		optionsMenu.add(options, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		JLabel controls = new JLabel("Controls");
		optionsMenu.add(controls, c);
		
		c.gridy = 2;
		JLabel movement = new JLabel("Movement");
		optionsMenu.add(movement, c);
		
		c.gridy = 3;
		c.gridwidth = 1;
		JLabel up = new JLabel("Up");
		optionsMenu.add(up, c);
		
		c.gridy = 4;
		JLabel left = new JLabel("Left");
		optionsMenu.add(left, c);
		
		c.gridy = 5;
		JLabel down = new JLabel("Down");
		optionsMenu.add(down, c);
		
		c.gridy = 6;
		JLabel right = new JLabel("Right");
		optionsMenu.add(right, c);
		
		c.gridx = 1;
		c.gridy = 3;
		JButton upB = new JButton("W");
		optionsMenu.add(upB, c);
		
		c.gridy = 4;
		JButton leftB = new JButton("A");
		optionsMenu.add(leftB, c);
		
		c.gridy = 5;
		JButton downB = new JButton("S");
		optionsMenu.add(downB, c);
		
		c.gridy = 6;
		JButton rightB = new JButton("D");
		optionsMenu.add(rightB, c);
		
		c.gridx = 2;
		c.gridy = 1;
		JLabel audio = new JLabel("Audio");
		optionsMenu.add(audio, c);
		
		c.gridy = 2;
		JLabel masterVol = new JLabel("Master Volume");
		optionsMenu.add(masterVol, c);
		
		c.gridy = 3;
		JSlider mvSlider = new JSlider(0, 100, 50);
		mvSlider.setMajorTickSpacing(50);
		mvSlider.setMinorTickSpacing(10);
		mvSlider.setPaintLabels(true);
		mvSlider.setPaintTicks(true);
		optionsMenu.add(mvSlider, c);
		
		c.gridy = 4;
		JLabel music = new JLabel("Music");
		optionsMenu.add(music, c);
		
		c.gridy = 5;
		JSlider mSlider = new JSlider(0, 100, 50);
		mSlider.setMajorTickSpacing(50);
		mSlider.setMinorTickSpacing(10);
		mSlider.setPaintLabels(true);
		mSlider.setPaintTicks(true);
		optionsMenu.add(mSlider, c);
		
		c.gridy = 6;
		JLabel soundEff = new JLabel("Sound Effects");
		optionsMenu.add(soundEff, c);
		
		c.gridy = 7;
		JSlider seSlider = new JSlider(0, 100, 50);
		seSlider.setMajorTickSpacing(50);
		seSlider.setMinorTickSpacing(10);
		seSlider.setPaintLabels(true);
		seSlider.setPaintTicks(true);
		optionsMenu.add(seSlider, c);
		
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 3;
		MenuButton back = new MenuButton("Back","mainMenu");
		back.addActionListener(this);
		optionsMenu.add(back, c);
	}
	
	/**
	 * Put the cards together
	 */
	public void createCards() {
		deck = new CardLayout();
		cards = new JPanel(deck);
		cards.setSize(screenSize);
		
		cards.add(mainMenu,"mainMenu");
		cards.add(playMenu,"playMenu");
		cards.add(canvas,"game");
		cards.add(instructions,"instructionMenu");
		cards.add(optionsMenu,"optionMenu");
	}
	
	/**
	 * Override ActionListener.actionPerformed
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof MenuButton) { // Menu button
			MenuButton place = ((MenuButton) e.getSource());
			if (place.target.equals("quitMenu")) System.exit(0);
			deck.show(cards,place.target);
		}
		else if (e.getSource() instanceof SongButton) { // Song selection
			SongButton song = ((SongButton) e.getSource());
			canvas.loadGame(song.songFolder,song.songMap);
			deck.show(cards,"game");
			canvas.startGame();
			animator.start();
		}
	}

	/**
	 * Override KeyListener.keyTyped
	 */
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Override KeyListener.keyPressed
	 */
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Override keyListener.keyReleased
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}