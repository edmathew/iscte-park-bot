package main_stuff;
import java.awt.event.KeyEvent;


import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import support.Car;
import support.ParkingSensor;
import framework.TestbedSettings;
import framework.TestbedTest;


public class CarTest extends TestbedTest{

	final static float MAX_STEER_ANGLE = (float) (Math.PI/3);
	final static float STEER_SPEED = 1.5f;
	final static int SIDEWAYS_FRICTION_FORCE = 10;
	static int HORSPOWER = 51;
	final static Vec2 CAR_STARTING_POSITION = new Vec2(10,10);

	final static Vec2 LEFT_REAR_WHEEL_POSITION = new Vec2(-1.5f, 1.9f);
	final static Vec2 RIGHT_REAR_WHEEL_POSITION = new Vec2(1.5f, 1.9f);
	final static Vec2 LEFT_FRONT_WHEEL_POSITION = new Vec2(-1.5f, -1.9f);
	final static Vec2 RIGHT_FRONT_WHEEL_POSITION = new Vec2(1.5f, -1.9f);

	static float engineSpeed = 0;
	static float steeringAngle = 0;

	Car car;
	Body pspot;

	public void setspeed(int hp) {
		HORSPOWER = hp;
	}

	@Override
	public void initTest(boolean argDeserialized) {
		if (argDeserialized)
			return;

		getWorld().setGravity(new Vec2(0,0));

		//map limits
		BodyDef bd = new BodyDef();
		Body ground = getWorld().createBody(bd);

		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-50f, 0f), new Vec2(50f,0f));
		ground.createFixture(shape,0);
		shape.setAsEdge(new Vec2(-29,0), new Vec2(-29,50));
		ground.createFixture(shape, 1);
		shape.setAsEdge(new Vec2(29, 0), new Vec2(29, 50));
		ground.createFixture(shape, 1);
		shape.setAsEdge(new Vec2(-50, 30), new Vec2(50, 30));
		ground.createFixture(shape, 1);

		//create the parking spot
		PolygonShape pSpot = new PolygonShape();
		pSpot.setAsEdge(new Vec2(20, 20), new Vec2(20, 50));
		ground.createFixture(pSpot, 1);

		pSpot.setAsEdge(new Vec2(20,10), new Vec2(20, 0));
		ground.createFixture(pSpot, 1);

		//creating car body
		car = new Car(getWorld(), CAR_STARTING_POSITION);

		//creating wheels
		car.addFrontWheel(LEFT_FRONT_WHEEL_POSITION);
		car.addFrontWheel(RIGHT_FRONT_WHEEL_POSITION);
		car.addBackWheel(LEFT_REAR_WHEEL_POSITION);
		car.addBackWheel(RIGHT_REAR_WHEEL_POSITION);
		
		//parking-spot
		BodyDef pspotBodyDef = new BodyDef();
		pspotBodyDef.position = new Vec2(22, 15);
		pspotBodyDef.userData = 0;
		pspot = getWorld().createBody(pspotBodyDef);
		PolygonShape pspotShape = new PolygonShape();
		pspotShape.setAsBox(2, 3);
		FixtureDef pspotDef = new FixtureDef();
		pspotDef.isSensor = true;
		pspotDef.shape = pspotShape;
		pspot.createFixture(pspotDef);

	}

	@Override
	public void step(TestbedSettings settings){
		super.step(settings);

		//distance to parking spot center
		addTextLine("Distance to center of parking spot: " + car.distanceTo(pspot));
		
		//Straight back parking sensor
		ParkingSensor s1 = new ParkingSensor(this, car.getWorldPoint(new Vec2(0, 2.5f)),car.getWorldPoint(new Vec2(0, 6)));
		s1.setSensorName("BackStraight");
		s1.getSensorStatus();

		//Back-Left 45º Parking Sensor
		ParkingSensor s2 = new ParkingSensor(this, car.getWorldPoint(new Vec2(1.5f, 2.5f)), car.getWorldPoint(new Vec2(3.5f, 5)));
		s2.setSensorName("Back45Left");
		s2.getSensorStatus();

		//Back-Right 45º Parking Sensor
		ParkingSensor s3 = new ParkingSensor(this, car.getWorldPoint(new Vec2(-1.5f, 2.5f)), car.getWorldPoint(new Vec2(-3.5f, 5)));
		s3.setSensorName("Back45Right");
		s3.getSensorStatus();

		//Straight front parking sensor
		ParkingSensor s4 = new ParkingSensor(this, car.getWorldPoint(new Vec2(0, -2.5f)),car.getWorldPoint(new Vec2(0, -6)));
		s4.setSensorName("FrontStraight");
		s4.getSensorStatus();

		//Front-Left 45º Parking Sensor
		ParkingSensor s5 = new ParkingSensor(this, car.getWorldPoint(new Vec2(1.5f, -2.5f)), car.getWorldPoint(new Vec2(3.5f, -5)));
		s5.setSensorName("Front45Left");
		s5.getSensorStatus();

		//Front-Right 45º Parking Sensor
		ParkingSensor s6 = new ParkingSensor(this, car.getWorldPoint(new Vec2(-1.5f, -2.5f)), car.getWorldPoint(new Vec2(-3.5f, -5)));
		s6.setSensorName("Front45Right");
		s6.getSensorStatus();

		//Left-Side Parking Sensor 1
		ParkingSensor s7 = new ParkingSensor(this, car.getWorldPoint(new Vec2(1.7f, -1.9f)), car.getWorldPoint(new Vec2(4.7f, -1.9f)));
		s7.setSensorName("LeftSide1");
		s7.getSensorStatus();

		//Left-Side Parking Sensor 2
		ParkingSensor s8 = new ParkingSensor(this, car.getWorldPoint(new Vec2(1.7f, 1.9f)), car.getWorldPoint(new Vec2(4.7f, 1.9f)));
		s8.setSensorName("LeftSide2");
		s8.getSensorStatus();

		//Left-Side Parking Sensor 1
		ParkingSensor s9 = new ParkingSensor(this, car.getWorldPoint(new Vec2(-1.7f, -1.9f)), car.getWorldPoint(new Vec2(-4.7f, -1.9f)));
		s9.setSensorName("RightSide1");
		s9.getSensorStatus();

		//Left-Side Parking Sensor 2
		ParkingSensor s10 = new ParkingSensor(this, car.getWorldPoint(new Vec2(-1.7f, 1.9f)), car.getWorldPoint(new Vec2(-4.7f, 1.9f)));
		s10.setSensorName("RightSide2");
		s10.getSensorStatus();
	}

	@Override
	public void update(){
		super.update();
		getWorld().step(1/30, 8, 8);

		car.killOrthogonalVelocity();
		car.drive(engineSpeed);
		car.steer(steeringAngle, STEER_SPEED);
	}

	@Override
	public void keyReleased(char argKeyChar, int argKeyCode) {
		if (argKeyCode == KeyEvent.VK_UP || argKeyCode == KeyEvent.VK_DOWN)
			engineSpeed = 0;
		if (argKeyCode == KeyEvent.VK_LEFT || argKeyCode == KeyEvent.VK_RIGHT)
			steeringAngle = 0;
	}

	@Override
	public void keyPressed(char key, int argKeyCode){
		switch(argKeyCode){
		case KeyEvent.VK_UP:
			engineSpeed = -HORSPOWER;
			break;
		case KeyEvent.VK_DOWN:
			engineSpeed = HORSPOWER;
			break;
		case KeyEvent.VK_LEFT:
			steeringAngle = MAX_STEER_ANGLE;
			break;
		case KeyEvent.VK_RIGHT:
			steeringAngle = -MAX_STEER_ANGLE;
			break;
		case KeyEvent.VK_O:
			car.superBrake();
			break;

		}
	}

	@Override
	public String getTestName() {
		return "ParkBot";
	}
}
