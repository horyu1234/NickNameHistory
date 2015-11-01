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
 * The JSONException is thrown by the JSON.org classes when things are amiss.
 * @author JSON.org
 * @version 2010-12-24
 */
public class JSONException extends Exception {
	private static final long serialVersionUID = 0;
	private Throwable cause;

    /**
     * Constructs a JSONException with an explanatory message.
     * @param message Detail about the reason for the exception.
     */
    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }
}
