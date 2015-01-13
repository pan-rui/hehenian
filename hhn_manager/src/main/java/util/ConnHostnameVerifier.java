package util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by lenovo on 2014/12/10.
 * 用于主机名验证的基接口。
 * 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，则验证机制可以回调此的实现程序来确定是否应该允许此连接
 */
public class ConnHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        if ("localhost".equals(hostname)) {
            return true;
        } else {
            return false;
        }
    }
}