package main_stuff;

import java.awt.event.KeyEvent;
import java.util.Random;

import framework.TestbedTest;
import main_stuff.MyCar;

public class Driver extends Thread{
	
	private MyCar t;
	private int sleep;
	
	public Driver(TestbedTest t, int hp, int sleep) {
		this.t = (MyCar) t;
		this.t.setspeed(hp);
		this.sleep = sleep;
	}
	
	@Override
	public synchronized void start() {
		super.start();
		Random r = new Random();
		
		while(true){
//			switch(r.nextInt(6)){
//
//				case 0:
////					System.out.println("accelerating");
//					t.keyPressed('u', KeyEvent.VK_UP);
//					break;
//				case 1:
////					System.out.println("stopping movement");
//					t.keyReleased('u', KeyEvent.VK_UP);
//					break;
//				case 2:
////					System.out.println("braking");
//					t.keyPressed('d', KeyEvent.VK_DOWN);
//					break;
////				case 3: 
////					System.out.println("stopping braking");
////					t.keyReleased('d', KeyEvent.VK_DOWN);
////					break;
//				case 3:
////					System.out.println("turning left");
//					t.keyPressed('l', KeyEvent.VK_LEFT);
//					break;
//				case 4:
////					System.out.println("turning right");
//					t.keyPressed('r', KeyEvent.VK_RIGHT);
//					break;
//				case 5:
////					System.out.println("resetting steering wheel position");
//					t.keyReleased('r', KeyEvent.VK_RIGHT);
//					break;
//			}
			try {
				this.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
