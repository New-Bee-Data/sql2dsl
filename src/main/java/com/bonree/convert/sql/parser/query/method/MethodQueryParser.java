package com.bonree.convert.sql.parser.query.method;


import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.parser.query.method.expr.MethodExpression;

public interface MethodQueryParser extends MethodExpression {
    AtomQuery parseAtomMethodQuery(MethodInvocation invocation) throws ElasticSql2DslException;
}
