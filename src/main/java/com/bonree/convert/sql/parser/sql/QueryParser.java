package com.bonree.convert.sql.parser.sql;


import com.bonree.convert.sql.bean.ElasticDslContext;

@FunctionalInterface
public interface QueryParser {
    void parse(ElasticDslContext dslContext);
}
