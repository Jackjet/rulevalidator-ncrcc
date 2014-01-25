package ncmdp.figures.ui;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.text.AbstractFlowBorder;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

public class MultiLineLabel  extends FlowPage {
	private class CustomBorder extends AbstractFlowBorder{
		public Insets getInsets(IFigure arg0) {
			return new Insets(0,2,0,0);
		}
		
	}
    private TextFlow contents;
    public MultiLineLabel() {
        this("");
    }
    public MultiLineLabel(String text) {
        contents = new TextFlow();
        ParagraphTextLayout layout = new ParagraphTextLayout(contents, ParagraphTextLayout.WORD_WRAP_SOFT);
        contents.setLayoutManager(layout);
        contents.setText(text);
        contents.setBorder(new CustomBorder());
//        BorderLayout layout = new BorderLayout();
//        setLayoutManager(layout);
        add(contents);
//        layout.setConstraint(contents, BorderLayout.CENTER);
    }
    public void setText(String text) {
        contents.setText(text);
    }
    public String getText() {
        return contents.getText();
    }


}
