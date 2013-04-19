package org.scauhci.studentAssistant.common;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scauhci.studentAssistant.entity.ExamInformation;
import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Score;

public class HtmlUtil {

	public static Lesson getLessonFromValue(String value) {
		String[] ss = value.split("\\s");
		System.out.println(ss[0]);
		System.out.println(ss[1]);
		System.out.println(ss[2]);
		String name = ss[0];
		String[] sss = ss[1].split("µÚ");
		for (String s : sss) {
			System.out.println(s);
		}
		String weekday = sss[0];
		String lessonTime = "µÚ"+sss[1].substring(0, sss[1].indexOf("{"));
		String week = "µÚ"+sss[2].substring(0, sss[2].indexOf("}"));
		String teacher = ss[2].replace("(", " ").split("\\s")[0];
		String classroom = "";
		if (ss.length > 3) {
			classroom = ss[3];
		}
		return new Lesson(name, weekday, lessonTime, week, teacher, classroom);
	}
	
	public static List<Lesson> getLessonFromHtmlDoc(Document doc){
		

		Element table = doc.getElementById("Table1");
		Elements elements = table.select("tr");
		System.out.println(elements.size());
		List<Lesson> lessonList = new ArrayList<Lesson>();

		for (Element element : elements) {
			System.out.println(element.text());
			Elements tdElements = element.select("[rowspan=2]");
			for (Element tdElement : tdElements) {
				System.out.println(tdElement.text());
				lessonList.add(getLessonFromValue(tdElement.text()));
			}
		}
		System.out.println("*********************");
		for (Lesson lesson : lessonList) {
			System.out.println(lesson);
		}
		return lessonList;
	}
	
	public static List<Score> getScoresFromHtmlDoc(Document doc){
		Element table=doc.getElementById("Datagrid1");
		Elements trs=table.select("tr");
		List<Score> scores=new ArrayList<Score>();
		for(int i=1;i<trs.size();i++){
			Elements tds=trs.get(i).select("td");
			String name=tds.get(3).text();
			String quality=tds.get(4).text();
			double credit=Double.parseDouble(tds.get(6).text());
			double score_point=Double.parseDouble(tds.get(7).text());
			int mark=Integer.parseInt(tds.get(8).text());
			String course_code=tds.get(2).text();
			String college=tds.get(12).text();
			int auxiliary_mark=0;//Integer.parseInt(tds.get(9).text());
			Score score=new Score(name, quality, credit, 
					score_point, mark, course_code, college, auxiliary_mark);
			System.out.println(score.toString());
			scores.add(score);
		}
		return scores;
	}
	
	public static List<ExamInformation> getExamInfosFromHtmlDoc(Document doc){
		Element table=doc.getElementById("DataGrid1");
		Elements trs=table.select("tr");
		List<ExamInformation> infos=new ArrayList<ExamInformation>();
		for(int i=1;i<trs.size();i++){
			Elements tds=trs.get(i).select("td");
			String course_name=tds.get(1).text();
			String time=tds.get(3).text();
			String place=tds.get(4).text();
			String exam_style=tds.get(5).text();
			String seat=tds.get(6).text();
			String school_area=tds.get(7).text();
			ExamInformation info=new ExamInformation(course_name, 
					time, place, exam_style, seat, school_area);
			System.out.println(info.toString());
			infos.add(info);
		}
		return infos;
	}
}
