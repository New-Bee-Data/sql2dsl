package com.bonree.convert.sql.parser.sql.sort;

import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;
import com.bonree.convert.sql.parser.query.method.expr.MethodExpression;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;


public interface MethodSortParser extends MethodExpression {
    SortBuilder parseMethodSortBuilder(MethodInvocation invocation, SortOrder order) throws ElasticSql2DslException;
}
