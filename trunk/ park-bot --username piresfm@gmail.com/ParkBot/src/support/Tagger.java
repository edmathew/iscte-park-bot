package support;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class Tagger {
	String name;
	Body taggerBody;
	Vec2 relativePosition;
	
	
	public Tagger(String name, Body taggerBody, Vec2 position) {
		this.name = name;
		this.taggerBody = taggerBody;
		this.relativePosition = position;
	}
	
	public Body getTaggerBody() {
		return taggerBody;
	};
	
	public Vec2 getRelativePosition() {
		return relativePosition;
	}
	
	public String getName() {
		return name;
	}

}
