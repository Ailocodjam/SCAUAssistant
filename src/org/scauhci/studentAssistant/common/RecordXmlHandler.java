package org.scauhci.studentAssistant.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.scauhci.studentAssistant.entity.Lesson;
import org.scauhci.studentAssistant.entity.Schedule;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Xml;

public class RecordXmlHandler {

	public static final String LESSONS_DATA_DIRECTORY="/sdcard/ScauAssistant";
	public static final String defaultXmlFilePath = "/lessonRecord.xml";

	public static Schedule getLessonsFromXML() {
		File file = new File(LESSONS_DATA_DIRECTORY+defaultXmlFilePath);
		if (!file.exists()) {
			System.out.println("createContactsFromXML file not exits");
			return null;
		}
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		Schedule schedule = new Schedule();
		try {
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();

			doc = docBuilder.parse(file);

			Element root = doc.getDocumentElement();
			String userId = root.getAttribute("userId");
			schedule.setUserId(userId);
			String semester = root.getAttribute("semester");
			schedule.setSemester(semester);
			String year = root.getAttribute("year");
			schedule.setYear(year);
			System.out.println("userId:"+userId+" semester:"+semester+" year:"+year);
			Map<Integer, List<Lesson>> lessonLists = new HashMap<Integer, List<Lesson>>();
			schedule.setLessonLists(lessonLists);
			NodeList nodeList = root.getElementsByTagName("lessons");
			System.out.println("lessons length:"+nodeList.getLength());
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				String day = node.getAttributes().getNamedItem("day").getNodeValue();
				System.out.println("day:"+day);
				NodeList conValList = node.getChildNodes();
				List<Lesson> lessonList=new ArrayList<Lesson>();
				System.out.println("lessonList length:"+conValList.getLength());
				for (int j = 0; j < conValList.getLength(); j++) {
					Node conNode = conValList.item(j);
					
					NodeList lessonValues = conNode.getChildNodes();
					System.out.println("lessonValues length:"+lessonValues.getLength());
					Lesson lesson = new Lesson();
					for (int k = 0; k < lessonValues.getLength(); k++) {
						
						String name = lessonValues.item(k).getNodeName();
						String value = lessonValues.item(k).getTextContent();
						System.out.println("name :" + name + " value: " + value);
						if (name != null && !"".equals(name)) {
							if (value != null) {
								Method method = lesson.getClass().getMethod(getSetterMethod(name), String.class);
								method.invoke(lesson, value);
							}
							Method method = lesson.getClass().getMethod(getGetterMethod(name));
							String conValue = (String) method.invoke(lesson);
							System.out.println(" conatct name:" + name+ " value:" + conValue);

						}
					}
					lessonList.add(lesson);
				}
				lessonLists.put(Integer.parseInt(day), lessonList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			doc = null;
			docBuilder = null;
			docBuilderFactory = null;
		}
		return schedule;
	}

	/**
	 * 获取当前属性的GETTER方法
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getGetterMethod(String fieldName) {
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return methodName;
	}

	/**
	 * 获取当前属性的SETTER方法
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getSetterMethod(String fieldName) {
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		return methodName;
	}

	/**
	 * 将课表转成XML字符串
	 * 
	 * @param schedule
	 * @return
	 */
	public static String getScheduleXmlString(Schedule schedule) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "schedule");
			serializer.attribute("", "userId", schedule.getUserId());
			serializer.attribute("", "semester", schedule.getSemester());
			serializer.attribute("", "year", schedule.getYear());
			Map<Integer, List<Lesson>> lessonLists = schedule.getLessonLists();
			Iterator it = lessonLists.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Integer key = (Integer) entry.getKey();
				List<Lesson> lessons = (List<Lesson>) entry.getValue();
				serializer.startTag("", "lessons");
				serializer.attribute("", "day", key.toString());
				for (Lesson lesson : lessons) {

					Field fields[] = lesson.getClass().getDeclaredFields();

					serializer.startTag("", "lesson");
					System.out.println("fields length:"+fields.length);
					for (int i = 0; i < fields.length; i++) {
						String fieldName = fields[i].getName();
						Method method = lesson.getClass().getMethod(
								getGetterMethod(fieldName));
						String fieldValue = (String) method.invoke(lesson);
						serializer.startTag("", fieldName);
						serializer.text(fieldValue);
						serializer.endTag("", fieldName);
					}
					serializer.endTag("", "lesson");
				}
				serializer.endTag("", "lessons");
			}
			serializer.endTag("", "schedule");
			serializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

	public static boolean writeXmlToFile(String content) {
		if(isSDCardAvalible()){
		File outFile = new File(LESSONS_DATA_DIRECTORY+defaultXmlFilePath);
		OutputStream output = null;
		try {
			output = new FileOutputStream(outFile);
			output.write(content.getBytes());
			output.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
		}else{
			return false;
		}
	}
	
	public static boolean isRecordXmlCache(){
		File file = new File(LESSONS_DATA_DIRECTORY+defaultXmlFilePath);
		return file.exists();
	}
	
	/**判断SD卡是否插入**/
	public static boolean isSDCardAvalible(){
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File directory = new File(LESSONS_DATA_DIRECTORY);
			File xmlFile = new File(LESSONS_DATA_DIRECTORY+defaultXmlFilePath);
			if(!directory.exists())
				directory.mkdirs();
			if(!xmlFile.exists())
				try {
					xmlFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			return true;
		} else {
			return false;
		}
	}
}
