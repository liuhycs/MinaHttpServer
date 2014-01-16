/**   
* @Title: HttpResponseImpl.java 
* @Package com.assemsoft.forge.server.http.message 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-16 上午10:16:16 
* @version V1.0   
*/
package com.assemsoft.forge.server.http.message;

import java.util.Map;

/** 
 * @ClassName: HttpResponseImpl 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-16 上午10:16:16 
 *  
 */
public class HttpResponseImpl implements HttpResponse {
    
    private final HttpVersion version;

    private final Map<String, String> headers;

    private byte[] body;
    
    public HttpResponseImpl(HttpVersion version, Map<String, String> headers) {
	this.version = version;
	this.headers = headers;
    }

    /**
     * @return the body
     */
    public byte[] getBody() {
	return body;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(byte[] body) {
	this.body = body;
    }
    
    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpResponse#getStatus()
     */
    @Override
    public HttpStatus getStatus() {
	return HttpStatus.SUCCESS_OK;
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#containsHeader(java.lang.String)
     */
    @Override
    public boolean containsHeader(String name) {
	return headers.containsKey(name);
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#getContentType()
     */
    @Override
    public String getContentType() {
	return headers.get("accept");
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
	return headers.get(name);
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() {
	return headers;
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#getProtocolVersion()
     */
    @Override
    public HttpVersion getProtocolVersion() {
	return version;
    }

    /* (non-Javadoc)
     * @see com.assemsoft.forge.server.http.message.HttpMessage#isKeepAlive()
     */
    @Override
    public boolean isKeepAlive() {
	return true;
    }

}
