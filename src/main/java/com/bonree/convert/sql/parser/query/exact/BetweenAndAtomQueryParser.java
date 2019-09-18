package com.bonree.convert.sql.parser.query.exact;

import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.bean.SQLArgs;
import com.bonree.convert.sql.enums.SQLConditionOperator;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.helper.ElasticSqlArgConverter;
import com.bonree.convert.sql.listener.ParseActionListener;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;


public class BetweenAndAtomQueryParser extends AbstractAtomExactQueryParser {

    public BetweenAndAtomQueryParser(ParseActionListener parseActionListener) {
        super(parseActionListener);
    }

    public AtomQuery parseBetweenAndQuery(SQLBetweenExpr betweenAndExpr, String queryAs, SQLArgs SQLArgs) {
        Object from = ElasticSqlArgConverter.convertSqlArg(betweenAndExpr.getBeginExpr(), SQLArgs);
        Object to = ElasticSqlArgConverter.convertSqlArg(betweenAndExpr.getEndExpr(), SQLArgs);

        if (from == null || to == null) {
            throw new ElasticSql2DslException("[syntax error] Between Expr only support one of [number,date] arg type");
        }

        return parseCondition(betweenAndExpr.getTestExpr(), SQLConditionOperator.BetweenAnd, new Object[]{from, to}, queryAs, new IConditionExactQueryBuilder() {
            @Override
            public QueryBuilder buildQuery(String queryFieldName, SQLConditionOperator operator, Object[] rightParamValues) {
                return QueryBuilders.rangeQuery(queryFieldName).gte(rightParamValues[0]).lte(rightParamValues[1]);
            }
        });
    }
}
