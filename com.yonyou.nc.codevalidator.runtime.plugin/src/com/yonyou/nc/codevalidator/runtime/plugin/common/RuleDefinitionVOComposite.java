package com.yonyou.nc.codevalidator.runtime.plugin.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.browser.WebBrowserView;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;

@SuppressWarnings("restriction")
public class RuleDefinitionVOComposite extends Composite {

	private Text ruleScope;
	private Text ruleIdentifier;
	private Text ruleCatalog;
	private Text ruleSubCatalog;
	private Text ruleCheckRole;
	private Text ruleCoder;
	private Text ruleDetail;
	private Text ruleMemo;
	private Text ruleSuggestion;
	private Link relatedIssueLink;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public RuleDefinitionVOComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		Group ruleResultCommon = new Group(this, SWT.NONE);
		ruleResultCommon.setText("\u89C4\u5219\u6982\u8FF0");
		ruleResultCommon.setLayout(new GridLayout(4, false));
		ruleResultCommon.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Label label1 = new Label(ruleResultCommon, SWT.NONE);
		label1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label1.setText("\u89C4\u5219\u6807\u8BC6");

		ruleIdentifier = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleIdentifier.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = new Label(ruleResultCommon, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setBounds(0, 0, 49, 13);
		label.setText("\u89C4\u5219\u5E94\u7528\u8303\u56F4");

		ruleScope = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleScope.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label2 = new Label(ruleResultCommon, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label2.setText("\u89C4\u5219\u7C7B\u578B");

		ruleCatalog = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleCatalog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label3 = new Label(ruleResultCommon, SWT.NONE);
		label3.setText("\u89C4\u5219\u5B50\u7C7B\u578B");

		ruleSubCatalog = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleSubCatalog.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label4 = new Label(ruleResultCommon, SWT.NONE);
		label4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label4.setText("\u786E\u8BA4\u89D2\u8272");

		ruleCheckRole = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleCheckRole.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label5 = new Label(ruleResultCommon, SWT.NONE);
		label5.setText("\u8D1F\u8D23\u4EBA");

		ruleCoder = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY);
		ruleCoder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblissue = new Label(ruleResultCommon, SWT.NONE);
		lblissue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblissue.setText("\u5173\u8054Issue");

		relatedIssueLink = new Link(ruleResultCommon, SWT.NONE);
		relatedIssueLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		relatedIssueLink.setText("<a>                                                      </a>");
		relatedIssueLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IViewPart viewPart = activePage.findView("org.eclipse.ui.browser.view");
				WebBrowserView webBrowserView = (WebBrowserView) viewPart;
				webBrowserView.setURL(relatedIssueLink.getText().replaceAll("<a>", "").replaceAll("</a>", ""));
				activePage.activate(viewPart);
			}
		});

		Label label6 = new Label(ruleResultCommon, SWT.NONE);
		label6.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		label6.setText("\u89C4\u5219\u8BE6\u60C5");

		ruleDetail = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		GridData gdRuleDetail = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gdRuleDetail.heightHint = 45;
		ruleDetail.setLayoutData(gdRuleDetail);

		Label label7 = new Label(ruleResultCommon, SWT.NONE);
		label7.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		label7.setText("\u5907\u6CE8");

		ruleMemo = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		GridData gdRuleMemo = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gdRuleMemo.heightHint = 45;
		ruleMemo.setLayoutData(gdRuleMemo);

		Label label8 = new Label(ruleResultCommon, SWT.NONE);
		label8.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		label8.setText("\u4FEE\u6539\u5EFA\u8BAE");

		ruleSuggestion = new Text(ruleResultCommon, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		GridData gdRuleSuggestion = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gdRuleSuggestion.heightHint = 45;
		ruleSuggestion.setLayoutData(gdRuleSuggestion);

	}

	public void loadRuleDefinitionVo(String ruleDefinitionIdentifier) {
		RuleDefinitionAnnotationVO ruleDefinitionVo = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
				ruleDefinitionIdentifier);
		loadRuleDefinitionVo(ruleDefinitionVo);
	}

	public void loadRuleDefinitionVo(RuleDefinitionAnnotationVO ruleDefinitionVo) {
		if (ruleDefinitionVo != null) {
			ruleIdentifier.setText(ruleDefinitionVo.getRuleDefinitionIdentifier());
			ruleScope.setText(ruleDefinitionVo.getScope() == null ? "Œ¥…Ë÷√" : ruleDefinitionVo.getScope().getName());
			ruleCatalog
					.setText(ruleDefinitionVo.getCatalog() == null ? "Œ¥…Ë÷√" : ruleDefinitionVo.getCatalog().getName());
			ruleSubCatalog.setText(ruleDefinitionVo.getSubCatalog() == null ? "Œ¥…Ë÷√" : ruleDefinitionVo.getSubCatalog()
					.getName());
			ruleCheckRole.setText(ruleDefinitionVo.getCheckRole().getName() == null ? "Œ¥…Ë÷√" : ruleDefinitionVo
					.getCheckRole().getName());
			ruleCoder.setText(ruleDefinitionVo.getCoder());
			ruleDetail.setText(ruleDefinitionVo.getDescription());
			relatedIssueLink.setText(String.format("<a>%s</a>", ruleDefinitionVo.getRelatedSystemIssueLink()));
			ruleMemo.setText(ruleDefinitionVo.getMemo());
			ruleSuggestion.setText(ruleDefinitionVo.getSolution());
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
