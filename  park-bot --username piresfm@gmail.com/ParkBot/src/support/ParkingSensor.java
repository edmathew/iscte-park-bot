package support;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import library.RayCastClosestCallback;
import main_stuff.CarTest;

import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;

public class ParkingSensor {
	private String sensorName;
	private Vec2 v1 = new Vec2();
	private Vec2 v2 = new Vec2();
	private Vec2 p1 = new Vec2();
	private Vec2 p2 = new Vec2();
	private CarTest car;
	private RayCastClosestCallback rccc;
	double last_recorded_distance;

	public ParkingSensor(CarTest car, String name, Vec2 v1, Vec2 v2) {
		this.sensorName = name;
		this.v1.set(v1);
		this.v2.set(v2);
		this.v2.add(v1);
		this.car = car;
	}

	public void getSensorStatus() {
		rccc = new RayCastClosestCallback();
		rccc.init();
		car.getWorld().raycast(rccc, p1, p2);
		if(rccc.m_hit){
			car.getDebugDraw().drawPoint(rccc.m_point, 5, new Color3f(0.4f, 0.9f, 0.4f));
			car.getDebugDraw().drawSegment(p1, rccc.m_point, new Color3f(0.9f, 0.9f, 0.9f));
			double d = rccc.m_point.clone().sub(p1).length();
			BigDecimal bd = new BigDecimal(d);
			bd = bd.setScale(1, BigDecimal.ROUND_DOWN);
			car.addTextLine(sensorName +" to obstacle: " + bd.doubleValue());
			last_recorded_distance = bd.doubleValue();
		}else{
			car.getDebugDraw().drawSegment(p1, p2, new Color3f(0.9f, 0.9f, 0.9f));
			last_recorded_distance = 9999;
		}
	}
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	public String getSensorName() {
		return sensorName;
	}

	public void updatePosition(Car car) {
		p1.set(car.getWorldPoint(v1));
		p2.set(car.getWorldPoint(v2));
		p2.add(p1);
	}

	public double getSensorDistanceToObject() {
		return last_recorded_distance;
	}
	
	
}
