package com.sumavision.tetris.mims.app.media.encode;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.file.AESUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBuilderWrapper;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.encode.exception.FileNotExitException;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileEncodeService {
	
	public static final String AES_KEY = "sumasumasumasuma";
	public static final String IV_KEY = "aaaaaaaaaaaaaaaa";
	
	@Autowired
	private AudioFileEncodeDAO audioFileEncodeDao;
	
	/**
	 * 音频媒资加密<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月25日 下午1:14:06
	 * @param MediaAudioPO audio 音频媒资信息
	 * @return String 加密文件previewUrl
	 */
	public String encodeAudioFile(MediaAudioPO audio) throws Exception{
		
		//AES加密
		AESUtil aesUtil = new AESUtil();
		byte[] key = AES_KEY.getBytes();
		byte[] iv = new byte[16];
//		byte[] iv = IV_KEY.getBytes();
		
		String filePath = audio.getUploadTmpPath();
		String previewUrl = audio.getPreviewUrl();
		
		File file = new File(filePath);
		String fileName = file.getName();
		
		if (file.isFile() && file.exists()) {
			String[] nameSplit = fileName.split("\\.");
			String dirPath = filePath.split(fileName)[0];
			String newDirPath = new StringBuilderWrapper()
					.append(dirPath)
					.append("encode")
					.toString();
			File encodeDirFile = new File(newDirPath);
			if (!encodeDirFile.exists()) {
				encodeDirFile.mkdirs();
			}
			
			String newFileName = new StringBuilderWrapper().append(nameSplit[0])
					.append("_encode.")
					.append(nameSplit[1])
					.toString();
			String newFilePath = new StringBuilderWrapper().append(newDirPath)
					.append(File.separator)
					.append(newFileName)
					.toString();
			
			File encodeFile = new File(newFilePath);
			
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			ByteArrayOutputStream bos1 = null;
			
			try{
				
				InputStream is = new FileInputStream(file);
				OutputStream os = new FileOutputStream(encodeFile);
				
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(os);
				bos1 = new ByteArrayOutputStream((int)(file.length()));
				
				byte[] data = new byte[1024];
				int len = 0;
				while((len = bis.read(data,0, 1024)) != -1){
					bos1.write(data, 0, len);
				}
				
				byte[] result = aesUtil.encrypt(bos1.toByteArray(), key, iv);
				
				bos.write(result);
				
				String encodePreviewUrl = new StringBuilderWrapper().append(previewUrl.split(fileName)[0])
																	.append("encode")
																	.append("/")
																	.append(newFileName)
																	.toString();
				
				AudioFileEncodePO audioEncode = new AudioFileEncodePO();
				audioEncode.setMediaId(audio.getId());
				audioEncode.setUpdateTime(new Date());
				audioEncode.setFilePath(newFilePath);
				audioEncode.setPreviewUrl(encodePreviewUrl);
				audioFileEncodeDao.save(audioEncode);
				
				return encodePreviewUrl;
				
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				try {
					if(bis != null){
						bis.close();
						bis = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if(bos != null){
						bos.close();
						bos = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if(bos1 != null){
						bos1.close();
						bos1 = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return null;
			
		}
		
		throw new FileNotExitException(fileName);
	}
	
	/**
	 * 复制音频加密信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月8日 上午10:38:11
	 * @param AudioFileEncodePO audioEncode 被复制音频加密信息
	 * @param MediaAudioPO audio 音频媒资信息
	 * @return AudioFileEncodePO 音频加密信息
	 */
	public AudioFileEncodePO copy(AudioFileEncodePO audioEncode, MediaAudioPO audio) throws Exception{
		
		AudioFileEncodePO audioCopy = audioEncode.copy();
		
		audioCopy.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		audioCopy.setMediaId(audio.getId());
		
		audioFileEncodeDao.save(audioCopy);
		
		return audioCopy;
	}
	
	public static void decodeAudioFile(String filePath, String previewUrl) throws Exception{
	
		//AES加密
		AESUtil aesUtil = new AESUtil();
		byte[] key = AES_KEY.getBytes();
		byte[] iv = new byte[16];
//		byte[] iv = IV_KEY.getBytes();
		
		File file = new File(filePath);
		String fileName = file.getName();
		
		if (file.isFile() && file.exists()) {
			String[] nameSplit = fileName.split("\\.");
			String dirPath = filePath.split(fileName)[0];
			
			String newFileName = new StringBuilderWrapper().append(nameSplit[0])
					.append("_decode.")
					.append(nameSplit[1])
					.toString();
			String newFilePath = new StringBuilderWrapper().append(dirPath)
					.append(File.separator)
					.append(newFileName)
					.toString();
			
			File encodeFile = new File(newFilePath);
			
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			ByteArrayOutputStream bos1 = null;
			
			try{
				
				InputStream is = new FileInputStream(file);
				OutputStream os = new FileOutputStream(encodeFile);
				
				bis = new BufferedInputStream(is);
				bos = new BufferedOutputStream(os);
				bos1 = new ByteArrayOutputStream((int)(file.length()));
				
				byte[] data = new byte[1024];
				int len = 0;
				while((len = bis.read(data, 0, 1024)) != -1){
					bos1.write(data, 0, len);
				}
				
				byte[] result = aesUtil.decrypt(bos1.toByteArray(), key, iv);
				bos.write(result);
				
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				try {
					if(bis != null){
						bis.close();
						bis = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if(bos != null){
						bos.close();
						bos = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if(bos1 != null){
						bos1.close();
						bos1 = null;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}			
	}
	
	public static void main(String[] args) throws Exception{
		
//		encodeAudioFile("G:\\M\\Alin.mp3", null);
		decodeAudioFile("G:\\M\\encode\\Alin_encode.mp3", null);
		
	}
}
