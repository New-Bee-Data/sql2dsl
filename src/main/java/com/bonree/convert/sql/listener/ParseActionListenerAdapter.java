package com.bonree.convert.sql.listener;

import com.bonree.convert.sql.bean.ElasticSqlQueryField;
import com.bonree.convert.sql.enums.SQLConditionOperator;

import java.util.List;

public class ParseActionListenerAdapter implements ParseActionListener {

    @Override
    public void onSelectFieldParse(ElasticSqlQueryField field) {

    }

    @Override
    public void onAtomExactQueryConditionParse(ElasticSqlQueryField paramName, Object[] params, SQLConditionOperator operator) {

    }

    @Override
    public void onAtomMethodQueryConditionParse(ElasticSqlQueryField paramName, Object[] params) {

    }

    @Override
    public void onRoutingValuesParse(List<String> routingValues) {

    }

    @Override
    public void onLimitSizeParse(int from, int size) {

    }
}
