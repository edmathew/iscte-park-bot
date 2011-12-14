package main_stuff;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import jb2d_framework.TestbedSettings;
import jb2d_framework.TestbedTest;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

import support.Car;
import support.ParkingSensor;
import support.Tagger;


public class CarTest extends TestbedTest{

	public static double DEFAULT_PARKING_SENSOR_DISTANCE = 3.1;

	final static float MAX_STEER_ANGLE = (float) (Math.PI/3);
	final static float STEER_SPEED = 1.5f;
	final static int SIDEWAYS_FRICTION_FORCE = 10;
	final static int HORSPOWER = 75;

	final static Vec2 CAR_STARTING_POSITION = new Vec2(-25,25);

	final static Vec2 LEFT_REAR_WHEEL_POSITION = new Vec2(-1.5f, 1.9f);
	final static Vec2 RIGHT_REAR_WHEEL_POSITION = new Vec2(1.5f, 1.9f);
	final static Vec2 LEFT_FRONT_WHEEL_POSITION = new Vec2(-1.5f, -1.9f);
	final static Vec2 RIGHT_FRONT_WHEEL_POSITION = new Vec2(1.5f, -1.9f);

	final static Vec2 PARKING_SPOT_POSITION = new Vec2(22,15);
	final static Vec2 TOP_LEFT_TAG = new Vec2(-0.9f,2);
	final static Vec2 TOP_RIGHT_TAG = new Vec2(0.9f,2);
	final static Vec2 CENTER_TAG = new Vec2();
	final static Vec2 BOTTOM_LEFT_TAG = new Vec2(-0.5f,-2);
	final static Vec2 BOTTOM_RIGHT_TAG = new Vec2(0.5f, -2);


	static float engineSpeed = 0;
	static float steeringAngle = 0;

	private Car car;
	private LinkedList<Body> tags = new LinkedList<Body>();
	private double score = 9999;


	public boolean isColliding(){
		return super.getPointCount() > 0;
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

		pSpot.setAsEdge(new Vec2(20, 10), new Vec2(30, 10));
		ground.createFixture(pSpot,1);

		pSpot.setAsEdge(new Vec2(20, 20), new Vec2(30, 20));
		ground.createFixture(pSpot,1);

		pSpot.setAsEdge(new Vec2(25, 10), new Vec2(25, 20));
		ground.createFixture(pSpot,1);

		//creating car body
		car = new Car(getWorld(), CAR_STARTING_POSITION);

		//creating wheels
		car.addFrontWheel(LEFT_FRONT_WHEEL_POSITION);
		car.addFrontWheel(RIGHT_FRONT_WHEEL_POSITION);
		car.addBackWheel(LEFT_REAR_WHEEL_POSITION);
		car.addBackWheel(RIGHT_REAR_WHEEL_POSITION);

		//parking sensors
		car.addParkingSensor(new ParkingSensor(this, "FrontStraight", new Vec2(0, -2.5f),new Vec2(0, -5.7f), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "BackStraight", new Vec2(0, 2.5f),new Vec2(0, 5.7f), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "Back45Left", new Vec2(1.5f, 2.5f), new Vec2(3.5f, 5), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "Back45Right", new Vec2(-1.5f, 2.5f), new Vec2(-3.5f, 5), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "Front45Left", new Vec2(1.5f, -2.5f), new Vec2(3.5f, -5), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "Front45Right", new Vec2(-1.5f, -2.5f), new Vec2(-3.5f, -5), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "LeftSide1", new Vec2(1.7f, -1.9f), new Vec2(4.9f, -1.9f), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "LeftSide2", new Vec2(1.7f, 1.9f), new Vec2(4.9f, 1.9f), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "RightSide1", new Vec2(-1.7f, -1.9f), new Vec2(-4.9f, -1.9f), DEFAULT_PARKING_SENSOR_DISTANCE));
		car.addParkingSensor(new ParkingSensor(this, "RightSide2", new Vec2(-1.7f, 1.9f), new Vec2(-4.9f, 1.9f), DEFAULT_PARKING_SENSOR_DISTANCE));

		//parking-spot tag detectors
		car.addTagger("1001", TOP_LEFT_TAG);
		car.addTagger("1002", TOP_RIGHT_TAG);
		car.addTagger("1003", CENTER_TAG);
		car.addTagger("1004", BOTTOM_LEFT_TAG);
		car.addTagger("1005", BOTTOM_RIGHT_TAG);

		//parking-spot
		createParkingSpotTags();



	}

	private void createParkingSpotTags() {
		for (Tagger t : car.getTaggers()){
			BodyDef tbd = new BodyDef();
			tbd.position = PARKING_SPOT_POSITION.clone().add(t.getRelativePosition());
			tbd.userData = Integer.parseInt(t.getName());
			Body tb = getWorld().createBody(tbd);

			PolygonShape ts = new PolygonShape();
			ts.setAsBox(0.25f, 0.25f);
			FixtureDef tfd = new FixtureDef();
			tfd.isSensor = true;
			tfd.shape = ts;
			tb.createFixture(tfd);
			tags.add(tb);
		}
	}

	@Override
	public void step(TestbedSettings settings){
		super.step(settings);

		//distance to parking spot center
		score = car.distanceTo(tags);

		//addTextLine("Distance to center of parking spot: " + score + " nTaggerss: " + car.getTaggers().size() + "nTags: " + tags.size());
		addTextLine("Score: " + score);
		car.updateSensorPositions(car);
		car.outputSensorStatus();
		addTextLine("Colliding? " + super.getPointCount());

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
			car.applyHandBrake();
			break;

		}
	}

	@Override
	public String getTestName() {
		return "ParkBot";
	}

	public Car getCar() {
		return car;
	}

	@Override
	public void reset(){
		super.reset();
		engineSpeed = 0;
		steeringAngle = 0;
		tags = new LinkedList<Body>();
	}
	
	public double getScore() {
		return score;
	}
}
