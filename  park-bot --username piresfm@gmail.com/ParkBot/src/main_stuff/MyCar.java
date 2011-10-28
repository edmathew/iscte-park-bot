package main_stuff;
import java.awt.event.KeyEvent;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonShape;
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
	final static int HORSPOWER = 109;
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
	
	
	@Override
	public void initTest(boolean argDeserialized) {
		if (argDeserialized)
			return;
		
		getWorld().setGravity(new Vec2(0,0));
		
		//let's try and get some ground work
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
		
		
		
	}

	public void killOrthogonalVelocity(Body target){
		Vec2 localPoint = new Vec2(0,0);
		Vec2 velocity = target.getLinearVelocityFromLocalPoint(localPoint);
		
		
		
		
		
		
		Vec2 sidewaysAxis = target.getTransform().R.col2.clone();
//		Mat22.mulToOut(target.getTransform().R, velocity, sidewaysAxis);
//		Vec2 res = Transform.mul(target.getTransform(), sidewaysAxis);
//		target.setLinearVelocity(res);
		
		 /*b2Dot: function(a, b) {
	         return a.x * b.x + a.y * b.y;
	      },*/
		float dotter = velocity.x * sidewaysAxis.x + velocity.y * sidewaysAxis.y;
		sidewaysAxis.x *= dotter;
		sidewaysAxis.y *= dotter;
		target.setLinearVelocity(sidewaysAxis);

		
		
	}
	
	@Override
	public void step(TestbedSettings settings){
		super.step(settings);
		
	}
	
	@Override
	public void update(){
		super.update();
getWorld().step(1/30, 8, 8);
//		System.out.println(engineSpeed);
//		System.out.println(steeringAngle);
		
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
				//rearLeftWheel.applyForce(new Vec2(0, engineSpeed), rearLeftWheel.getPosition());
				//rearRightWheel.applyForce(new Vec2(0, engineSpeed), rearRightWheel.getPosition());
				//frontRightJoint.setMotorSpeed(20f);
				//frontLeftJoint.setMotorSpeed(20f);
				break;
			case KeyEvent.VK_DOWN:
				engineSpeed = HORSPOWER;
				//rearLeftWheel.applyForce(new Vec2(0, engineSpeed), rearLeftWheel.getPosition());
				//rearRightWheel.applyForce(new Vec2(0, engineSpeed), rearRightWheel.getPosition());
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
		return "MyCar";
	}
}
