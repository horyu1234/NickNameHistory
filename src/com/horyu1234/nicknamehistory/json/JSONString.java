/*
 * Copyright (c) 2015 HoryuSystems All rights reserved.
 *
 * �� ���۹��� ��� ���۱��� HoryuSystems �� �ֽ��ϴ�.
 *
 * �ҽ��� �����Ͽ� �ٸ� ���α׷��� �����ϴ� ���� ��������,
 * ���α׷��� ���λ�, ��ɵ��� ���� �� ������ �Ұ����մϴ�.
 *
 * �ҽ��� ���� �ǵ����� ������ ȯ���մϴ�! �Ʒ��� ������ ����ó�Դϴ�.
 *
 * Skype: horyu1234
 * KakaoTalk: horyu1234
 * Telegram: @horyu1234
 */

package com.horyu1234.nicknamehistory.json;
/**
 * The <code>JSONString</code> interface allows a <code>toJSONString()</code> 
 * method so that a class can change the behavior of 
 * <code>JSONObject.toString()</code>, <code>JSONArray.toString()</code>,
 * and <code>JSONWriter.value(</code>Object<code>)</code>. The 
 * <code>toJSONString</code> method will be used instead of the default behavior 
 * of using the Object's <code>toString()</code> method and quoting the result.
 */
public interface JSONString {
	/**
	 * The <code>toJSONString</code> method allows a class to produce its own JSON 
	 * serialization. 
	 * 
	 * @return A strictly syntactically correct JSON text.
	 */
	public String toJSONString();
}
