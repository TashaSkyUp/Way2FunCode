package com.ntu.way2fungames;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class TgaWriter {
	
	  private static short flipEndian(short signedShort) {
	    int input = signedShort & 0xFFFF;
	    return (short) (input << 8 | (input & 0xFF00) >>> 8);
	  }

	  public static void saveImage(Image image, OutputStream output, boolean writeAlpha) throws IOException {
	    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(output));

	    // ID Length
	    out.writeByte((byte) 0);

	    // Color Map
	    out.writeByte((byte) 0);

	    // Image Type
	    out.writeByte((byte) 2);

	    // Color Map - Ignored
	    out.writeShort(flipEndian((short) 0));
	    out.writeShort(flipEndian((short) 0));
	    out.writeByte((byte) 0);

	    // X, Y Offset
	    out.writeShort(flipEndian((short) 0));
	    out.writeShort(flipEndian((short) 0));

	    // Width, Height, Depth
	    out.writeShort(flipEndian((short) image.getWidth(null)));
	    out.writeShort(flipEndian((short) image.getHeight(null)));
	    
	    if (writeAlpha) {
	      out.writeByte((byte) 32);
	      // Image Descriptor (can't be 0 since we're using 32-bit TGAs)
	      // needs to not have 0x20 set to indicate it's not a flipped image
	      out.writeByte((byte) 1);
	    } else {
	      out.writeByte((byte) 24);
	      // Image Descriptor (must be 0 since we're using 24-bit TGAs)
	      // needs to not have 0x20 set to indicate it's not a flipped image
	      out.writeByte((byte) 0);
	    }
	    BufferedImage imageBi = (BufferedImage) image;
	    
	    

	    // Write out the image data
	    Color c;
	    		
	    for (int y = image.getHeight(null)-1; y >= 0; y--) {
	      for (int x = 0; x < image.getWidth(null); x++) {
	        c = new Color(imageBi.getRGB(x, y));
	        
	        out.writeByte((byte) (c.getBlue()));
	        out.writeByte((byte) (c.getGreen()));	        
	        out.writeByte((byte) (c.getRed()));
	        
	        if (writeAlpha) {
	          out.writeByte((byte) (c.getAlpha()));
	        }
	      }
	    }

	    out.close();
	  }
	}
