package com.ntu.way2fungames;
import java.io.*;
import java.nio.Buffer;
import java.nio.CharBuffer;

public class OGLTriReader {

  /**
  * Fetch the entire contents of a text file, and return it in a String.
  * This style of implementation does not throw Exceptions to the caller.
  *
  * @param aFile is a file which already exists and can be read.
  */
	static public float[] GetVertsFromStream(InputStream nStream){
		String sData = readTextFile(nStream);		
		char[] cData = sData.toCharArray();
		
		char charF = "f".charAt(0);
		char charComma = ",".charAt(0);
		
		CharBuffer cBuffer = CharBuffer.wrap(cData);
		int floatCount =0;
		
		char n = 0;
		String s="";
		
		n=cBuffer.get();
		while (n != charComma){
			s=s+n;
			n=cBuffer.get();
		}
		//cBuffer.rewind();
		floatCount = Integer.valueOf(s);
		
		float[] retArray = new float[floatCount];
		
		char sn = "\n".toCharArray()[0];
		char sr = "\r".toCharArray()[0];
		
		String sBuildFloat="";
		int currentFloat = 0;		
		int imax = cBuffer.remaining();
		for(int i =0;i<imax-1;i=i+1){
			char cn = cBuffer.get();
			
			if (cn== charF){
				//Log.w("io", sBuildFloat);
				retArray[currentFloat]= Float.valueOf(sBuildFloat);
				currentFloat = currentFloat+1;
				sBuildFloat="";
			}else if (cn == charComma){
				
			}else if (cn == sn){
			}else if (cn == sr){
				
			}else{
				sBuildFloat=sBuildFloat+cn;
			}
		}
		
		return retArray;
	}
  static private String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuilder contents = new StringBuilder();
    
    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
          contents.append(line);
          //contents.append(System.getProperty("line.separator"));
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    
    return contents.toString();
  }
  		/**
       * This method reads simple text file
       * @param inputStream
       * @return data from file
       */
      private static String readTextFile(InputStream inputStream) {
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          byte buf[] = new byte[1024];
          int len;
          try {
  
              while ((len = inputStream.read(buf)) != -1) {
                  outputStream.write(buf, 0, len);
              }
              outputStream.close();
              inputStream.close();
          } catch (IOException e) {
          }
          return outputStream.toString();
      }
  } 
