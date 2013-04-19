package org.scauhci.studentAssistant.concrete;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.scauhci.studentAssistant.common.HtmlUtil;
import org.scauhci.studentAssistant.entity.ExamInformation;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Score;
import org.scauhci.studentAssistant.model.WebFetch;

public class SCAUWebFetch implements WebFetch {

	private String id;
	private HttpClient httpClient = null;
	private String viewstate = "";
	private boolean first;
	private boolean isLogin;
	private boolean isFirstScore;
	private String scoreViewState = "";
	private boolean firstExamInfo;
	private String examInfoViewState="";
	private String headerCookie="";
	private int connectTimeout=10000;

	public String getHeaderCookie() {
		return headerCookie;
	}

	public void setHeaderCookie(String headerCookie) {
		this.headerCookie = headerCookie;
	}

	public SCAUWebFetch() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, connectTimeout);
		httpClient = new DefaultHttpClient(httpParameters);
		
		first = true;
		isFirstScore = true;
		firstExamInfo=true;
		isLogin = false;
		
	}

	public List<Score> getScoresFromNetwork(String semester,String year,String property)
			throws UnsupportedEncodingException, IOException {
		String url = "http://202.116.160.167/xscjcx.aspx?xh="+id
		+"&xm=%D6%A3%BA%E8%B2%FD&gnmkdm=N121605";
		Document doc=null;
		HttpResponse getResponse;
		if (isFirstScore||semester==null||year==null) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Referer", "http://202.116.160.167/xs_main.aspx?xh=" + id);
			getResponse = httpClient.execute(httpGet);
			isFirstScore = false;
			InputStream in = getResponse.getEntity().getContent();
			doc = Jsoup.parse(in, "gb2312", "http://202.116.160.167/");
			scoreViewState = doc.select("input[name=__VIEWSTATE]").val();
			return null;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Referer", "http://202.116.160.167/xs_main.aspx?xh=" + id);
            
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ddlXQ", semester));
			params.add(new BasicNameValuePair("ddlXN", year));
			params.add(new BasicNameValuePair("btn_xq", "%D6%A3%BA%E8%B2%FD"));
			params.add(new BasicNameValuePair("ddl_kcxz", property));
			params.add(new BasicNameValuePair("__VIEWSTATE", scoreViewState));
			HttpEntity httpEntity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(httpEntity);
			getResponse = httpClient.execute(httpPost);
			InputStream in = getResponse.getEntity().getContent();
			doc = Jsoup.parse(in, "gb2312", "http://202.116.160.167/");
			scoreViewState = doc.select("input[name=__VIEWSTATE]").val();
		}
		//System.out.println(scoreViewState);
		//System.out.println(doc.html());
		
		return HtmlUtil.getScoresFromHtmlDoc(doc);
	}

	@Override
	public List<Lesson> getLessonsFromNetwork(String semester, String year,
			boolean isStudent) throws ClientProtocolException, IOException {
		if (!isLogin) {
			System.out.println("hadn't login");
			return null;
		}
		String url;
		if (isStudent) {
			url = "http://202.116.160.167/xskbcx.aspx?xh=" + id
					+ "&amp;xm=%D6%A3%BA%E8%B2%FD&amp;gnmkdm=N121603";
		} else {
			url = "http://202.116.160.167/jstjkbcx.aspx?zgh=" + id
					+ "&xm=%CB%BE%B9%FA%B6%AB&gnmkdm=N122303";
		}
		HttpResponse getResponse;

		if (first || year == null || semester == null) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Cookie", getHeaderCookie());
			if (isStudent) {
				httpGet.setHeader("Referer",
						"http://202.116.160.167/xs_main.aspx?xh=" + id);
			} else {
				httpGet.setHeader("Referer",
						"http://202.116.160.167/js_main.aspx?xh=" + id);
			}
			getResponse = httpClient.execute(httpGet);
			first = false;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Referer",
					"http://202.116.160.167/xs_main.aspx?xh=" + id);
			httpPost.setHeader("Cookie", getHeaderCookie());
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("__EVENTTARGET", "xnd"));
			params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			params.add(new BasicNameValuePair("xnd", year));
			params.add(new BasicNameValuePair("xqd", semester));
			params.add(new BasicNameValuePair("__VIEWSTATE", viewstate));
			HttpEntity httpEntity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(httpEntity);
			getResponse = httpClient.execute(httpPost);
		}
		InputStream in = getResponse.getEntity().getContent();
		Document doc = Jsoup.parse(in, "gb2312", "http://202.116.160.167/");
		viewstate = doc.select("input[name=__VIEWSTATE]").val();
		System.out.println(viewstate);
		// this.semester = semester;
		// this.year = year;
		return HtmlUtil.getLessonFromHtmlDoc(doc);
	}
	
	public InputStream getVerificationCode() throws ClientProtocolException, IOException{
		HttpGet getPage=new HttpGet("http://202.116.160.167");
		getPage.setHeader("Cookie", "ASP.NET_SessionId=qxbpbmbstvrnrx4525gagw55");
		getPage.setHeader("Connection", "keep-alive");
		getPage.setHeader("Host", "202.116.160.167");
		Header header = getPage.getLastHeader("Cookie");   
        System.out.println(header.getValue());  
        setHeaderCookie( header.getValue());  
        
		HttpResponse pageResponse=httpClient.execute(getPage);
		getPage.abort();
		HttpGet getImg=new HttpGet("http://202.116.160.167/CheckCode.aspx");
		getImg.setHeader("Cookie", getHeaderCookie());
		getImg.setHeader("Connection", "keep-alive");
		getImg.setHeader("Host", "202.116.160.167");
		HttpResponse imgResponse=httpClient.execute(getImg);
		getImg.abort();
		return imgResponse.getEntity().getContent();
		/*
		File file=new File("C:/regist.jpg");
		OutputStream output=new FileOutputStream(file);
		imgResponse.getEntity().writeTo(output);
		output.close();
		System.out.println("«Î ‰»Î—È÷§¬Î£∫");
		Scanner scanner=new Scanner(System.in);
		String code=scanner.nextLine();
		return code;*/
	}
	@Override
	public boolean login(String id, String password,String choose,String verificatioCode)
			throws ClientProtocolException, IOException {
		
		
		HttpPost httpPost = new HttpPost("http://202.116.160.167/default2.aspx");
		httpPost.setHeader("Cookie", getHeaderCookie());
		List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
		paramPairs.add(new BasicNameValuePair("TextBox1", id));
		paramPairs.add(new BasicNameValuePair("TextBox2", password));
		paramPairs.add(new BasicNameValuePair("TextBox3", verificatioCode));
		paramPairs.add(new BasicNameValuePair("RadioButtonList1", choose));
		paramPairs.add(new BasicNameValuePair("Button1", ""));
		paramPairs.add(new BasicNameValuePair("lbLanguage", ""));
		paramPairs.add(new BasicNameValuePair("__VIEWSTATE",
				"dDwtMTg3MTM5OTI5MTs7PpSoYR38Mx0U+AZn4KwFo3wnpa4O"));
		httpPost.setHeader("Referer", "http://202.116.160.167/default2.aspx");
		HttpEntity httpEntity = new UrlEncodedFormEntity(paramPairs);
		httpPost.setEntity(httpEntity);

		HttpResponse response = httpClient.execute(httpPost);
		
		boolean success = true;
		if (response.getStatusLine().getStatusCode() == 404) {
			success = false;
		}
		System.out.println(response.getStatusLine().getStatusCode());
		
		
		//InputStream in = response.getEntity().getContent();
		//Document doc = Jsoup.parse(in, "gb2312", "http://202.116.160.167/xs_main.aspx?xh="+id);
		//System.out.println(doc.html());
		httpPost.abort();
		
		/*
		HttpGet post2=new HttpGet("http://202.116.160.167/xs_main.aspx?xh="+id);
		post2.setHeader("Cookie", getHeaderCookie());
		response=httpClient.execute(post2);
		InputStream in2 = response.getEntity().getContent();
		Document doc2 = Jsoup.parse(in2, "gb2312", "http://202.116.160.167/xs_main.aspx?xh="+id);
		System.out.println(doc2.html());
		post2.abort();
		*/
		this.id = id;
		isLogin = true;
		return success;
	}

	@Override
	public List<ExamInformation> getExamInfosfromNetwork(String semester,String year) 
	      throws ClientProtocolException, IOException {
		String url="http://202.116.160.167/xskscx.aspx?xh="+id+"&xm=%D6%A3%BA%E8%B2%FD&gnmkdm=N121604";
		
		HttpResponse getResponse;
		if (firstExamInfo||semester==null||year==null) {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("Referer", "http://202.116.160.167/xs_main.aspx?xh=" + id);
			getResponse = httpClient.execute(httpGet);
			firstExamInfo = false;
		} else {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Referer", "http://202.116.160.167/xs_main.aspx?xh=" + id);
            
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("xqd", semester));
			params.add(new BasicNameValuePair("xnd", year));
			params.add(new BasicNameValuePair("__VIEWSTATE", examInfoViewState));
			HttpEntity httpEntity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(httpEntity);
			getResponse = httpClient.execute(httpPost);
		}
		InputStream in = getResponse.getEntity().getContent();
		Document doc = Jsoup.parse(in, "gb2312", "http://202.116.160.167/");
		examInfoViewState = doc.select("input[name=__VIEWSTATE]").val();
		//System.out.println(doc.html());
		
		return HtmlUtil.getExamInfosFromHtmlDoc(doc);
	}

	@Override
	public boolean login(String id, String password, String choose)
			throws Exception {
		
		return false;
	}

}
