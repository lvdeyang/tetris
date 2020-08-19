package com.sumavision.tetris.mims.app.bo;

import java.util.UUID;

import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;

public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			MimsServerPropsQuery mimsServerPropsQuery = new MimsServerPropsQuery();
			String uuid = generateShortUuid();
			BoUtil.uploadFile("D:\\duofen.mp4", uuid.toString(), mimsServerPropsQuery.queryProps().getOmcftpIp(),
					Integer.parseInt(mimsServerPropsQuery.queryProps().getOmcftpPort()),
					mimsServerPropsQuery.queryProps().getOmcftpUsername(),
					mimsServerPropsQuery.queryProps().getOmcftpPassword());
			BoUtil.uploadFile("D:\\1.jpg", uuid.toString(), mimsServerPropsQuery.queryProps().getOmcftpIp(),
					Integer.parseInt(mimsServerPropsQuery.queryProps().getOmcftpPort()),
					mimsServerPropsQuery.queryProps().getOmcftpUsername(),
					mimsServerPropsQuery.queryProps().getOmcftpPassword());
			AdiBo adiBo = new AdiBo();
			adiBo.setFileName("duofen.mp4");
			adiBo.setMediaId(uuid.toString());
			adiBo.setFormat("video");
			String adi = BoUtil.injectBo(adiBo);
			String result = BoUtil.post("http://" + mimsServerPropsQuery.queryProps().getOmcftpIp()
					+ ":8120/jc/JcAssetController/createProgram", adi);
			System.out.println(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();

	}

}
