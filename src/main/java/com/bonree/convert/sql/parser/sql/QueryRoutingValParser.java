package com.bonree.convert.sql.parser.sql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.bonree.convert.sql.bean.ElasticDslContext;
import com.bonree.convert.sql.druid.ElasticSqlSelectQueryBlock;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.helper.ElasticSqlArgConverter;
import com.bonree.convert.sql.listener.ParseActionListener;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class QueryRoutingValParser implements QueryParser {

    private ParseActionListener parseActionListener;

    public QueryRoutingValParser(ParseActionListener parseActionListener) {
        this.parseActionListener = parseActionListener;
    }

    @Override
    public void parse(ElasticDslContext dslContext) {
        ElasticSqlSelectQueryBlock queryBlock = (ElasticSqlSelectQueryBlock) dslContext.getQueryExpr().getSubQuery().getQuery();
        if (queryBlock.getRouting() != null && CollectionUtils.isNotEmpty(queryBlock.getRouting().getRoutingValues())) {
            List<String> routingStringValues = Lists.newLinkedList();
            for (SQLExpr routingVal : queryBlock.getRouting().getRoutingValues()) {
                if (routingVal instanceof SQLCharExpr) {
                    routingStringValues.add(((SQLCharExpr) routingVal).getText());
                }
                else if (routingVal instanceof SQLVariantRefExpr) {
                    Object targetVal = ElasticSqlArgConverter.convertSqlArg(routingVal, dslContext.getSQLArgs());
                    routingStringValues.add(targetVal.toString());
                }
                else {
                    throw new ElasticSql2DslException("[syntax error] Index routing val must be a string");
                }
            }
            dslContext.getParseResult().setRoutingBy(routingStringValues);

            parseActionListener.onRoutingValuesParse(routingStringValues);
        }
    }
}
