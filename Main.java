import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class Main {
	
	static int pageCount=0;
	//linkListToDo, MapToDoLayer 需要保持同步
	static LinkedList<String>linkListToDo=new LinkedList<String>();
	static HashMap<String,Integer>MapToDoLayer=new HashMap<String,Integer>();
	static HashMap<String,String>MapUrlTitle=new HashMap<String,String>();
	static HashMap<String,Integer>MapDone=new HashMap<String,Integer>();
	
	public static void main(String[] args){ 

		String URL=new String();
		//URL="https://www.zhihu.com/question/31427895";
		//URL="https://www.linkedin.com/profile/view?id=AAMAAA_LDYgBJmAUNxFzc1OvDijq72IxzgUxgKs&authType=name&authToken=Feu5&trk=hp-feed-member-name";
		//URL="http://www.buaa.edu.cn/";
		URL="http://www.cnn.com/";
		//URL="http://www.facebook.com";
		//URL="https://xtubeth.com";
		//URL="http://www.adidas.com";
		//URL="http://www.linkedin.com";
		//URL="http://www.indeed.com/jobs?q=chicago&l=Chicago%2C+IL";
		linkListToDo.add(URL);
		MapToDoLayer.put(URL, 0);
		MapUrlTitle.put(URL, "Root");
		BFS(linkListToDo,MapDone);
		System.out.println(MapDone); //显示有多少待检测的url

	}
	public static LinkedList<String> UrlSelector(Document PageRawHtml){ //处理DOC生成Link Set
		LinkedList<String> linkListNewURLSet=new LinkedList<String>();int i=0;
		//检测Document是否空白，否则 .getElementsByTan() 会报错
		if(PageRawHtml!=null){
			//System.out.println(PageRawHtml.body().text());
			//获取 href= 标记 ,(href="www.xxx.com")
			Elements links = PageRawHtml.getElementsByTag("a");
			for (org.jsoup.nodes.Element link : links) {
			  String linkHref = link.attr("href");
			  String linkText = link.text();;
			  //判断网址是否合法，是否为空。判断有无http,https开头
			  if(linkHref.length()>7){
				  if((linkHref.substring(0,7)).equals("http://")||(linkHref.substring(0,8)).equals("https://")){
					  linkListNewURLSet.add(linkHref);
					  MapUrlTitle.put(linkHref,linkText); //保存抓取的url名字
		} else {
			//空白网页返回空的linkSet, 不可设置为null
		}

			  }
		  }
		  
		}
		//linkListNewURLSet.add("http://www.baidu.com");
		return linkListNewURLSet;
	}
	
	public static void BFS(LinkedList<String>ToDo, HashMap<String,Integer>Done){ //对Link Set进行BFS
		//BFS 宽度优先
		while(!ToDo.isEmpty()){
			String str=ToDo.getFirst();
			int layer=MapToDoLayer.get(str);
			if(Done.containsKey(str)){
				//Done重复的url
				//Done.put(str, (Done.get(str)+1));
				ToDo.removeFirst();
				MapToDoLayer.remove(str);
			}else{
				//Done非重复的url, 写入Done
				Done.put(str, layer);
				System.out.println("Title: "+MapUrlTitle.get(str)+"\n"+"	Layer: "+layer+" --Analyzed: "+MapDone.size()+" --Remain: "+linkListToDo.size()+" --"+str);
				ToDo.removeFirst();
				MapToDoLayer.remove(str);
				//url生成Link Set
				//检测ToDo是否已经有这个url了
				for(String str1:(UrlSelector(connection(str)))){
					if(!ToDo.contains(str1)){
						ToDo.add(str1);
						MapToDoLayer.put(str1, layer+1);
						//System.out.println(ToDo.size()); //显示有多少待检测的url
					}else{}
				}
				//ToDo.addAll(UrlSelector(connection(str)));
			}
		}		
		
	}
	
	public static Document connection(String URL){ //输入URL，返回DOC
		Document webPage = null;
		pageCount++;
		try {
			webPage=Jsoup.connect(URL).header("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2").cookie("auth", "token").timeout(5000).get();
			//webPage=null;
			//System.out.println(webPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			webPage=null; //connection失败返回一个空网页
			System.out.println("===============================");
			System.out.println("Bad url: "+URL+"\n"+"===============================");
		
		}
		return webPage;
	}
}
