package nc.web.pub.fw.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.web.pub.exception.business.PubBusinessException;
import com.web.pub.exception.business.PubPageBusinessException;
import com.web.pub.exception.message.ExceptionMessage;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-7 下午4:48:59
 */
public class PubExceptionResolver extends AbstractHandlerExceptionResolver {
	public final static String ERROR = "/error/error";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		try {
			// handler business exception
			if (ex instanceof PubBusinessException) {
				if (isAjaxRequest(request)) {
					return handleEcpAjaxBusinessException(ex, request,
							response, handler);
				} else if (ex instanceof PubPageBusinessException) {
					return handleEcpPageBusinessException(
							(PubPageBusinessException) ex, request, response,
							handler);
				} else {
					return handleEcpBusinessException(
							(PubBusinessException) ex, request, response,
							handler);
				}
			}
			// handler system exception
			else if (isAjaxRequest(request)) {
				return handleAjaxEcpSystemException(ex, request, response,
						handler);
			} else {
				return handleEcpSystemException(ex, request, response, handler);
			}
		} catch (Exception handlerException) {
			logger.warn("Handling of [" + ex.getClass().getName()
					+ "] resulted in Exception", handlerException);
		}
		return null;
	}

	/**
	 * 通过请求头部判断是否为ajax请求
	 * 
	 * @param request
	 *            请求体
	 * @return 真：ajax请求；假：非ajax请求
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		final String header = request.getHeader("x-requested-with");
		if (header != null && header.trim().equalsIgnoreCase("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * handler ajax business exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 * @throws HttpMessageNotWritableException
	 * @throws IOException
	 */
	private ModelAndView handleEcpAjaxBusinessException(Exception ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws HttpMessageNotWritableException, IOException {
		// get input and output message
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);
		// get contentType
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders()
				.getAccept();
		MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
		String returnValue = ex.getMessage();
		for (MediaType mediaType : acceptedMediaTypes) {
			Class<? extends String> returnValueType = returnValue.getClass();
			if (messageConverter.canWrite(returnValueType, mediaType)) {
				messageConverter.write(returnValue, mediaType, outputMessage);
				return new ModelAndView();
			}
		}
		return null;
	}

	/**
	 * handler page business exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 * @param handler
	 * @return
	 */
	private ModelAndView handleEcpPageBusinessException(
			PubPageBusinessException ex, HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		// forward to error.jsp
		ModelAndView model = new ModelAndView(ERROR);
		// put errorMessage to modelmap
		model.addObject("exceptionMessage", ex.getExceptionMessage());
		return model;
	}

	private ModelAndView handleAjaxEcpSystemException(Exception ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws HttpMessageNotWritableException, IOException {
		// get input and output message
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(
				response);
		// get contentType
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders()
				.getAccept();
		MappingJacksonHttpMessageConverter messageConverter = new MappingJacksonHttpMessageConverter();
		// printStackTrace To String
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintStream pstream = new PrintStream(byteOut);
		ex.printStackTrace(pstream);
		pstream.close();
		// ExceptionMessage returnValue = new ExceptionMessage("系统正在维护，请稍候再试！",
		// new String(byteOut.toByteArray()));
		ExceptionMessage returnValue = new ExceptionMessage(ex.getMessage(),
				new String(byteOut.toByteArray()));
		for (MediaType mediaType : acceptedMediaTypes) {
			Class<? extends ExceptionMessage> returnValueType = returnValue
					.getClass();
			if (messageConverter.canWrite(returnValueType, mediaType)) {
				messageConverter.write(returnValue, mediaType, outputMessage);
			}
		}
		// convert java object to json
		// write to responseMessage
		return new ModelAndView();
	}

	private ModelAndView handleEcpSystemException(Exception ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) {
		ModelAndView model = new ModelAndView(ERROR);
		// print stackTrace to string
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintStream pstream = new PrintStream(byteOut);
		ex.printStackTrace(pstream);
		pstream.close();
		ExceptionMessage exceptionMessage = new ExceptionMessage(
				ex.getMessage(), new String(byteOut.toByteArray()));
		model.addObject("exceptionMessage", exceptionMessage);
		return model;
	}

	private ModelAndView handleEcpBusinessException(PubBusinessException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) {
		ModelAndView model = new ModelAndView(ERROR);
		model.addObject("exceptionMessage", ex.getExceptionMessage());
		return model;
	}

}
