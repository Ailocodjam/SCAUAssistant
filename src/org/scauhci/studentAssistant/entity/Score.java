package org.scauhci.studentAssistant.entity;

import java.io.Serializable;

public class Score implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1099847691466720389L;
	
	private String name;
	private String quality;
	private double credit;
	private double score_point;
	private int mark;
	private String course_code;
	private String college;
	private int auxiliary_mark;
	
	
	public Score(String name, String quality, double credit,
			double score_point, int mark, String course_code, String college,
			int auxiliary_mark) {
		super();
		this.name = name;
		this.quality = quality;
		this.credit = credit;
		this.score_point = score_point;
		this.mark = mark;
		this.course_code = course_code;
		this.college = college;
		this.auxiliary_mark = auxiliary_mark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public double getScore_point() {
		return score_point;
	}
	public void setScore_point(double score_point) {
		this.score_point = score_point;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getCourse_code() {
		return course_code;
	}
	public void setCourse_code(String course_code) {
		this.course_code = course_code;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public int getAuxiliary_mark() {
		return auxiliary_mark;
	}
	public void setAuxiliary_mark(int auxiliary_mark) {
		this.auxiliary_mark = auxiliary_mark;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{"+"name:"+name+",quality:"+quality+",credit:"+credit+
		        ",score_point:"+score_point+",mark:"+mark+",course_code:"+course_code+
		        ",college:"+college+",auxiliary_mark:"+auxiliary_mark+"}";
	}
	
	
	
}
