package com.ntu.way2fungames;
import java.awt.Image;
import java.awt.image.BufferedImage;


public class Land extends FractalLand{
	public float elevationMap[][];
	public int size;
	public Image img;
	private boolean[][] rainmap;
	private int[] drops; 
	private float erf;
	private float[][] freshWaterMap;
	
	public Land(int nSize){
		size=nSize;
		System.out.println("Fractal.createNormalized2DFract2DArray");
		elevationMap = Fractal.createNormalized2DFract2DArray(size);
		System.out.println("FractalLand.getFractalLand");
		img = FractalLand.getFractalLand(elevationMap);
	}
	
	public void morphRain(int dpp){
		freshWaterMap  = new float[elevationMap.length][elevationMap.length];
		
		int di = 0;
		erf= .00001f;
		int lres[];
		int life = 300;
		float per= .001f;
		
		drops = new int[(int) (elevationMap.length*elevationMap.length*per*2)];
		
		rainmap = new boolean [elevationMap.length][elevationMap.length];
		for (int dropN=0;dropN<dpp;dropN++){for (int x=0;x<size;x++){for (int y=0;y<size;y++){
			if (Math.random()<per){
			if (elevationMap[x][y]>.8f){
				rainmap[x][y]=true;
				elevationMap[x][y] = elevationMap[x][y]-erf;
				int cx=x;
				int cy=y;
				
				drops[di] = (int) ((Math.random()*8)+1);
				
				
				for (int l=0;l<life;l++){
					lres = findLowest(cx, cy,di);
					if ((lres[0]!=-1)){
						cx = lres[0];
						cy = lres[1];
						//elevationMap[cx][cy] = elevationMap[cx][cy]-erf;
						freshWaterMap[cx][cy] = 1f;
						try{
							//freshWaterMap[cx+1][cy]   = .5f;
							//freshWaterMap[cx-1][cy] = .5f;
							//freshWaterMap[cx][cy+1]   = .5f;
							//freshWaterMap[cx][cy-1]   = .5f;
						}catch (ArrayIndexOutOfBoundsException e){}
					}else{l=life;}
				}
				di=di+1;
			}
			}
		}
		//System.out.println("x: "+x);
		}}
		img = addMorphs(FractalLand.getFractalLand(elevationMap));
		//img = FractalLand.getFractalLand(elevationMap);
		System.out.println("Rain done");
	}
	
	private Image addMorphs(Image fractalLand) {
		BufferedImage bi = (BufferedImage) fractalLand;
		int rgb,r = 0,g = 0,b = 0;
		float fw;
		for (int x=0;x<size;x++){for (int y=0;y<size;y++){
			
			
			fw=freshWaterMap[x][y];
			
			if (fw>0){
				r=(int) (fw*0);
				g=(int) (fw*128);
				b=(int) (fw*255);
				
				rgb = r;rgb = rgb << 8;rgb |= g;rgb = rgb << 8;rgb |= b;
				bi.setRGB(x, y, rgb);
			}
		}}
		return bi;
	}

	private int[] findLowest(int x,int y, int didx){
		int dir = drops[didx];
		int itx=0;
		int ity=0;
		float lwst= Float.NEGATIVE_INFINITY ;
		float ch = elevationMap[x][y];
		float d;
		int size = elevationMap.length-1;

		
			
		int itdir = 0;
		for (int sx=x-1;sx<=x+1;sx++){for (int sy=y-1;sy<=y+1;sy++){
			if ((sx>=0)&(sx<size)){if ((sy>=0)&(sy<size)){
				if (rainmap[sx][sy]==false){

					if ((sx!=x)|(sy!=y)){
						int ndir  = getdirfromxym(x,y);
						d = ch - elevationMap[sx][sy];
						if (ndir ==  dir){d=d+.09f;d=d*.1f;}
						if (ndir+1 ==dir){d=d+.025f;d=d*.1f;}
						if (ndir-1 ==dir){d=d+.025f;d=d*.1f;}
						
						if (ndir+4 ==dir){d=d-.09f;d=d*-.1f;}
						if (ndir+3 ==dir){d=d-.09f;d=d*-.05f;}
						if (ndir+5 ==dir){d=d-.09f;d=d*-.05f;}
						
						if (d > lwst){
							itx = sx;
							ity = sy;
							lwst = d;
							itdir = ndir;
							
						}
					}
				}
			}}
		}}
		
		if (lwst<0){
			//elevationMap[itx][ity]=elevationMap[x][y];
			//elevationMap[x][y]=elevationMap[itx][ity];
			return new int[]{-1,0};		
		}else{
			elevationMap[itx][ity]=elevationMap[itx][ity] - erf;
			drops[didx] = itdir;
			try{
				elevationMap[itx+1][ity]   = elevationMap[itx+1][ity]   - (erf*.1f);
				elevationMap[itx-1][ity]   = elevationMap[itx-1][ity]   - (erf*.1f);
				elevationMap[itx][ity+1]   = elevationMap[itx][ity+1]   - (erf*.1f);
				elevationMap[itx][ity-1]   = elevationMap[itx][ity-1]   - (erf*.1f);
				
				elevationMap[itx+1][ity+1]   = elevationMap[itx+1][ity+1] - (erf*.1f);
				elevationMap[itx-1][ity-1]   = elevationMap[itx-1][ity-1] - (erf*.1f);
				elevationMap[itx+1][ity-1]   = elevationMap[itx+1][ity-1] - (erf*.1f);
				elevationMap[itx-1][ity+1]   = elevationMap[itx-1][ity+1] - (erf*.1f);
				
			}catch (ArrayIndexOutOfBoundsException e){}
		}
		rainmap[itx][ity]=true;
		
		if (itx==0){
			if (ity==0){
				return new int[]{-1,0};		
			}else{
				return new int[]{itx,ity};
			}
		}else{
			return new int[]{itx,ity};
		}
		
	}

	private int getdirfromxym(int x, int y) {
		
		if (x==-1){
			if (y==-1){
				return 4;
			}
			else if (y==0){
				return 5;
			}
			else{
				return 6;
			}
		}
		else if (x==0){
			if (y==-1){
				return 3;	
			}
			else if (y==0){
				return 0;	
			}
			else {
				return 7;	
			}

	
		}
		else if (x==1){
			if (y==-1){
				return 2;
			}
			else if (y==0){
				return 1;
			}
			else{
				return 8;	
			}
			
		}

		return 0;
	}
}
