package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Zheng Xuqiang on 2014/3/25 0025.
 * 一些加解密相关方法
 */
public class Crypt {
    /**
     * 生成一个随机salt值
     * @return 随机salt值
     */
    public static String genSalt() {
        return UUID.randomUUID().toString();
    }

    /**
     * 将bytes转换为16进制的String
     */
    public static String bytes2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        String tmp;

        for (byte b : bytes) {
            tmp = Integer.toHexString(b & 0xff);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }

        return sb.toString();
    }

    /**
     * SHA-256加密
     */
    public static byte[] sha256(String param) {
        return digest(param, "SHA-256");
    }

    /**
     * MD5加密
     */
    public static byte[] md5(String param) {
        return digest(param, "MD5");
    }

    /**
     * 根据传入的算法名来加密字符串
     * @param param 需要加密的字符串
     * @param algorithm 加密算法
     */
    public static byte[] digest(String param, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(param.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据传入的算法名来Hash文件
     * TODO 就算是选用MD5也耗时太长
     * @param file 需要Hash的字符串
     * @param algorithm 加密算法
     */
    public static byte[] digest(File file, String algorithm) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance(algorithm);
            FileInputStream fis = new FileInputStream(file);
            DigestInputStream dis = new DigestInputStream(fis, md);
            while(dis.read() != -1) ; // empty

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert md != null;
        return md.digest();
    }

    /**
     * 返回文件的MD5值
     * @param file digest的文件
     * @return 文件的MD5值
     */
    public static String digestFile(File file) {
        return bytes2Hex(digest(file, "MD5"));
    }
}
