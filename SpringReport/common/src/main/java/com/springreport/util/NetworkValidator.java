package com.springreport.util;

import java.util.regex.Pattern;

public class NetworkValidator {

    // IPv4 正则
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}" +
            "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );

    // IPv6 正则（简化版，覆盖常见格式，含 :: 压缩格式）
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,7}:$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}$|" +
            "^(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}$|" +
            "^[0-9a-fA-F]{1,4}:(?:(?::[0-9a-fA-F]{1,4}){1,6})$|" +
            "^:(?:(?::[0-9a-fA-F]{1,4}){1,7}|:)$"
    );

    // 域名正则（支持子域名，顶级域至少2位字母，支持国际化域名需额外处理）
    private static final Pattern DOMAIN_PATTERN = Pattern.compile(
            "^(?=.{1,253}$)" +
            "(?!-)[A-Za-z0-9-]{1,63}(?<!-)\\." +
            "((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)*" +
            "(?!-)[A-Za-z]{2,63}(?<!-)$"
    );

    // 单段主机名正则（如 localhost）
    private static final Pattern HOSTNAME_PATTERN = Pattern.compile(
            "^(?!-)[A-Za-z0-9-]{1,63}(?<!-)$"
    );

    /**
     * 校验 IPv4
     */
    public static boolean isIpv4(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * 校验 IPv6
     */
    public static boolean isIpv6(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        return IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * 校验 IP（IPv4 或 IPv6）
     */
    public static boolean isIp(String ip) {
        return isIpv4(ip) || isIpv6(ip);
    }

    /**
     * 校验域名
     */
    public static boolean isDomain(String domain) {
        if (domain == null || domain.isEmpty()) return false;
        return DOMAIN_PATTERN.matcher(domain).matches();
    }

    /**
     * 校验主机名（单段，如 localhost）
     */
    public static boolean isHostname(String hostname) {
        if (hostname == null || hostname.isEmpty()) return false;
        return HOSTNAME_PATTERN.matcher(hostname).matches();
    }

    /**
     * 校验域名或 IP（统一入口）
     */
    public static boolean isDomainOrIp(String value) {
        return isIp(value) || isDomain(value);
    }

    /**
     * 判断输入是 IP 还是域名（更细粒度的结果）
     *
     * @return "IPV4" / "IPV6" / "DOMAIN" / "INVALID"
     */
    public static String getType(String value) {
        if (isIpv4(value)) return "IPV4";
        if (isIpv6(value)) return "IPV6";
        if (isDomain(value)) return "DOMAIN";
        return "INVALID";
    }

    // ==================== 测试 ====================
    public static void main(String[] args) {
        String[] testCases = {
        		"12",
                "192.168.1.1",
                "255.255.255.255",
                "256.1.1.1",
                "1.1.1",
                "::1",
                "2001:0db8:85a3:0000:0000:8a2e:0370:7334",
                "2001:db8::1",
                "fe80::",
                "example.com",
                "www.example.com",
                "sub.domain.example.co.uk",
                "-invalid.com",
                "invalid-.com",
                "example",
                "localhost",
                "example.123",
                "工具.example.com"
        };

        for (String t : testCases) {
            System.out.printf("%-45s => %-8s | ip=%b, domain=%b, domainOrIp=%b%n",
                    t, getType(t), isIp(t), isDomain(t), isDomainOrIp(t));
        }
    }
}
