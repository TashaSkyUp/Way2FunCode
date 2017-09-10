package com.nsu.way2fungames;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.plaf.metal.OceanTheme;

import com.nsu.way2fungames.*;

public class FractalLand extends Fractal {
protected static float oceanLevel = .33f;
static float shoreSize = .025f;
static float plainsSize = .25f;
static float hillsSize = .15f;
static float oceanToShore     = oceanLevel;;
static float shoreToPlains    = oceanLevel +shoreSize;
static float plainsToHills    = shoreToPlains+plainsSize;
protected static float hillsToMountains = plainsToHills+hillsSize;

public static Image getFractalLand(int size){
	float[][] fa = Fractal.createNormalized2DFract2DArray(size);
	BufferedImage bi2 = (BufferedImage) new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	//Graphics2D bi2_g2d = (Graphics2D) bi2.createGraphics();
	//Color clr = new Color(0,0,0,0);
	
	int rgb;
	for (int x=0;x<size;x++){for (int y=0;y<size;y++){
		
		
			
			bi2.setRGB(x, y, getCforE(fa[x][y]));
			
	}}
	
	return (Image) bi2;
}
public static Image getFractalLand(float[][] ar){
	int size = (int)ar.length;
	BufferedImage bi2 = (BufferedImage) new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	for (int x=0;x<size;x++){for (int y=0;y<size;y++){
			bi2.setRGB(x, y, getCforE(ar[x][y]));
	}}
	
	return (Image) bi2;
	
}

private static int getCforE(float e) {
	int r = 0,g = 0,b = 0,rgb;
	
	
	if ( (e>0)&(e <= oceanLevel) ){//ocean
		float pp = (e)/(oceanLevel);
		r=(int) (0+  (pp* 80));
		g=(int) (0+  (pp* 80));
		b=(int) (150+(pp* 80));
	}
	if ((e>oceanLevel)&(e<(shoreSize+oceanLevel)) ){//shore
		float pp = (e-oceanLevel)/(shoreSize);
		r=(int) (((100+(pp*40))*pp) +  (80*(1-pp)) );
		g=(int) (((100+(pp*40))*pp) +  (80*(1-pp)) );
		b=(int) ((1f-pp)*204);
	}

	if ((e>oceanLevel+shoreSize)&(e<(shoreSize+oceanLevel+plainsSize)) ){//normal land
		float pp = (e-oceanLevel-shoreSize)/(plainsSize);
		r=(int) (0+  (pp*55));
		g=(int) (150+(pp*55));
		b=(int) (0+  (pp*55));
		
	}

	if ((e>oceanLevel+shoreSize+plainsSize)&(e<(shoreSize+oceanLevel+plainsSize)+hillsSize) ){//hills
		float pp = (e-oceanLevel-shoreSize-plainsSize)/(hillsSize);
		r=(int) (((140+(pp*40))*pp) +  (0*(1-pp)) );
		g=(int) (((120+(pp*40))*pp) +  (205*(1-pp)) );
		b=(int) (((0+(pp*40))*pp)   +  (0*(1-pp)) );

	}
	if ((e>oceanLevel+shoreSize+plainsSize+hillsSize)){//mountians
		float pp = (e-oceanLevel-shoreSize-plainsSize-hillsSize)/(1-oceanLevel-shoreSize-plainsSize-hillsSize);

		r=(int) (160+  (pp*94));
		g=(int) (160+  (pp*94));
		b=(int) (160+   (pp*94));
	}

	rgb = r;
	rgb = rgb << 8;
	rgb |= g;
	rgb = rgb << 8;
	rgb |= b;

	return (int) rgb;
}


}
