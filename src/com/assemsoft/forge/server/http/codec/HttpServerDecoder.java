/**
 * @Title: HttpServerDecoder.java
 * @Package com.assemsoft.forge.server.http.codec
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 下午03:07:04
 * @version V1.0
 */
package com.assemsoft.forge.server.http.codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.assemsoft.forge.server.http.message.HttpMethod;
import com.assemsoft.forge.server.http.message.HttpRequestImpl;
import com.assemsoft.forge.server.http.message.HttpVersion;
import com.assemsoft.forge.util.ArrayUtil;

/**
 * @ClassName: HttpServerDecoder
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 下午03:07:04
 */
public class HttpServerDecoder extends CumulativeProtocolDecoder {

    /** Regex to parse HttpRequest Request Line */
    public static final Pattern REQUEST_LINE_PATTERN = Pattern.compile(" ");

    /** Regex to parse out QueryString from HttpRequest */
    public static final Pattern QUERY_STRING_PATTERN = Pattern.compile("\\?");

    /** Regex to parse out parameters from query string */
    public static final Pattern PARAM_STRING_PATTERN = Pattern.compile("\\&|;");

    /** Regex to parse out key/value pairs */
    public static final Pattern KEY_VALUE_PATTERN = Pattern.compile("=");

    /** Regex to parse raw headers and body */
    public static final Pattern RAW_VALUE_PATTERN = Pattern.compile("\\r\\n\\r\\n");

    /** Regex to parse raw headers from body */
    public static final Pattern HEADERS_BODY_PATTERN = Pattern.compile("\\r\\n");

    /** Regex to parse header name and value */
    public static final Pattern HEADER_VALUE_PATTERN = Pattern.compile(": ");

    /** Regex to split cookie header following RFC6265 Section 5.4 */
    public static final Pattern COOKIE_SEPARATOR_PATTERN = Pattern.compile(";");

    /*
     * (non-Javadoc)
     * @see
     * org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache
     * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer,
     * org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer msg, ProtocolDecoderOutput out) throws Exception {
	boolean issuccess = false;
	final HttpRequestImpl rq = parseHttpRequestHead(msg.buf());
	if (rq != null) {
	    boolean flag = true;
	    if (rq.getMethod() == HttpMethod.POST || rq.getMethod() == HttpMethod.PUT) {
		final String contentLen = rq.getHeader("content-length");
		if (contentLen != null && contentLen != "" && contentLen != "0") {
		    long size = Integer.valueOf(contentLen);
		    if (size <= msg.remaining()) {
			final IoBuffer wb = IoBuffer.allocate(Integer.valueOf(contentLen));
			wb.put(msg.array(), msg.position(), wb.capacity());
			wb.flip();
			rq.setBody(wb);
		    }else{
			flag = false;
		    }
		}
	    }
	    
	    if(flag){
		issuccess = true;
		out.write(rq);
	    }
	}

	return issuccess;
    }

    private HttpRequestImpl parseHttpRequestHead(final ByteBuffer buffer) {
	final String raw = new String(buffer.array(), 0, buffer.limit(), Charset.forName("UTF-8"));
	final String[] headersAndBody = RAW_VALUE_PATTERN.split(raw, -1);

	if (headersAndBody.length <= 1) {
	    return null;
	}

	String[] headerFields = HEADERS_BODY_PATTERN.split(headersAndBody[0]);
	headerFields = ArrayUtil.dropFromEndWhile(headerFields, "");

	final String requestLine = headerFields[0];
	final Map<String, String> generalHeaders = new HashMap<String, String>();

	for (int i = 1; i < headerFields.length; i++) {
	    final String[] header = HEADER_VALUE_PATTERN.split(headerFields[i]);
	    generalHeaders.put(header[0].toLowerCase(), header[1]);
	}

	final String[] elements = REQUEST_LINE_PATTERN.split(requestLine);
	final HttpMethod method = HttpMethod.valueOf(elements[0]);
	final HttpVersion version = HttpVersion.fromString(elements[2]);
	final String[] pathFrags = QUERY_STRING_PATTERN.split(elements[1]);
	final String requestedPath = pathFrags[0];
	final String queryString = pathFrags.length == 2 ? pathFrags[1] : "";

	// we put the buffer position where we found the beginning of the HTTP body
        buffer.position(headersAndBody[0].length() + 4);

	return new HttpRequestImpl(version, method, requestedPath, queryString, generalHeaders);
    }

}
