import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SwingMain{
	public static JPanel cards;
	public static CardLayout cl;
	public static JPanel mainMenu;
	public static JPanel playMenu;
	public static JPanel instructions;
	public static JPanel optionsMenu;
	public static GridBagConstraints c;
	public static boolean upBool = false;
	public static boolean leftBool = false;
	public static boolean downBool = false;
	public static boolean rightBool = false;
	
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		JFrame frame = new JFrame();
		createMain();
		createPlay();
		createInstructions();
		createOptions();
		createCards();
		frame.add(cards);
		
		frame.setSize(500, 500);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
	
	//creates main menu panel
	public static void createMain(){
		mainMenu = new JPanel();
		mainMenu.setLayout(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton play = new JButton("Play");
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "play");
			}
		});
		mainMenu.add(play, c);
		
		c = new GridBagConstraints();
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "inst");
			}
		});
		mainMenu.add(instructions, c);
		
		c = new GridBagConstraints();
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton options = new JButton("Options");
		options.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "optn");
			}
		});
		mainMenu.add(options, c);
		
		c = new GridBagConstraints();
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		mainMenu.add(quit, c);
	}
	
	//creates play menu panel
	public static void createPlay(){
		playMenu = new JPanel();
		playMenu.setLayout(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img1 = new ImageIcon("img1.jpg");
		JButton test = new JButton("Emotional Skyscraper - Demetori", img1);
		test.setVerticalTextPosition(AbstractButton.BOTTOM);
		test.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img2 = new ImageIcon("img2.jpg");
		JButton test2 = new JButton("TearVid - An", img2);
		test2.setVerticalTextPosition(AbstractButton.BOTTOM);
		test2.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test2, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img3 = new ImageIcon("img3.jpg");
		JButton test3 = new JButton("Scarlet Rose - Lily", img3);
		test3.setVerticalTextPosition(AbstractButton.BOTTOM);
		test3.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test3, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img4 = new ImageIcon("img4.png");
		JButton test4 = new JButton("The Big Black - The Quick Brown Fox", img4);
		test4.setVerticalTextPosition(AbstractButton.BOTTOM);
		test4.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test4, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img5 = new ImageIcon("img5.jpg");
		JButton test5 = new JButton("Killer Song - Yanaginagi", img5);
		test5.setVerticalTextPosition(AbstractButton.BOTTOM);
		test5.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test5, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img6 = new ImageIcon("img6.jpg");
		JButton test6 = new JButton("Only My Railgun - FripSide", img6);
		test6.setVerticalTextPosition(AbstractButton.BOTTOM);
		test6.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test6, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		ImageIcon img7 = new ImageIcon("img7.jpg");
		JButton test7 = new JButton("Days of Dash - Suzuki Konomi", img7);
		test7.setVerticalTextPosition(AbstractButton.BOTTOM);
		test7.setHorizontalTextPosition(AbstractButton.CENTER);
		playMenu.add(test7, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "main");
				((SongButton) e.getSource()).getaopsidjf();
			}
		});
		playMenu.add(back, c);
	}
	
	//creates instructions panel
	public static void createInstructions(){
		instructions = new JPanel();
		instructions.setLayout(new BoxLayout(instructions, BoxLayout.PAGE_AXIS));
		
		JLabel hi = new JLabel("Instructions");
		instructions.add(hi);
		
		JLabel hi2 = new JLabel("Objective");
		instructions.add(hi2);
		
		JLabel hi3 = new JLabel("The objective of the game is to complete a song while avoiding as many bullets and enemies as possible.");
		instructions.add(hi3);
		
		JLabel hi4 = new JLabel("Controls");
		instructions.add(hi4);
		
		JLabel hi5 = new JLabel("Use the WASD keys to navigate around the screen and the mouse to aim. Left click to shoot.");
		instructions.add(hi5);
		
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "main");
			}
		});
		instructions.add(back);
	}
	
	//creates options menu panel
	public static void createOptions(){
		
		optionsMenu = new JPanel();
		optionsMenu.setLayout(new GridBagLayout());
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		JLabel options = new JLabel("Options");
		optionsMenu.add(options, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		JLabel controls = new JLabel("Controls");
		optionsMenu.add(controls, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		JLabel movement = new JLabel("Movement");
		optionsMenu.add(movement, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		JLabel up = new JLabel("Up");
		optionsMenu.add(up, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		JLabel left = new JLabel("Left");
		optionsMenu.add(left, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 5;
		JLabel down = new JLabel("Down");
		optionsMenu.add(down, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 6;
		JLabel right = new JLabel("Right");
		optionsMenu.add(right, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		JButton upB = new JButton("W");
		upB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				upBool = true;
				leftBool = false;
				downBool = false;
				rightBool = false;
			}
		});
//		upB.addKeyListener(new KeyAdapter(){
//			public void keyPressed(KeyEvent e){
//				if (upBool && e.getKeyCode() == KeyEvent.VK_CONTROL){
//					Character upChar = e.getKeyChar();
//				}
//			}
//		});
		optionsMenu.add(upB, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 4;
		JButton leftB = new JButton("A");
		leftB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				upBool = false;
				leftBool = true;
				downBool = false;
				rightBool = false;
			}
		});
		optionsMenu.add(leftB, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 5;
		JButton downB = new JButton("S");
		downB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				upBool = false;
				leftBool = false;
				downBool = true;
				rightBool = false;
			}
		});
		optionsMenu.add(downB, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 6;
		JButton rightB = new JButton("D");
		rightB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				upBool = false;
				leftBool = false;
				downBool = false;
				rightBool = true;
			}
		});
		optionsMenu.add(rightB, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		JLabel audio = new JLabel("Audio");
		optionsMenu.add(audio, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		JLabel masterVol = new JLabel("Master Volume");
		optionsMenu.add(masterVol, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		JSlider mvSlider = new JSlider(0, 100, 50);
		mvSlider.setMajorTickSpacing(50);
		mvSlider.setMinorTickSpacing(10);
		mvSlider.setPaintLabels(true);
		mvSlider.setPaintTicks(true);
		optionsMenu.add(mvSlider, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 4;
		JLabel music = new JLabel("Music");
		optionsMenu.add(music, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 5;
		JSlider mSlider = new JSlider(0, 100, 50);
		mSlider.setMajorTickSpacing(50);
		mSlider.setMinorTickSpacing(10);
		mSlider.setPaintLabels(true);
		mSlider.setPaintTicks(true);
		optionsMenu.add(mSlider, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 6;
		JLabel soundEff = new JLabel("Sound Effects");
		optionsMenu.add(soundEff, c);
		
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 7;
		JSlider seSlider = new JSlider(0, 100, 50);
		seSlider.setMajorTickSpacing(50);
		seSlider.setMinorTickSpacing(10);
		seSlider.setPaintLabels(true);
		seSlider.setPaintTicks(true);
		optionsMenu.add(seSlider, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 3;
		JButton back = new JButton("Back");
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cl.show(cards, "main");
			}
		});
		optionsMenu.add(back, c);
	}
	
	public static void createCards(){
		cards = new JPanel(new CardLayout());
		cards.add(mainMenu, "main");
		cards.add(playMenu, "play");
		cards.add(instructions, "inst");
		cards.add(optionsMenu, "optn");
		cl = (CardLayout)(cards.getLayout());
	}
}