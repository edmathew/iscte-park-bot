package support;

import library.RayCastClosestCallback;
import main_stuff.MyCar;

import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;

public class ParkingSensor {
	private String sensorName;
	private Vec2 p1 = new Vec2();
	private Vec2 p2 = new Vec2();
	private MyCar car;
	private RayCastClosestCallback rccc;

	public ParkingSensor(MyCar car, Vec2 p1, Vec2 p2) {
		this.p1.set(p1);
		this.p2.set(p2);
		this.p2.add(p1);
		this.car = car;
		rccc = new RayCastClosestCallback();
		rccc.init();
		car.getWorld().raycast(rccc, p1, p2);
	}

	public void getSensorStatus() {
		if(rccc.m_hit){
			car.getDebugDraw().drawPoint(rccc.m_point, 5, new Color3f(0.4f, 0.9f, 0.4f));
			car.getDebugDraw().drawSegment(p1, rccc.m_point, new Color3f(0.9f, 0.9f, 0.9f));
			car.addTextLine(sensorName +" to obstacle: " + rccc.m_point.clone().sub(p1).length());
		}else{
			car.getDebugDraw().drawSegment(p1, p2, new Color3f(0.9f, 0.9f, 0.9f));
		}
	}
	
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	public String getSensorName() {
		return sensorName;
	}
	
	
}
