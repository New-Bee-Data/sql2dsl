package com.bonree.convert.sql.parser.query.method.expr;

import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;

import java.util.List;

public interface MethodExpression {
    List<String> defineMethodNames();

    boolean isMatchMethodInvocation(MethodInvocation invocation);

    void checkMethodInvocation(MethodInvocation invocation) throws ElasticSql2DslException;
}
