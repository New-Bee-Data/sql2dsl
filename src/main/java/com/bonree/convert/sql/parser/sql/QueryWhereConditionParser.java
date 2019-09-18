package com.bonree.convert.sql.parser.sql;

import com.bonree.convert.sql.bean.ElasticDslContext;
import com.bonree.convert.sql.druid.ElasticSqlSelectQueryBlock;
import com.bonree.convert.sql.listener.ParseActionListener;
import org.elasticsearch.index.query.BoolQueryBuilder;

public class QueryWhereConditionParser extends BoolExpressionParser implements QueryParser{

    public QueryWhereConditionParser(ParseActionListener parseActionListener) {
        super(parseActionListener);
    }

    @Override
    public void parse(ElasticDslContext dslContext) {
        ElasticSqlSelectQueryBlock queryBlock = (ElasticSqlSelectQueryBlock) dslContext.getQueryExpr().getSubQuery().getQuery();

        if (queryBlock.getWhere() != null) {
            String queryAs = dslContext.getParseResult().getQueryAs();

            BoolQueryBuilder whereQuery = parseBoolQueryExpr(queryBlock.getWhere(), queryAs, dslContext.getSQLArgs());

            dslContext.getParseResult().setWhereCondition(whereQuery);
        }
    }
}
