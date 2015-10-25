package com.ntu.way2fungames;


import java.math.*;
import java.net.NoRouteToHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public final class Utils {

//        private static Matrix yFlipMatrix;
//        
//        static
//        {
//                yFlipMatrix = new Matrix();
//                yFlipMatrix.postScale(1, -1); // flip Y axis
//        }
//        
//        public static Bitmap getTextureFromBitmapResource(Context context, int resourceId)
//        {
//                Bitmap bitmap = null;
//                try {
//                        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);                        
//                        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), yFlipMatrix, false);
//                        
//                }
//                finally {
//                        if (bitmap != null) {
//                                bitmap.recycle();
//                        }
//                }
//        }
        

        public static float[] ArrayFindMinMax(float[] fa){
        	float max = Float.MIN_VALUE;
        	float min = Float.MAX_VALUE;
        	
        	for (int i=0;i<fa.length;i=i+1){
        		if (fa[i]> max){max =fa[i];}
        		if (fa[i]< min){min =fa[i];}
        	}
			return new float[]{min,max};
        }
        public static float[][] normalizeArray(float[] nFarry){
        	float info[] = ArrayFindMinMax(nFarry);
        	float min = info[0];
        	float max = info[1];
        	float range = max-min;
        	int size = (int) Math.sqrt(nFarry.length);
        	float[][] outar = new float[size][size];
        	int i=0;
        	float nscaled=0;
        	for (int x =0;x<size;x=x+1){
        		for (int y =0;y<size;y=y+1){
        			nscaled =(nFarry[i]-min)/range; 
        			outar[x][y]= nscaled;
        			i=i+1;
        	}}
			return outar;
        }
        
        public static float[][] normalizeArray(float[][] nFarry){
        	float info[] = ArrayFindMinMax(nFarry);
        	float min = info[0];
        	float max = info[1];
        	float range = max-min;
        	int x=0;
        	int y=0;
        	for(float[] e:nFarry){
        		
        		for(float n:e){
        			
        			nFarry[y][x]=((n-min)/range);
        			x=x+1;
        		}
        		x=0;
        		y=y+1;
        	}
        	
			return nFarry;
        }
        private static float[] ArrayFindMinMax(float[][] nFarry) {
         	float max = Float.MIN_VALUE;
        	float min = Float.MAX_VALUE;
        	
        	for(float[] e:nFarry){
        		for(float n:e){
        			if (n > max){max =n;}
            		if (n < min){min =n;}
        		}
        	}
       	
			return new float[]{min,max};
		}
		public static FloatBuffer WrapRightF(float[] fArryIn) {
    		FloatBuffer outbuff;
    		ByteBuffer tBB = ByteBuffer.allocateDirect(fArryIn.length * Float.SIZE/8);
    		tBB.order(ByteOrder.nativeOrder());
    		outbuff = tBB.asFloatBuffer();
    		outbuff.put(fArryIn);
    		outbuff.rewind();
    		return outbuff;
    	}
        public static FloatBuffer WrapRightF(float[] fArryIn,int nLength) {
    		FloatBuffer outbuff;
    		ByteBuffer tBB = ByteBuffer.allocateDirect(nLength * Float.SIZE/8);
    		tBB.order(ByteOrder.nativeOrder());
    		outbuff = tBB.asFloatBuffer();
    		outbuff.put(fArryIn,0,nLength
    				);
    		outbuff.rewind();
    		return outbuff;
    	}
        
        public static float[] SmoothNormals(float[] vertexAr,float[]normalAr){
        	int vIdx = 0;
        	int vIdxScan = 0;
        	float xm;
        	float ym;
        	float xna=0,yna=0,zna=0;
        	float[] tn;
        	
        	while (vIdx< vertexAr.length/100){
        		xm = vertexAr[vIdx+0];
        		ym = vertexAr[vIdx+1];
        		while (vIdxScan< vertexAr.length/100){
            		if((vertexAr[vIdxScan+0]==xm)&(vertexAr[vIdxScan+1]==ym)){
            			xna= xna+normalAr[vIdxScan+0];
            			yna= yna+normalAr[vIdxScan+1];
            			zna= zna+normalAr[vIdxScan+2];
            		}            		
        			
            		vIdxScan=vIdxScan+3;	
        		}
        	tn = CNormalize(new float[]{xna,yna,zna});
        	vIdxScan=0;
        	xna=0;yna=0;zna=0;
    		while (vIdxScan< vertexAr.length/100){
        		if((vertexAr[vIdxScan+0]==xm)&(vertexAr[vIdxScan+1]==ym)){
        			normalAr[vIdxScan+0]=tn[0];
        			normalAr[vIdxScan+1]=tn[1];
        			normalAr[vIdxScan+2]=tn[2];
        		}            		
        		vIdxScan=vIdxScan+3;	
    		}

        	vIdx = vIdx +3;
        	}
			return normalAr;
        }
        
//        static Bitmap GeneratePath(Bitmap bmp,int[][] rArr){
//        	int[][] path;
//        	boolean completed = false;
//        	int sy= rArr.length/2;
//        	int sx = 20;
//        	int ey = rArr.length/2;
//        	int ex= rArr.length-21;
//        	int cx = sx;
//        	int cy = sy;
//        	int d=0;
//        	int currentElevation;
//        	float dRemain,upInportance,downInportance;
//        	
//        	float[] moveCost= new float[5];
//        	float[] moveCost2= new float[5];
//        	int color = Color.argb(255, 128,96,32);
//        	int length = 0;
//        	path = new int [rArr.length][rArr.length];
//        	
//        	while (completed==false){
//        		dRemain = (float) Math.hypot(cx-ex,cy-ey);
//        		upInportance =   ((cy-ey)/dRemain)*(length/250);
//        		downInportance = ((ey-cy)/dRemain)*(length/250);
//        		currentElevation=rArr[cx][cy];
//        		//if (currentElevation==0){bmp = null;return null;}
//        		//if(currentElevation>1000){currentElevation=currentElevation-1000;}
//        		
//        		
//        		moveCost[0] =((rArr[cx+0][cy+1]+path[cx+0][cy+1])-currentElevation);
//        		moveCost[1] =((rArr[cx+1][cy+1]+path[cx+1][cy+1])-currentElevation);
//        		moveCost[2] =((rArr[cx+1][cy+0]+path[cx+1][cy+0])-currentElevation);
//        		moveCost[3] =((rArr[cx+1][cy-1]+path[cx+1][cy-1])-currentElevation);
//        		moveCost[4] =((rArr[cx+0][cy-1]+path[cx+0][cy-1])-currentElevation);
//
//        		for (int i =0;i <5;i =i +1){
//        			if (moveCost[i] <= -currentElevation){moveCost[i]=100000;}
//        		}
//        		
//        		moveCost2[0] =((rArr[cx+0][cy+3]+path[cx+0][cy+3])-currentElevation);
//        		moveCost2[1] =((rArr[cx+3][cy+3]+path[cx+3][cy+3])-currentElevation);
//        		moveCost2[2] =((rArr[cx+3][cy+0]+path[cx+3][cy+0])-currentElevation);
//        		moveCost2[3] =((rArr[cx+3][cy-3]+path[cx+3][cy-3])-currentElevation);
//        		moveCost2[4] =((rArr[cx+0][cy-3]+path[cx+0][cy-3])-currentElevation);
//
//        		for (int i =0;i <5;i =i +1){
//        			if (moveCost2[i] <= -currentElevation){moveCost2[i]=100000;}
//        		}
//
//        		moveCost[0] =((moveCost[0]*.75f)+(moveCost2[0]*.25f));
//        		moveCost[1] =((moveCost[1]*.75f)+(moveCost2[1]*.25f));
//        		moveCost[2] =((moveCost[2]*.75f)+(moveCost2[2]*.25f));
//        		moveCost[3] =((moveCost[3]*.75f)+(moveCost2[3]*.25f));
//        		moveCost[4] =((moveCost[4]*.75f)+(moveCost2[4]*.25f));
//
//        		
//        		moveCost[0]=(moveCost[0]*2.55f)-downInportance+upInportance;
//        		moveCost[1]=(moveCost[1]*.7f)-downInportance+upInportance;
//        		moveCost[2]= moveCost[2]*.7f;
//        		moveCost[3]=(moveCost[3]*.7f)-upInportance+downInportance;
//        		moveCost[4]=(moveCost[4]*2.55f)-upInportance+downInportance;
//        		
//        		moveCost[d]= moveCost[d]*.45f;
//        		if (d>0){moveCost[d-1] = moveCost[d-1]*.9f;}else{moveCost[4] = moveCost[4]*.6f;}
//        		if (d<4){moveCost[d+1] = moveCost[d+1]*.9f;}else{moveCost[3] = moveCost[3]*.6f;}
//        		//d = FindGreaterIdxFloat(moveCost);
//        		
//        		
//        		d = FindLesserIdxFloat(moveCost);
//        		//upCost       =Math.abs(upCost);
//        		//straightCost =Math.abs(straightCost);
//        		//downCost     =Math.abs(downCost);
//
//        		
//        		//if      (upInportance   >.90){d=4;}
//        		//else if (downInportance >.90){d=0;}
//        		//else if (upInportance   >.65){d=3;}
//        		//else if (downInportance >.65){d=1;}
//        		
//        		for (int repeat=0;repeat<2;repeat=repeat+1){
//        			path[cx][cy]=path[cx][cy]+10000;
//        		if     (d==0){cx=cx+0;cy=cy+1;}
//        		else if(d==1){cx=cx+1;cy=cy+1;}        		
//        		else if(d==2){cx=cx+1;cy=cy+0;}
//        		else if(d==3){cx=cx+1;cy=cy-1;}
//        		else if(d==4){cx=cx+0;cy=cy-1;}
//        		
//        		
//        		//cx = cx+1;
//        		if (cx<3){cx=3;};if (cx>rArr.length-4){cx=rArr.length-4;}
//        		if (cy<3){cy=3;};if (cy>rArr.length-4){cy=rArr.length-4;}
//        		
//        		color = Color.argb(255, 128,96,32);
//        		bmp.setPixel(cx, cy, color);
//      			color = Color.argb(128, 255,0,0);
//      			if(length >= 999){color = Color.YELLOW;}
//      			for (int xx=-1;xx<1;xx=xx+1){for (int yy=-1;yy<1;yy=yy+1){
//       				//bmp.setPixel(cx+xx, cy+yy, color);
//      				}}
//      			
//        		if ((cx>=ex)&(cy>=ey)){completed=true;}
//        		length =length+1;
//        		if (length >= 1000){completed =true;}
//        		}
//        		}
//			return bmp;
//        }
        private static int FindGreaterIdxFloat(float[] checkAr) {
        	int i;
        	float low = Float.MIN_VALUE;
        	int fIdx=0 ;
        	float inVal=0;
			for (i=0;i<checkAr.length;i=i+1){
				inVal = checkAr[i];
				if (inVal > low){fIdx=i;low =inVal;} 
			}
			return fIdx;
		}

        private static int FindLesserIdxFloat(float[] checkAr) {
        	int i;
        	float high= Float.MAX_VALUE;
        	int fIdx=0 ;
        	float inVal=0;
			for (i=0;i<checkAr.length;i=i+1){
				inVal = checkAr[i];
				if (inVal < high){fIdx=i;high =inVal;} 
			}
			return fIdx;
		}

		static float DirectionPoint(int sx,int sy,int ex, int ey){
        	return (float) Math.atan2(sx-ex, sy=ey);
        }


		public static float[] CNormalize(float[] vNormal){
		    double Magnitude = CMag(vNormal);			// Get the magnitude
		
		    vNormal[0] /= (float)Magnitude;				// Divide each axis by the magnitude
		    vNormal[1] /= (float)Magnitude;
		    vNormal[2] /= (float)Magnitude;
		
		    return vNormal;								// Return the normal
		}


		public static  float CMag(float[] norm) {
		    return (float)Math.sqrt(norm[0]*norm[0] + norm[1]*norm[1] + norm[2]*norm[2]);
		}
        
        
}




