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
	 * ����,��ȡָ���û��������ѧ�������Կγ��б�,�γ��б�����
	 * @param semester
	 * @param year
	 * @return �α��б�
	 * @throws Exception
	 */
	public List<Lesson> getLessonsFromNetwork(String semester, String year,boolean isStudent)throws Exception;
	
	/**
	 * ��½����ϵͳ
	 * @param id
	 * @param password
	 * @return �Ƿ�ɹ�
	 * @throws Exception
	 */
	public boolean login(String id,String password,String choose)throws Exception;
	
	
    /**
     * ��ȡ�ɼ���
     * @param semester
     * @param year
     * @param property
     * @return
     * @throws Exception
     */
    public List<Score> getScoresFromNetwork(String semester,String year,String property) throws Exception;
	
    
	/**
	 * ��ȡ������Ϣ
	 * @param semester
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public List<ExamInformation> getExamInfosfromNetwork(String semester,String year) throws Exception;
	
	/**
	 * ��½����ϵͳ������֤��������
	 * @param id
	 * @param password
	 * @param choose ���ͣ�ѧ��������ʦ
	 * @param verificationCode ��֤��
	 * @return �Ƿ�ɹ�
	 * @throws Exception
	 */
	public boolean login(String id,String password,String choose,String verificationCode)throws Exception;
	
	/**
	 * ��ȡ��֤��������
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public InputStream getVerificationCode() throws ClientProtocolException, IOException;
}
