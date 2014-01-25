package com.yonyou.nc.codevalidator.plugin.domain.mm.uif;

/**
 * UIF的一些常量类
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmUIFactoryConstants {

    private MmUIFactoryConstants() {

    }

    /**
     * 功能注册中的 功能类名
     */
    public static final String FUN_CLASS_NAME = "nc.ui.pubapp.uif2app.ToftPanelAdaptorEx";

    /**
     * 主子表事件监听类
     */
    public static final String EVENT_LISTENER_CALSS = "nc.ui.pubapp.uif2app.model.AppEventHandlerMediator";

    /**
     * 表头表尾编辑前事件
     */
    public static final String HEAD_TAIL_BEFORE_EDIT_EVENT =
            "nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent";

    /**
     * 表头表尾编辑后事件
     */
    public static final String HEAD_TAIL_AFTER_EDIT_EVENT =
            "nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent";

    /**
     * 表体行编辑后事件
     */
    public static final String BODY_AFTER_ROW_EDIT_EVENT = "nc.ui.pubapp.uif2app.event.card.CardBodyAfterRowEditEvent";

    /**
     * 主组织改变事件
     */
    public static final String ORG_CHANGED_EVENT = "nc.ui.pubapp.uif2app.event.OrgChangedEvent";

    /**
     * 表体字段编辑前事件
     */
    public static final String BODY_BEFORE_EDIT_EVENT = "nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent";

    /**
     * 表体字段编辑后事件
     */
    public static final String BODY_AFTER_EDIT_EVENT = "nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent";

    /**
     * 工具栏cardInfoPnl的实现类
     */
    public static final String CARD_TOOL_BARPANEL_CLASS = "nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel";

    /**
     * 卡片返回按钮类
     */
    public static final String RETURN_ACTION_CLASS = "nc.ui.pubapp.uif2app.actions.UEReturnAction";

    /**
     * 查询区域类queryArea
     */
    public static final String QUERY_AREA_CLASS = "nc.ui.pubapp.uif2app.tangramlayout.UEQueryAreaShell";

    /**
     * 查询信息类queryInfo
     */
    public static final String QUERY_INFO_CLASS = "nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel";

    /**
     * 卡片用户自定义项容器
     */
    public static final String CARD_USER_DEF_ITEM_PREPARATOR_CLASS = "nc.ui.uif2.editor.UserdefitemContainerPreparator";

    /**
     * 列表自定义项容器
     */
    public static final String LIST_USER_DEF_ITEM_PREPARATOR_CLASS =
            "nc.ui.uif2.editor.UserdefitemContainerListPreparator";

    /**
     * 物料自由辅助属性
     */
    public static final String MAR_ASST_PREPARATOR_CLASS =
            "nc.ui.pubapp.uif2app.view.material.assistant.MarAsstPreparator";

    /**
     * 模板非空校验
     */
    public static final String TEMPLATE_NOT_NULL_VALIDATION =
            "nc.ui.pubapp.uif2app.validation.TemplateNotNullValidation";

    /**
     * 模板容器
     */
    public static final String TEMPLATE_CONTAINER = "nc.ui.uif2.editor.TemplateContainer";

    /**
     * 用户自定义项容器
     */
    public static final String USER_DEF_CONTAINER = "nc.ui.uif2.userdefitem.UserDefItemContainer";

    /**
     * 查询模板加载容器
     */
    public static final String QUERY_TEMPLATE_CONTAINER = "nc.ui.uif2.editor.QueryTemplateContainer";

}
