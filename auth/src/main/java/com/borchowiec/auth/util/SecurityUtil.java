package com.borchowiec.auth.util;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.util.SimpleMethodInvocation;

public class SecurityUtil {
    public static boolean checkExpression(Authentication authentication, String securityExpresion) {
        SpelExpressionParser parser;
        parser = new SpelExpressionParser();

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        EvaluationContext evaluationContext = expressionHandler
                .createEvaluationContext(authentication, new SimpleMethodInvocation());

        Expression expression = parser.parseExpression(securityExpresion);
        expression.getValue(evaluationContext);
        return ExpressionUtils.evaluateAsBoolean(parser.parseExpression(securityExpresion), evaluationContext);
    }
}
