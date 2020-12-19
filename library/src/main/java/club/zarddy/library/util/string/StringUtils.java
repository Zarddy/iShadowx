package club.zarddy.library.util.string;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

public class StringUtils {

    public static String getPercentString(float percent) {
        return String.format(Locale.US, "%d%%", (int) (percent * 100));
    }

    /**
     * 删除字符串中的空白符
     *
     * @param content
     * @return String
     */
    public static String removeBlanks(String content) {
        if (content == null) {
            return null;
        }
        StringBuilder buff = new StringBuilder();
        buff.append(content);
        for (int i = buff.length() - 1; i >= 0; i--) {
            if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
                    || ('\r' == buff.charAt(i))) {
                buff.deleteCharAt(i);
            }
        }
        return buff.toString();
    }

    /**
     * 获取32位uuid（不包含中划线，例如：969464eac8a04f58a161ebc0ed88e590）
     *
     * @return
     */
    public static String get32UUID() {
        return get36UUID().replaceAll("-", "");
    }

    /**
     * 生成唯一号（包含中划线，例如：67e4d7a6-7322-43da-afa2-b420484bcc3d）
     *
     * @return
     */
    public static String get36UUID() {
        return UUID.randomUUID().toString();
    }

    public static String makeMd5(String source) {
        return MD5.getStringMD5(source);
    }

    public static String filterUCS4(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        if (str.codePointCount(0, str.length()) == str.length()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();

        int index = 0;
        while (index < str.length()) {
            int codePoint = str.codePointAt(index);
            index += Character.charCount(codePoint);
            if (Character.isSupplementaryCodePoint(codePoint)) {
                continue;
            }
            sb.appendCodePoint(codePoint);
        }
        return sb.toString();
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    /**
     * 将字符串转换成每字符为四位数的unicode编码的字符串，不足四位的前方补0，例如：可爱，转换成 \u53ef\u7231
     * @param string
     * @return
     */
    public static String stringToUnicode(String string) {
        char[] chars = string.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            sb.append("\\u" + String.format("%04x", ((int) chars[i]) ));
        }
        return sb.toString();
    }

    /**
     * 去除重复元素
     */
    public static ArrayList<String> removeDuplicate(ArrayList<String> list){
        ArrayList<String> newList = new ArrayList<String>();     //创建新集合
        Iterator it = list.iterator();        //根据传入的集合(旧集合)获取迭代器
        while(it.hasNext()){          //遍历老集合
            String obj = String.valueOf(it.next());       //记录每一个元素
            if(!newList.contains(obj)){      //如果新集合中不包含旧集合中的元素
                newList.add(obj);       //将元素添加
            }
        }
        return newList;
    }
}
