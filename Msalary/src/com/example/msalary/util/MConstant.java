package com.example.msalary.util;

public class MConstant {
//	115.29.45.6 192.168.1.111
	public final static String URL_HOME_PATH = "http://192.168.1.111:8080/Web/servlet/";
	
	public final static String URL_TEST = "Test";
	/**��ѯ��˾*/
	public final static String URL_SELECT_COMPANY = URL_HOME_PATH + "QueryCompanyServlet?";
	/**��ѯְλ*/
	public final static String URL_SELECT_POSITION = URL_HOME_PATH + "QueryJobServlet?";
	
	/**��˾����--�ù�˾�ĸ�λ�б�*/
	public final static String URL_COMPANY_DETAIL = URL_HOME_PATH + "JobsOfCompanyServlet?";
	/**��˾н������*/
	public final static String URL_COMPANY_SALARY_RANK = URL_HOME_PATH + "CompanySalaryRankServlet";
	/**��˾����*/
	public final static String URL_COMPANY_COMMENT =  URL_HOME_PATH +"CompanyCommentsServlet?";
	/**�½�����*/
	public final static String URL_COMPANY_COMMENT_NEW = URL_HOME_PATH + "NewCommentServlet?";
	/**�и�ְλ�����й�˾*/
	public final static String URL_COMPANYS_OF_JOB = URL_HOME_PATH + "CompanysOfJobServlet?";
	/**��λ����*/
	public final static String URL_JOB_DETAIL = URL_HOME_PATH + "JobDetailServlet?";
	/**�ع���*/
	public final static String URL_NEW_JOB = URL_HOME_PATH + "NewSalaryServlet?";
	/**��н����ϸ��������*/
	public final static String URL_USERFUL = URL_HOME_PATH +"";
	
	
	
}