package com.ascend.assetcheck_jinhua.utils;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件处理工具
 */
public class FileUtils {

    /**
     * 保存字节流至文件
     *
     * @param bytes 字节流
     * @param file  目标文件
     */
    public static final boolean saveBytesToFile(byte[] bytes, File file) {
        if (bytes == null) {
            return false;
        }

        ByteArrayInputStream bais = null;
        BufferedOutputStream bos = null;
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            bais = new ByteArrayInputStream(bytes);
            bos = new BufferedOutputStream(new FileOutputStream(file));

            int size;
            byte[] temp = new byte[1024];
            while ((size = bais.read(temp, 0, temp.length)) != -1) {
                bos.write(temp, 0, size);
            }

            bos.flush();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bais = null;
            }
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     */
    public static final boolean copyFile(File srcFile, File destFile) {
        if (!srcFile.exists()) {
            return false;
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();

            bis = new BufferedInputStream(new FileInputStream(srcFile));
            bos = new BufferedOutputStream(new FileOutputStream(destFile));

            int size;
            byte[] temp = new byte[1024];
            while ((size = bis.read(temp, 0, temp.length)) != -1) {
                bos.write(temp, 0, size);
            }

            bos.flush();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bos = null;
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
        }
        return false;
    }

    /**
     * 根据文件路径获得全文件名
     *
     * @param path 文件路径
     */
    public static final String getFileFullNameByPath(String path) {
        String name = null;
        if (path != null) {
            int start = path.lastIndexOf(File.separator);
            name = path.substring(start == -1 ? 0 : start + 1);
        }
        return name;
    }

    /**
     * 根据文件路径获得文件名
     *
     * @param path 文件路径
     */
    public static final String getFileNameByPath(String path) {
        String name = null;
        if (path != null) {
            int start = path.lastIndexOf(File.separator);
            int end = path.lastIndexOf(".");
            name = path.substring(start == -1 ? 0 : start + 1, end == -1 ? path.length() : end);
        }
        return name;
    }

    /**
     * 根据文件路径获得后缀名
     *
     * @param path 文件路径
     */
    public static final String getFileTypeByPath(String path) {
        String type = null;
        if (path != null) {
            int start = path.lastIndexOf(".");
            if (start != -1) {
                type = path.substring(start + 1);
            }
        }
        return type;
    }

    /**
     * 根据URL获得全文件名
     *
     * @param url URL
     */
    public static final String getFileFullNameByUrl(String url) {
        String name = null;
        if (url != null) {
            int end = url.lastIndexOf("?");
            if (end == -1)
                end = url.length();
            int start = url.substring(0, end).lastIndexOf("/");
            name = url.substring(start == -1 ? 0 : start + 1, end == -1 ? url.length() : end);
        }
        return name;
    }

    /**
     * 根据URL获得文件名
     *
     * @param url URL
     */
    public static final String getFileNameByUrl(String url) {
        String name = null;
        if (url != null) {
            int start = url.lastIndexOf("/");
            int end = url.lastIndexOf(".");
            int end2 = url.lastIndexOf("?");
            name = url.substring(start == -1 ? 0 : start + 1, end == -1 ? (end2 == -1 ? url.length() : end2) : end);
        }
        return name;
    }

    /**
     * 根据URL获得后缀名
     *
     * @param url URL
     */
    public static final String getFileTypeByUrl(String url) {
        String type = null;
        if (url != null) {
            int start = url.lastIndexOf(".");
            int end = url.lastIndexOf("?");
            if (start != -1) {
                type = url.substring(start + 1, end == -1 ? url.length() : end);
            }
        }
        return type;
    }

    /**
     * 清理文件
     */
    public static final void deleteFile(String path) {
        if (path != null && !path.trim().equals("")) {
            File tempFolder = new File(path);
            if (tempFolder.exists()) {
                File[] tempFiles = tempFolder.listFiles();
                for (File tempFile : tempFiles) {
                    tempFile.delete();
                }
            }
        }
    }

    /**
     * 追加内容到sd卡
     * @author lish
     * created at 2018-08-03 14:29
     */
    public static void write(String content) {
        try {
            //判断实际是否有SD卡，且应用程序是否有读写SD卡的能力，有则返回true
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的目录
                File sdCardDir = Environment.getExternalStorageDirectory();
                String path = "/资产盘点/";
                File dir = new File(sdCardDir + path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File targetFile = new File(sdCardDir.getCanonicalPath() + path + "data.txt");
                //使用RandomAccessFile是在原有的文件基础之上追加内容，
                //而使用outputstream则是要先清空内容再写入
                RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
                //光标移到原始文件最后，再执行写入
                raf.seek(targetFile.length());
                raf.write(content.getBytes());
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
