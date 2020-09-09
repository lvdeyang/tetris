package com.sumavision.tetris.commons.log;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class StdOutErrRedirect {
	
	private final static Logger logger = LoggerFactory.getLogger(StdOutErrRedirect.class);
 
    public static void redirectSystemOutAndErrToLog() {
    	PrintStream printStreamForOut = createLoggingWrapper(System.out, false);
    	PrintStream printStreamForErr = createLoggingWrapper(System.out, true);
        System.setOut(printStreamForOut);
        System.setErr(printStreamForErr);
    }
    
    public static PrintStream createLoggingWrapper(
    		final PrintStream printStream, 
    		final boolean isErr){
    	
        return new PrintStream(printStream){
        	
        	@Override
            public void print(final String string){
        		if (!isErr){
        			logger.info(string);
        		}else{
        			logger.error(string);
        		}
            }
        	
        	@Override
            public void print(boolean b){
        		if (!isErr){
        			logger.info(String.valueOf(b));
        		}else{
        			logger.error(String.valueOf(b));
        		}
            }
        	
        	@Override
            public void print(char c){
        		if (!isErr){
            		logger.info(String.valueOf(c));
        		}else{
        			logger.error(String.valueOf(c));
        		}
            }
        	
        	@Override
            public void print(int i){
        		if (!isErr){
        			logger.info(String.valueOf(i));
        		}else{
        			logger.error(String.valueOf(i));
        		}
            }
        	
        	@Override
            public void print(long l){
        		if (!isErr){
        			logger.info(String.valueOf(l));
        		}else{
        			logger.error(String.valueOf(l));
        		}
            }
        	
        	@Override
            public void print(float f){
        		if (!isErr){
        			logger.info(String.valueOf(f));
        		}else{
        			logger.error(String.valueOf(f));
        		}
            }
        	
        	@Override
            public void print(double d){
        		if (!isErr){
            		logger.info(String.valueOf(d));
        		}else{
        			logger.error(String.valueOf(d));
        		}
            }
        	
        	@Override
            public void print(char[] x){
        		if (!isErr){
            		logger.info(x == null ? null : new String(x));
        		}else{
        			logger.error(x == null ? null : new String(x));
        		}
            }
        	
        	@Override
            public void print(Object obj){
        		if (!isErr){
        			if(obj instanceof Integer || 
        					obj instanceof Long || 
        					obj instanceof Float ||
        					obj instanceof Boolean ||
        					obj instanceof Double){
        				logger.info(obj.toString());
        			}else{
        				logger.info(JSON.toJSONString(obj));
        			}
        		}else{
        			if(obj instanceof Exception){
        				Exception e = (Exception)obj;
        				logger.error(e.getMessage(), e);
        			}else{
        				if(obj instanceof Integer || 
            					obj instanceof Long || 
            					obj instanceof Float ||
            					obj instanceof Boolean ||
            					obj instanceof Double){
            				logger.error(obj.toString());
            			}else{
            				logger.error(JSON.toJSONString(obj));
            			}
        			}
        		}
            }
        	
        	@Override
            public void println(final String string){
        		print(string);
            }
        	
        	@Override
            public void println(boolean b){
        		print(b);
            }
        	
        	@Override
            public void println(char c){
        		print(c);
            }
        	
        	@Override
            public void println(int i){
        		print(i);
            }
        	
        	@Override
            public void println(long l){
        		print(l);
            }
        	
        	@Override
            public void println(float f){
        		print(f);
            }
        	
        	@Override
            public void println(double d){
        		print(d);
            }
        	
        	@Override
            public void println(char[] x){
        		print(x);
            }
        	
        	@Override
            public void println(Object obj){
        		print(obj);
            }
        	
        };
    }
    
    public static void main(String[] args){
    	byte[] b = {9, 97, 116, 32};
    	System.out.println(1);
    	System.out.println(new String(b));
    	System.out.println(111);
    			
    }
    
}