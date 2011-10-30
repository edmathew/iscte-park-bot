package support;

import java.util.LinkedList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Car {
	World world;
	Body chassis;
	Body windshield;
	Body frontLeftWheel;
	Body frontRightWheel;

	LinkedList<Body> frontWheels = new LinkedList<Body>();
	LinkedList<Body> backWheels = new LinkedList<Body>();
	LinkedList<RevoluteJoint> frontWheelJoints = new LinkedList<RevoluteJoint>();

	public Car(World world, Vec2 position) {
		this.world = world;
		BodyDef chassisDef = new BodyDef();
		chassisDef = new BodyDef();
		chassisDef.linearDamping = 1;
		chassisDef.angularDamping = 1;
		chassisDef.position = position;
		chassisDef.type = BodyType.DYNAMIC;

		BodyDef windshieldDef = new BodyDef();
		windshieldDef.position = position.clone().addLocal(0,-1);
		windshieldDef.type = BodyType.DYNAMIC;


		chassis = create(chassisDef);
		windshield = create(windshieldDef);



		PolygonShape chassisShape = new PolygonShape();
		chassisShape.setAsBox(1.5f, 2.5f);

		PolygonShape windshieldShape = new PolygonShape();
		windshieldShape.setAsBox(0.7f, 0.5f);


		chassis.createFixture(chassisShape, 1);
		windshield.createFixture(windshieldShape, 1);

		PrismaticJointDef windshieldJointDef = new PrismaticJointDef();
		windshieldJointDef.initialize(chassis, windshield, windshield.getWorldCenter(), new Vec2(1, 0));
		windshieldJointDef.enableLimit = true;
		windshieldJointDef.lowerTranslation = windshieldJointDef.upperTranslation = 0;
		create(windshieldJointDef);

	}

	private Body create(BodyDef bodyDef) {
		return world.createBody(bodyDef);
	}

	private Joint create(JointDef jointDef){
		return world.createJoint(jointDef);
	}

	private Body addWheel(Vec2 position){
		BodyDef wheelDef = new BodyDef();
		wheelDef.position = chassis.getPosition().clone().add(position);
		wheelDef.type = BodyType.DYNAMIC;
		Body wheel = create(wheelDef);

		PolygonShape wheelShape = new PolygonShape();
		wheelShape.setAsBox(0.2f, 0.5f);
		wheel.createFixture(wheelShape, 1);

		return wheel;
	}

	public void addFrontWheel(Vec2 position){
		Body wheel = addWheel(position);
		RevoluteJointDef wheelJointDef = new RevoluteJointDef();
		wheelJointDef.initialize(chassis, wheel, wheel.getWorldCenter());
		wheelJointDef.enableMotor = true;
		wheelJointDef.maxMotorTorque = 100;
		frontWheelJoints.add((RevoluteJoint) create(wheelJointDef));
		frontWheels.add(wheel);

	}

	public void addBackWheel(Vec2 position){
		Body wheel = addWheel(position);
		PrismaticJointDef wheelJointDef = new PrismaticJointDef();
		wheelJointDef.initialize(chassis, wheel, wheel.getWorldCenter(), new Vec2(1, 0));
		wheelJointDef.enableLimit = true;
		wheelJointDef.lowerTranslation = wheelJointDef.upperTranslation = 0;
		create(wheelJointDef);
		backWheels.add(wheel);
	}

	public Vec2 getWorldPoint(Vec2 vec) {
		return chassis.getWorldPoint(vec);
	}

	private LinkedList<Body> getWheels() {
		@SuppressWarnings("unchecked")
		LinkedList<Body> temp = (LinkedList<Body>) backWheels.clone();
		temp.addAll(frontWheels);

		return temp;
	}

	public void drive(float engineSpeed) {
		/*
		 * This method could be much more handsome code-wise, however
		 * I'm completely paranoid that the millisecond between the forces
		 * being applied to each of the wheels can affect the car's movement,
		 * so: screw beautiful, I don't want my car understeering into a wall
		 * or something. 
		 */
		LinkedList<Vec2> direction_list = new LinkedList<Vec2>();
		for(Body w : frontWheels){
			Vec2 direction = w.getTransform().R.col2.clone();
			direction.x *= engineSpeed;
			direction.y *= engineSpeed;
			direction_list.add(direction);
		}
		for(Body w : frontWheels)
			w.applyForce(direction_list.pop(), w.getPosition());
	}
	
	public void steer(float steeringAngle, float steerSpeed) {
		float mspeed;
		for(RevoluteJoint rj : frontWheelJoints){
			mspeed = steeringAngle - rj.getJointAngle();
			rj.setMotorSpeed(mspeed*steerSpeed);
		}
	}

	public void killOrthogonalVelocity() {
		/*
		 * weird code for the same reason as in drive method
		 */
		LinkedList<Vec2> direction_list = new LinkedList<Vec2>();
		for (Body w: getWheels()){
			Vec2 localPoint = new Vec2(0,0);
			Vec2 velocity = w.getLinearVelocityFromLocalPoint(localPoint);

			Vec2 sidewaysAxis = w.getTransform().R.col2.clone();
			float dotter = velocity.x * sidewaysAxis.x + velocity.y * sidewaysAxis.y;
			sidewaysAxis.x *= dotter;
			sidewaysAxis.y *= dotter;
			direction_list.add(sidewaysAxis);
		}
		
		for (Body w: getWheels())
			w.setLinearVelocity(direction_list.pop());
	}



}