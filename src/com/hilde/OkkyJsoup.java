package com.hilde;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OkkyJsoup {

    public void getOkky(String html) {

        String author = "";
        String link = "";
        String content = "";

        List<String> authors = new ArrayList<String>();
        List<String> links = new ArrayList<String>();
        List<String> contents = new ArrayList<String>();

        System.out.println("::: OkkyJsoup ::: Ver 0.0.5 :::");
        System.out.println("커뮤니티의 최신 글 20개만 받아옵니다.");
        // System.out.println("게시글 / 댓글 내부에 태그가 쓰인 경우엔 그대로 나오므로 주의!");
        System.out.println("==============================\n");

        try {
            // Connect With OKKY
            Document doc = Jsoup.connect(html).timeout(20000).get();

            Elements els = doc.getElementsByTag("ul");
            Element el = els.get(7);
            Elements els2 = el.getElementsByTag("a");

            // GET Article URL
            for (Element el2 : els2) {
                if (el2.attr("href").startsWith("/article")){
                    link = "http://okky.kr" +el2.attr("href");
                    content = el2.select("a").text();

                    contents.add(content);
                    links.add(link);

                } else if (el2.hasClass("nickname")) {
                    author = el2.attr("title");

                    authors.add(author);
                }
            }

            // Loop Article & Subs
            for (int i=0; i<authors.size(); i++) {
                System.out.println("no     :: [ " + (i + 1) + " ]");
                System.out.println("author :: " + authors.get(i));
                System.out.println("title  :: " + contents.get(i));
                System.out.println("link   :: " + links.get(i));


                // GET Article Contents
                getDept2(links.get(i));
            }

            System.out.println(":::[COMPLETE]:::");

        } catch (Exception e) {
            System.out.println(":::[ERROR]::: 왠지 몰라도 안되었음");
            e.printStackTrace();
        }

    }

    public void getDept2(String html) {

        String content = "";
        String content2 = "";

        List<String> sub_writer = new ArrayList<String>();
        List<String> sub_content = new ArrayList<String>();
        List<String> sub_like = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect(html).maxBodySize(0).timeout(10000).get();

            /* LEGACY CODE
            Elements els = doc.select("article[itemprop='articleBody'] p");
            content = replaceTag(els.toString());
             */

            // 22.03.08 EDIT :: $("article[itemprop='articleBody'] p") -> $("article[itemprop='articleBody'] p").text()
            // 22.03.17 EDIT :: $("article[itemprop='articleBody'] p").text() -> $("article[itemprop='articleBody']").text()
            content = doc.select("article[itemprop='articleBody']").text();

            // Get likes (include Main Article's like)
            Elements likes = doc.getElementsByClass("content-eval-count");

            System.out.println("like   :: " + likes.get(0).text());
            System.out.println("==============================\n");

            System.out.println(content);
            System.out.println("============================== \n");

            Elements els2 = doc.select("div[class='avatar-info'] a[class='nickname']");
            Elements els3 = doc.select("article[class='list-group-item-text note-text']");

            for (Element el2 : els2) {
                if (el2 != els2.get(0)) {
                    sub_writer.add(el2.attr("title"));
                }
            }
            for (Element el3 : els3) {
                // 22.03.15 : 리플 내용 제대로 가져오지 못하는 문제를 수정함.
                // content2 = replaceTag(el3.select("p").text());
                content2 = el3.text();
                sub_content.add(content2);
            }

            // G                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 et Sub Writer's like
            for (int i=1; i<likes.size(); i++) {
                sub_like.add(likes.get(i).text());
            }

            for (int i=0; i < sub_writer.size(); i++) {
                System.out.println("sub  :: " + sub_writer.get(i));
                System.out.println("like :: " + sub_like.get(i));
                System.out.println(sub_content.get(i));
                System.out.println("------------------------------ \n");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String replaceTag(String text) {

        String rep_text = "";

        if (null != text) {
            rep_text = text.replace("<p>", "").replace("</p>", "")
                    .replace("&nbsp;","").replace("<br>", " ").replace("&amp;", "");
        }

        return rep_text;
    }


}
