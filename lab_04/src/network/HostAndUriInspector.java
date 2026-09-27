package network;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostAndUriInspector {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Lỗi thiếu tham số!");
            System.out.println("Cú pháp sử dụng: java network.HostAndUriInspector <hostname> <uri>");
            return;
        }

        String host = args[0];
        String uriString = args[1];

        System.out.println("==================================================");
        System.out.println("       KẾT QUẢ KHẢO SÁT HOST VÀ PHÂN TÍCH URI");
        System.out.println("==================================================");

        // 1. Phân giải Hostname bằng InetAddress
        System.out.println("[1] KHẢO SÁT HOSTNAME: " + host);
        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            for (InetAddress addr : addresses) {
                System.out.println("  - IP: " + addr.getHostAddress());
                if (addr instanceof Inet4Address) {
                    System.out.println("    + Phiên bản: IPv4");
                } else if (addr instanceof Inet6Address) {
                    System.out.println("    + Phiên bản: IPv6");
                }
                System.out.println("    + Canonical Hostname: " + addr.getCanonicalHostName());
                System.out.println("    + Loopback: " + addr.isLoopbackAddress());
                System.out.println("    + Site local: " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("  Lỗi: Không thể phân giải hostname [" + host + "]: " + e.getMessage());
        }

        // 2. Phân tích URI bằng java.net.URI
        System.out.println("\n[2] PHÂN TÍCH THÀNH PHẦN URI: " + uriString);
        try {
            URI uri = new URI(uriString);
            System.out.println("  - Scheme   : " + (uri.getScheme() != null ? uri.getScheme() : "(none)"));
            System.out.println("  - Host     : " + (uri.getHost() != null ? uri.getHost() : "(none)"));
            System.out.println("  - Port     : " + (uri.getPort() != -1 ? uri.getPort() : "(default/none)"));
            System.out.println("  - Path     : " + (uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "(none)"));
            System.out.println("  - Query    : " + (uri.getQuery() != null ? uri.getQuery() : "(none)"));
            System.out.println("  - Fragment : " + (uri.getFragment() != null ? uri.getFragment() : "(none)"));
        } catch (URISyntaxException e) {
            System.err.println("  Lỗi: Cú pháp URI không hợp lệ [" + uriString + "]: " + e.getReason());
        }
        System.out.println("==================================================\n");
    }
}
