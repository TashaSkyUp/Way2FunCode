package com.ntu.way2fungames;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;



public class LoadFloatArray {
	public static float[] FromFile(File nf){

		return null;
	}

	public static FloatBuffer FromDataInputStream(DataInputStream dis) {
		//try {totalBytes = dis.available();} catch (IOException e) {e.printStackTrace();}
		int totalFloats = 0;
		try {
			totalFloats = (int) dis.readFloat();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		float retArray[] = new float[totalFloats];
		
		FloatBuffer retArrayWrapped = FloatBuffer.wrap(retArray);
        ByteBuffer tBB = ByteBuffer.allocateDirect(totalFloats*Float.SIZE/8);
        tBB.order(ByteOrder.nativeOrder());
        retArrayWrapped = tBB.asFloatBuffer();            

		for (int floatsLeft=totalFloats;floatsLeft>0;floatsLeft=floatsLeft-1){
			try {
				retArrayWrapped.put(dis.readFloat());
				//Log.w("io", String.valueOf(totalFloats-floatsLeft));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		retArrayWrapped.rewind();
		return retArrayWrapped;
	}
}
