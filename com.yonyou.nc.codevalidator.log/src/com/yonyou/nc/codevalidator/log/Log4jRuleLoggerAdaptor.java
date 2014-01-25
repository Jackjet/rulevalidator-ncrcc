package com.yonyou.nc.codevalidator.log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.RuleConstants;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.IRuleLogger;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * log4j规则日志适配器
 * 
 * @author mazhqa
 * @since 2.3
 */
public class Log4jRuleLoggerAdaptor implements IRuleLogger {

	private final String POSITION = getClass().getName();

	private final String DEBUG = "debug";
	private final String INFO = "info";
	private final String WARN = "warn";
	private final String ERROR = "error";
	private final String FATAL = "fatal";

	// private String currentLogPath = null;
	private Properties properties = new Properties();

	public Log4jRuleLoggerAdaptor() {
		URL resource = this.getClass().getResource("log4j.properties");
		InputStream openStream = null;
		try {
			openStream = resource.openStream();
			properties.load(openStream);
			String currentServerFolder = System.getProperty(RuleConstants.CURRENT_SERVER_FOLDER) == null ? "" : System
					.getProperty(RuleConstants.CURRENT_SERVER_FOLDER);
			if(!StringUtils.isBlank(currentServerFolder)) {
				String globalLogFilePath = currentServerFolder + System.getProperty(RuleConstants.GLOBAL_LOG_FILEPATH);
				properties.setProperty("log4j.appender.logfile.File", globalLogFilePath + "/" + RuleConstants.LOG_FILENAME);
				PropertyConfigurator.configure(properties);
			}
		} catch (IOException e) {
		} finally {
			IOUtils.closeQuietly(openStream);
		}
	}

	@Override
	public void debug(String msg) {
		executeLog(DEBUG, msg, POSITION);
	}

	@Override
	public void info(String msg) {
		executeLog(INFO, msg, POSITION);
	}

	@Override
	public void warn(String msg, Throwable throwable) {
		executeLog(WARN, msg, throwable, POSITION);
	}

	@Override
	public void warn(String msg) {
		executeLog(WARN, msg, POSITION);
	}

	@Override
	public void error(String msg, Throwable throwable) {
		executeLog(ERROR, msg, throwable, POSITION);
	}

	@Override
	public void error(String msg) {
		executeLog(ERROR, msg, POSITION);
	}

	private void executeLog(String level, Object message, String position) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(Activator.class.getClassLoader());
		Logger log = LogManager.getLogger(position);
		if (level.equals(this.DEBUG)) {
			log.debug(message);
		} else if (level.equals(this.INFO)) {
			log.info(message);
		} else if (level.equals(this.WARN)) {
			log.info(message);
		} else if (level.equals(this.ERROR)) {
			log.error(message);
		} else if (level.equals(this.FATAL)) {
			log.error(message);
		} else {
			log.debug(message);
		}
		Thread.currentThread().setContextClassLoader(cl);
	}

	private void executeLog(String level, Object message, Throwable t, String position) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(Activator.class.getClassLoader());
		Logger log = LogManager.getLogger(position);
		if (level.equals(this.DEBUG)) {
			log.debug(message, t);
		} else if (level.equals(this.INFO)) {
			log.info(message, t);
		} else if (level.equals(this.WARN)) {
			log.info(message, t);
		} else if (level.equals(this.ERROR)) {
			log.error(message, t);
		} else if (level.equals(this.FATAL)) {
			log.error(message, t);
		} else {
			log.debug(message, t);
		}
		Thread.currentThread().setContextClassLoader(cl);
	}

	@Override
	public void init() {
		URL resource = this.getClass().getResource("log4j.properties");
		InputStream is = null;
		try {
			is = resource.openStream();
			properties.load(is);
			RuntimeContext currentRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper()
					.getCurrentRuntimeContext();
			if (currentRuntimeContext != null) {
				String globalLogFilePath = currentRuntimeContext.getSystemRuntimeContext().getGlobalLogFilePath();
				if(!StringUtils.isBlank(globalLogFilePath)) {
					String globalLogLevel = currentRuntimeContext.getSystemRuntimeContext().getGlobalLogLevel();
					String globalLogPath = String.format("%s/%s", globalLogFilePath, RuleConstants.LOG_FILENAME);
					properties.setProperty("log4j.appender.logfile.File", globalLogPath);
					if (globalLogLevel != null) {
						properties.setProperty("log4j.rootLogger", String.format("%s, logfile", globalLogLevel));
					}
					PropertyConfigurator.configure(properties);
				}
			}
		} catch (IOException e) {
		} catch (RuleBaseException e) {
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
