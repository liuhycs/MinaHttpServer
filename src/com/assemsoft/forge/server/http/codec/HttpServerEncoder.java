/**   
* @Title: HttpServerEncoder.java 
* @Package com.assemsoft.forge.server.http.codec 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-14 下午03:09:05 
* @version V1.0   
*/
package com.assemsoft.forge.server.http.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.assemsoft.forge.server.http.message.HttpResponse;

/** 
 * @ClassName: HttpServerEncoder 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 下午03:09:05 
 *  
 */
public class HttpServerEncoder implements ProtocolEncoder {

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#dispose(org.apache.mina.core.session.IoSession)
     */
    @Override
    public void dispose(IoSession session) throws Exception {
	
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core.session.IoSession, java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
	    throws Exception {
	
	HttpResponse hr = (HttpResponse) message;
	
	StringBuffer sb = new StringBuffer();
	sb.append(hr.getStatus().line())
        .append("Content-Type: ").append(hr.getContentType()).append("\r\n")
        .append("Content-Length: ").append(hr.getBody().length).append("\r\n\r\n")
        .append(new String(hr.getBody())).append("\r\n");
	
	out.write(IoBuffer.wrap(sb.toString().getBytes()));
    }

}
