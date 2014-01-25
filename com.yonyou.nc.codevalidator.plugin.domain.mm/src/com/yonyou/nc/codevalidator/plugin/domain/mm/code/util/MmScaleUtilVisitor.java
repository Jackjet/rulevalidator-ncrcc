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
 * ���*ScaleUtil����ļ����Ƿ������������ֱ���Ƭ���б���ӡ�ľ��ȣ���������������ͬ���� public void
 * setScale(BillScaleProcessor scale)�������������ǣ���ô����ʹ��һ�׾����㷨
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
public class MmScaleUtilVisitor extends VoidVisitorAdapter<String> {

    private List<MethodDeclaration> mdList = new ArrayList<MethodDeclaration>();

    private boolean isUserPubScaleFrame = false;

    /**
     * ���ȴ�������
     */
    private List<MethodDeclaration> scaleProcessMdList = new ArrayList<MethodDeclaration>();

    /**
     * ��Ŵ�����Ϣ
     */
    private StringBuilder noteBuilder = new StringBuilder();

    @Override
    public void visit(MethodDeclaration md, String obj) {
        List<Parameter> params = md.getParameters();
        if (MMValueCheck.isEmpty(params)) {
            this.mdList.add(md);
            return;
        }
        // �ҵ����ȴ�����
        String paramValue = params.get(0).toString();
        paramValue = paramValue.substring(0, paramValue.indexOf(" "));
        // �еĿ�����ȫ·������
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
                // �е����ڷ���������ʹ��ȫ·����������Import���Ҳ���
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
            this.noteBuilder.append("�С�" + this.scaleProcessMdList.size() + "�������ȴ�����" + tmpMdNames.toString());
            return false;
        }
        // �õ����ȴ�������name
        String mdName = this.scaleProcessMdList.get(0).getName();
        // ������ŵ��þ��ȴ�����ķ���
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
                // �������һ�㣬�ܹ����������ĸ�ûʹ�þ��Ĵ����࣬����ͨ����������һ����ʶ���������ʱ������
                if (stmt.toString().contains(mdName)) {
                    tmpMdSet.add(md);
                }
            }

        }
        // ���ʹ��һ�״�������������������
        if (tmpMdSet.size() >= 3) {
            return true;
        }
        List<String> noteList = new ArrayList<String>();

        // �ж��Ƿ�����˿�Ƭ���ȴ���
        if (!this.hasScaleProcess(tmpMdSet, "CardPaneScaleProcessor")) {
            noteList.add("��Ƭ");
        }
        if (!this.hasScaleProcess(tmpMdSet, "ListPaneScaleProcessor")) {
            noteList.add("�б�");
        }
        if (!this.hasScaleProcess(tmpMdSet, "BillVOScaleProcessor")) {
            noteList.add("��ӡ");
        }
        if (!noteList.isEmpty()) {
            this.noteBuilder.append("ȱ��" + noteList.toString() + "�ľ��ȴ���;");
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
     * ����������ߴ��������õ�process����ô�ͷ���true,���򷵻�false
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
