package com.yonyou.nc.codevalidator.runtime.plugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

public final class PluginProjectUtils {

	private static final String MDE_PROJECT_NATURE = "nc.uap.mde.ModuleProjectNature";
	
	private PluginProjectUtils() {
		
	}

	public static List<IProject> getMdeProjectsInCurrentWorkspace() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<IProject> mdeProjects = new ArrayList<IProject>();
		for (IProject project : projects) {
			if (PluginProjectUtils.isMdeProject(project)) {
				mdeProjects.add(project);
			}
		}
		return mdeProjects;
	}

	public static boolean isMdeProject(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			return description.hasNature(MDE_PROJECT_NATURE);
		} catch (CoreException e) {
			return false;
		}
	}

}
