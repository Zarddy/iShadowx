package club.zarddy.ishadowx.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import club.zarddy.ishadowx.model.IShadowAccount;

public class JsoupParser {

    public static List<IShadowAccount> parse(String html) {
        List<IShadowAccount> list = new ArrayList<>();
        try {
            Document document = Jsoup.parse(html);
            if (document == null) {
                return null;
            }

            Elements elements = document.select("div.hover-text");
            if (elements == null) {
                return null;
            }

            for (Element element : elements) {
                if (element == null) {
                    continue;
                }

                String ip = "";
                String port = "";
                String password = "";
                String method = "";

                Element ipElement = element.selectFirst("span[id~=ip*]");
                if (ipElement != null) {
                    ip = ipElement.text().trim();
                } else {
                    continue;
                }

                Element portElement = element.selectFirst("span[id~=port*]");
                if (portElement != null) {
                    port = portElement.text().trim();
                }

                Element pwdElement = element.selectFirst("span[id~=pw*]");
                if (pwdElement != null) {
                    password = pwdElement.text().trim();
                }

                Elements h4Elements = element.select("h4");
                if (h4Elements != null) {
                    if (h4Elements.size() > 1) {
                        method = h4Elements.get(h4Elements.size() - 2).text();
                        method = method.substring(method.indexOf(":") + 1).trim();
                    }
                }

                System.out.println("IP: " + ip);
                System.out.println("Port: " + port);
                System.out.println("Password: " + password);
                System.out.println("Method: " + method);
                System.out.println();

                list.add(new IShadowAccount(ip, port, password, method));
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getLocalizedMessage());
        }
        return list;
    }
}
