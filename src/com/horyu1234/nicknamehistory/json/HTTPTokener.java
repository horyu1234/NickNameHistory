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
 * The HTTPTokener extends the JSONTokener to provide additional methods
 * for the parsing of HTTP headers.
 * @author JSON.org
 * @version 2010-12-24
 */
public class HTTPTokener extends JSONTokener {

    /**
     * Construct an HTTPTokener from a string.
     * @param string A source string.
     */
    public HTTPTokener(String string) {
        super(string);
    }


    /**
     * Get the next token or string. This is used in parsing HTTP headers.
     * @throws JSONException
     * @return A String.
     */
    public String nextToken() throws JSONException {
        char c;
        char q;
        StringBuffer sb = new StringBuffer();
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '"' || c == '\'') {
            q = c;
            for (;;) {
                c = next();
                if (c < ' ') {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                sb.append(c);
            }
        } 
        for (;;) {
            if (c == 0 || Character.isWhitespace(c)) {
                return sb.toString();
            }
            sb.append(c);
            c = next();
        }
    }
}
