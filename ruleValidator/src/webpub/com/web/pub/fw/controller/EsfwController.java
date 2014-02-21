package com.web.pub.fw.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.pub.fw.exception.PubBaseException;
import com.web.pub.fw.tools.EsFwConstants;
import com.web.pub.fw.tools.EsFwUtil;

/**
 * 数据缓存管理控制器
 * 
 * @author:sunshine
 * @date:2014-1-7 下午4:50:02
 */
@Controller
@RequestMapping("esfw/pub")
public class EsfwController extends BaseController {
	private static final Log logger = LogFactory.getLog(EsfwController.class);

	/**
	 * 
	 * @param pk
	 * @param nodecode
	 * @param popConditionView
	 * @param bqparameters
	 * @return
	 * @throws PubBaseException
	 * @throws IOException
	 */
	@RequestMapping(value = "bapforward.htm", method = RequestMethod.GET)
	public String uploadFile(@RequestParam String pk,
			@RequestParam String nodecode,
			@RequestParam String popConditionView,
			@RequestParam String bqparameters) throws PubBaseException,
			IOException {
		// String portal = EcpLoadConfigProperty.getPortal();
		String portal = "";
		String registerAddress = portal
				+ "/registerServlet?type=2&dsname=design&userid=woojun&password=aa1111";
		InputStream is = null;
		BufferedReader br = null;
		try {
			URL ssoUrl = new URL(registerAddress);
			String ssoKey = "";
			URLConnection connection = ssoUrl.openConnection();
			connection.setDoOutput(true);
			is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String tempLine = br.readLine();
			StringBuffer temp = new StringBuffer();
			while (tempLine != null) {
				temp.append(tempLine);
				tempLine = br.readLine();
			}
			String[] keys = temp.toString().split(":");

			if (keys.length > 1) {
				ssoKey = keys[1];
			}
			StringBuffer sb = new StringBuffer();
			bqparameters = buildParameter(bqparameters);
			sb.append("redirect:").append(portal).append("/app/bap.app?")
					.append("pk=").append(pk).append("&nodecode=")
					.append(nodecode).append("&popConditionView=")
					.append(popConditionView).append("&bqparameters=")
					.append(bqparameters).append("&ssoKey=").append(ssoKey);
			logger.debug("bap address:" + sb.toString());
			return sb.toString();
		} catch (MalformedURLException e) {
			throw new PubBaseException(e);
		} catch (IOException e) {
			throw new PubBaseException(e);
		} finally {
			is.close();
			br.close();
		}
	}

	private String buildParameter(String bqparameters)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = objectMapper.readValue(bqparameters,
				Map.class);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String value = entry.getValue().toString();
			if (value.startsWith("@")) {
				value = value.substring(1, value.length());
				value = convert(value);
			}
			;
			entry.setValue(value);
		}
		String result = objectMapper.writeValueAsString(map);
		return result;
	}

	/**
	 * 把变量替换为具体的值
	 * 
	 * @param value
	 * @return
	 */
	private String convert(String value) {
		if (value.equals("pkcustomer")) {
			// value = EcpInvocationInfoProxy.getInstance().getPkCustomer();
		}
		return value;
	}

	/**
	 * 请求参数编码字符集
	 */
	public static final String REQUEST_PARAMETER_ENCODE_CHARSET = "ISO8859-1";

	/**
	 * 服务端编码字符集
	 */
	public static final String SERVER_ENCODE_CHARSET = "UTF-8";

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws PubBaseException
	 */
	@RequestMapping(value = "downloadByRealPathAndFileName.json", method = RequestMethod.GET)
	public @ResponseBody
	void downloadFile(@RequestParam String realPath,
			@RequestParam String fileName) throws PubBaseException {
		try {
			realPath = URLDecoder.decode(realPath, SERVER_ENCODE_CHARSET);
			// 获取文件名称
			String strFileName = URLDecoder.decode(fileName,
					SERVER_ENCODE_CHARSET);
			// 获取数据源名称
			// String strDsName = EcpLoadConfigProperty.getDataSource();
			String strDsName = "design";
			download(getRequest(), getResponse(), strDsName, strFileName,
					realPath);
		} catch (UnsupportedEncodingException e1) {
			if (logger.isDebugEnabled()) {
				logger.debug("参数解码报错");
				throw new PubBaseException("参数解码报错");
			}
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("下载报错");
				throw new PubBaseException("下载报错");
			}
		}

	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            相应
	 * @param strDsName
	 *            数据源
	 * @param strFileName
	 *            文件名
	 * @param strFilePath
	 *            文件路径(文件路径为业务主键/文件名)
	 * @throws Exception
	 */
	private void download(HttpServletRequest request,
			HttpServletResponse response, String strDsName, String strFileName,
			String strFilePath) throws Exception {
		ObjectOutputStream oos = null;
		InputStream in = null;
		// String ncHomeHostAndPort = EcpLoadConfigProperty.getEjbAddress();
		String ncHomeHostAndPort = "127.0.0.1:80";
		if ("127.0.0.1:80".equals(ncHomeHostAndPort)) {
			ncHomeHostAndPort = request.getServerName() + ":"
					+ request.getServerPort();
		}
		StringBuilder sbUrl = new StringBuilder(request.getScheme())
				.append("://").append(ncHomeHostAndPort)
				.append("/service/FileManageServlet");
		try {
			response.setContentType("application/x-download");
			String fileNameEncoded = encodeFileName(request, strFileName);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileNameEncoded);
			URL url = new URL(sbUrl.toString());
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type",
					"application/x-java-serialized-object,charset=ISO8895-1");

			// 传递token
			// byte[] token = NetStreamContext.getToken();
			// String tokenStr = KeyUtil.encodeToken(token);
			// String cookieStr = "security_token=" + tokenStr;
			String cookieStr = "security_token=";
			conn.setRequestProperty("Cookie", cookieStr);

			oos = new ObjectOutputStream(conn.getOutputStream());
			HashMap<String, String> headInfo = new HashMap<String, String>();
			headInfo.put("dsName", strDsName);
			headInfo.put("operType", "download");
			headInfo.put("path", strFilePath);
			// headInfo.put("user_code", EcpInvocationInfoProxy.getInstance()
			// .getUserCode());
			oos.writeObject(headInfo);
			oos.flush();
			in = conn.getInputStream();
			byte[] buf = new byte[2048];
			int len = -1;
			// logger.info("文件下载token=" + tokenStr);
			while ((len = in.read(buf)) != -1) {
				response.getOutputStream().write(buf, 0, len);
				response.getOutputStream().flush();
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * 对下载文件名编码
	 * <p>
	 * 解决中文乱码：
	 * </p>
	 * <ul>
	 * <li>FireFox浏览器，使用iso8859-1编码</li>
	 * <li>Chrome浏览器，使用iso8859-1或者utf-8编码</li>
	 * <li>IE浏览器，使用utf-8编码</li>
	 * </ul>
	 * <p>
	 * 解决空格：
	 * </p>
	 * <ul>
	 * <li>FireFox浏览器，使用java.lang.String编码处理之后，包含空格，浏览器将会从第一个空格截断，不会显示后面的文件名，
	 * 所以采用下划线替换所有的空格</li>
	 * <li>Chrome浏览器、IE浏览器使用java.net.URLEncoder编码处理之后，浏览器可以识别空格，不会截断</li>
	 * </ul>
	 * 
	 * @param request
	 *            请求（获取浏览器信息）
	 * @param fileName
	 *            要编码的文件名
	 * @return 编码之后的文件名
	 * @author:lixl
	 * @throws IOException
	 * @date:2012-8-21 下午01:51:14
	 */
	private static String encodeFileName(HttpServletRequest request,
			final String fileName) throws IOException {
		String browserType = request.getHeader("User-Agent");
		// FireFox和Chrome等多数浏览器使用iso8859-1编码
		String fileNameEncoded = new String(fileName.getBytes("UTF-8"),
				"ISO8859-1");
		// 替换空格为下划线
		fileNameEncoded = fileNameEncoded.replaceAll(" ", "_");
		// fileNameEncoded = MimeUtility.encodeText(fileName, "UTF-8", "B");
		if (browserType != null
				&& !browserType.trim().equals("")
				&& (browserType.toUpperCase().indexOf("MSIE") > -1
						|| browserType.toUpperCase().indexOf("CHROME") > -1 || browserType
						.toUpperCase().indexOf("OPERA") > -1)) {
			// IE和Chrome
			// fileNameEncoded = processFileName(fileName, "UTF-8");
			fileNameEncoded = URLEncoder.encode(fileName, "UTF-8");
			fileNameEncoded = fileNameEncoded.replaceAll("[+]", "%20");
		}
		return fileNameEncoded;
	}

	/**
	 * 跳转错误页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "error.htm", method = RequestMethod.GET)
	public String error() {
		return "/error/error";
	}

	/**
	 * 创建随机验证码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @author:sunshine
	 * @throws IOException
	 * @date:2012-4-12 上午09:37:42
	 */
	@RequestMapping(value = "randomid.json", method = RequestMethod.GET)
	public @ResponseBody
	String randomid(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 验证码图片的宽度。
		int width = 80;
		// 验证码图片的高度。
		int height = 20;
		// 验证码字符个数
		int codeCount = 4;
		// 字体高度
		int fontHeight = height - 2;
		// X坐标
		int xx = width / (codeCount + 1);
		// Y坐标
		int codeY = height - 4;

		String randomCode = EsFwUtil.genNumPassword(codeCount);

		// 这里根据生成的验证码生成图片，并返回
		// 定义图像buffer
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gra = image.createGraphics();

		// 将图像填充为白色
		gra.setColor(Color.WHITE);
		gra.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		gra.setFont(font);

		// 画边框。
		gra.setColor(Color.BLACK);
		// gra.drawRect(0, 0, width - 1, height - 1);
		Random random = new Random();
		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		gra.setColor(Color.BLACK);
		int red = 0, green = 0, blue = 0;
		char c;
		// 颜色深度系数，数值越大，RGB颜色值越小，颜色越深
		float colorDeep = 2;
		int rgbColorMax = Float.valueOf(255 / colorDeep).intValue();
		for (int i = 0; i < 4; i++) {
			c = randomCode.charAt(i);
			red = random.nextInt(rgbColorMax);
			green = random.nextInt(rgbColorMax);
			blue = random.nextInt(rgbColorMax);
			// 用随机产生的颜色将验证码绘制到图像中。
			gra.setColor(new Color(red, green, blue));
			gra.drawString("" + c, (i + 1) * xx, codeY);
		}
		// 把当前的验证码存入session中，以供登录时验证
		request.getSession().setAttribute(EsFwConstants.RANDOMCODEID,
				randomCode);
		// 禁止图像缓存。
		// HttpServletResponse response = getResponse();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(image, "jpeg", sos);
		sos.close();

		return null;
	}
}
