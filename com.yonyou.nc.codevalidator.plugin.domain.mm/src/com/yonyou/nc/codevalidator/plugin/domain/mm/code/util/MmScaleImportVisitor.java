package com.yonyou.nc.codevalidator.plugin.domain.mm.code.util;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MmScaleImportVisitor extends VoidVisitorAdapter<Set<String>> {
    /**
     * 相同目录下不是IMPORT尽量的。
     * 这个list存放所有的类名
     */
    List<String> classNameList = new ArrayList<String>();

    public MmScaleImportVisitor(List<String> newClassNameList) {
        if (newClassNameList != null) {
            this.classNameList = newClassNameList;
        }

    }

    @Override
    public void visit(CompilationUnit n, Set<String> set) {
        super.visit(n, set);
        List<ImportDeclaration> idcList = n.getImports();
        if (idcList != null && idcList.size() > 0) {
            for (ImportDeclaration impt : idcList) {
                if (impt.getName() instanceof QualifiedNameExpr) {
                    QualifiedNameExpr qne = (QualifiedNameExpr) impt.getName();
                    if (qne.toString() == null) {
                        continue;
                    }
                    if (qne.toString().indexOf(".scale.") > 0) {
                        set.add(this.getClassName(qne.toString()));
                    }
                }
            }
        }
    }

    public String getClassName(String name) {
        String tName = name.trim();
        if (tName.endsWith(".java")) {
            tName = tName.substring(0, tName.length() - ".java".length());
        }
        if (tName.indexOf(".") > 0) {
            tName = tName.substring(tName.lastIndexOf(".") + 1);
        }
        return tName;
    }

    @Override
    public void visit(MethodDeclaration n, Set<String> set) {
        super.visit(n, set);
        String txt = n.toString();
        for (String className : this.classNameList) {
            if (txt.indexOf(className) > 0) {
                set.add(className);
            }
        }
    }
}
