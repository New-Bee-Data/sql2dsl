package com.bonree.convert.sql.parser.query.method.join;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.bean.SQLArgs;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.helper.ElasticSqlMethodInvokeHelper;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;
import com.bonree.convert.sql.parser.query.method.MethodQueryParser;
import com.bonree.convert.sql.parser.sql.BoolExpressionParser;
import com.google.common.collect.ImmutableList;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.HasParentQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

/**
 * has_parent(parentType, filterExpression)
 * <p>
 * has_parent('investment', principal > 100 and status='SUCCESS')
 *
 * @author chennan
 */
public class HasParentAtomQueryParser implements MethodQueryParser {

    private static List<String> HAS_PARENT_METHOD = ImmutableList.of("has_parent", "hasParent", "has_parent_query", "hasParentQuery");

    @Override
    public List<String> defineMethodNames() {
        return HAS_PARENT_METHOD;
    }

    @Override
    public boolean isMatchMethodInvocation(MethodInvocation invocation) {
        return ElasticSqlMethodInvokeHelper.isMethodOf(defineMethodNames(), invocation.getMethodName());
    }

    @Override
    public void checkMethodInvocation(MethodInvocation invocation) throws ElasticSql2DslException {
        if (invocation.getParameterCount() != 2) {
            throw new ElasticSql2DslException(
                    String.format("[syntax error] There's no %s args method named [%s].",
                            invocation.getParameterCount(), invocation.getMethodName()));
        }
    }

    @Override
    public AtomQuery parseAtomMethodQuery(MethodInvocation invocation) throws ElasticSql2DslException {
        String parentType = invocation.getParameterAsString(0);
        SQLExpr filter = invocation.getParameter(1);

        BoolExpressionParser boolExpressionParser = new BoolExpressionParser();
        String queryAs = invocation.getQueryAs();
        SQLArgs sqlArgs = invocation.getSqlArgs();

        BoolQueryBuilder filterBuilder = boolExpressionParser.parseBoolQueryExpr(filter, queryAs, sqlArgs);
        HasParentQueryBuilder hasParentQueryBuilder = QueryBuilders.hasParentQuery(parentType, filterBuilder, false);

        return new AtomQuery(hasParentQueryBuilder);
    }
}
