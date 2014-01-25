package ncmdp.wizard.multiwizard.util;

/**
 * 多语资源元素 接口
 * @author dingxm   2010-8-6
 *
 */
public interface IMultiElement {
	public String getDisplayName();

	public String getResid();

	public void setResid(String resid);
	
	public String getElementType();
}
