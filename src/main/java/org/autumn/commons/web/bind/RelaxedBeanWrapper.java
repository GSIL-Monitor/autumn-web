package org.autumn.commons.web.bind;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyValue;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 宽松的Bean操作包装器，可以使用中括号[]的形式表示类的属性，a[b]和a.b等价<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
/*package*/ class RelaxedBeanWrapper extends BeanWrapperImpl {

    public RelaxedBeanWrapper(Object target) {
        super(target);
    }

    @Override
    protected void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException {
        try {
            super.setPropertyValue(tokens, pv);
        } catch (Exception e) {
            if (e instanceof InvalidPropertyException && tokens.keys != null) {
                try {
                    String name = convertPropertyName(pv.getName(), 0);
                    super.setPropertyValue(name, pv.getValue());
                } catch (Exception i) {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private String convertPropertyName(String name, int begin) {
        StringBuilder sb = new StringBuilder();
        char[] chs = name.toCharArray();
        int leftCount = 0;//未匹配的‘[’字符的个数
        boolean in = true;//是否处于解析中
        for (int i = begin, s = chs.length; i < s; i++) {
            char ch = chs[i];
            if (in) {
                if (ch == '[') {
                    if (leftCount == 0) {
                        ch = '.';//使用点号替换第一个[
                    }
                    leftCount++;
                } else if (ch == ']') {
                    leftCount--;//未匹配的[的个数减1
                    if (leftCount == 0) {
                        if (i == s - 1) {
                            break;
                        } else {
                            in = false;
                            ch = '.';
                        }
                    }
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
