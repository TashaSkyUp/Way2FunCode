package com.nsu.way2fungames;

import java.nio.Buffer;

import com.nsu.way2fungames.*;
public class Fractal3d {

	public static float[] vectorProductMJN(float[] a, float[] b){
	    //answer[0] = (a[1]*b[2])-(b[1]*a[2]);
	    //answer[1] = (a[2]*b[0])-(b[2]*a[0]);
	    //answer[2] = (a[0]*b[1])-(b[0]*a[1]);
		
	    return new float[]{(a[1]*b[2])-(b[1]*a[2]),(a[2]*b[0])-(b[2]*a[0]),(a[0]*b[1])-(b[0]*a[1])};
	}
	
	public static Buffer[] FractalHeightValueTo3DTris(float[][] inArr){
		
		int[][] outa = new int[inArr.length][inArr[0].length];
		
		for (int i = 0;i<inArr.length;i++){for (int ii = 0;ii<inArr[0].length;ii++){
				outa[i][ii]= Math.round(inArr[i][ii]);
		}}
		
		return FractalHeightValueTo3DTris(outa);
		
	}
	public static Buffer[] FractalHeightValueTo3DTris(int[][] inArr){
			float sizeX  = 1.0f;
			float startX = -sizeX/2f;
			float sizeY  =  .685f*3f;
			float startY = -sizeY/2f;
			
			int dataSizeX = 128;
			int dataStartX = 0;
			
			int dataStartY = 1;//(int) (.25f*inArr[0].length);
			int dataSizeY =  inArr.length;//;(int) (.50f*inArr[0].length);
			
		 	int	i, j;
		    float	x, z,tx,ty; 
		    int skp=0;
		    int	subSize = (int) inArr.length-skp;
		    int size= subSize;
		    size++;
		    int baseArSize = 200000;
		    float[] texAr =   new float [baseArSize*3];
		    float[] stripAr = new float [baseArSize*3];
		    float[] normalAr = new float[baseArSize*3];
		    
		    int sIdx1 =0;
		    int tIdx1 =0;
		    int nIdx1 =0;
		    
		    	
		    float incX = (sizeX/dataSizeX)*skp;
		    float incY = (sizeY/dataSizeY)*skp;
		    
		    float texStartX =  132f/512f;
		    float texEndX   = (132f+249f)/512f;
		    float texStartY = .0f;//(64+0  )/256f;
		    float texEndY=    1.00f;//(64+128)/256f;
		    
		    float texRangeX = texEndX-texStartX;
		    float tincX =(texRangeX/dataSizeX)*skp;
		    tx = texStartX;
		    
		    float texRangeY = texEndY-texStartY;
		    float tincY =(texRangeY/dataSizeY)*skp;
		    ty = texStartY;
		    
		    float[] tn;
		    float[] p1 = new float[3];
		    float[] p2 = new float[3];
		    float[] p3 = new float[3];
		    
		    z = startY;
		    ty = texStartY;
	
		    for (i=dataStartY; i<(dataStartY+dataSizeY); i=i+skp) {
		    	x = startX;
		    	tx=texStartX;
	
		    	for (j=dataStartX; j<(dataStartX+dataSizeX); j=j+skp) {
					
				if (((j+skp)<(dataStartX+dataSizeX))&((i+skp)<(dataStartY+dataSizeY))){
					
				
				//1st&6th,3rd&4th,5th&7th,4th&8th	
				//p1	
	    			p1[0]=x;
	    			p1[1]=z+incY;
	    			p1[2]=inArr[j][i+skp]/256f;;
	    			
	    			stripAr[sIdx1+0] = p1[0];stripAr[sIdx1+1] = p1[1];stripAr[sIdx1+2] = p1[2];
	    			//stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
	    			sIdx1 = sIdx1+3;
	    			
	    			texAr[tIdx1+0]= tx;
	    			texAr[tIdx1+1]= ty+tincY;
	    			tIdx1 = tIdx1+2;
				
				////p2        			
	    			p2[0]=x;
	    			p2[1]=z;
	    			p2[2]=inArr[j][i]/256f;;
	    			
	    			stripAr[sIdx1+0] = p2[0];stripAr[sIdx1+1] = p2[1];stripAr[sIdx1+2] = p2[2];
	    			//stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
	    			sIdx1 = sIdx1+3;
	    			texAr[tIdx1+0]= tx;
	    			texAr[tIdx1+1]= ty;
	    			tIdx1 = tIdx1+2;
	    			
				////p3
	    			p3[0]=x+incX;
	    			p3[1]=z;
	    			p3[2]=inArr[j+skp][i]/256f;
	    			
	    			stripAr[sIdx1+0] = p3[0];stripAr[sIdx1+1] = p3[1];stripAr[sIdx1+2] = p3[2];
	    			//stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
	    			sIdx1 = sIdx1+3;
	    			texAr[tIdx1+0]= tx+tincX;
	    			texAr[tIdx1+1]= ty;
	    			tIdx1 = tIdx1+2;
	    			
				//normal
	    			
	    			tn = GetSmoothedNormal(j, i+1, inArr);
	    			//tn= 
	    			normalAr[nIdx1+0]= tn[0];
	    			normalAr[nIdx1+1]= tn[1];
	    			normalAr[nIdx1+2]= tn[2];
	    			
	    			tn= GetSmoothedNormal(j, i, inArr);
	    			normalAr[nIdx1+3]= tn[0];
	    			normalAr[nIdx1+4]= tn[1];
	    			normalAr[nIdx1+5]= tn[2];
	    			
	    			tn= GetSmoothedNormal(j+1, i, inArr);
	    			normalAr[nIdx1+6]= tn[0];
	    			normalAr[nIdx1+7]= tn[1];
	    			normalAr[nIdx1+8]= tn[2];
	    			
	    			nIdx1=nIdx1+9;
				
	
				
				
				//tri 2
				
				
	
				stripAr[sIdx1+0] = x+incX;
				stripAr[sIdx1+1] = z;//actually y
				stripAr[sIdx1+2] = inArr[j+skp][i];
				stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
				sIdx1 = sIdx1+3;
				texAr[tIdx1+0]= tx+tincX;
				texAr[tIdx1+1]= ty;
				tIdx1 = tIdx1+2;
	
				stripAr[sIdx1+0] = x+incX;
				stripAr[sIdx1+1] = z+incY;//actually y
				stripAr[sIdx1+2] = inArr[j+skp][i+skp];
				stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
				sIdx1 = sIdx1+3;
				texAr[tIdx1+0]= tx+tincX;
				texAr[tIdx1+1]= ty+tincY;
				tIdx1 = tIdx1+2;
	
				stripAr[sIdx1+0] = x;
				stripAr[sIdx1+1] = z+incY;//actually y
				stripAr[sIdx1+2] = inArr[j][i+skp];
				stripAr[sIdx1+2]= stripAr[sIdx1+2]/256f;
				sIdx1 = sIdx1+3;
				texAr[tIdx1+0]= tx;
				texAr[tIdx1+1]= ty+tincY;
				tIdx1 = tIdx1+2;
				
				p1[0]=x+incX;
				p1[1]=z;
				p1[2]=inArr[j+skp][i]/256f;;
				
				p2[0]=x+incX;
				p2[1]=z+incY;
				p2[2]=inArr[j+skp][i+skp]/256f;;
				
				p3[0]=x;
				p3[1]=z+incX;
				p3[2]=inArr[j][i+skp]/256f;;
				//normal
				
				tn= GetSmoothedNormal(j, i+1, inArr);
				//tn= 
				normalAr[nIdx1+0]= tn[0];
				normalAr[nIdx1+1]= tn[1];
				normalAr[nIdx1+2]= tn[2];
				
				tn= GetSmoothedNormal(j+1, i+1, inArr);
				normalAr[nIdx1+3]= tn[0];
				normalAr[nIdx1+4]= tn[1];
				normalAr[nIdx1+5]= tn[2];
				
				tn= GetSmoothedNormal(j+1, i, inArr);
				normalAr[nIdx1+6]= tn[0];
				normalAr[nIdx1+7]= tn[1];
				normalAr[nIdx1+8]= tn[2];
				
				nIdx1=nIdx1+9;
				
				}
				
			    x += incX;
			    tx=tx+tincX;
			    
			}
			z += incY;
			ty=ty+tincY;
		    }
		    //normalAr= SmoothNormals(stripAr, normalAr);
		return new Buffer[]{Utils.WrapRightF(stripAr,sIdx1),Utils.WrapRightF(texAr,tIdx1),Utils.WrapRightF(normalAr,nIdx1)};
	}

	public static float[] GetTriNormal(float[] a, float[] b, float[] c){
	    return vectorProductMJN(subMJN(b,a),subMJN(c,a));
	}

//	public static Buffer[] FractalHeightValueTo3DTris(int[][] inArr){
//		// vertex cords from -.5,-.5 to .5,.5
//		float vX1 = -.5f;
//		float vX2 = +.5f;
//		float vY1 = -.5f;
//		float vY2 = +.5f;
//		int UVTiles = 14;
//		
//		for (int y =0;y<inArr.length-1;y=y+1){
//		for (int x =0;x<inArr.length;x=x+1){
//			
//			
//		}}
//		
//		boolean done= true;		
//		while (done==false){
//			
//		}
//		///00,01,10,10,11,20
//		///  ,Y ,Xy,  ,Y ,Xy
//	
//		// xa = 0, 0,+1,,-1,+1, 0,,
//		// ya = 0,+1,-1,,+1, 0,-1,,
//	
//	}

//	public static Buffer[] FractalHeightValueTo3DTris(float[][] inArr){
//		int size = inArr.length;
//		int[][] outar = new int[size][size];
//		for     (int x =0; x<size;x=x+1){
//			for (int y =0; y<size;y=y+1){
//				outar[x][y] = Math.round(inArr[x][y]*255);
//			}}
//		
//		return FractalHeightValueTo3DTris(outar);
//	}

	public static float[] CNormalize(float[] vNormal){
	    double Magnitude = CMag(vNormal);			// Get the magnitude
	
	    vNormal[0] /= (float)Magnitude;				// Divide each axis by the magnitude
	    vNormal[1] /= (float)Magnitude;
	    vNormal[2] /= (float)Magnitude;
	
	    return vNormal;								// Return the normal
	}

	public static float[] GetSmoothedNormal(int x,int y,int[][] hmap){
		
		float[] o = new float[] {x+0,0,hmap[x][y]/256f};
		float[] d0 = new float[3];
		float[] d1 = new float[3];
		float[] d2 = new float[3];
		float[] d4 = new float[3];
		float[] d5 = new float[3];
		float[] d6 = new float[3];
		
		boolean rEdge     =(x == hmap.length-1);
		boolean lEdge     =(x == 0);
		boolean topEdge   =(y == 0);
		boolean bottomEdge=(y == hmap.length-1);
		
		if (!rEdge)                {d0 = new float[]{x+1,y+0,hmap[x+1][y+0]/256f};}
		if ((!rEdge)&(!bottomEdge)){d1 = new float[]{x+1,y+1,hmap[x+1][y+1]/256f};}
		if (!bottomEdge)           {d2 = new float[]{x+0,y+1,hmap[x+0][y+1]/256f};}
		if (!lEdge)                {d4 = new float[]{x-1,y+0,hmap[x-1][y+0]/256f};}
		if ((!lEdge)&(!topEdge))   {d5 = new float[]{x-1,y-1,hmap[x-1][y-1]/256f};}
		if (!topEdge)              {d6 = new float[]{x+0,y-1,hmap[x+0][y-1]/256f};}
		
		float[] n0= GetTriNormal(o, d0, d1);
		float[] n1= GetTriNormal(o, d1, d2);
		
		float[] n2= GetTriNormal(o, d2, d4);
		
		float[] n3= GetTriNormal(o, d4, d5);
		float[] n4= GetTriNormal(o, d5, d6);
		
		float[] n5= GetTriNormal(o, d6, d0);
		
		float nx = n0[0]+n1[0]+n2[0]+n3[0]+n4[0]+n5[0];
		float ny = n0[1]+n1[1]+n2[1]+n3[1]+n4[1]+n5[1];
		float nz = n0[2]+n1[2]+n2[2]+n3[2]+n4[2]+n5[2];
		
		
		return CNormalize(new float[]{nx,ny,nz});        	
	}

	public static float[] subMJN(float[] a, float[] b){
	    //answer[0] = a[0] - b[0];
	    //answer[1] = a[1] - b[1];
	    //answer[2] = a[2] - b[2];
	    return new float[]{a[0] - b[0],a[1] - b[1],a[2] - b[2]};
	}

	public static  float CMag(float[] norm) {
	    return (float)Math.sqrt(norm[0]*norm[0] + norm[1]*norm[1] + norm[2]*norm[2]);
	}

}
