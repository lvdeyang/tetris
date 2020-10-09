package com.sumavision.tetris.commons.util.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;

public class FileBodyWithProgress extends FileBody{

	public static interface UpdateProgress {
        public void update(long contentLength, long progress);
        public int getBlockSize();
    }
	
	private UpdateProgress up;

    public FileBodyWithProgress(File file, UpdateProgress up) {
        super(file);
        this.up = up;
    }
	
    @Override
    public void writeTo(OutputStream out) throws IOException {
        final InputStream in = super.getInputStream();
        try {
            final byte[] tmp = new byte[up.getBlockSize()];
            final long contentLength = getContentLength();
            long progress = 0;
            for (int len=0; (len=in.read(tmp))!=-1; up.update(contentLength, progress)) {
                out.write(tmp, 0, len);
                progress += len;
            }
            out.flush();
        } finally {
            in.close();
        }
    }
    
}
