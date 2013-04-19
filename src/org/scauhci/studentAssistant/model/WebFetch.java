package org.scauhci.studentAssistant.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.scauhci.studentAssistant.entity.Lesson;

import org.scauhci.studentAssistant.entity.ExamInformation;
import org.scauhci.studentAssistant.entity.Score;


/**
 * @author david
 *
 */
public interface WebFetch {

	/**
	 * 联网,获取指定用户和密码的学生的所以课程列表,课程列表无序
	 * @param semester
	 * @param year
	 * @return 课表列表
	 * @throws Exception
	 */
	public List<Lesson> getLessonsFromNetwork(String semester, String year,boolean isStudent)throws Exception;
	
	/**
	 * 登陆教务系统
	 * @param id
	 * @param password
	 * @return 是否成功
	 * @throws Exception
	 */
	public boolean login(String id,String password,String choose)throws Exception;
	
	
    /**
     * 获取成绩单
     * @param semester
     * @param year
     * @param property
     * @return
     * @throws Exception
     */
    public List<Score> getScoresFromNetwork(String semester,String year,String property) throws Exception;
	
    
	/**
	 * 获取考试信息
	 * @param semester
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public List<ExamInformation> getExamInfosfromNetwork(String semester,String year) throws Exception;
	
	/**
	 * 登陆教务系统（有验证码的情况）
	 * @param id
	 * @param password
	 * @param choose 类型，学生还是老师
	 * @param verificationCode 验证码
	 * @return 是否成功
	 * @throws Exception
	 */
	public boolean login(String id,String password,String choose,String verificationCode)throws Exception;
	
	/**
	 * 获取验证码输入流
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public InputStream getVerificationCode() throws ClientProtocolException, IOException;
}
