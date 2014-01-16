/**   
* @Title: Main.java 
* @Package com.assemsoft.forge 
* @Description: TODO
* @author liuhy(andyliuhy@126.com)
* @date 2014-1-14 上午11:12:22 
* @version V1.0   
*/
package com.assemsoft.forge;

import com.assemsoft.forge.util.SpringUtil;

/** 
 * @ClassName: Main 
 * @Description: TODO
 * @author liuhy(andyliuhy@126.com)
 * @date 2014-1-14 上午11:12:22 
 *  
 */
public class Main {

    /** 
     * @Title: main 
     * @Description: TODO
     * @param     
     * @return void   
     * @throws 
     * @param args
     */
    public static void main(String[] args) {
	SpringUtil.getInstance();
	System.out.println("启动完成...");
    }

}
