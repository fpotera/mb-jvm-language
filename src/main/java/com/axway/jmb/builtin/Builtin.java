package com.axway.jmb.builtin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class Builtin {
	
	private static InputStream is = System.in;
	private static OutputStream os = System.out;
	
	public static Date currentDate() {
		return new Date();
	}
	
	public static void openFile ( String fileName, boolean forRead ) {
		try {
			if ( forRead  ) {
				is = new FileInputStream( fileName );
			}
			else {
				os = new FileOutputStream( fileName );
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeFile ( boolean forRead ) {
		try {
			if ( forRead ) {
				if ( is != System.in ) {
					is.close();
					is = System.in;
				}
			}
			else {
				if ( os != System.out ) {
					os.close();
					os = System.out;
				}			
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String read ( Object param ) {
		try {
			if ( param instanceof Long ) {
				byte[] buff = new byte[((Long) param).intValue()];
				int cnt = is.read(buff);
				if ( cnt == -1 ) {
					return "";
				}
				return new String( buff );
			}
			if ( param instanceof String ) {
				StringBuilder sb = new StringBuilder();
				byte ch = ((String) param).getBytes()[0];
				byte crtByte;
				crtByte = (byte) is.read();
				while ( crtByte != ch ) {
					sb.append(new String(new byte[] {crtByte}));
					crtByte = (byte) is.read();
				}
				return sb.toString();
			}
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public static void print ( Object str  ) {
		try {
			if ( str instanceof String ) {
				os.write( ((String) str).getBytes() );
			}
			if ( str instanceof char[] ) {
				os.write( new String( (char[]) str ).getBytes() );
			}			
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}		
	}
}
