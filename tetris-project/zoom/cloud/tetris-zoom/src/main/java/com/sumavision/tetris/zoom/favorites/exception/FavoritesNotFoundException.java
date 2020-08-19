package com.sumavision.tetris.zoom.favorites.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class FavoritesNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public FavoritesNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("收藏夹不存在， id：").append(id).toString());
	}

}
