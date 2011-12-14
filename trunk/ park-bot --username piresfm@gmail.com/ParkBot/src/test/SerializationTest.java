package test;

import jb2d_framework.TestbedModel;
import main_stuff.GoodDriver;

public class SerializationTest {

	public static void main(String[] args) {
		TestbedModel model = new TestbedModel();
		GoodDriver dasStig = new GoodDriver(model.getTest());
		System.out.println("LOADING BRAIN...");
		dasStig = new GoodDriver(model.getTest());
		dasStig.loadBrain("Brain.dat");
		System.out.println(dasStig.getBrain());
		System.out.println("DONE");
	}
}
