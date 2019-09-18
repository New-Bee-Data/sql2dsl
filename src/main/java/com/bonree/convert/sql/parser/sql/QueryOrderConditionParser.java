package com.bonree.convert.sql.parser.sql;

import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.bonree.convert.sql.bean.ElasticDslContext;
import com.bonree.convert.sql.bean.ElasticSqlQueryField;
import com.bonree.convert.sql.bean.SQLArgs;
import com.bonree.convert.sql.druid.ElasticSqlSelectQueryBlock;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.listener.ParseActionListener;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;
import com.bonree.convert.sql.parser.sql.sort.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

public class QueryOrderConditionParser implements QueryParser {

    private ParseActionListener parseActionListener;

    private List<MethodSortParser> methodSortParsers;

    public QueryOrderConditionParser(ParseActionListener parseActionListener) {
        this.parseActionListener = parseActionListener;

        methodSortParsers = ImmutableList.of(
                new NvlMethodSortParser(),
                new ScriptMethodSortParser(),
                new NestedSortMethodParser()
        );
    }

    @Override
    public void parse(ElasticDslContext dslContext) {
        ElasticSqlSelectQueryBlock queryBlock = (ElasticSqlSelectQueryBlock) dslContext.getQueryExpr().getSubQuery().getQuery();
        SQLOrderBy sqlOrderBy = queryBlock.getOrderBy();
        if (sqlOrderBy != null && CollectionUtils.isNotEmpty(sqlOrderBy.getItems())) {
            List<SortBuilder> orderByList = Lists.newLinkedList();

            String queryAs = dslContext.getParseResult().getQueryAs();
            SQLArgs sqlArgs = dslContext.getSQLArgs();

            for (SQLSelectOrderByItem orderByItem : sqlOrderBy.getItems()) {
                SortBuilder orderBy = parseOrderCondition(orderByItem, queryAs, sqlArgs);
                if (orderBy != null) {
                    orderByList.add(orderBy);
                }
            }
            dslContext.getParseResult().setOrderBy(orderByList);
        }
    }

    private SortBuilder parseOrderCondition(SQLSelectOrderByItem orderByItem, String queryAs, SQLArgs SQLArgs) {

        SortOrder order = orderByItem.getType() == SQLOrderingSpecification.ASC ? SortOrder.ASC : SortOrder.DESC;

        if (ParseSortBuilderHelper.isFieldExpr(orderByItem.getExpr())) {
            QueryFieldParser fieldParser = new QueryFieldParser();
            ElasticSqlQueryField sortField = fieldParser.parseConditionQueryField(orderByItem.getExpr(), queryAs);
            return ParseSortBuilderHelper.parseBasedOnFieldSortBuilder(sortField, new ConditionSortBuilder() {
                @Override
                public FieldSortBuilder buildSort(String queryFieldName) {
                    return SortBuilders.fieldSort(queryFieldName).order(order);
                }
            });
        }

        if (ParseSortBuilderHelper.isMethodInvokeExpr(orderByItem.getExpr())) {
            MethodInvocation sortMethodInvocation = new MethodInvocation((SQLMethodInvokeExpr) orderByItem.getExpr(), queryAs, SQLArgs);
            for (MethodSortParser methodSortParser : methodSortParsers) {
                if (methodSortParser.isMatchMethodInvocation(sortMethodInvocation)) {
                    return methodSortParser.parseMethodSortBuilder(sortMethodInvocation, order);
                }
            }
        }

        throw new ElasticSql2DslException("[syntax error] can not support sort type: " + orderByItem.getExpr().getClass());
    }
}
