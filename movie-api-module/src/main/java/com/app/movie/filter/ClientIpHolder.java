package com.app.movie.filter;


public class ClientIpHolder {

    private static final ThreadLocal<String> clientIp = new ThreadLocal<>();

    public static void setClientIp(String ip) {
        clientIp.set(ip);
    }

    public static String getClientIp() {
        return clientIp.get();
    }

    public static void removeClientIp() {
        clientIp.remove();
    }


}
