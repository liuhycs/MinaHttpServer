/**   
* @Title: RequestProc.java 
* @Package com.assemsoft.forge.server.http.handler 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-16 上午10:24:06 
* @version V1.0   
*/
package com.assemsoft.forge.server.http.handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.assemsoft.forge.server.http.message.HttpRequest;
import com.assemsoft.forge.server.http.message.HttpResponse;
import com.assemsoft.forge.server.http.message.HttpResponseImpl;
import com.assemsoft.forge.util.IOUtils;
import com.assemsoft.forge.util.PageParser;

/** 
 * @ClassName: RequestProc 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-16 上午10:24:06 
 *  
 */
public class RequestProc {
    
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private final static String PATH = "WebRoot\\";
    private IoSession session;
    private HttpRequest request;
    
    public RequestProc(IoSession session, HttpRequest request){
	this.session = session;
	this.request = request;
    }
    
    public HttpResponse process() throws FileNotFoundException, IOException{
	HttpResponseImpl hsp = new HttpResponseImpl(request.getProtocolVersion(), request.getHeaders());
	
	String file = processRequest(request.getRequestPath());
	file = PATH + file;
	byte[] bytes = IOUtils.readBytesAndClose(new FileInputStream(file), -1);
	String page = new String(bytes, CHARSET);
	page = PageParser.parse(page, getSessionAttributes());
        bytes = page.getBytes(CHARSET);
        
        hsp.setBody(bytes);
        
	return hsp;
    }
    
    private Map<String, Object> getSessionAttributes(){
	Map<String, Object> map = new HashMap<String, Object>();
	for(Object key : session.getAttributeKeys()){
	    map.put(key.toString(), session.getAttribute(key));
	}
	return map;
    }
    
    /**
     * Process an HTTP request.
     *
     * @param file the file that was requested
     * @param hostAddr the host address
     * @return the name of the file to return to the client
     */
    protected String processRequest(String file) {
        int index = file.lastIndexOf('.');
        String suffix;
        if (index >= 0) {
            suffix = file.substring(index + 1);
        } else {
            suffix = "";
        }
        String mimeType="";
        if ("ico".equals(suffix)) {
            mimeType = "image/x-icon";
        } else if ("gif".equals(suffix)) {
            mimeType = "image/gif";
        } else if ("css".equals(suffix)) {
            mimeType = "text/css";
        } else if ("html".equals(suffix) || "do".equals(suffix) || "jsp".equals(suffix)) {
            mimeType = "text/html";
        } else if ("js".equals(suffix)) {
            mimeType = "text/javascript";
        } else {
            mimeType = "application/octet-stream";
        }
        if (file.endsWith(".do")) {
            file = process(file);
        }
        request.getHeaders().put("accept", mimeType);
        return file;
    }

    private String process(String file) {
        if(file.endsWith(".do")) {
            if ("/index.do".equals(file)) {
                file = index();
            } else {
                file = "error.jsp";
            }
        }
        return file;
    }
    
    private String index() {
	session.setAttribute("username", "liuhy");
	session.setAttribute("password", "liuhy");
	return "index.jsp";
    }

    /**
     * @return the session
     */
    public IoSession getSession() {
        return session;
    }
    /**
     * @param session the session to set
     */
    public void setSession(IoSession session) {
        this.session = session;
    }
    /**
     * @return the request
     */
    public HttpRequest getRequest() {
        return request;
    }
    /**
     * @param request the request to set
     */
    public void setRequest(HttpRequest request) {
        this.request = request;
    }

}
