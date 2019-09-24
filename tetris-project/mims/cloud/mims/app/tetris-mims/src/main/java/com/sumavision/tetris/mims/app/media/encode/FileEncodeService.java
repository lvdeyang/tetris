package com.sumavision.tetris.mims.app.media.encode;

import java.io.File;

import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;
import com.sumavision.tetris.mims.app.media.encode.exception.FileNotExitException;

public class FileEncodeService {
	public static String encodeFile(String filePath, String previewUrl) throws Exception{
		File file = new File(filePath);
		String fileName = file.getName();
		
		if (file.isFile() && file.exists()) {
			String[] nameSplit = fileName.split("\\.");
			String dirPath = filePath.split(fileName)[0];
			String newDirPath = new StringBuilderWrapper()
					.append(dirPath)
					.append(File.separator)
					.append("encode")
					.toString();
			File encodeDirFile = new File(newDirPath);
			if (!encodeDirFile.exists()) {
				encodeDirFile.mkdirs();
			}
			
			String newFileName = new StringBuilderWrapper().append(nameSplit[0])
					.append("_encode")
					.append(nameSplit[1])
					.toString();
			String newFilePath = new StringBuilderWrapper().append(newDirPath)
					.append(File.separator)
					.append(newFileName)
					.toString();
			
			
			return new StringBuilderWrapper().append(previewUrl.split(fileName)[0])
					.append("/")
					.append("encode")
					.append("/")
					.append(newFileName)
					.toString();
		}
		
		throw new FileNotExitException(fileName);
	}
}
