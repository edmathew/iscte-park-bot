package main_stuff;
import java.awt.event.KeyEvent;

import library.RayCastClosestCallback;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import framework.TestbedSettings;
import framework.TestbedTest;


public class MyCar extends TestbedTest{

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
	
	static World myWorld;
	static AABB worldBox = new AABB();
	
	Body carBody;
	RevoluteJoint frontLeftJoint;
	RevoluteJoint frontRightJoint;
	
	Body frontLeftWheel;
	Body frontRightWheel;
	Body rearLeftWheel;
	Body rearRightWheel;
	
	//parking sensors
	RayCastClosestCallback s1 = new RayCastClosestCallback();
	RayCastClosestCallback s2 = new RayCastClosestCallback();
	RayCastClosestCallback s3 = new RayCastClosestCallback();
	RayCastClosestCallback s4 = new RayCastClosestCallback();
	RayCastClosestCallback s5 = new RayCastClosestCallback();
	
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
		BodyDef carBodyDef = new BodyDef();
		carBodyDef.linearDamping = 1;
		carBodyDef.angularDamping = 1;
		carBodyDef.position = CAR_STARTING_POSITION;
		carBodyDef.type = BodyType.DYNAMIC;
		
		carBody = getWorld().createBody(carBodyDef);
		
		BodyDef windshieldDef = new BodyDef();
		windshieldDef.position = CAR_STARTING_POSITION.clone().addLocal(0, -1);
		windshieldDef.type = BodyType.DYNAMIC;
		
		Body windshield = getWorld().createBody(windshieldDef);
		
		//creating wheels
		
		BodyDef frontLeftWheelDef = new BodyDef();
		frontLeftWheelDef.position = CAR_STARTING_POSITION.clone().add(LEFT_FRONT_WHEEL_POSITION);
		frontLeftWheelDef.type = BodyType.DYNAMIC;
		frontLeftWheel = getWorld().createBody(frontLeftWheelDef);
		
		BodyDef frontRightWheelDef = new BodyDef();
		frontRightWheelDef.position = CAR_STARTING_POSITION.clone().add(RIGHT_FRONT_WHEEL_POSITION);
		frontRightWheelDef.type = BodyType.DYNAMIC;
		frontRightWheel = getWorld().createBody(frontRightWheelDef);
		
		BodyDef rearLeftWheelDef = new BodyDef();
		rearLeftWheelDef.position = CAR_STARTING_POSITION.clone().add(LEFT_REAR_WHEEL_POSITION);
		rearLeftWheelDef.type = BodyType.DYNAMIC;
		rearLeftWheel = getWorld().createBody(rearLeftWheelDef);
		
		BodyDef rearRightWheelDef = new BodyDef();
		rearRightWheelDef.position = CAR_STARTING_POSITION.clone().add(RIGHT_REAR_WHEEL_POSITION);
		rearRightWheelDef.type = BodyType.DYNAMIC;
		rearRightWheel = getWorld().createBody(rearRightWheelDef);
		
		
		//shapes
		PolygonShape carBodyShape = new PolygonShape();
		carBodyShape.setAsBox(1.5f, 2.5f);
		carBody.createFixture(carBodyShape, 1);
		
		PolygonShape windshieldShape = new PolygonShape();
		windshieldShape.setAsBox(0.7f, 0.5f);
		windshield.createFixture(windshieldShape, 1);
		
		
		PolygonShape wheel = new PolygonShape();
		wheel.setAsBox(0.2f, 0.5f);
		
		frontLeftWheel.createFixture(wheel, 1);
		frontRightWheel.createFixture(wheel, 1);
		rearLeftWheel.createFixture(wheel, 1);
		rearRightWheel.createFixture(wheel, 1);
		
		//joints
		RevoluteJointDef frontLeftJointDef = new RevoluteJointDef();
		frontLeftJointDef.initialize(carBody, frontLeftWheel, frontLeftWheel.getWorldCenter());
		frontLeftJointDef.enableMotor = true;
		frontLeftJointDef.maxMotorTorque = 100;
		
		RevoluteJointDef frontRightJointDef = new RevoluteJointDef();
		frontRightJointDef.initialize(carBody, frontRightWheel, frontRightWheel.getWorldCenter());
		frontRightJointDef.enableMotor = true;
		frontRightJointDef.maxMotorTorque = 100;
		
		PrismaticJointDef rearLeftJointDef = new PrismaticJointDef();
		rearLeftJointDef.initialize(carBody, rearLeftWheel, rearLeftWheel.getWorldCenter(), new Vec2(1,0));
		rearLeftJointDef.enableLimit = true;
		rearLeftJointDef.lowerTranslation = rearLeftJointDef.upperTranslation = 0;
		
		PrismaticJointDef rearRightJointDef = new PrismaticJointDef();
		rearRightJointDef.initialize(carBody, rearRightWheel, rearRightWheel.getWorldCenter(), new Vec2(1,0));
		rearRightJointDef.enableLimit = true;
		rearRightJointDef.lowerTranslation = rearRightJointDef.upperTranslation = 0;
		
		frontRightJoint = (RevoluteJoint)getWorld().createJoint(frontRightJointDef);
		frontLeftJoint = (RevoluteJoint)getWorld().createJoint(frontLeftJointDef);
		
		getWorld().createJoint(rearRightJointDef);
		getWorld().createJoint(rearLeftJointDef);
		
		PrismaticJointDef windshieldJointDef = new PrismaticJointDef();
		windshieldJointDef.initialize(carBody, windshield, windshield.getWorldCenter(), new Vec2(1, 0));
		windshieldJointDef.enableLimit = true;
		windshieldJointDef.lowerTranslation = windshieldJointDef.upperTranslation = 0;
		getWorld().createJoint(windshieldJointDef);
		
	}

	public void killOrthogonalVelocity(Body target){
		Vec2 localPoint = new Vec2(0,0);
		Vec2 velocity = target.getLinearVelocityFromLocalPoint(localPoint);
		
		
		
		
		
		
		Vec2 sidewaysAxis = target.getTransform().R.col2.clone();
		float dotter = velocity.x * sidewaysAxis.x + velocity.y * sidewaysAxis.y;
		sidewaysAxis.x *= dotter;
		sidewaysAxis.y *= dotter;
		target.setLinearVelocity(sidewaysAxis);

		
		
	}
	
	
	
	@Override
	public void step(TestbedSettings settings){
		super.step(settings);

		//Straight back parking sensor
		Vec2 s1p1 = new Vec2();
		Vec2 s1p2 = new Vec2();
		s1p1.set(carBody.getWorldPoint(new Vec2(0, 2.5f)));
		s1p2.set(carBody.getWorldPoint(new Vec2(0, 6)));
		s1p2.add(s1p1);
		s1.init();
		getWorld().raycast(s1, s1p1, s1p2);

		if(s1.m_hit){
			getDebugDraw().drawPoint(s1.m_point, 5, new Color3f(0.4f, 0.9f, 0.4f));
			getDebugDraw().drawSegment(s1p1, s1.m_point, new Color3f(0.9f, 0.9f, 0.9f));
			addTextLine("S1 to obstacle: " + s1.m_point.clone().sub(s1p1).length());
		}else{
			getDebugDraw().drawSegment(s1p1, s1p2, new Color3f(0.9f, 0.9f, 0.9f));
		}
		
		//Back-Left 45º Parking Sensor
		Vec2 s2p1 = new Vec2();
		Vec2 s2p2 = new Vec2();
		s2p1.set(carBody.getWorldPoint(new Vec2(1.5f, 2.5f)));
		s2p2.set(carBody.getWorldPoint(new Vec2(3.5f, 5)));
		s2p2.add(s2p1);
		s2.init();
		getWorld().raycast(s2, s2p1, s2p2);
		
		if (s2.m_hit){
			getDebugDraw().drawPoint(s2.m_point, 5, new Color3f(0.4f, 0.9f, 0.4f));
			getDebugDraw().drawSegment(s2p1, s2.m_point, new Color3f(0.9f, 0.9f, 0.9f));
			addTextLine("S2 to obstacle: " + s2.m_point.clone().sub(s2p1).length());
		}else{
			getDebugDraw().drawSegment(s2p1, s2p2, new Color3f(0.9f, 0.9f, 0.9f));
		}
		
		//Back-Right 45º Parking Sensor
		Vec2 s3p1 = new Vec2();
		Vec2 s3p2 = new Vec2();
		s3p1.set(carBody.getWorldPoint(new Vec2(-1.5f, 2.5f)));
		s3p2.set(carBody.getWorldPoint(new Vec2(-3.5f, 5)));
		s3p2.add(s3p1);
		s3.init();
		getWorld().raycast(s3, s3p1, s3p2);
		
		if (s3.m_hit){
			getDebugDraw().drawPoint(s3.m_point, 5, new Color3f(0.4f, 0.9f, 0.4f));
			getDebugDraw().drawSegment(s3p1, s3.m_point, new Color3f(0.9f, 0.9f, 0.9f));
			addTextLine("S3 to obstacle: " + s3.m_point.clone().sub(s3p1).length());
		}else{
			getDebugDraw().drawSegment(s3p1, s3p2, new Color3f(0.9f, 0.9f, 0.9f));
		}
		
	}
	
	@Override
	public void update(){
		super.update();
		getWorld().step(1/30, 8, 8);
		
		killOrthogonalVelocity(frontLeftWheel);
		killOrthogonalVelocity(frontRightWheel);
		killOrthogonalVelocity(rearLeftWheel);
		killOrthogonalVelocity(rearRightWheel);
		
		//drive

		Vec2 lDirection = frontLeftWheel.getTransform().R.col2.clone();
		lDirection.x *= engineSpeed;
		lDirection.y *= engineSpeed;
		Vec2 rDirection = frontRightWheel.getTransform().R.col2.clone();
		rDirection.x *= engineSpeed;
		rDirection.y *= engineSpeed;
		
		
		frontLeftWheel.applyForce(lDirection, frontLeftWheel.getPosition());
		frontRightWheel.applyForce(rDirection, frontRightWheel.getPosition());
		
		//steer
		float mspeed;
		mspeed = steeringAngle - frontLeftJoint.getJointAngle();
		frontLeftJoint.setMotorSpeed(mspeed*STEER_SPEED);
		mspeed = steeringAngle - frontRightJoint.getJointAngle();
		frontRightJoint.setMotorSpeed(mspeed*STEER_SPEED);
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
			
		}
	}
	
	 public void accelerate(){
		  System.out.println("accelerating");
		  engineSpeed =- HORSPOWER;
	  }
	  
	  public void brake(){
		  System.out.println("braking");
		  engineSpeed += HORSPOWER;
	  }
	  
	  public void turnLeft(){
		  System.out.println("turning left");
		  steeringAngle = MAX_STEER_ANGLE;
		  
	  }
	  
	  public void turnRight(){
		  System.out.println("turning right");
		  steeringAngle = -MAX_STEER_ANGLE;
	  }

	@Override
	public String getTestName() {
		return "ParkBot";
	}
}
