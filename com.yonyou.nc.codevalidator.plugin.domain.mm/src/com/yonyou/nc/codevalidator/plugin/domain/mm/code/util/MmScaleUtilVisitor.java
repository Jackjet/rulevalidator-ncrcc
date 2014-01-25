package com.yonyou.nc.codevalidator.plugin.domain.mm.code.util;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;

/**
 * 检查*ScaleUtil这个文件中是否有三个方法分别处理卡片，列表，打印的精度，这三个方法都共同调用 public void
 * setScale(BillScaleProcessor scale)这个方法，如果是，那么就是使用一套精度算法
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmScaleUtilVisitor extends VoidVisitorAdapter<String> {

    private List<MethodDeclaration> mdList = new ArrayList<MethodDeclaration>();

    private boolean isUserPubScaleFrame = false;

    /**
     * 精度处理方法的
     */
    private List<MethodDeclaration> scaleProcessMdList = new ArrayList<MethodDeclaration>();

    /**
     * 存放错误信息
     */
    private StringBuilder noteBuilder = new StringBuilder();

    @Override
    public void visit(MethodDeclaration md, String obj) {
        List<Parameter> params = md.getParameters();
        if (MMValueCheck.isEmpty(params)) {
            this.mdList.add(md);
            return;
        }
        // 找到精度处理方法
        String paramValue = params.get(0).toString();
        paramValue = paramValue.substring(0, paramValue.indexOf(" "));
        // 有的可能是全路径引用
        if (params.size() == 1
                && (paramValue.equals("BillScaleProcessor") || paramValue
                        .equals("nc.vo.pubapp.scale.BillScaleProcessor"))) {
            this.scaleProcessMdList.add(md);
        }
        this.mdList.add(md);

    }

    @Override
    public void visit(CompilationUnit n, String rlist) {
        super.visit(n, rlist);
        List<ImportDeclaration> idcList = n.getImports();
        for (ImportDeclaration impt : idcList) {
            if (impt.getName() instanceof QualifiedNameExpr) {
                QualifiedNameExpr qne = (QualifiedNameExpr) impt.getName();
                if (qne.toString() == null) {
                    continue;
                }
                // 有的是在方法参数中使用全路径，所以在Import中找不到
                if (n.toString().contains("nc.vo.pubapp.scale.BillScaleProcessor")) {
                    this.isUserPubScaleFrame = true;
                    break;
                }
            }
        }

    }

    public boolean isAllUserOneMethod() {
        return this.analizyAllUserOneMethod();
    }

    public boolean isUserPubScaleFrame() {
        return this.isUserPubScaleFrame;
    }

    private boolean analizyAllUserOneMethod() {
        if (!this.isUserPubScaleFrame) {
            return false;
        }
        if (this.scaleProcessMdList.size() > 1) {
            List<String> tmpMdNames = new ArrayList<String>();
            for (MethodDeclaration md : this.scaleProcessMdList) {
                tmpMdNames.add(md.getName());
            }
            this.noteBuilder.append("有【" + this.scaleProcessMdList.size() + "】个精度处理方法" + tmpMdNames.toString());
            return false;
        }
        // 得到精度处理方法的name
        String mdName = this.scaleProcessMdList.get(0).getName();
        // 用来存放调用精度处理类的方法
        Set<MethodDeclaration> tmpMdSet = new HashSet<MethodDeclaration>();
        for (MethodDeclaration md : this.mdList) {
            BlockStmt bodyStmt = md.getBody();
            if (null == bodyStmt) {
                continue;
            }
            List<Statement> stmts = bodyStmt.getStmts();
            if (MMValueCheck.isEmpty(stmts)) {
                continue;
            }
            for (Statement stmt : stmts) {
                if (stmt == null) {
                    continue;
                }
                // 如果更深一层，能够分析出是哪个没使用精的处理类，但是通过方法名不一定能识别出来，暂时先这样
                if (stmt.toString().contains(mdName)) {
                    tmpMdSet.add(md);
                }
            }

        }
        // 如果使用一套处理，则至少有三个方法
        if (tmpMdSet.size() >= 3) {
            return true;
        }
        List<String> noteList = new ArrayList<String>();

        // 判断是否进行了卡片精度处理
        if (!this.hasScaleProcess(tmpMdSet, "CardPaneScaleProcessor")) {
            noteList.add("卡片");
        }
        if (!this.hasScaleProcess(tmpMdSet, "ListPaneScaleProcessor")) {
            noteList.add("列表");
        }
        if (!this.hasScaleProcess(tmpMdSet, "BillVOScaleProcessor")) {
            noteList.add("打印");
        }
        if (!noteList.isEmpty()) {
            this.noteBuilder.append("缺少" + noteList.toString() + "的精度处理;");
        }

        return false;
    }

    private boolean hasScaleProcess(Set<MethodDeclaration> tmpMdSet, String process) {
        for (MethodDeclaration methodDeclaration : tmpMdSet) {
            if (this.isUseScaleProcess(methodDeclaration, process)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果参数或者代码中有用到process，那么就返回true,否则返回false
     * 
     * @param md
     * @param parcess
     * @return
     */
    private boolean isUseScaleProcess(MethodDeclaration md, String process) {

        List<Parameter> params = md.getParameters();

        BlockStmt body = md.getBody();

        if (MMValueCheck.isEmpty(params) || MMValueCheck.isEmpty(body)) {
            return false;
        }

        if (params.toString().contains(process) || body.toString().contains(process)) {
            return true;
        }

        return false;
    }

    public StringBuilder getNoteBuilder() {
        return this.noteBuilder;
    }

}
