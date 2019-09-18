package com.bonree.convert.sql.parser.query.method.fulltext;

import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.bonree.convert.sql.bean.AtomQuery;
import com.bonree.convert.sql.bean.SQLArgs;
import com.bonree.convert.sql.exception.ElasticSql2DslException;
import com.bonree.convert.sql.listener.ParseActionListener;
import com.bonree.convert.sql.parser.query.method.MethodInvocation;
import com.bonree.convert.sql.parser.query.method.MethodQueryParser;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Predicate;

public class FullTextAtomQueryParser {

    private final List<MethodQueryParser> methodQueryParsers;

    public FullTextAtomQueryParser(ParseActionListener parseActionListener) {
        methodQueryParsers = ImmutableList.of(
                new MatchAtomQueryParser(parseActionListener),
                new MultiMatchAtomQueryParser(),
                new QueryStringAtomQueryParser(),
                new SimpleQueryStringAtomQueryParser()
        );
    }

    public Boolean isFulltextAtomQuery(MethodInvocation invocation) {
        return methodQueryParsers.stream().anyMatch(new Predicate<MethodQueryParser>() {
            @Override
            public boolean test(MethodQueryParser methodQueryParser) {
                return methodQueryParser.isMatchMethodInvocation(invocation);
            }
        });
    }

    public AtomQuery parseFullTextAtomQuery(SQLMethodInvokeExpr methodQueryExpr, String queryAs, SQLArgs SQLArgs) {
        MethodInvocation methodInvocation = new MethodInvocation(methodQueryExpr, queryAs, SQLArgs);
        MethodQueryParser matchAtomQueryParser = getQueryParser(methodInvocation);
        return matchAtomQueryParser.parseAtomMethodQuery(methodInvocation);
    }

    private MethodQueryParser getQueryParser(MethodInvocation invocation) {
        for (MethodQueryParser methodQueryParser : methodQueryParsers) {
            if (methodQueryParser.isMatchMethodInvocation(invocation)) {
                return methodQueryParser;
            }
        }
        throw new ElasticSql2DslException(
                String.format("[syntax error] Can not support method query expr[%s] condition", invocation.getMethodName()));
    }
}
