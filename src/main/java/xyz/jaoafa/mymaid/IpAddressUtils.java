package xyz.jaoafa.mymaid;

import java.util.regex.Pattern;

/**
 * IPアドレスに関するユーティリティクラス
 */
public class IpAddressUtils {

    private static final Pattern V4_FORMAT = Pattern.compile("((([01]?\\d{1,2})|(2[0-4]\\d)|(25[0-5]))\\.){3}(([01]?\\d{1,2})|(2[0-4]\\d)|(25[0-5]))");

    /**
     * 指定された文字列がIPアドレスフォーマットか判定します。
     * 以下の場合にIPアドレスフォーマットとみなします。
     * ・ドットを区切り文字として、数字4組で構成される。
     * ・数字は0～255の範囲の3桁以下の値である (ゼロパディング許容)。
     * ・数字は半角数字である。
     *
     * @param str    判定する文字列
     * @return IPアドレスフォーマットの場合は true
     */
    public static boolean isIpv4Address(String str) {
        return isIpv4Address(str, true);
    }

    /**
     * 指定された文字列がIPアドレスフォーマットか判定します。
     * 以下の場合にIPアドレスフォーマットとみなします。
     * ・ドットを区切り文字として、数字4組で構成される。
     * ・数字は0～255の範囲の値である。
     * ・数字はInteger#parseInt()でint値と解釈される。
     *
     * @param str       判定する文字列
     * @param strict    厳密にフォーマットチェックするかの真偽値
     * @return IPアドレスフォーマットの場合は true
     */
    public static boolean isIpv4Address(String str, boolean strict) {
        if (strict) {
            return V4_FORMAT.matcher(str).matches();
        }

        String[] addrs = str.split("\\.");
        if (addrs.length != 4) {
            return false;
        }
        for (String addr : addrs) {
            try {
                int b = Integer.parseInt(addr);
                if (b < 0 || 255 < b) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}