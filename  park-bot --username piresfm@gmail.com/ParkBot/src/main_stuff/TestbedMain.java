/*******************************************************************************
 * Copyright (c) 2011, Daniel Murphy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright notice,
 * 	  this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright notice,
 * 	  this list of conditions and the following disclaimer in the documentation
 * 	  and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/**
 * Created at 4:23:48 PM Jul 17, 2010
 */
package main_stuff;

import javax.swing.JFrame;
import javax.swing.UIManager;

import jb2d_framework.TestList;
import jb2d_framework.TestPanelJ2D;
import jb2d_framework.TestbedFrame;
import jb2d_framework.TestbedModel;
import jb2d_framework.TestbedPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The entry point for the testbed application
 * @author Daniel Murphy
 */
public class TestbedMain {
	private static final Logger log = LoggerFactory.getLogger(TestbedMain.class);
	public static Driver myDriver = null;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			log.warn("Could not set the look and feel to nimbus.  "
					+ "Hopefully you're on a mac so the window isn't ugly as crap.");
		}
		TestbedModel model = new TestbedModel();
		TestbedPanel panel = new TestPanelJ2D(model);
		TestList.populateModel(model);
		JFrame testbed = new TestbedFrame(model, panel);

		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Driver driver = new GoodDriver(model.getTest());
		
//		Driver driver = new StupidDriver(model.getTest());
		myDriver = driver;
		LoadGUI lgui = new LoadGUI(myDriver);
		lgui.start();
		
		
		
	}
}