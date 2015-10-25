package com.ntu.way2fungames;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Nio {
	 public static FloatBuffer WrapRightF(float[] fArryIn){
     	FloatBuffer outbuff;        	
         ByteBuffer tBB = ByteBuffer.allocateDirect(fArryIn.length*Float.SIZE/8);
         tBB.order(ByteOrder.nativeOrder());
         outbuff = tBB.asFloatBuffer();            
         outbuff.put(fArryIn);
         outbuff.rewind();
         return outbuff;        	
     }
}
