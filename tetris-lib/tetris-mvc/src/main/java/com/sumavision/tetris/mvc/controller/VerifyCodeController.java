package com.sumavision.tetris.mvc.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.image.ImageUtil;
import com.sumavision.tetris.commons.util.random.RandomMessage;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.context.HttpSessionContext;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * 生成验证码
 * lvdeyang 2017年6月12日
 */
@Controller
@RequestMapping("/api/verify/code")
public class VerifyCodeController{
	
	@Autowired
	private RandomMessage randomMessage;
	
	@Autowired
	private ImageUtil imageUtil;
	
	private Random random = new Random(System.currentTimeMillis());

	/**
	 * 生成图片验证码返回base64格式<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>Administrator<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 上午11:39:20
	 * @param @PathVariable int width 宽
	 * @param @PathVariable int height 高
	 * @return sessionToken 下次提交后携带的token
	 * @return image base64图片
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/generate/base64/{width}/{height}", method=RequestMethod.GET)
	public Object generateBase64(
			@PathVariable int width, 
			@PathVariable int height, 
			HttpServletRequest request) throws Exception {
		
		ByteArrayOutputStream out = null;
		try{
	        //生成随机字串 
	        String verifyCode = randomMessage.numberAndLetter(4); 
	        
	        //存入session
	        HttpSession session = request.getSession();
	        session.setMaxInactiveInterval(HttpConstant.VERIFICATION_CODE_TIMEOUT);
	        session.setAttribute("verification-code-image", verifyCode);
	        
	        //生成图片 
	        out = new ByteArrayOutputStream();
	        outputImage(width, height, out, verifyCode); 
	        String imageUrlSchema = imageUtil.getUrlSchema(out.toByteArray());
	        
	        return new HashMapWrapper<String, String>().put("sessionToken", session.getId())
	        										   .put("image", imageUrlSchema)
	        										   .getMap();
		}finally{
			if(out != null) out.close();
		}
	} 
	
	/**
	 * 携带token生成图片<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月12日 下午12:59:59
	 * @param @PathVariable int width 宽
	 * @param @PathVariable int height 高
	 * @param String sessionToken 指定sesison token
	 */
	@RequestMapping(value = "/get/{width}/{height}/{sessionToken}", method=RequestMethod.GET)
	public void generate(
			@PathVariable int width, 
			@PathVariable int height, 
			@PathVariable String sessionToken,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		response.setHeader("Pragma", "No-cache"); 
        response.setHeader("Cache-Control", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        response.setContentType("image/jpeg"); 
           
        //生成随机字串 
        String verifyCode = randomMessage.numberAndLetter(4); 
        
        //存入session
        HttpSession session = HttpSessionContext.get(sessionToken);
        if(session == null){
        	session = request.getSession();
        	session.setMaxInactiveInterval(HttpConstant.VERIFICATION_CODE_TIMEOUT);
        	HttpSessionContext.put(sessionToken, session);
        }
        session.setAttribute("verification-code-image", verifyCode);
        
        //生成图片 
        outputImage(width, height, response.getOutputStream(), verifyCode); 
	} 
	
    /**
     * 生成随机验证码文件,并返回验证码值
     * @param w
     * @param h
     * @param outputFile
     * @param verifySize
     * @return
     * @throws IOException
     */
    public String outputVerifyImage(int w, int h, File outputFile, int verifySize) throws Exception{
        String verifyCode = randomMessage.numberAndLetter(verifySize);
        outputImage(w, h, outputFile, verifyCode);
        return verifyCode;
    }
     
    /**
     * 输出随机验证码图片流,并返回验证码值
     * @param w
     * @param h
     * @param os
     * @param verifySize
     * @return
     * @throws IOException
     */
    public String outputVerifyImage(int w, int h, OutputStream os, int verifySize) throws Exception{
        String verifyCode = randomMessage.numberAndLetter(verifySize);
        outputImage(w, h, os, verifyCode);
        return verifyCode;
    }
     
    /**
     * 生成指定验证码图像文件
     * @param w
     * @param h
     * @param outputFile
     * @param code
     * @throws IOException
     */
    public void outputImage(int w, int h, File outputFile, String code) throws IOException{
        if(outputFile == null){
            return;
        }
        File dir = outputFile.getParentFile();
        if(!dir.exists()){
            dir.mkdirs();
        }
        try{
            outputFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(outputFile);
            outputImage(w, h, fos, code);
            fos.close();
        } catch(IOException e){
            throw e;
        }
    }
     
    /**
     * 输出指定验证码图片流
     * @param w
     * @param h
     * @param os
     * @param code
     * @throws IOException
     */
    public void outputImage(int w, int h, OutputStream os, String code) throws IOException{
        int verifySize = code.length();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
                Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                Color.PINK, Color.YELLOW };
        float[] fractions = new float[colors.length];
        for(int i = 0; i < colors.length; i++){
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }
        Arrays.sort(fractions);
         
        g2.setColor(Color.GRAY);// 设置边框色
        g2.fillRect(0, 0, w, h);
         
        Color c = getRandColor(200, 250);
        g2.setColor(c);// 设置背景色
        g2.fillRect(0, 2, w, h-4);
         
        //绘制干扰线
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));// 设置线条的颜色
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(w - 1);
            int y = random.nextInt(h - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g2.drawLine(x, y, x + xl + 40, y + yl + 20);
        }
         
        // 添加噪点
        float yawpRate = 0.05f;// 噪声率
        int area = (int) (yawpRate * w * h);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }
         
        shear(g2, w, h, c);// 使图片扭曲
 
        g2.setColor(getRandColor(100, 160));
        int fontSize = h-4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        g2.setFont(font);
        char[] chars = code.toCharArray();
        for(int i = 0; i < verifySize; i++){
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize/2, h/2);
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 + fontSize/2 - 10);
        }
         
        g2.dispose();
        
        ImageIO.write(image, "jpg", os);
    }
     
    private Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
     
    private int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }
     
    private int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        return rgb;
    }
 
    private void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }
     
    private void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                            / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
    }
 
    private void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10; // 50;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                            / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
        }
    }
	
}
