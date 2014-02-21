package com.web.pub.console.management;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.web.pub.fw.controller.BaseController;
import com.web.pub.fw.exception.PubBaseException;

/**
 * 后台管理controller
 * 
 * @author:songjl
 * @date:2013-09-10 下午03:17:28
 */
@Controller
@SuppressWarnings("restriction")
@RequestMapping("esfw/management")
public class ManagementController extends BaseController {
	private static final Log logger = LogFactory
			.getLog(ManagementController.class);

	/**
	 * 保存上传的logo图片
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "logoupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String logoUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.logoUploadCallback('" + savePicture(request)
				+ "')</script>";
	}

	/**
	 * 保存上传登录页面logo图片
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "loginlogoupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String loginLogoUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.loginLogoUploadCallback('"
				+ savePicture(request) + "')</script>";
	}

	/**
	 * 保存上传登录页面左侧图片
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "loginleftupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String loginLeftUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.loginLeftUploadCallback('"
				+ savePicture(request) + "')</script>";
	}

	/**
	 * 保存上传登录页面左侧图片
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "loginbgupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String loginBGUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.loginLBGUploadCallback('" + savePicture(request)
				+ "')</script>";
	}

	/**
	 * 保存上传的头部图片
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "toppicupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String topPicUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.topPicUploadCallback('" + savePicture(request)
				+ "')</script>";
	}

	/**
	 * 保存导航图片 选中和未选中两种状态
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "navpicupload.json", method = RequestMethod.POST)
	public @ResponseBody
	String navPicUpload(DefaultMultipartHttpServletRequest request)
			throws IOException {
		return "<script>parent.navPicUploadCallback('" + savePicture(request)
				+ "')</script>";
	}

	/**
	 * 获取ecpsystem.properties配置文件内容
	 * 
	 * @return Map<String,String>
	 * @throws EcpBaseException
	 *             ,IOException
	 */
	@RequestMapping(value = "systemPorpertiesList.json", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> protletConfigList() throws PubBaseException,
			IOException {
		Properties props = new Properties();
		InputStream is = null;
		InputStreamReader isr = null;
		// try {
		// // 读取应用下的配置文件
		// String path = ServerRuntime.getECPAppHome()
		// + "/WEB-INF/classes/ecpsystem.properties";
		// is = new BufferedInputStream(new FileInputStream(path));
		// isr = new InputStreamReader(is, "utf-8");
		// props.load(isr);
		// } catch (Exception e) {
		// logger.error("读取系统配置错误,请配置正确的system.config信息!", e);
		// } finally {
		// IOUtils.closeQuietly(isr);
		// IOUtils.closeQuietly(is);
		// }
		// // 读取props中的值
		Map<String, String> propsMap = new HashMap<String, String>();
		// Enumeration<Object> en = props.keys();
		// while (en.hasMoreElements()) {
		// String key = en.nextElement().toString();
		// String value = props.getProperty(key);
		// propsMap.put(key, value);
		// }
		return propsMap;
	}

	/**
	 * 保存properties内容
	 * 
	 * @param queryParam
	 * @return
	 * @throws EcpBaseException
	 */
	@RequestMapping(value = "systemPorpertiesUpdate.json", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> protletConfigSave(
			@RequestParam(value = "data") String data) throws PubBaseException {
		InputStream is = null;
		InputStreamReader isr = null;
		Boolean result = false;
		Map<String, String> map = new HashMap<String, String>();
		// try {
		// // 读取应用下的配置文件
		// String path = ServerRuntime.getECPAppHome()
		// + "/WEB-INF/classes/ecpsystem.properties";
		// JSONObject jsonObj = JSONObject.fromObject(data);
		// @SuppressWarnings("unchecked")
		// Map<String, String> jsonMap = jsonObj;
		// result = PropertyFileTool.setProfileString(path, jsonMap);
		// } catch (IOException e) {
		// logger.error("读取系统配置配置文件ecpsystem.properties错误!", e);
		// } catch (Exception e) {
		// logger.error("保存系统配置配置文件ecpsystem.properties错误!", e);
		// } finally {
		// IOUtils.closeQuietly(isr);
		// IOUtils.closeQuietly(is);
		// }
		// if (result) {
		// map.put("result", "保存成功");
		// } else {
		// map.put("result", "读取系统配置配置文件ecpsystem.properties错误!");
		// }
		return map;
	}

	/**
	 * 保存ecpuiproperties内容
	 * 
	 * @param queryParam
	 * @return
	 * @throws EcpBaseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "uiPorpertiesUpdate.json", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> uiPropConfigSave(
			@RequestParam(value = "data") String data) {
		// IManagementService service = NCLocator.getInstance().lookup(
		// IManagementService.class);
		// return service.uiPropConfigSave(data);
		return null;
	}

	/**
	 * 获取ecpui.properties配置文件内容
	 * 
	 * @return Map<String,String>
	 * @throws EcpBaseException
	 *             ,IOException
	 */
	@RequestMapping(value = "uiPorpertiesQuery.json", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> uiConfigList() throws PubBaseException, IOException {
		Properties props = new Properties();
		InputStream is = null;
		InputStreamReader isr = null;
		// try {
		// // 读取应用下的配置文件
		// String path = ServerRuntime.getECPAppHome()
		// + "/WEB-INF/classes/ecpui.properties";
		// is = new BufferedInputStream(new FileInputStream(path));
		// isr = new InputStreamReader(is, "utf-8");
		// props.load(isr);
		// } catch (Exception e) {
		// logger.error("读取系统配置错误,请配置正确的system.config信息!", e);
		// } finally {
		// IOUtils.closeQuietly(isr);
		// IOUtils.closeQuietly(is);
		// }
		// 读取props中的值
		Map<String, String> propsMap = new HashMap<String, String>();
		Enumeration<Object> en = props.keys();
		while (en.hasMoreElements()) {
			String key = en.nextElement().toString();
			String value = props.getProperty(key);
			propsMap.put(key, value);
		}
		return propsMap;
	}

	/**
	 * 把上传的图片保存到本地路径
	 * 
	 * @param obj
	 * @return
	 * @author:songjl
	 * @throws IOException
	 * @date:2013-09-16 下午08:40:05
	 */
	private String savePicture(DefaultMultipartHttpServletRequest req)
			throws IOException {
		// 获取上传图片对象
		Map<String, MultipartFile> hashMap = req.getFileMap();
		// 如果上传了图片
		// if (hashMap.entrySet().size() > 0) {
		// IManagementService service = NCLocator.getInstance().lookup(
		// IManagementService.class);
		// for (Map.Entry<String, MultipartFile> entry : hashMap.entrySet()) {
		// CommonsMultipartFile partFile = (CommonsMultipartFile) entry
		// .getValue();
		// return service.uiManagementPictureSave(partFile);
		// }
		// }
		// 返回空表示没有上传图片
		return "";
	}

	/**
	 * 跳转到后台管理页面
	 * 
	 * @param obj
	 * @return
	 * @author:songjl
	 * @throws IOException
	 * @date:2013-09-16 下午08:40:05
	 */
	@RequestMapping(value = "manage.htm", method = RequestMethod.GET)
	public @ResponseBody
	String managehtmredirect(HttpServletResponse response)
			throws PubBaseException, IOException {
		// byte[] token = NetStreamContext.getToken();
		// UserContext context =
		// UserWhiteList.getInstance().getUserContext(token);
		// if (!(context.getUserType().equals(
		// IUserMaintainService.USER_TYPE_PERSON) && context
		// .getBaseDocType().equals(
		// IUserMaintainService.BASE_DOC_TYPE_G_ADMIN))) {
		// // 没有权限
		// return "false";
		// }
		return "true";
	}

}
