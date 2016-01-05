/*
 * Copyright (c) 2014~2016 HoryuSystems All rights reserved.
 *
 * 본 저작물의 모든 저작권은 HoryuSystems 에 있습니다.
 *
 * 소스를 참고하여 다른 프로그램을 제작하는 것은 허용되지만,
 * 프로그램의 접두사, 기능등의 수정 및 배포는 불가능합니다.
 *
 * 기능을 거의 똑같이 하여 제작하는 행위등은 '참고하여 다른 프로그램을 제작한다는 것' 에 해당하지 않습니다.
 *
 * ============================================
 * 본 소스를 참고하여 프로그램을 제작할 시 해당 프로그램에 본 소스의 출처/라이센스를 공식적으로 안내를 해야 합니다.
 * 출처: https://github.com/horyu1234
 * 라이센스: Copyright (c) 2014~2016 HoryuSystems All rights reserved.
 * ============================================
 *
 * 소스에 대한 피드백등은 언제나 환영합니다! 아래는 개발자 연락처입니다.
 *
 * Skype: horyu1234
 * KakaoTalk: horyu1234
 * Telegram: @horyu1234
 */

package com.horyu1234.nicknamehistory.json;

import java.io.IOException;
import java.io.Writer;

/**
 * JSONWriter provides a quick and convenient way of producing JSON text.
 * The texts produced strictly conform to JSON syntax rules. No whitespace is
 * added, so the results are ready for transmission or storage. Each instance of
 * JSONWriter can produce one JSON text.
 * <p>
 * A JSONWriter instance provides a <code>value</code> method for appending
 * values to the
 * text, and a <code>key</code>
 * method for adding keys before values in objects. There are <code>array</code>
 * and <code>endArray</code> methods that make and bound array values, and
 * <code>object</code> and <code>endObject</code> methods which make and bound
 * object values. All of these methods return the JSONWriter instance,
 * permitting a cascade style. For example, <pre>
 * new JSONWriter(myWriter)
 *     .object()
 *         .key("JSON")
 *         .value("Hello, World!")
 *     .endObject();</pre> which writes <pre>
 * {"JSON":"Hello, World!"}</pre>
 * <p>
 * The first method called must be <code>array</code> or <code>object</code>.
 * There are no methods for adding commas or colons. JSONWriter adds them for
 * you. Objects and arrays can be nested up to 20 levels deep.
 * <p>
 * This can sometimes be easier than using a JSONObject to build a string.
 * @author JSON.org
 * @version 2010-12-24
 */
public class JSONWriter {
    private static final int maxdepth = 20;

    /**
     * The comma flag determines if a comma should be output before the next
     * value.
     */
    private boolean comma;

    /**
     * The current mode. Values:
     * 'a' (array),
     * 'd' (done),
     * 'i' (initial),
     * 'k' (key),
     * 'o' (object).
     */
    protected char mode;

    /**
     * The object/array stack.
     */
    private JSONObject stack[];

    /**
     * The stack top index. A value of 0 indicates that the stack is empty.
     */
    private int top;

    /**
     * The writer that will receive the output.
     */
    protected Writer writer;

    /**
     * Make a fresh JSONWriter. It can be used to build one JSON text.
     */
    public JSONWriter(Writer w) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new JSONObject[maxdepth];
        this.top = 0;
        this.writer = w;
    }

    /**
     * Append a value.
     * @param string A string value.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    private JSONWriter append(String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null pointer");
        }
        if (this.mode == 'o' || this.mode == 'a') {
            try {
                if (this.comma && this.mode == 'a') {
                    this.writer.write(',');
                }
                this.writer.write(string);
            } catch (IOException e) {
                throw new JSONException(e);
            }
            if (this.mode == 'o') {
                this.mode = 'k';
            }
            this.comma = true;
            return this;
        }
        throw new JSONException("Value out of sequence.");
    }

    /**
     * Begin appending a new array. All values until the balancing
     * <code>endArray</code> will be appended to this array. The
     * <code>endArray</code> method must be called to mark the array's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public JSONWriter array() throws JSONException {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            this.push(null);
            this.append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }

    /**
     * End something.
     * @param mode Mode
     * @param c Closing character
     * @return this
     * @throws JSONException If unbalanced.
     */
    private JSONWriter end(char mode, char c) throws JSONException {
        if (this.mode != mode) {
            throw new JSONException(mode == 'a' ? "Misplaced endArray." : 
            		"Misplaced endObject.");
        }
        this.pop(mode);
        try {
            this.writer.write(c);
        } catch (IOException e) {
            throw new JSONException(e);
        }
        this.comma = true;
        return this;
    }

    /**
     * End an array. This method most be called to balance calls to
     * <code>array</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public JSONWriter endArray() throws JSONException {
        return this.end('a', ']');
    }

    /**
     * End an object. This method most be called to balance calls to
     * <code>object</code>.
     * @return this
     * @throws JSONException If incorrectly nested.
     */
    public JSONWriter endObject() throws JSONException {
        return this.end('k', '}');
    }

    /**
     * Append a key. The key will be associated with the next value. In an
     * object, every value must be preceded by a key.
     * @param string A key string.
     * @return this
     * @throws JSONException If the key is out of place. For example, keys
     *  do not belong in arrays or if the key is null.
     */
    public JSONWriter key(String string) throws JSONException {
        if (string == null) {
            throw new JSONException("Null key.");
        }
        if (this.mode == 'k') {
            try {
                stack[top - 1].putOnce(string, Boolean.TRUE);
                if (this.comma) {
                    this.writer.write(',');
                }
                this.writer.write(JSONObject.quote(string));
                this.writer.write(':');
                this.comma = false;
                this.mode = 'o';
                return this;
            } catch (IOException e) {
                throw new JSONException(e);
            }
        }
        throw new JSONException("Misplaced key.");
    }


    /**
     * Begin appending a new object. All keys and values until the balancing
     * <code>endObject</code> will be appended to this object. The
     * <code>endObject</code> method must be called to mark the object's end.
     * @return this
     * @throws JSONException If the nesting is too deep, or if the object is
     * started in the wrong place (for example as a key or after the end of the
     * outermost array or object).
     */
    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        if (this.mode == 'o' || this.mode == 'a') {
            this.append("{");
            this.push(new JSONObject());
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");

    }


    /**
     * Pop an array or object scope.
     * @param c The scope to close.
     * @throws JSONException If nesting is wrong.
     */
    private void pop(char c) throws JSONException {
        if (this.top <= 0) {
            throw new JSONException("Nesting error.");
        }
        char m = this.stack[this.top - 1] == null ? 'a' : 'k';
        if (m != c) {
            throw new JSONException("Nesting error.");
        }
        this.top -= 1;
        this.mode = this.top == 0 ? 
        		'd' : this.stack[this.top - 1] == null ? 'a' : 'k';
    }

    private void push(JSONObject jo) throws JSONException {
        if (this.top >= maxdepth) {
            throw new JSONException("Nesting too deep.");
        }
        this.stack[this.top] = jo;
        this.mode = jo == null ? 'a' : 'k';
        this.top += 1;
    }


    /**
     * Append either the value <code>true</code> or the value
     * <code>false</code>.
     * @param b A boolean.
     * @return this
     * @throws JSONException
     */
    public JSONWriter value(boolean b) throws JSONException {
        return this.append(b ? "true" : "false");
    }

    /**
     * Append a double value.
     * @param d A double.
     * @return this
     * @throws JSONException If the number is not finite.
     */
    public JSONWriter value(double d) throws JSONException {
        return this.value(new Double(d));
    }

    /**
     * Append a long value.
     * @param l A long.
     * @return this
     * @throws JSONException
     */
    public JSONWriter value(long l) throws JSONException {
        return this.append(Long.toString(l));
    }


    /**
     * Append an object value.
     * @param object The object to append. It can be null, or a Boolean, Number,
     *   String, JSONObject, or JSONArray, or an object that implements JSONString.
     * @return this
     * @throws JSONException If the value is out of sequence.
     */
    public JSONWriter value(Object object) throws JSONException {
        return this.append(JSONObject.valueToString(object));
    }
}
