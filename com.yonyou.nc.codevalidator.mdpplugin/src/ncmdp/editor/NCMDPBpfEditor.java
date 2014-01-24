package ncmdp.editor;

import ncmdp.factory.PaletteFactory;

import org.eclipse.gef.palette.PaletteRoot;

public class NCMDPBpfEditor extends NCMDPEditor {
	public static final String EDITOR_ID = "ncmdp.editor.NCMDPBpfEditor";

	private PaletteRoot upfPaleteRoot = null;

	public NCMDPBpfEditor() {
		super();
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (upfPaleteRoot == null) {
			upfPaleteRoot = PaletteFactory.createPaletteRoot(true);
		}
		return upfPaleteRoot;
	}

}
