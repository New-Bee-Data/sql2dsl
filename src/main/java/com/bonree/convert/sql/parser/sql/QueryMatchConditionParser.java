package com.bonree.convert.sql.parser.sql;

import com.bonree.convert.sql.bean.ElasticDslContext;
import com.bonree.convert.sql.druid.ElasticSqlSelectQueryBlock;
import com.bonree.convert.sql.listener.ParseActionListener;
import org.elasticsearch.index.query.BoolQueryBuilder;

public class QueryMatchConditionParser extends BoolExpressionParser implements QueryParser{

    public QueryMatchConditionParser(ParseActionListener parseActionListener) {
        super(parseActionListener);
    }

    @Override
    public void parse(ElasticDslContext dslContext) {
        ElasticSqlSelectQueryBlock queryBlock = (ElasticSqlSelectQueryBlock) dslContext.getQueryExpr().getSubQuery().getQuery();

        if (queryBlock.getMatchQuery() != null) {
            String queryAs = dslContext.getParseResult().getQueryAs();

            BoolQueryBuilder matchQuery = parseBoolQueryExpr(queryBlock.getMatchQuery(), queryAs, dslContext.getSQLArgs());

            dslContext.getParseResult().setMatchCondition(matchQuery);
        }
    }
}
