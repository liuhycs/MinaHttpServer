/**   
* @Title: HttpServerCodec.java 
* @Package com.assemsoft.forge.server.http.codec 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-14 下午02:19:17 
* @version V1.0   
*/
package com.assemsoft.forge.server.http.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/** 
 * @ClassName: HttpServerCodec 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 下午02:19:17 
 *  
 */
public class HttpServerCodec implements ProtocolCodecFactory {
    
    private HttpServerDecoder decoder;
    private HttpServerEncoder encoder;
    
    public HttpServerCodec(){
	decoder = new HttpServerDecoder();
	encoder = new HttpServerEncoder();
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
	return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
	return encoder;
    }

    
}
