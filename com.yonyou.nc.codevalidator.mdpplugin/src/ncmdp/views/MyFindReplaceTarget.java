package ncmdp.views;

import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

public class MyFindReplaceTarget implements IFindReplaceTarget {
	private Text text = null;
	private Text getText(){
		if(text == null){
			text =  NCMDPViewSheet.getNCMDPViewPage().getGraphFileTextView().getText();
		}
		return text;
	}
	public boolean canPerformFind() {
		return true;
	}

//	public int findAndSelect(int widgetOffset, String findString, boolean searchForward, boolean caseSensitive, boolean wholeWord) {
//		int index = -1;
//		int position = -1;
//		if(searchForward){
//			String str = text.getText(widgetOffset, text.getCharCount());
//			index = str.indexOf(findString);
//			if(index != -1){
//				position = widgetOffset+index;
//				text.setSelection(position, position+findString.length());
//			}
//		}else{
//			String str = text.getText(0, widgetOffset);
//			index = str.lastIndexOf(findString);
//			if(index != -1){
//				position = index;
//				text.setSelection(position, position+findString.length());
//			}
//		}
//		return position; 
//	}
	public int findAndSelect(int widgetOffset, String findString, boolean searchForward, boolean caseSensitive, boolean wholeWord) {

		int index = -1;
		int position = -1;
		if(searchForward){
			if(widgetOffset == -1)
				widgetOffset = 0;
			String str = text.getText(widgetOffset, text.getCharCount());
			if(!caseSensitive){
				str = str.toLowerCase();
				findString = findString.toLowerCase();
			}
			index = str.indexOf(findString);
			if(index != -1){
				position = widgetOffset+index;
				text.setSelection(position, position+findString.length());
			}
		}else{
			if(widgetOffset == -1)
				widgetOffset = text.getCharCount();
			String str = text.getText(0, widgetOffset);
			if(!caseSensitive){
				str = str.toLowerCase();
				findString = findString.toLowerCase();
			}
			index = str.lastIndexOf(findString);

			if(index != -1){
				position = index;
				text.setSelection(position, position+findString.length());
			}
		}
		return position; 
	}
	public Point getSelection() {
		Point p =getText().getSelection();
		
		return new Point(p.x, p.y - p.x);
	}

	public String getSelectionText() {
		return getText().getSelectionText();
	}

	public boolean isEditable() {
		return false;
	}

	public void replaceSelection(String text) {

	}

}

