package main_stuff;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import learning_methods.LearningMethod;
import learning_methods.Procriation;
import learning_methods.StandardLearning;

public class LoadGUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4008918717515477552L;
	private final int WIDTH = 400;
	private final int HEIGHT = 180;
	private JButton btLoad = new JButton("Carregar");
	private JButton btSave = new JButton("Guardar");
	private JButton btGenerate = new JButton("Novo Cérebro");
	private static JLabel networkRunning = new JLabel("not running");
	private JButton btLoadSeveral = new JButton("Multi-Load");
	private JTextField mutationRate = new JTextField("0.2");
	private String[] algTypes = {"Simple Mutation","X-Over"}; 
	private JComboBox algorithmType = new JComboBox(algTypes);
	private static JLabel previousPerformance = new JLabel ("not enough iterations yet");
	private static JTextField reportFile = new JTextField("report.csv");
	
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
					LearningMethod lm = null;
					switch(algorithmType.getSelectedIndex()){
					case 0:
						lm = new StandardLearning();
						break;
					case 1:
						lm = new Procriation();
						break;
					}
					((GoodDriver)driver).setLearningMethod(lm, Double.parseDouble(mutationRate.getText()));
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
					new Thread(driver).start();
				}
			}
		};
		
		mutationRate.setColumns(10);
		reportFile.setColumns(20);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new FlowLayout());
		JPanel plabel = new JPanel(new BorderLayout());
		JPanel previousPerformancePanel = new JPanel();
		JPanel reportFilePanel = new JPanel();
		JPanel pbuttons = new JPanel();
		JPanel networkRunningPanel = new JPanel();
		
		networkRunningPanel.add(networkRunning);
		btGenerate.addActionListener(listener);
		btLoad.addActionListener(listener);
		btSave.addActionListener(listener);
		btLoadSeveral.addActionListener(listener);
		
		reportFilePanel.add(reportFile);
		
		plabel.add(BorderLayout.NORTH, reportFilePanel);
		plabel.add(BorderLayout.SOUTH, networkRunningPanel);
		previousPerformancePanel.add(previousPerformance);
		
		pbuttons.add(btGenerate);
		pbuttons.add(btLoad);
		pbuttons.add(btSave);
		pbuttons.add(btLoadSeveral);
		
		inputPanel.add(algorithmType);
		inputPanel.add(mutationRate);
		
		
		topPanel.add(BorderLayout.NORTH, plabel);
		topPanel.add(BorderLayout.CENTER, previousPerformancePanel);
		topPanel.add(BorderLayout.SOUTH, inputPanel);
		
		getContentPane().add(BorderLayout.NORTH, topPanel);
		getContentPane().add(BorderLayout.SOUTH, pbuttons);
		
		
	}
	


	public void start() {
		setVisible(true);
	}
	
	public static void updateRunningLabel(String s){
		networkRunning.setText("Running: "+s);
	}
	
	public static void setPreviousPerformance(String s){
		previousPerformance.setText(s);
	}



	public static String getReportFile() {
		return reportFile.getText();
	}
	
}
