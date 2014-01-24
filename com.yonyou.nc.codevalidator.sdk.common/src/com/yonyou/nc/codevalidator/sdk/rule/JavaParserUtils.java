package com.yonyou.nc.codevalidator.sdk.rule;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.VoidVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

public final class JavaParserUtils {
	
	private JavaParserUtils() {
		
	}

	public static List<File> listJavaFiles(String srcPath) {
		return listJavaFiles(srcPath, null);
	}

	public static List<File> listJavaFiles(String srcPath, final String regex) {
		List<File> result = new ArrayList<File>();
		File srcFolder = new File(srcPath);
		@SuppressWarnings("unchecked")
		Collection<File> listFiles = FileUtils.listFiles(srcFolder, new String[] { "java" }, true);
		if (regex == null) {
			return new ArrayList<File>(listFiles);
		}
		for (File file : listFiles) {
			if (file.getAbsolutePath().matches(regex)) {
				result.add(file);
			}
		}
		return result;
	}

	/**
	 * Visit access javaFiles
	 * 
	 * @param javaFiles
	 * @param visitor
	 * @param arg
	 * @throws ParseException
	 * @throws IOException
	 */
	public static <T> void visitFiles(List<File> javaFiles, VoidVisitor<String> visitor) throws ParseException,
			IOException {
		if (javaFiles == null) {
			return;
		}
		for (File javaFile : javaFiles) {
			CompilationUnit compilationUnit = JavaParser.parse(javaFile);
			visitor.visit(compilationUnit, javaFile.getName());
		}
	}

}
