package bots.JamBot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides some higher level access to common file input and output commands used.
 * 
 * @author Kiefer
 *
 */
public class FileIO {
	
	/**
	 * Converts a text file into a string array, which are split by newline characters.
	 * 
	 * @param path Path to the text file.
	 * @return A string array consisting of the text file line by line.
	 */
	public static String[] getFile(String path) {
		BufferedReader inputStream = null;
		List<String> fileList = new ArrayList<String>();
		try {
			inputStream = new BufferedReader(new FileReader(path));
			 String l;
	            while ((l = inputStream.readLine()) != null) {
	                fileList.add(l);
	            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		String[] tempstr = fileList.toArray(new String[0]);
		
		return tempstr;
	}
	
	/**
	 * Writes a string array to a text file.
	 * 
	 * @param file Path to the file.
	 * @param lines String array consisting of the file to be written, line by line.
	 */
	public static void writeFile(String file, String[] lines) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			for (String s : lines) {
				bw.write(s);
				bw.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Appends a line to a text file.
	 * 
	 * @param file Path to the file.
	 * @param line Line of text to append.
	 */
	public static void appendLineFile(String file, String line) {
		ArrayList<String> myfile = new ArrayList<String>();
		String[] prefile = getFile(file);
		for (String s : prefile) {
			myfile.add(s);
		}
		myfile.add(line);
		
		writeFile(file, myfile.toArray(new String[0]));
	}
	
	/**
	 * Serializes an object and writes it to a file.
	 * 
	 * @param filename Path to the file to write to.
	 * @param object Object to serialize and write.
	 */
	public static void writeObject(String filename, Object object) {
		FileOutputStream fi = null;
		ObjectOutputStream out = null;
		
		try {
			fi = new FileOutputStream(filename);
			out = new ObjectOutputStream(fi);
			out.writeObject(object);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find object: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Deserializes an object from a file.
	 * 
	 * @param filename Path to the file
	 * @return The deserialized object.
	 */
	public static Object loadObject(String filename) {
		FileInputStream fi = null;
		ObjectInputStream in = null;
		Object obj = null;
		try {
			fi = new FileInputStream(filename);
			in = new ObjectInputStream(fi);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find object: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				obj = in.readObject();
				in.close();
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return obj;
		
	}
	
	public static String getPageSource(String theURL) throws IOException {
		URL url = new URL(theURL);
		InputStream is = url.openStream();
		int ptr = 0;
		StringBuffer buffer = new StringBuffer();
		while ((ptr = is.read()) != -1) {
			buffer.append((char)ptr);
		}

		is.close();

		return buffer.toString();
	}
	
}
