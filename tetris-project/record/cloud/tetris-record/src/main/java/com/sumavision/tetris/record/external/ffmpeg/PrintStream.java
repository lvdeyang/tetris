package com.sumavision.tetris.record.external.ffmpeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintStream implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintStream.class);

	java.io.InputStream __is = null;

	public PrintStream(java.io.InputStream is) {
		__is = is;
	}

	public void run() {
		try {
			StringBuilder sb = new StringBuilder(); 
					
			while (this != null) {
				int _ch = __is.read();
				if (_ch != -1) {
					// LOGGER.info((char) _ch);
					sb.append((char)_ch);
				} else {
					break;
				}
				
			}
			
			LOGGER.info("PrintStream=" + sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}