package br.com.contabilidadereal.deccontrol.util;

import javax.servlet.http.HttpServletRequest;

public class buscarUrl {

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}