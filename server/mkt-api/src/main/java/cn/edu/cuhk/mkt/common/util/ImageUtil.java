package cn.edu.cuhk.mkt.common.util;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 图片工具类
 *
 * @author taokai
 */
public class ImageUtil {
    /**
     * 上传文件生成的缩略图的默认宽度
     */
    public final static int UPLOAD_PREVIEW_WIDTH = 150;

    /**
     * 上传文件生成的缩略图的默认高度
     */
    public final static int UPLOAD_PREVIEW_HEIGHT = 150;

    /**
     * base64图片
     */
    public final static String IMAGE_BASE64_PREFIX = "data:image/jpeg;base64,";

    /**
     * 图片后缀
     */
    public final static List<String> IMAGE_SUFFIX = new ArrayList<>(Arrays.asList(
        ".png",
        ".gif",
        ".jpg",
        ".jpeg",
        ".bmp",
        ".tff",
        ".tiff"
    ));

    /**
     * 通过原图创建压缩图
     *
     * @param file
     * @return
     */
    public static InputStream generateCompressImage(File file) {
        // 先将存在CMYK模式的图片转RGB模式。
        InputStream sourceImgFile = cmykToRGB(file);
        try {
            Thumbnails.Builder builder = Thumbnails.of(sourceImgFile);
            //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
            builder.scale(1f);
            if(sourceImgFile.available()>=1024*1024*4){
                builder.outputQuality(0.1d);
            }else  if(sourceImgFile.available()>=1024*1024*3){
                builder.outputQuality(0.2d);
            }else  if(sourceImgFile.available()>=1024*1024*2){
                builder.outputQuality(0.3d);
            }else if(sourceImgFile.available()>=1024*500){
                builder.outputQuality(0.7d);
            }else if(sourceImgFile.available()>=1024*300){
                builder.outputQuality(0.8d);
            }else if(sourceImgFile.available()>=1024*200)  {
                builder.outputQuality(0.9d);
            }else {
                //少于200KB不压缩
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            builder.outputFormat("jpg").toOutputStream(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sourceImgFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过原图创建预览图
     *
     * @param file
     * @return
     */
    public static InputStream generatePreviewImage(File file) {
        // 先将存在CMYK模式的图片转RGB模式。
        InputStream sourceImgFile = cmykToRGB(file);
        try {
            //图片尺寸不变，压缩图片文件大小outputQuality实现,参数1为最高质量
            BufferedImage sourceBuffImage = ImageIO.read(sourceImgFile);
            int sWidth = sourceBuffImage.getWidth();
            int sHeight = sourceBuffImage.getHeight();
            int tWidth;
            int tHeight;
            Thumbnails.Builder builder = Thumbnails.of(sourceBuffImage);
            if(sWidth < UPLOAD_PREVIEW_WIDTH
                    && sHeight < UPLOAD_PREVIEW_HEIGHT) {
                tWidth = sWidth;
                tHeight = sHeight;
            } else if(sWidth > sHeight) {
                tWidth = UPLOAD_PREVIEW_WIDTH;
                tHeight = UPLOAD_PREVIEW_WIDTH * sHeight / sWidth;
            } else {
                tHeight = UPLOAD_PREVIEW_HEIGHT;
                tWidth = UPLOAD_PREVIEW_HEIGHT * sWidth / sHeight;
            }
            sourceBuffImage.flush();
            builder.size(tWidth, tHeight);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            builder.outputFormat("jpg").toOutputStream(baos);
            // 输出缩略图
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sourceImgFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过原图创建缩略图
     *
     * @param localFile
     * @param path
     * @param fileName
     * @param extension
     * @return 返回生成的缩略图文件名
     */
    public String generateThumbnailImage(File localFile, String path, String fileName, String extension){
        try{
            //1464947211905_s.png
            String smallImgPath = path + fileName + "_s" + extension;
            //1464947211905_t.png
            String thumbImgPath = path + fileName + "_t" +extension;
            //图像处理
            BufferedImage image = ImageIO.read(localFile);
            int imageWidth = image.getWidth();
            int imageHeitht = image.getHeight();
            //生成缩小图，尺寸除以4倍
            Thumbnails.of(image).size(imageWidth/4, imageHeitht/4).outputQuality(0.5f).toFile(smallImgPath);
            //生成缩略图，尺寸除以2倍
            Thumbnails.of(image).size(imageWidth/2, imageHeitht/2).outputQuality(0.5f).toFile(thumbImgPath);
        }catch(IOException e){
            e.printStackTrace();
        }
        return fileName + "_t" +extension;
    }

    /**
     * 将图片转成base64
     * @param file
     * @return
     */
    public String generateBase64(File file){
        String imgStr = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length && (numRead = fis.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            BASE64Encoder encoder = new BASE64Encoder();
            imgStr = encoder.encode(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return IMAGE_BASE64_PREFIX + imgStr;
    }

    /**
     * 通过文件，cmyk类型转RGB
     * @param file
     * @return
     */
    public static InputStream cmykToRGB(File file) {
        InputStream sourceImgFile = null;
        try {
            ImageInputStream input = ImageIO.createImageInputStream(file);
            Iterator readers = ImageIO.getImageReaders(input);
            if (readers == null || !readers.hasNext()) {
                throw new RuntimeException("1 No ImageReaders found");
            }
            ImageReader reader = (ImageReader) readers.next();
            reader.setInput(input);
            String format = reader.getFormatName();
            BufferedImage image;
            try {
                // 尝试读取图片 (包括颜色的转换)，RGB
                image = reader.read(0);
            } catch (IIOException e) {
                // 读取Raster (没有颜色的转换)，CMYK
                Raster raster = reader.readRaster(0, null);
                image = createJPEG4(raster);
            }
            image.getGraphics().drawImage(image, 0, 0, null);
            // BufferedImage 转 InputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(image, "jpg", imageOutput);
            sourceImgFile = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            input.close();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sourceImgFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sourceImgFile;
    }

    /**
     * 创建JPEG4图片
     * @param raster
     * @return
     */
    private static BufferedImage createJPEG4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];
        // 彩色空间转换
        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);
        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i],
                    cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);
        }
        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[]{0, 1, 2}, null);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }

    /**
     * 判断上传附件类型，看是否有生成缩略图的权限
     *
     * @param suffix
     *            只有.tff .tiff .png .gif .jpg .jpeg 才会生成缩略图
     *
     * @return true /false
     */
    public Boolean isImageFormat(String suffix) {
        return IMAGE_SUFFIX.contains(suffix);
        /*return ".TFF".equalsIgnoreCase(suffix)
                || ".TIFF".equalsIgnoreCase(suffix)
                || ".PNG".equalsIgnoreCase(suffix)
                || ".GIF".equalsIgnoreCase(suffix)
                || ".JPG".equalsIgnoreCase(suffix)
                || ".JPEG".equalsIgnoreCase(suffix);*/
    }

}
