package com.ntu.way2fungames;

import java.awt.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Fractal {

	/*
	 * avgDiamondVals - Given the i,j location as the center of a diamond,
	 * average the data values at the four corners of the diamond and
	 * return it. "Stride" represents the distance from the diamond center
	 * to a diamond corner.
	 *
	 * Called by fill2DFractArray.
	 */
	static float avgDiamondVals (int i, int j, int stride,int size, int subSize, float[] fa)
	{
	    /* In this diagram, our input stride is 1, the i,j location is
	       indicated by "X", and the four value we want to average are
	       "*"s:
	           .   *   .
	
	           *   X   *
	
	           .   *   .
	       */
	
	    /* In order to support tiled surfaces which meet seamless at the
	       edges (that is, they "wrap"), We need to be careful how we
	       calculate averages when the i,j diamond center lies on an edge
	       of the array. The first four 'if' clauses handle these
	       cases. The final 'else' clause handles the general case (in
	       which i,j is not on an edge).
	     */
	    if (i == 0)
		return ((float) (fa[(i*size) + j-stride] +
				 fa[(i*size) + j+stride] +
				 fa[((subSize-stride)*size) + j] +
				 fa[((i+stride)*size) + j]) * .25f);
	    else if (i == size-1)
		return ((float) (fa[(i*size) + j-stride] +
				 fa[(i*size) + j+stride] +
				 fa[((i-stride)*size) + j] +
				 fa[((0+stride)*size) + j]) * .25f);
	    else if (j == 0)
		return ((float) (fa[((i-stride)*size) + j] +
				 fa[((i+stride)*size) + j] +
				 fa[(i*size) + j+stride] +
				 fa[(i*size) + subSize-stride]) * .25f);
	    else if (j == size-1)
		return ((float) (fa[((i-stride)*size) + j] +
				 fa[((i+stride)*size) + j] +
				 fa[(i*size) + j-stride] +
				 fa[(i*size) + 0+stride]) * .25f);
	    else
		return ((float) (fa[((i-stride)*size) + j] +
				 fa[((i+stride)*size) + j] +
				 fa[(i*size) + j-stride] +
				 fa[(i*size) + j+stride]) * .25f);
	}

	/*
	 * avgEndpoints - Given the i location and a stride to the data
	 * values, return the average those data values. "i" can be thought of
	 * as the data value in the center of two line endpoints. We use
	 * "stride" to get the values of the endpoints. Averaging them yields
	 * the midpoint of the line.
	 *
	 * Called by fill1DFractArray.
	 */
	static float avgEndpoints (int i, int stride, float[] fa){
	    return ((float) (fa[i-stride]+fa[i+stride]) * .5f);
	}

	/*
	 * avgSquareVals - Given the i,j location as the center of a square,
	 * average the data values at the four corners of the square and return
	 * it. "Stride" represents half the length of one side of the square.
	 *
	 * Called by fill2DFractArray.
	 */
	static float avgSquareVals (int i, int j, int stride, int size, float[] fa)
	{
	    /* In this diagram, our input stride is 1, the i,j location is
	       indicated by "*", and the four value we want to average are
	       "X"s:
	           X   .   X
	
	           .   *   .
	
	           X   .   X
	       */
	    return ((float) (fa[((i-stride)*size) + j-stride] +
			     fa[((i-stride)*size) + j+stride] +
			     fa[((i+stride)*size) + j-stride] +
			     fa[((i+stride)*size) + j+stride]) * .25f);
	}
	public static float[][] createNormalized2DFract2DArray (int nSize){
		float[] tmpAr = fill2DFractArray(nSize, 0, 1, 0.5f);
		return Utils.normalizeArray(tmpAr);
	}
	
	/*
	 * fill2DFractArray - Use the diamond-square algorithm to tessalate a
	 * grid of float values into a fractal height map.
	 */	
	private static float[] fill2DFractArray (int size,int seedValue, float heightScale, float h)
	{
	    int	i, j;
	    int	stride;
	    int	oddline;
	    int subSize;
		float ratio, scale;
		float[] fa;
		if (seedValue==0){seedValue= (int) (Math.random()*100000);}
	    if (!powerOf2(size) || (size==1)) {
	    	/* We can't tesselate the array if it is not a power of 2. */
	    	return null;
	    }
	    fa = new float[(size+1)*(size+1)];
	
	    /* subSize is the dimension of the array in terms of connected line
	       segments, while size is the dimension in terms of number of
	       vertices. */
	    subSize = size;
	    size++;
	    
	    /* initialize random number generator */
	    //srandom (seedValue);
	    
	
		/* Set up our roughness constants.
		   Random numbers are always generated in the range 0.0 to 1.0.
		   'scale' is multiplied by the randum number.
		   'ratio' is multiplied by 'scale' after each iteration
		   to effectively reduce the randum number range.
		   */
		ratio = (float) Math.pow (2.,-h);
		scale = heightScale * ratio;
	
	    /* Seed the first four values. For example, in a 4x4 array, we
	       would initialize the data points indicated by '*':
	
	           *   .   .   .   *
	
	           .   .   .   .   .
	
	           .   .   .   .   .
	
	           .   .   .   .   .
	
	           *   .   .   .   *
	
	       In terms of the "diamond-square" algorithm, this gives us
	       "squares".
	
	       We want the four corners of the array to have the same
	       point. This will allow us to tile the arrays next to each other
	       such that they join seemlessly. */
	
	    stride = subSize / 2;
	    fa[(0*size)+0] =
	      fa[(subSize*size)+0] =
	        fa[(subSize*size)+subSize] =
	          fa[(0*size)+subSize] = 0.f;
	    
	
	    /* Now we add ever-increasing detail based on the "diamond" seeded
	       values. We loop over stride, which gets cut in half at the
	       bottom of the loop. Since it's an int, eventually division by 2
	       will produce a zero result, terminating the loop. */
	    while (stride>=1) {
			/* Take the existing "square" data and produce "diamond"
			   data. On the first pass through with a 4x4 matrix, the
			   existing data is shown as "X"s, and we need to generate the
		       "*" now:
	
	               X   .   .   .   X
	
	               .   .   .   .   .
	
	               .   .   *   .   .
	
	               .   .   .   .   .
	
	               X   .   .   .   X
	
		      It doesn't look like diamonds. What it actually is, for the
		      first pass, is the corners of four diamonds meeting at the
		      center of the array. */
			for (i=stride; i<subSize; i+=stride) {
				for (j=stride; j<subSize; j+=stride) {
					fa[(i * size) + j] =
						scale * fractRand (.5f) +
						avgSquareVals (i, j, stride, size, fa);
					j += stride;
				}
				i += stride;
			}
	
			/* Take the existing "diamond" data and make it into
		       "squares". Back to our 4X4 example: The first time we
		       encounter this code, the existing values are represented by
		       "X"s, and the values we want to generate here are "*"s:
	
	               X   .   *   .   X
	
	               .   .   .   .   .
	
	               *   .   X   .   *
	
	               .   .   .   .   .
	
	               X   .   *   .   X
	
		       i and j represent our (x,y) position in the array. The
		       first value we want to generate is at (i=2,j=0), and we use
		       "oddline" and "stride" to increment j to the desired value.
		       */
			oddline = 0;
			for (i=0; i<subSize; i+=stride) {
			    if (oddline ==0){oddline=1;}else{oddline = 0;}
			    
				for (j=0; j<subSize; j+=stride) {
					if ((oddline!=0) && (j==0)){ j+=stride;}
	
					/* i and j are setup. Call avgDiamondVals with the
					   current position. It will return the average of the
					   surrounding diamond data points. */
					fa[(i * size) + j] = scale * fractRand (.5f) +avgDiamondVals (i, j, stride, size, subSize, fa);
	
					/* To wrap edges seamlessly, copy edge values around
					   to other side of array */
					if (i==0)
						fa[(subSize*size) + j] =
							fa[(i * size) + j];
					if (j==0)
						fa[(i*size) + subSize] =
							fa[(i * size) + j];
	
					j+=stride;
				}
			}
	
			/* reduce random number range. */
			scale *= ratio;
			stride >>= 1;
	    }
		return fa;
	
	}

	public static byte[] fractalByteMap(int[][] ra,float[] fa ,int sX,int eX,int sY,int eY){
		int sizeX = ra.length;
		int sizeY = ra[0].length;
		int sizeXP1 = sizeX+1;
		int rangeX =  eX-sX;
		int rangeY =  eY-sY;        	
		int size = rangeX;
		
		byte[] outTexture= new byte[size*size*4];
		
		float cv;
		int cvi;
		Color colr;
		
		float[] minmax = Utils.ArrayFindMinMax(fa);
		float min = minmax[0];
		float max = minmax[1];
		float range = Math.abs(max-min);
		float por,mRnd,segRange,segBase;
		 
		float r = 0,g=0,b=0;
		int outRange=128;
		int ux = 0,uy = 0;
		float y=0,x=0;
		float xAdd = sizeX/(float)size;
		float yAdd = sizeY/(float)(size);
		int bmpX = 0,bmpY=0;
		int offset=0;
		
		int xStart = sizeX/4;
		int xEnd   = sizeX-xStart;
		ux = xStart;
		x=ux;
		float yLightAmount=1;
		float yLightElevation = 0;
		float yLightDif=0;
		long sTime = System.currentTimeMillis();
		
		while(ux<xEnd){
			
			long cTime = System.currentTimeMillis();
			//Message msg = new Message();
			//msg.what=1;
			//msg.arg1=2;
			//msg.arg2=(int) (((x-xStart)/(xEnd-xStart))*100f);
			float lpsValue = (x-xStart)/((cTime-sTime)*.001f);
			//Bundle dataBundle = new Bundle();
			//dataBundle.putFloat("lps", lpsValue);
			//msg.setData(dataBundle);    			
			//outMessage.sendMessage(msg);
			yLightAmount=1;
			yLightElevation=0;
			float cva = 0;
			while(uy<sizeY){
				offset=(int) (ux+(uy*sizeXP1));
				cv =  ((((fa[offset]-min)/range)));
				//cva = ((((fa[offset]-min)/range))); 
				//cva = cva+((((fa[offset]-min)/range)));
				if (uy>1){cva = cva+((((fa[(int) (ux+((uy-1)*sizeXP1))]-min)/range)));}
				if (uy<(sizeY-1)){cva = cva+((((fa[(int) (ux+((uy+1)*sizeXP1))]-min)/range)));}
				cva= (cva+cv)/3;
				
				cvi =(int) (cv* outRange);
				
				
				if (cva>yLightElevation){
					//yLightAmount= yLightAmount+(yLightDif*.1f);
					yLightAmount= yLightAmount+.10f;
					//yLightElevation= yLightElevation-(yLightDif*.2f);
					yLightElevation=cva;
				}else{
					yLightAmount=yLightAmount-.10f;
					yLightElevation= yLightElevation-.05f   ;  					
				}
				if (yLightElevation<cva){yLightElevation=cva;}
				if (yLightAmount>1){yLightAmount=1;}
				if (yLightAmount<.85){yLightAmount=.85f;}
				
				ra[ux][uy]= cvi;
				if ((x==0)||(y==0)||(x==sizeX)||(y==sizeY)){
					colr = new Color(1f, 1f,0f,0f);
				
				}else if ((x==50)||(y==50)||(x==sizeX-50)||(y==sizeY-50)){
					colr = new  Color(1f, 1f,0f,0f);
				}else if ((x==75)||(y==75)||(x==sizeX-75)||(y==sizeY-75)){
					colr = new  Color(1f, 1f,0,0);
				}else if (cvi>(outRange*.90f)){
					colr = new  Color(1f, cvi/255f, cvi/255f, cvi/255f);
				}else if (cvi>(outRange*.80f)){
					segRange = outRange *.10f;
					segBase  = outRange *.80f;
					por = (float) ((cvi-segBase)/segRange);
					por = (por*.25f)+.75f;    					
					mRnd = (float) (Math.random()*.75)+.25f;
					
					r = (int) (mRnd *  200f);
					g = (int) ((Math.random()*25f)+125f);
					b = (int) (mRnd*200f);
					r=(int) (r*por);
					g=(int) (g*por);
					b=(int) (b*por);
					
				}else if(cvi>(outRange*.06f)){
					//color = Color.argb(255, cvi/10, cvi, cvi/10);
					
					por = (float) ((cvi-(outRange*.06f))/((outRange*.95)-(outRange*.06)));
					por = (float) ((por*.75f)+.25f);
					mRnd = (float) Math.random();
					r = (int) ((mRnd *  200f)- 0);
					g = (int) (((Math.random()*25f)+125)-0);
					b = (int) ((mRnd*100f)-0);
					r=(int) (r*por);
					g=(int) (g*por);
					b=(int) (b*por);
					
					
				}else if(cvi<(outRange*.05f)){
					r=(int) 32;
					g=(int) 64;
					b=(int) 128;
				}
				r=(int) (yLightAmount*r);
				g=(int) (yLightAmount*g);
				b=(int) (yLightAmount*b);
				
				
				//color = Color.argb(255,128,0,0);
				
				colr = new  Color(1f,(float) (r)/255f ,(int)g/255f,(int)b/255f);
				offset = (bmpY*size*4)+(bmpX*4);
				outTexture[offset+0]=(byte) ((colr.getRGB() >> 16) & 0xFF) ;
				outTexture[offset+1]=(byte) ((colr.getRGB() >> 8) & 0xFF) ;
				outTexture[offset+2]=(byte) (colr.getRGB() & 0xFF) ;
				outTexture[offset+3]=(byte) 127;
				
				
				
				//ux=(int)Math.round(x);
				//uy=(int)Math.round(y);
			y=y+yAdd;uy=(int)Math.round(y);bmpY=(int) (y/yAdd);}
			uy=0;bmpY=0;
			y=0;
	
		x=x+xAdd;ux=(int)Math.round(x);bmpX=(int) (x/xAdd);}
	
		return outTexture;
	}

	/*
	 * alloc1DFractArray - Allocate float-sized data points for a 1D strip
	 * containing size line segments.
	 */
	
	
	/*
	 * freeFractArray - Takes a pointer to float and frees it. Can be used
	 * to free data that was allocated by either alloc1DFractArray or
	 * alloc2DFractArray.
	 */
	
	
	
	
	/*
	 * draw1DFractArrayAsLines - Draws the height map as a single set of
	 * connected line segments.
	 *
	 * This is a simplified routine intended for getting things up and
	 * running quickly, and as a demonstration of how to walk through the
	 * array.
	 *
	 * To use this routine, you MUST define your own "draw2DLine" function
	 * according to the extern definition below. It takes 4 parameters,
	 * the X and Y world coordinates of the first endpoint followed by the
	 * X and Y world coordinates of the second endpoint.
	 *
	 * The X coordinates will be distributed evenly from -1.0 to
	 * 1.0. Corresponding Y coordinates will be extracted from the fract
	 * array.
	 */
	
	/*
	 * draw2DFractArrayAsLines - Draws the height map as a set of line
	 * segments comprising a grid.
	 *
	 * This is a simplified routine intended for getting things up and
	 * running quickly, and as a demonstration of how to walk through the
	 * array.
	 *
	 * To use this routine, you MUST define your own "draw3DLine" function
	 * according to the extern definition below. It takes 6 parameters,
	 * the X, Y, and Z world coordinates of the first endpoint followed by
	 * the X, Y, and Z world coordinates of the second endpoint.
	 *
	 * X and Z coordinates will be distributed evenly over a grid from
	 * -1.0 to 1.0 along both axes. Corresponding Y coordinates will be
	 * extracted from the fract array.
	 */
	public static byte[] fractalByteMap(int[][] ra,int size){
		//outMessage.sendEmptyMessage(2);
		//Message msg = new Message();
		//msg.what=1;
		//msg.arg1=1;
		//outMessage.sendMessage(msg);
		float[] fa = fill2DFractArray(size, (int) (Math.random()*1000000), .1f, 1f);
		return fractalByteMap(ra,fa,0,size,0,size);
		
	}
	public static Image fractalImage(int size){
		float[][] fa = createNormalized2DFract2DArray(size);
		BufferedImage bi2 = (BufferedImage) new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		//Graphics2D bi2_g2d = (Graphics2D) bi2.createGraphics();
		//Color clr = new Color(0,0,0,0);
		int rgb,r,g,b;
		for (int x=0;x<size;x++){for (int y=0;y<size;y++){
				r=(int) (fa[x][y]*255);
				g=r;
				b=g;
				
				rgb = r;
				rgb = rgb << 8;
				rgb |= g;
				rgb = rgb << 8;
				rgb |= b;
				
				bi2.setRGB(x, y, rgb);
				
		}}
		
		return (Image) bi2;
	}
	
	public static Buffer FractalTextureCords(int size){
		float[] rArr = new float[size*size*2];
		//float x=0;
		//float y=0;
		int i =0;
		float adder = 1f/size;
		
		for (float     x=0;x<1;x=x+adder){
			for (float y=0;y<1;y=y+adder){
			
			rArr[i+0] = x;
			rArr[i+1] = y;
			i=i+2;
		}}
		return Utils.WrapRightF(rArr);
	}

	/*
	 * fractRand is a useful interface to randnum.
	 */
	private static float fractRand(float v) {return randnum (-v, v);}

	static float randnum (float min, float max){
		return (float) ((Math.random()*(max-min))+min);
	}

	/*
	 * powerOf2 - Returns 1 if size is a power of 2. Returns 0 if size is
	 * not a power of 2, or is zero.
	 */
	static boolean powerOf2 (int size)
	{
		if((size & (size-1)) == 0){return true;}else{return false;}
	}

	/*
	 * fill1DFractArray - Tessalate an array of values into an
	 * approximation of fractal Brownian motion.
	 */
	void fill1DFractArray (float[] fa, int size,
			       int seedValue, float heightScale, float h)
	{
	    int	i;
	    int	stride;
	    int subSize;
		float ratio, scale;
	
	    if (!powerOf2(size) || (size==1)) {
		/* We can't tesselate the array if it is not a power of 2. */
		return;
	    }
	
	    /* subSize is the dimension of the array in terms of connected line
	       segments, while size is the dimension in terms of number of
	       vertices. */
	    subSize = size;
	    size++;
	    
	    /* initialize random number generator */
	    //srandom (seedValue);
	
	
		/* Set up our roughness constants.
		   Random numbers are always generated in the range 0.0 to 1.0.
		   'scale' is multiplied by the randum number.
		   'ratio' is multiplied by 'scale' after each iteration
		   to effectively reduce the randum number range.
		   */
		ratio = (float) Math.pow(2.,-h);
		scale = heightScale * ratio;
	
	    /* Seed the endpoints of the array. To enable seamless wrapping,
	       the endpoints need to be the same point. */
	    stride = subSize / 2;
	    fa[0] =
	      fa[subSize] = 0.f;
	
	
	    while (stride >1) {
			for (i=stride; i<subSize; i+=stride) {
				fa[i] = scale * fractRand (.5f) + avgEndpoints (i, stride, fa);
	
				/* reduce random number range */
				scale *= ratio;
	
				i+=stride;
			}
			stride >>= 1;
	    }
	
	}

}
