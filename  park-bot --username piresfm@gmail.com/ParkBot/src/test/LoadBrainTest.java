package test;

import java.util.LinkedList;

import jb2d_framework.TestbedModel;

import main_stuff.GoodDriver;

public class LoadBrainTest {

	public static void main(String[] args) {
		TestbedModel model = new TestbedModel();
		GoodDriver dasStig = new GoodDriver(model.getTest());
		System.out.println("LOADING BRAIN...");
		dasStig = new GoodDriver(model.getTest());
		
		LinkedList<String> files = new LinkedList<String>();
		files.add("newTest.brain");
		files.add("test2.brain");
		dasStig.loadMultipleFiles(files);
		
		System.out.println("BRAIN AFTER loadMultipleFiles()");
		System.out.println(dasStig.getBrain().topN());
		System.out.println(dasStig.getBrain().getNeuralNetworks().size());
		System.out.println("DONE");
	}
}
