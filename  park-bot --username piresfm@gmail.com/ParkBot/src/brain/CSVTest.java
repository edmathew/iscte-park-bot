package brain;


import util.ReadCSV;

public class CSVTest {
	public static void main(String[] args) {
		try {
			ReadCSV csv = new ReadCSV("SavedNetworks/network1.net");
			while (csv.next()){
				double in1 = Double.parseDouble(csv.get(0));
				double in2 = Double.parseDouble(csv.get(1));
				double out1 = Double.parseDouble(csv.get(2));
				System.out.println("Input 1: " + in1);
				System.out.println("Input 2: " + in2);
				System.out.println("Output 1: " + out1);
				System.out.println("----------");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
