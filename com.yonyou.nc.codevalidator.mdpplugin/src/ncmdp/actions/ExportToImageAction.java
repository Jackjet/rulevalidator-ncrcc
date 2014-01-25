package ncmdp.actions;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ncmdp.editor.NCMDPEditor;
import ncmdp.util.MDPLogger;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;

public class ExportToImageAction extends Action {

	public ExportToImageAction() {
		super(Messages.ExportToImageAction_0);
		
	}

	@Override
	public void run() {
		NCMDPEditor editor = NCMDPEditor.getActiveMDPEditor();
		if (editor != null) {
			FileDialog fd = new FileDialog(editor.getSite().getShell(), SWT.SAVE);
			fd.setFilterNames(new String[]{Messages.ExportToImageAction_1,Messages.ExportToImageAction_2});
			fd.setFilterExtensions(new String[]{"*.jpg","*.bmp"}); //$NON-NLS-1$ //$NON-NLS-2$
			String fileName = fd.open();
			if(fileName != null){
				ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)editor.getGV().getRootEditPart();
				IFigure fi = root.getLayer(ScalableFreeformRootEditPart.PRINTABLE_LAYERS);
				byte[] data = createImage(fi, getImgFormat(fileName));
				FileOutputStream fos  = null;
				try {
					fos = new FileOutputStream(fileName);
					fos.write(data);
					fos.flush();
				} catch (Exception e) {
					MDPLogger.error(e.getMessage(), e);
				}finally{
					if(fos != null){
						try {
							fos.close();
						} catch (IOException e) {
							MDPLogger.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}
	private int getImgFormat(String fileName){
		int imgFormat = SWT.IMAGE_JPEG;
		if(fileName.toLowerCase().endsWith(".gif")){ //$NON-NLS-1$
			imgFormat = SWT.IMAGE_GIF;
		}else if(fileName.toLowerCase().endsWith(".bmp")){ //$NON-NLS-1$
			imgFormat = SWT.IMAGE_BMP;
		}else if(fileName.toLowerCase().endsWith(".png")){ //$NON-NLS-1$
			imgFormat = SWT.IMAGE_PNG;
		}else if(fileName.toLowerCase().endsWith(".tiff")){ //$NON-NLS-1$
			imgFormat = SWT.IMAGE_TIFF;
		}
		return imgFormat;
	}
	private byte[] createImage(IFigure figure, int format) {
	    Rectangle r = figure.getBounds();
	    ByteArrayOutputStream result = new ByteArrayOutputStream();
	    Image image = null;
	    GC gc = null;
	    Graphics g = null;
	    try {
	        image = new Image(null, r.width, r.height);
	        gc = new GC(image);
	        g = new SWTGraphics(gc);
	        g.translate(r.x * -1, r.y * -1);
	        figure.paint(g);
	        ImageLoader imageLoader = new ImageLoader();
	        imageLoader.data = new ImageData[] { image.getImageData() };
	        imageLoader.save(result, format);
	    } finally {
	        if (g != null) {
	            g.dispose();
	        }
	        if (gc != null) {
	            gc.dispose();
	        }
	        if (image != null) {
	            image.dispose();
	        }
	    }
	    return result.toByteArray();
	}
}
