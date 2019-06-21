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
		
		System.out.println("::: OkkyJsoup ::: Ver 0.0.2 :::");
		System.out.println("커뮤니티의 최신 글 20개만 받아옵니다.");
		System.out.println("게시글 / 댓글 내부에 태그가 쓰인 경우엔 그대로 나오므로 주의!");
		System.out.println("==============================\n");
		
		try {
			Document doc = Jsoup.connect(html).timeout(10000).get();
			
			Elements els = doc.getElementsByTag("ul");
			Element el = els.get(7);
			Elements els2 = el.getElementsByTag("a");
			
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
			
			for (int i=0; i<authors.size(); i++) {
				System.out.println("no :: [ " + (i + 1) + " ]");
				System.out.println("author :: " + authors.get(i));
				System.out.println("title :: " + contents.get(i));
				System.out.println("link :: " + links.get(i));
				System.out.println("=========\n");
				
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
		
		try {
			Document doc = Jsoup.connect(html).timeout(5000).get();
			Elements els = doc.select("article[itemprop='articleBody'] p");
			
			content = replaceTag(els.toString());
			
			System.out.println(content);
			System.out.println("========== \n");
			
			Elements els2 = doc.select("div[class='avatar-info'] a[class='nickname']");
			Elements els3 = doc.select("article[class='list-group-item-text note-text']");
			
			for (Element el2 : els2) {
				if (el2 != els2.get(0)) {
					sub_writer.add(el2.attr("title"));
				}
			}
			for (Element el3 : els3) {
				content2 = replaceTag(el3.select("p").text());
				sub_content.add(content2);
			}
			
			for (int i=0; i < sub_writer.size(); i++) {
				System.out.println("sub :: " + sub_writer.get(i));
				System.out.println(sub_content.get(i));
				System.out.println("---------- \n");
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public String replaceTag(String text) {
		
		String rep_text = "";
		
		if (null != text) {
			rep_text = text.replace("<p>", "").replace("</p>", "")
						   .replace("&nbsp","").replace("<br>", " ").replace("&amp;", "");
		}
		
		return rep_text;
	}
	
	
}
