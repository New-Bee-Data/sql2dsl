package com.bonree.convert.sql;

import com.bonree.convert.sql.bean.ElasticSqlParseResult;
import com.bonree.convert.sql.parser.ElasticSql2DslParser;
import org.junit.Test;

public class ElasticSql2DslParserTest {

    @Test
    public void testSql2Dsl() {
//        String sql = "select name,age from file1 group by terms(name),terms(age)";
//        String sql = "select time,sum(pull_kafka_count) from apms_preprocess_topology where time >='2019-08-15 21:40:20' and time <= '2019-08-15 22:40:20' group by histogram(a,5)";
        String sql = "select * from test where id = '123123'";
//        String sql = "select * from emp where (deptno = 20 or deptno = 30) and sal >= 2000";
        ElasticSql2DslParser parser = new ElasticSql2DslParser();
        ElasticSqlParseResult sqlParseResult = parser.parse(sql);
        String dsl = sqlParseResult.toDsl();
        System.out.println("dsl: \n" + dsl);
    }
}
