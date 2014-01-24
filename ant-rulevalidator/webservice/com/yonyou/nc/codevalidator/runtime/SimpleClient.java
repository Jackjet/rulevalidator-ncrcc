package com.yonyou.nc.codevalidator.runtime;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * User: mazhqa
 * Date: 14-1-2
 */
public class SimpleClient {

    public static void main(String[] args) throws Exception {
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient("http://127.0.0.1:9090/ruleexecute?wsdl");
        Object[] result = client.invoke("execute", "D:\\Develop\\test\\uap631", "D:\\Develop\\test\\NC_MM_PAC6.31_dev_2\\NC6_MM_VOB\\NC_MM_PAC", "nc631_xxq1227");
        System.out.println(result[0]);
    }

    /**
     * Actual execute method
     * @param wsUrl - Web service url
     * @param methodName - Web Service method name
     * @param parameters - The parameters of the web service, it's mutable
     * @throws Exception
     */
    public static void execute(String wsUrl, String methodName, String... parameters) throws Exception {
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(wsUrl);
        client.invoke(methodName, parameters[0], parameters[1], parameters[2]);
    }
    
    /**
     * 异步调用执行web service调用，在阻塞的方法上使用额外的线程进行执行调用，不考虑结果并忽略异常
     * @param wsUrl - Web service url
     * @param methodName - Web Service method name
     * @param parameters - The parameters of the web service, it's mutable
     * @throws Exception
     */
    public static void asyncExecute(String wsUrl, final String methodName, final String... parameters)  {
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        final Client client = clientFactory.createClient(wsUrl);
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					client.invoke(methodName, parameters[0], parameters[1], parameters[2]);
				} catch (Exception e) {
					System.out.println("client invoke exception");
					e.printStackTrace();
				}
			}
		}).start();
    }
}
