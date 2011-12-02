package main_stuff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LoadGUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4008918717515477552L;
	private final int WIDTH = 400;
	private final int HEIGHT = 100;
	private JButton btLoad = new JButton("Carregar");
	private JButton btSave = new JButton("Guardar");
	private JButton btGenerate = new JButton("Novo Cérebro");
	private static JLabel networkRunning = new JLabel("not running");
	private JButton btLoadSeveral = new JButton("Multi-Load");
	
	Driver driver;
	
	public LoadGUI(Driver driver2){
		this.driver = driver2;
		setSize(WIDTH, HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) screenSize.getWidth()/2 + 350, 200);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btLoad){
					String filename = JOptionPane.showInputDialog("Introduza o ficheiro para carregar: ");
					System.out.println("Loading brain");
					((GoodDriver)driver).loadBrain(filename);
					new Thread(driver).start();
					
				}
				if (e.getSource() == btSave){
					String filename = JOptionPane.showInputDialog("Introduza o ficheiro para guardar: ");
					((GoodDriver)driver).saveBrain(filename);
					System.out.println("Brain Saved");
				}
				if (e.getSource() == btGenerate){
					System.out.println("creating new brain");
					new Thread(driver).start();
					
				}
				if (e.getSource() == btLoadSeveral){
					LinkedList<String> filesToLoad = new LinkedList<String>();
					String file = "";
					while (!file.equals("0")){
						file = JOptionPane.showInputDialog("Introduza o ficheiro a carregar. Para terminar, introduza 0.");
						if (!file.equals("0"))
							filesToLoad.add(file);
					}
					driver.loadMultipleFiles(filesToLoad);
				}
			}
		};
		
		JPanel plabel = new JPanel();
		JPanel pbuttons = new JPanel();
		
		btGenerate.addActionListener(listener);
		btLoad.addActionListener(listener);
		btSave.addActionListener(listener);
		btLoadSeveral.addActionListener(listener);
		
		plabel.add(networkRunning);
		
		pbuttons.add(btGenerate);
		pbuttons.add(btLoad);
		pbuttons.add(btSave);
		pbuttons.add(btLoadSeveral);
		
		getContentPane().add(BorderLayout.NORTH, plabel);
		getContentPane().add(BorderLayout.SOUTH, pbuttons);
		
		
	}
	


	public void start() {
		setVisible(true);
	}
	
	public static void updateRunningLabel(String s){
		networkRunning.setText("Running: "+s);
	}
	
}
