/**   
* @Title: HttpHandler.java 
* @Package com.assemsoft.forge.server.http 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-14 下午03:14:03 
* @version V1.0   
*/
package com.assemsoft.forge.server.http.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.assemsoft.forge.server.http.message.HttpRequest;
import com.spring.configuration.ConfigLoader;

/** 
 * @ClassName: HttpHandler 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 下午03:14:03 
 *  
 */
public class HttpHandler extends IoHandlerAdapter {
    
    private final static Logger log = Logger.getLogger(HttpHandler.class);

    /* (non-Javadoc)
     * @see org.apache.mina.core.service.IoHandler#exceptionCaught(org.apache.mina.core.session.IoSession, java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable t)
	    throws Exception {
	log.error(t.getMessage(), t);
    }

    /* (non-Javadoc)
     * @see org.apache.mina.core.service.IoHandler#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
	HttpRequest hr = (HttpRequest)message;
	log.info("-------------------------------------");
	log.info("Headers:"+hr.getHeaders());
	log.info("ContentType:"+hr.getContentType());
	log.info("QueryString:"+hr.getQueryString());
	log.info("RequestPath:"+hr.getRequestPath());
	log.info("Parameters:"+hr.getParameters());
	log.info("ProtocolVersion:"+hr.getProtocolVersion());
	log.info("-------------------------------------");
	
	RequestProc rp = new RequestProc(session, hr);
	session.write(rp.process());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
	log.info("session timeout. close it. session"+session.getId());
	session.close(false);
    }
    
    @Override
    public void sessionOpened(IoSession session) throws Exception {
	final int IDLE = Integer.valueOf(ConfigLoader.getContextProperty("session.timeout"));
	session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE);
    }
 
}
