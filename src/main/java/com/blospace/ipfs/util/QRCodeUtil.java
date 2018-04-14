package com.blospace.ipfs.util;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 990;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    private static BufferedImage createImage(String content, String imgPath,
            boolean needCompress) throws Exception {
        return createImage(content, imgPath, needCompress, QRCODE_SIZE, QRCODE_SIZE);
    }
    private static BufferedImage createImage(String content, String imgPath,
    		boolean needCompress,int imgWidth,int imgHeight) throws Exception {
    	Hashtable hints = new Hashtable();
    	hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    	hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
    	hints.put(EncodeHintType.MARGIN, 1);
    	BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
    			BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints);
    	int width = bitMatrix.getWidth();
    	int height = bitMatrix.getHeight();
    	BufferedImage image = new BufferedImage(width, height,
    			BufferedImage.TYPE_INT_RGB);
    	for (int x = 0; x < width; x++) {
    		for (int y = 0; y < height; y++) {
    			image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
    					: 0xFFFFFFFF);
    		}
    	}
    	if (imgPath == null || "".equals(imgPath)) {
    		return image;
    	}
    	// 插入图片
    	QRCodeUtil.insertImage(image, imgPath, needCompress);
    	return image;
    }

   
    private static void insertImage(BufferedImage source, String imgPath,
            boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(""+imgPath+"   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

   
    public static File encode(String content, String imgPath, String destPath,
            boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999)+".jpg";
        File outFile =  new File(destPath+"/"+file);
        ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
        return outFile;
    }

   
    public static void mkdirs(String destPath) {
        File file =new File(destPath);   
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

	public static void encodeByTemplate(String url,String templateImgPath,int templateW,int templateH,int qrInTemplateX,int qrInTemplateY,int qrImgW,int qrImgH,String imgPath, String destFile, boolean needCompress) throws Exception{
		BufferedImage img = QRCodeUtil.createImage(url, imgPath, true,qrImgW,qrImgH);
		BufferedImage image = new BufferedImage(templateW, templateH, BufferedImage.TYPE_INT_RGB);
		if(templateImgPath!=null){
			File templateImgFile = new File(templateImgPath);
			if(templateImgFile.exists()){
				Graphics graphics = image.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(0, 0, templateW, templateH);
				Image template = ImageIO.read(new File(templateImgPath));
				graphics.drawImage(template, 0, 0, null);
				graphics.setColor(Color.WHITE);
				graphics.fillRect(qrInTemplateX, qrInTemplateY, qrImgW, qrImgH);
				graphics.drawImage(img, qrInTemplateX, qrInTemplateY, null);
				File outFile = new File(destFile);
				ImageIO.write(image, "png", outFile);
			}else{
				File outFile = new File(destFile);
				ImageIO.write(img, "png", outFile);
			}
		}else{
			File outFile = new File(destFile);
			ImageIO.write(img, "png", outFile);
		}
	}
	


	public static void encode(String content, String imgPath, String destPath)
            throws Exception {
        QRCodeUtil.encode(content, imgPath, destPath, false);
    }

   
    public static void encode(String content, String destPath,
            boolean needCompress) throws Exception {
        QRCodeUtil.encode(content, null, destPath, needCompress);
    }

   
    public static void encode(String content, String destPath) throws Exception {
        QRCodeUtil.encode(content, null, destPath, false);
    }

   
    public static void encode(String content, String imgPath,
            OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

   
    public static void encode(String content, OutputStream output)
            throws Exception {
        QRCodeUtil.encode(content, null, output, false);
    }

   
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

   
    public static String decode(String path) throws Exception {
        return QRCodeUtil.decode(new File(path));
    }
   
   
    public static void main(String[] args) throws Exception {
        String text = "http://www.baidu.com";
//        QRCodeUtil.encode(text, "C:/Users/yind0/Desktop/logo.jpg", "C:/Users/yind0/Desktop", true);
        String destFile = "/Users/luohui/Desktop/" + new Random().nextInt(99999999) + ".jpg";

        // 定义图像buffer         
       /* BufferedImage buffImg = new BufferedImage(QRCODE_SIZE, 60,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();*/

//        QRCodeUtil.encodeByTemplate(text, "/Users/luohui/Desktop/cbto_tpl.jpg", 2480, 3508, 760, 1334, 990, 990, null,  destFile, true);
        QRCodeUtil.encodeByTemplate(text, "/Users/luohui/Desktop/qr_code_tpl/black.png", 2480, 3348, 468, 817, 1554, 1554, null,  destFile, true);


       /* // 将图像填充为白色
        g.setColor(Color.WHITE);    
        g.fillRect(0, 0, QRCODE_SIZE, 60);  
        g.setColor(Color.BLACK);    
        g.setFont(new Font("", 1, 40));
        g.drawString("一楼[A1]", 30, 30);
        String file = new Random().nextInt(99999999)+".jpg";
        File outFile =  new File("C:/Users/yind0/Desktop/"+file);
        ImageIO.write(buffImg, "jpg", outFile);*/
//        QRCodeUtil.encode(content, imgPath, destPath, needCompress);
    }
   
}