package com.sumavision.tetris.mims.app.media.compress.api.server.XmlVO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Signature")
public class SignatureVO {
	private String SignatureValue;

	@XmlElement(name = "SignatureValue")
	public String getSignatureValue() {
		return SignatureValue;
	}

	public void setSignatureValue(String signatureValue) {
		SignatureValue = signatureValue;
	}
}
