package com.bonree.convert.sql.listener;

import com.bonree.convert.sql.bean.ElasticSqlQueryField;
import com.bonree.convert.sql.enums.SQLConditionOperator;

import java.util.List;

public interface ParseActionListener {

    void onSelectFieldParse(ElasticSqlQueryField field);

    void onAtomExactQueryConditionParse(ElasticSqlQueryField paramName, Object[] params, SQLConditionOperator operator);

    void onAtomMethodQueryConditionParse(ElasticSqlQueryField paramName, Object[] params);

    void onRoutingValuesParse(List<String> routingValues);

    void onLimitSizeParse(int from, int size);
}
