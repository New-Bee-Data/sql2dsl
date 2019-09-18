package com.bonree.convert.sql.parser.query.method;


import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.parser.query.method.expr.AbstractParameterizedMethodExpression;
import com.bonree.convert.sql.helper.ElasticSqlMethodInvokeHelper;

import java.util.Map;

public abstract class ParameterizedMethodQueryParser extends AbstractParameterizedMethodExpression implements MethodQueryParser {

    protected abstract String defineExtraParamString(MethodInvocation invocation);

    protected abstract AtomQuery parseMethodQueryWithExtraParams(
            MethodInvocation invocation, Map<String, String> extraParamMap) throws ElasticSql2DslException;

    @Override
    public boolean isMatchMethodInvocation(MethodInvocation invocation) {
        return ElasticSqlMethodInvokeHelper.isMethodOf(defineMethodNames(), invocation.getMethodName());
    }

    @Override
    public AtomQuery parseAtomMethodQuery(MethodInvocation invocation) throws ElasticSql2DslException {
        if (!isMatchMethodInvocation(invocation)) {
            throw new ElasticSql2DslException(
                    String.format("[syntax error] Expected method name is one of [%s],but get [%s]",
                            defineMethodNames(), invocation.getMethodName()));
        }
        checkMethodInvocation(invocation);

        Map<String, String> extraParamMap = generateParameterMap(invocation);
        return parseMethodQueryWithExtraParams(invocation, extraParamMap);
    }
}
