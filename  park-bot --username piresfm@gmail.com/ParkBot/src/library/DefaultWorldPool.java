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
 * Created at 3:26:14 AM Jan 11, 2011
 */
package library;

import java.util.HashMap;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.Distance;
import org.jbox2d.collision.TimeOfImpact;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.jbox2d.dynamics.contacts.CircleContact;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.PolygonAndCircleContact;
import org.jbox2d.dynamics.contacts.PolygonContact;
import org.jbox2d.pooling.IDynamicStack;
import org.jbox2d.pooling.IOrderedStack;
import org.jbox2d.pooling.IWorldPool;
import org.jbox2d.pooling.MutableStack;
import org.jbox2d.pooling.OrderedStack;

/**
 * Provides object pooling for all objects used in the engine. Objects retrieved from here should
 * only be used temporarily, and then pushed back (with the exception of arrays).
 * 
 * @author Daniel Murphy
 */
public class DefaultWorldPool implements IWorldPool {

  private final OrderedStack<Vec2> vecs;
  private final OrderedStack<Vec3> vec3s;
  private final OrderedStack<Mat22> mats;
  private final OrderedStack<AABB> aabbs;

  private final HashMap<Integer, float[]> afloats = new HashMap<Integer, float[]>();
  private final HashMap<Integer, int[]> aints = new HashMap<Integer, int[]>();
  private final HashMap<Integer, Vec2[]> avecs = new HashMap<Integer, Vec2[]>();

  private final Class<?>[] classes = new Class<?>[] { IWorldPool.class };
  private final Object[] args = new Object[] { this };

  private final MutableStack<Contact, PolygonContact> pcstack = new MutableStack<Contact, PolygonContact>(
      PolygonContact.class, Settings.CONTACT_STACK_INIT_SIZE, classes, args);

  private final MutableStack<Contact, CircleContact> ccstack = new MutableStack<Contact, CircleContact>(
      CircleContact.class, Settings.CONTACT_STACK_INIT_SIZE, classes, args);

  private final MutableStack<Contact, PolygonAndCircleContact> cpstack = new MutableStack<Contact, PolygonAndCircleContact>(
      PolygonAndCircleContact.class, Settings.CONTACT_STACK_INIT_SIZE, classes, args);

  private final Collision collision;
  private final TimeOfImpact toi;
  private final Distance dist;

  public DefaultWorldPool(int argSize, int argContainerSize) {
    vecs = new OrderedStack<Vec2>(Vec2.class, argSize, argContainerSize);
    vec3s = new OrderedStack<Vec3>(Vec3.class, argSize, argContainerSize);
    mats = new OrderedStack<Mat22>(Mat22.class, argSize, argContainerSize);
    aabbs = new OrderedStack<AABB>(AABB.class, argSize, argContainerSize);

    dist = new Distance();
    collision = new Collision(this);
    toi = new TimeOfImpact(this);
  }

  public final IDynamicStack<Contact> getPolyContactStack() {
    return pcstack;
  }

  public final IDynamicStack<Contact> getCircleContactStack() {
    return ccstack;
  }

  public final IDynamicStack<Contact> getPolyCircleContactStack() {
    return cpstack;
  }

  public final Vec2 popVec2() {
    return vecs.pop();
  }

  public final Vec2[] popVec2(int argNum) {
    return vecs.pop(argNum);
  }
  
  public final void pushVec2(int argNum) {
    vecs.push(argNum);
  }

  public final Vec3 popVec3() {
    return vec3s.pop();
  }
  
  public final Vec3[] popVec3(int argNum) {
    return vec3s.pop(argNum);
  }

  public final void pushVec3(int argNum) {
    vec3s.push(argNum);
  }

  public final Mat22 popMat22() {
    return mats.pop();
  }

  public final Mat22[] popMat22(int argNum) {
    return mats.pop(argNum);
  }

  public final void pushMat22(int argNum) {
    mats.push(argNum);
  }

  public final AABB popAABB() {
    return aabbs.pop();
  }

  public final AABB[] popAABB(int argNum) {
    return aabbs.pop(argNum);
  }

  public final void pushAABB(int argNum) {
    aabbs.push(argNum);
  }

  public final Collision getCollision() {
    return collision;
  }

  public final TimeOfImpact getTimeOfImpact() {
    return toi;
  }

  public final Distance getDistance() {
    return dist;
  }

  public final float[] getFloatArray(int argLength) {
    if (!afloats.containsKey(argLength)) {
      afloats.put(argLength, new float[argLength]);
    }

    assert (afloats.get(argLength).length == argLength) : "Array not built with correct length";
    return afloats.get(argLength);
  }

  public final int[] getIntArray(int argLength) {
    if (!aints.containsKey(argLength)) {
      aints.put(argLength, new int[argLength]);
    }

    assert (aints.get(argLength).length == argLength) : "Array not built with correct length";
    return aints.get(argLength);
  }

  public final Vec2[] getVec2Array(int argLength) {
    if (!avecs.containsKey(argLength)) {
      Vec2[] ray = new Vec2[argLength];
      for (int i = 0; i < argLength; i++) {
        ray[i] = new Vec2();
      }
      avecs.put(argLength, ray);
    }

    assert (avecs.get(argLength).length == argLength) : "Array not built with correct length";
    return avecs.get(argLength);
  }

@Override
public IOrderedStack<AABB> getAABBStack() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IOrderedStack<Mat22> getMat22Stack() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IOrderedStack<Vec2> getVec2Stack() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public IOrderedStack<Vec3> getVec3Stack() {
	// TODO Auto-generated method stub
	return null;
}
}
