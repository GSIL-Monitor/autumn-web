package org.autumn.commons.web;

import java.util.Map;

import org.autumn.commons.web.response.code.ResponseCodeHolder;
import org.junit.Test;

public class ResponseCodeHolderTest {

    @Test
    public void test() {
        Map<String, String> codes = ResponseCodeHolder.getCodes();
        System.out.println(codes);
    }
}
