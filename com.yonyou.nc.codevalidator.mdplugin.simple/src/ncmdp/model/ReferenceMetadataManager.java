package ncmdp.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.io.FileUtils;

public class ReferenceMetadataManager {

	private Map<String, List<String>> ncHomeToMetadataFileMap;

	private static ReferenceMetadataManager instance;

	public static ReferenceMetadataManager getInstance() {
		if (instance == null) {
			instance = new ReferenceMetadataManager();
		}
		return instance;
	}

	private ReferenceMetadataManager() {
		ncHomeToMetadataFileMap = new WeakHashMap<String, List<String>>();
	}

	/**
	 * 在NCHome中查找对应的元数据文件，以fileName结尾
	 * 
	 * @param ncHome
	 * @param fileName
	 * @return
	 */
	public String findReferenceFilePath(String ncHome, String fileName) {
		synchronized (ncHome) {
			if (!ncHomeToMetadataFileMap.containsKey(ncHome)) {
				List<String> bmfFileList = new ArrayList<String>();
				File modulesFolder = new File(ncHome + "/modules/");
				@SuppressWarnings("rawtypes")
				Collection bmfFiles = FileUtils.listFiles(modulesFolder, new String[] { "bmf" }, true);
				for (Object bmfFile : bmfFiles) {
					File bmfFileObject = (File) bmfFile;
					bmfFileList.add(bmfFileObject.getAbsolutePath().toLowerCase());
				}
				ncHomeToMetadataFileMap.put(ncHome, bmfFileList);
			}
			List<String> bmfFileList = ncHomeToMetadataFileMap.get(ncHome);
			fileName = fileName.toLowerCase();
			for (String bmfFilePath : bmfFileList) {
				if (bmfFilePath.endsWith(fileName)) {
					return bmfFilePath;
				}
			}
			return null;
		}
	}

}
