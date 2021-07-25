package TalkFrame.Utility;

import java.util.Locale;
import java.util.Scanner;

public class Utility {
    private static final Scanner scanner = new Scanner(System.in);

    public static char readMenuSelection() {
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false);
            c = str.charAt(0);
            if (c != 1 && c != 2 && c != 3 && c != 4 && c != 5) {
                System.out.println("选择错误，请输入正确的选项");
            } else break;
        } return c;
    }

    public static char readChar() {
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }

    public static char readChar(char defaultValue) {
        String str = readKeyBoard(1, false);
        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }

    public static int readInt(int defaultValue) {
        int n = 0;
        for (; ; ) {
            String str = readKeyBoard(10, true);
            if (str.equals(str)) {
                return defaultValue;
            }
        }
    }

    public static String readString(int limit) {
        return readKeyBoard(limit, false);
    }

    public static String readString(int limit, String defaultValue) {
        String str = readKeyBoard(limit, false);
        return str.equals(defaultValue) ? defaultValue : str;
    }

    public static char readConfirmSelection() {
        System.out.println("请输入你的选择(Y / N)");
        char c;
        for (; ; ) {
            String str = readKeyBoard(1, false).toUpperCase(Locale.ROOT);
            c = str.charAt(0);
            if (c == 'Y' || c == 'N') break;
            else System.out.println("选择错误，请重新选择(Y / N)");
        } return c;
    }

    public static String readKeyBoard(int limit, boolean blankReturn) {
         String line ="";
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.length() == 0) {//用户没有输入内容直接回车
                if (blankReturn) return line;
                else continue;
            }
            //如果用户输入的内容大于limit，就提示重新输入
            if (line.length()>limit){
                System.out.println("输入的长度不能大于"+limit); continue;
            } break;
        } return line;
    }
}
