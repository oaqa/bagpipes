package util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

	public static final String PREFIX = "stream2file";
	public static final String SUFFIX = ".tmp";

	public static File stream2file(InputStream in) {
		File tempFile = null;
		FileOutputStream out = null;
		try {
			tempFile = File.createTempFile(PREFIX, SUFFIX);
			tempFile.deleteOnExit();
			out = new FileOutputStream(tempFile);
			IOUtils.copy(in, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile;
	}
}