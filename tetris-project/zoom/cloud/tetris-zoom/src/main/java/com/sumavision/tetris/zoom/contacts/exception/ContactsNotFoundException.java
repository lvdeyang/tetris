package com.sumavision.tetris.zoom.contacts.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ContactsNotFoundException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public ContactsNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("联系人不存在， id：").append(id).toString());
	}

}
