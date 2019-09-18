package com.bonree.convert.sql.parser.query.exact;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.bean.ElasticSqlQueryField;
import com.bonree.convert.sql.enums.QueryFieldType;
import com.bonree.convert.sql.enums.SQLConditionOperator;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.listener.ParseActionListener;
import com.bonree.convert.sql.parser.sql.QueryFieldParser;
import org.elasticsearch.index.query.QueryBuilder;

public abstract class AbstractAtomExactQueryParser {

    protected ParseActionListener parseActionListener;

    public AbstractAtomExactQueryParser(ParseActionListener parseActionListener) {
        this.parseActionListener = parseActionListener;
    }

    protected AtomQuery parseCondition(SQLExpr queryFieldExpr, SQLConditionOperator operator, Object[] params, String queryAs, IConditionExactQueryBuilder queryBuilder) {
        QueryFieldParser queryFieldParser = new QueryFieldParser();
        ElasticSqlQueryField queryField = queryFieldParser.parseConditionQueryField(queryFieldExpr, queryAs);

        AtomQuery atomQuery = null;
        if (queryField.getQueryFieldType() == QueryFieldType.RootDocField || queryField.getQueryFieldType() == QueryFieldType.InnerDocField) {
            QueryBuilder originalQuery = queryBuilder.buildQuery(queryField.getQueryFieldFullName(), operator, params);
            atomQuery = new AtomQuery(originalQuery);
        }

        if (queryField.getQueryFieldType() == QueryFieldType.NestedDocField) {
            QueryBuilder originalQuery = queryBuilder.buildQuery(queryField.getQueryFieldFullName(), operator, params);
            atomQuery = new AtomQuery(originalQuery, queryField.getNestedDocContextPath());
        }

        if (atomQuery == null) {
            throw new ElasticSql2DslException(String.format("[syntax error] where condition field can not support type[%s]", queryField.getQueryFieldType()));
        }

        parseActionListener.onAtomExactQueryConditionParse(queryField, params, operator);

        return atomQuery;
    }
}
