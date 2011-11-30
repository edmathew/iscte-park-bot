package main_stuff;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class LoadGUI extends JFrame{
	
	private final int WIDTH = 200;
	private final int HEIGHT = 60;
	private JButton btLoad = new JButton("Carregar");
	private JButton btSave = new JButton("Guardar");
	
	public LoadGUI(){
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btLoad){
					String filename = JOptionPane.showInputDialog("Introduza o ficheiro para carregar: ");
					GoodDriver.loadBrain(filename);
				}
				if (e.getSource() == btSave){
					String filename = JOptionPane.showInputDialog("Introduza o ficheiro para guardar: ");
					GoodDriver.saveBrain(filename);
				}
			}
		};
		
		//GoodDriver.saveBrain(String filename) && loadBrain(String filename)
		
		btLoad.addActionListener(listener);
		btSave.addActionListener(listener);
		
		getContentPane().add(btLoad);
		getContentPane().add(btSave);
	}
	
	public static void main(String[] args) {
		new LoadGUI().start();
	}

	private void start() {
		setVisible(true);
	}

}
