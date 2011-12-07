package main_stuff;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class PerformanceGUI extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9051182915796470988L;
	private final int WIDTH = 400;
	private final int HEIGHT = 600;
	private JList list;
	private DefaultListModel data = new DefaultListModel();

	public PerformanceGUI(){
		setSize(WIDTH, HEIGHT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int) screenSize.getWidth()/2 + 350, 350);
		if (data != null){
			list  = new JList(data);
		}else{
			list = new JList();
		}
		
		JScrollPane jscroll = new JScrollPane();
		jscroll.getViewport().setView(list);
		
		getContentPane().add(jscroll);

		setVisible(true);

	}

	public void addData(String newData){
		data.addElement(newData);
		list.setModel(data);
	}
	
	public void clear(){
		data = new DefaultListModel();
	}
}
