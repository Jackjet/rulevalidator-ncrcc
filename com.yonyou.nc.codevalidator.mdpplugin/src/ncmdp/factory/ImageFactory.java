package ncmdp.factory;

import java.net.URL;
import java.util.HashMap;

import ncmdp.NCMDPActivator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ImageFactory {

	private static HashMap<String, ImageDescriptor> hmImageDesc = new HashMap<String, ImageDescriptor>();

	private static HashMap<String, Image> hmImage = new HashMap<String, Image>();

	public static ImageDescriptor getImageDesc(String path) {
		ImageDescriptor imgDesc = hmImageDesc.get(path);
		if (imgDesc == null) {
			imgDesc = createImageDescriptor(path);
			hmImageDesc.put(path, imgDesc);
		}
		return imgDesc;
	}

	public static Image getImage(String path) {
		Image img = hmImage.get(path);
		if (img == null) {
			ImageDescriptor imgDesc = getImageDesc(path);
			img = imgDesc.createImage();
			hmImage.put(path, img);
		}
		return img;
	}

	@SuppressWarnings("deprecation")
	private static ImageDescriptor createImageDescriptor(String path) {
		URL url = NCMDPActivator.getDefault().getDescriptor().getInstallURL();
		ImageDescriptor id = null;
		try {
			id = ImageDescriptor.createFromURL(new URL(url, path));
		} catch (Exception e) {
			id = ImageDescriptor.getMissingImageDescriptor();
		}
		return id;

	}

	public static Image getCheckedImage(boolean isChecked) {
		String path = null;
		if (isChecked) {
			path = "icons/checked.gif";
		} else {
			path = "icons/unChecked.gif";
		}
		return getImage(path);
	}

	public static Image getErrorImg() {
		return getImage("icons/err.png");
	}

	public static ImageDescriptor getEntityImgDescriptor() {
		return getImageDesc("icons/new/entity.png");
	}

	public static Image getEntityImg() {
		return getImage("icons/new/entity.png");
	}

	public static ImageDescriptor getValueObjectImgDescriptor() {
		return getImageDesc("icons/valueobject.png");
	}

	public static Image getValueObjectImg() {
		return getImage("icons/valueobject.png");
	}

	public static ImageDescriptor getAbstractClassImgDescriptor() {
		return getImageDesc("icons/AbstractClass.png");
	}

	public static Image getAbstractClassImg() {
		return getImage("icons/AbstractClass.png");
	}

	public static ImageDescriptor getServiceImgDescriptor() {
		return getImageDesc("icons/service.png");
	}

	public static Image getServiceImg() {
		return getImage("icons/service.png");
	}
	
	public static Image getOPItfImplImg() {
		return getImage("icons/OpImpl.png");
	}

	public static ImageDescriptor getOpItfImgDescriptor() {
		return getImageDesc("icons/new/opItf.png");
	}

	public static Image getOpItfImg() {
		return getImage("icons/new/opItf.png");
	}

	public static ImageDescriptor getBusiOperationDescriptor() {
		return getImageDesc("icons/new/busioperation.png");
	}

	public static Image getBusiOperationImg() {
		return getImage("icons/new/busioperation.png");
	}
	
	public static ImageDescriptor getBusiActivityDescriptor() {
		return getImageDesc("icons/new/busiActivity.png");
	}

	public static Image getBusiActivityImg() {
		return getImage("icons/new/busiActivity.png");
	}

	public static ImageDescriptor getEnumImgDescriptor() {
		return getImageDesc("icons/new/enum.png");
	}

	public static ImageDescriptor getNoteImgDescriptor() {
		return getImageDesc("icons/note.png");
	}

	public static Image getEnumImg() {
		return getImage("icons/new/enum.png");
	}

	public static Image getAttrImage() {
		return getImage("icons/property.png");
	}

	public static Image getRefAttrImage() {
		return getImage("icons/refProperty.gif");
	}

	public static Image getCollectAttrImage() {
		return getImage("icons/collectionProperty.gif");
	}

	public static Image getkeyAttrImage() {
		return getImage("icons/keyProperty.gif");
	}

	public static Image getDirectoryImage() {
		return getImage("icons/dir.png");
	}

	public static Image getFileImage() {
		return getImage("icons/new/bmf.png");
	}

	public static Image getProjectImage() {
		return getImage("icons/project.png");
	}

	public static Image getAttrTreeRootImage() {
		return getImage("icons/propTreeRoot.png");
	}

	public static Image getRefModuleTreeItemImage() {
		return getImage("icons/refModules.png");
	}

	public static Image getOperationImg() {
		return getImage("icons/operation.png");
	}

	public static Image getRefOperationImg() {
		return getImage("icons/refOpration.png");
	}
	
	public static Image getRefBusiOperationImg() {
		return getImage("icons/OpImpl.png");
	}

	public static Image getParameterImgDescriptor() {
		return getImage("icons/parameter.gif");
	}

	public static Image getBusiItfImg() {
		return getImage("icons/new/busiItf.png");
	}

	public static Image getXMLAttrImg() {
		return getImage("icons/xmlattr.png");
	}

	public static Image getBpfImg() {
		return getImage("icons/new/bpf.png");
	}

	public static Image getXMLElement() {
		return getImage("icons/xmlelement.png");
	}

	public static ImageDescriptor getRelationImgDescriptor() {
		return getImageDesc("icons/new/relation.png");
	}

	public static ImageDescriptor getDependImgDescriptor() {
		return getImageDesc("icons/new/depend.png");
	}

	public static ImageDescriptor getAggregationImgDescriptor() {
		return getImageDesc("icons/new/aggregation.png");
	}

	public static ImageDescriptor getConvergeImgDescriptor() {
		return getImageDesc("icons/convergeConnection.png");
	}

	public static ImageDescriptor getExtendImgDescriptor() {
		return getImageDesc("icons/extend.png");
	}

	public static ImageDescriptor getEditorImgDescriptor() {
		return getImageDesc("icons/ufsoft.png");
	}

	public static ImageDescriptor getNoteConImgDescriptor() {
		return getImageDesc("icons/new/notecon.png");
	}

	public static ImageDescriptor getBusiItfConImgDescriptor() {
		return getImageDesc("icons/new/busiItfcon.png");
	}

	public static ImageDescriptor getBusiItfImgDescriptor() {
		return getImageDesc("icons/new/busiItf.png");
	}

	public static ImageDescriptor getDeleteImageDescriptor() {
		return getImageDesc("icons/delete.gif");
	}

	public static ImageDescriptor getRefreshImageDescriptor() {
		return getImageDesc("icons/refresh.gif");
	}

	public static ImageDescriptor getUpdateStateImageDescriptor() {
		return getImageDesc("icons/updateState.gif");
	}

	public static ImageDescriptor getLocatorImageDescriptor() {
		return getImageDesc("icons/locator.gif");
	}

	public static ImageDescriptor getGridImageDescriptor() {
		return getImageDesc("icons/grid.gif");
	}

	public static ImageDescriptor getRulerImageDescriptor() {
		return getImageDesc("icons/ruler.gif");
	}
}
