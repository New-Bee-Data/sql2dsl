package com.bonree.convert.sql.parser.query.method.join;

import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.bean.SQLArgs;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;
import com.bonree.convert.sql.parser.query.method.MethodQueryParser;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Predicate;

public class JoinAtomQueryParser {

    private final List<MethodQueryParser> joinQueryParsers;

    public JoinAtomQueryParser() {
        joinQueryParsers = ImmutableList.of(
                new HasParentAtomQueryParser(),
                new HasChildAtomQueryParser()
        );
    }

    public Boolean isJoinAtomQuery(MethodInvocation invocation) {
        return joinQueryParsers.stream().anyMatch(new Predicate<MethodQueryParser>() {
            @Override
            public boolean test(MethodQueryParser methodQueryParser) {
                return methodQueryParser.isMatchMethodInvocation(invocation);
            }
        });
    }

    public AtomQuery parseJoinAtomQuery(SQLMethodInvokeExpr methodQueryExpr, String queryAs, SQLArgs SQLArgs) {
        MethodInvocation methodInvocation = new MethodInvocation(methodQueryExpr, queryAs, SQLArgs);
        MethodQueryParser joinAtomQueryParser = getQueryParser(methodInvocation);
        return joinAtomQueryParser.parseAtomMethodQuery(methodInvocation);
    }

    private MethodQueryParser getQueryParser(MethodInvocation methodInvocation) {
        for (MethodQueryParser joinQueryParserItem : joinQueryParsers) {
            if (joinQueryParserItem.isMatchMethodInvocation(methodInvocation)) {
                return joinQueryParserItem;
            }
        }
        throw new ElasticSql2DslException(
                String.format("[syntax error] Can not support join query expr[%s] condition",
                        methodInvocation.getMethodName()));
    }
}
