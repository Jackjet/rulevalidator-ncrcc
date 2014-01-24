
package com.yonyou.nc.codevalidator.runtime;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.yonyou.nc.codevalidator.runtime package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ExecuteResponse_QNAME = new QName("http://runtime.codevalidator.nc.yonyou.com/", "executeResponse");
    private final static QName _Execute_QNAME = new QName("http://runtime.codevalidator.nc.yonyou.com/", "execute");
    private final static QName _ExecuteArg1_QNAME = new QName("http://runtime.codevalidator.nc.yonyou.com/", "arg1");
    private final static QName _ExecuteArg0_QNAME = new QName("http://runtime.codevalidator.nc.yonyou.com/", "arg0");
    private final static QName _ExecuteArg2_QNAME = new QName("http://runtime.codevalidator.nc.yonyou.com/", "arg2");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.yonyou.nc.codevalidator.runtime
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link com.yonyou.nc.codevalidator.com.yonyou.nc.codevalidator.runtime.Execute }
     * 
     */
    public Execute createExecute() {
        return new Execute();
    }

    /**
     * Create an instance of {@link com.yonyou.nc.codevalidator.com.yonyou.nc.codevalidator.runtime.ExecuteResponse }
     * 
     */
    public ExecuteResponse createExecuteResponse() {
        return new ExecuteResponse();
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.yonyou.nc.codevalidator.com.yonyou.nc.codevalidator.runtime.ExecuteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://runtime.codevalidator.nc.yonyou.com/", name = "executeResponse")
    public JAXBElement<ExecuteResponse> createExecuteResponse(ExecuteResponse value) {
        return new JAXBElement<ExecuteResponse>(_ExecuteResponse_QNAME, ExecuteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link com.yonyou.nc.codevalidator.com.yonyou.nc.codevalidator.runtime.Execute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://runtime.codevalidator.nc.yonyou.com/", name = "execute")
    public JAXBElement<Execute> createExecute(Execute value) {
        return new JAXBElement<Execute>(_Execute_QNAME, Execute.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://runtime.codevalidator.nc.yonyou.com/", name = "arg1", scope = Execute.class)
    public JAXBElement<String> createExecuteArg1(String value) {
        return new JAXBElement<String>(_ExecuteArg1_QNAME, String.class, Execute.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://runtime.codevalidator.nc.yonyou.com/", name = "arg0", scope = Execute.class)
    public JAXBElement<String> createExecuteArg0(String value) {
        return new JAXBElement<String>(_ExecuteArg0_QNAME, String.class, Execute.class, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://runtime.codevalidator.nc.yonyou.com/", name = "arg2", scope = Execute.class)
    public JAXBElement<String> createExecuteArg2(String value) {
        return new JAXBElement<String>(_ExecuteArg2_QNAME, String.class, Execute.class, value);
    }

}
