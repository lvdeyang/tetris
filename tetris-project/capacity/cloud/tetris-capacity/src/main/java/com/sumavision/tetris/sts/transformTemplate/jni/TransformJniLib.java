package com.sumavision.tetris.sts.transformTemplate.jni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TransformJniLib {

	private static Logger logger = LoggerFactory.getLogger(TransformJniLib.class);

	private static String Lib_PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath();

	static{
		try {

			logger.info("jni path in: {}",Lib_PATH);
			setLibraryPath(ClassUtils.getDefaultClassLoader().getResource("").getPath());
			String osName = System.getProperty("os.name");
			logger.info("web application will run on {}",osName);
			if (osName.toLowerCase().startsWith("w")) {
				if (Lib_PATH.startsWith("/")){
					Lib_PATH = Lib_PATH.substring(1);
				}
				System.loadLibrary("tp_template_jni");
			}else{
				if (!Lib_PATH.startsWith("/")){
					Lib_PATH = "/" + Lib_PATH;
				}
				System.load(Lib_PATH + "libtp_template_jni.so.1.0.0");
			}
//			setLibraryPath("E:\\");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private static TransformJniLib instance = new TransformJniLib();

	public static void setLibraryPath(String path) throws Exception {
		System.setProperty("java.library.path", path);
		final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
		sysPathsField.setAccessible(true);
		sysPathsField.set(null, null);
	}

	public native void SetTemplatePath(String file);

	public native ArrayList<String> GetMp3sampleRates();
	
	public native ArrayList<Integer> GetChLayouts(int enc_type);
	
	public native ArrayList<Integer> GetRcModeItems(int enc_type);//ûʵ��;
	
	public native ArrayList<String> GetLevelItems(int enc_type);
	
	public native ArrayList<String> GetProfileItems(int enc_type);
	
	public native ArrayList<Integer> GetSupportEncoder(int platform);
	
	public native String GetMp3EncParamTemplate(String samplerate);
	
	public native String GetAudioEncParamTemplate(int encoder_type, int layout);
	
	public native String GetVideoEncParamTemplate(int encoder_type, String profile, String level, int rc_mode);

    public native void HelloWordVoid();

    private TransformJniLib(){
    	logger.info("template json path :{}",Lib_PATH);
		SetTemplatePath(Lib_PATH);
	}

	public static TransformJniLib getInstance(){
    	return instance;
	}
    
    public static void main(String[] args) {

        // TODO Auto-generated method stub
    	
        //System.getProperty("java.library.path");
    	String er;

//		TransformJniLib transformJniLib = new TransformJniLib();
//		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
//		if (path.startsWith("/")){
//			path = path.substring(1);
//		}
	//	transformJniLib.SetTemplatePath();
//		System.out.println(path);

//    	temp.HelloWordVoid();
//    	System.out.println(TransformJniLib.getInstance().GetVideoEncParamTemplate(0, "main", "4.0" ,4));
    	System.out.println(TransformJniLib.getInstance().GetAudioEncParamTemplate(5, 0));
//    	System.out.println(TransformJniLib.getInstance().GetMp3EncParamTemplate("44.1"));



//		ArrayList<String> aa = TransformJniLib.getInstance().GetProfileItems(2);
//    	ArrayList<Integer> aa = temp.GetSupportEncoder(0);
//    	temp.GetMp3sampleRates();
//    	int size = aa.size();
//    	for(int i = 0; i < size; ++i)
//    	{
//    		System.out.println(aa.get(i));
//    	}
    	
    	//temp.HelloWordVoid();

    }
}
