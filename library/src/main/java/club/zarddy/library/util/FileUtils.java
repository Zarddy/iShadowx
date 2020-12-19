package club.zarddy.library.util;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class FileUtils {

    public static boolean hasExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文件扩展名，不包括点符号
     * @param filename 文件名，文件全路径
     * @return 例如 picture.jpg文件，则返回“jpg”
     */
    public static String getExtensionName(String filename) {
        return getExtensionName(filename, false);
    }

    /**
     * 获取文件扩展名
     * @param filename 文件名，文件全路径
     * @param includeDot 是否包括扩展名的点符号
     * @return 如果includeDot为true，则返回“.jpg”等，如果为false，则返回“jpg”
     */
    public static String getExtensionName(String filename, boolean includeDot) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                if (includeDot) { //
                    return filename.substring(dot);
                } else {
                    return filename.substring(dot + 1);
                }
            }
        }
        return "";
    }

    // 获取文件名
    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }

    /**
     * 从文件路径中，获取除文件名以外的目录路径
     * @param filepath 文件全路径
     * @return 文件路径
     */
    public static String getPathExceptFileName(String filepath) {
        if (!TextUtils.isEmpty(filepath)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(0, sep + 1);
            }
        }
        return filepath;
    }

    // 获取不带扩展名的文件名
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getMimeType(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String type = null;
        String extension = getExtensionName(filePath.toLowerCase());
        if (!TextUtils.isEmpty(extension)) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }

        // FIXME
        if (TextUtils.isEmpty(type) && filePath.endsWith("aac")) {
            type = "audio/aac";
        }

        return type;
    }

    public enum SizeUnit {
        Byte,
        KB,
        MB,
        GB,
        TB,
        Auto,
    }

    public static String formatFileSize(long size) {
        return formatFileSize(size, SizeUnit.Auto);
    }

    public static String formatFileSize(long size, SizeUnit unit) {
        if (size < 0) {
            return "未知大小";
        }

        final double KB = 1024;
        final double MB = KB * 1024;
        final double GB = MB * 1024;
        final double TB = GB * 1024;
        if (unit == SizeUnit.Auto) {
            if (size < KB) {
                unit = SizeUnit.Byte;
            } else if (size < MB) {
                unit = SizeUnit.KB;
            } else if (size < GB) {
                unit = SizeUnit.MB;
            } else if (size < TB) {
                unit = SizeUnit.GB;
            } else {
                unit = SizeUnit.TB;
            }
        }

        switch (unit) {
            case Byte:
                return size + "B";
            case KB:
                return String.format(Locale.US, "%.2fKB", size / KB);
            case MB:
                return String.format(Locale.US, "%.2fMB", size / MB);
            case GB:
                return String.format(Locale.US, "%.2fGB", size / GB);
            case TB:
                return String.format(Locale.US, "%.2fPB", size / TB);
            default:
                return size + "B";
        }
    }

    public static double formatSize(long byteSize, SizeUnit targetUnit) {
        if (byteSize < 0) {
            return -1;
        }

        final double KB = 1024;
        final double MB = KB * 1024;
        final double GB = MB * 1024;
        final double TB = GB * 1024;

        switch (targetUnit) {
            case Byte:
                return byteSize;
            case KB:
                return byteSize / KB;
            case MB:
                return byteSize / MB;
            case GB:
                return byteSize / GB;
            case TB:
                return byteSize / TB;
            default:
                return -1;
        }
    }

    /**
     * 判断文件或目录是否已存在
     * @param path 文件或目录路径
     */
    public static boolean fileExists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    /**
     * 复制文件
     * @param srcPath 源文件路径
     * @param dstPath 目标文件路径
     */
    public static boolean copyFile(String srcPath, String dstPath) {
        try {
            File srcFile = new File(srcPath);
            if (srcFile.exists()) { // 文件存在时
                InputStream is = new FileInputStream(srcPath); // 读入原文件

                String dstDirPath = FileUtils.getPathExceptFileName(dstPath);
                File dstDir = new File(dstDirPath);
                if (!dstDir.exists()) {
                    dstDir.mkdirs();
                }

                File dstFile = new File(dstPath);
                if (!dstFile.exists()) {
                    dstFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(dstPath);
                byte[] buffer = new byte[1024];
                int readLen = 0;
                while ((readLen = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, readLen);
                }
                is.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取文件内容
     * @param filepath 文件路径
     */
    public static String getFileContent(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException ioe) {
            ;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ioe) {
                    ;
                }
            }

            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException ioe) {
                    ;
                }
            }

            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException ioe) {
                    ;
                }
            }
        }

        return sb.toString();
    }
}
