package com.bonree.convert.sql.parser.query.exact;

import com.bonree.convert.sql.enums.SQLConditionOperator;
import org.elasticsearch.index.query.QueryBuilder;

@FunctionalInterface
public interface IConditionExactQueryBuilder {
    QueryBuilder buildQuery(String queryFieldName, SQLConditionOperator operator, Object[] rightParamValues);
}
