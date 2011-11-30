package main_stuff;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoadGUI extends JFrame{
	
	private final int WIDTH = 200;
	private final int HEIGHT = 100;
	private JButton btLoad = new JButton("Carregar");
	private JButton btSave = new JButton("Guardar");
	private JButton btGenerate = new JButton("Novo CŽrebro");
	
	Driver driver;
	
	public LoadGUI(Driver driver2){
		this.driver = driver2;
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btLoad){
					String filename = JOptionPane.showInputDialog("Introduza o ficheiro para carregar: ");
					System.out.println("Loading brain");
					((GoodDriver)driver).loadBrain(filename);
					
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
			}
		};
		
		//GoodDriver.saveBrain(String filename) && loadBrain(String filename)
		
		btGenerate.addActionListener(listener);
		btLoad.addActionListener(listener);
		btSave.addActionListener(listener);
		
		getContentPane().add(btGenerate);
		getContentPane().add(btLoad);
		getContentPane().add(btSave);
	}
	


	public void start() {
		setVisible(true);
	}
	
}
