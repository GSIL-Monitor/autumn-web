package org.autumn.commons.web.swagger.configure.impl;

import javax.servlet.ServletContext;

import org.autumn.commons.Utils;

import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Copy Right Information : @Copyright@ <br>
 * Project : @Project@ <br>
 * Description : 配置测试主机和上下文<br>
 * Author : andyslin <br>
 * Version : 0.0.1 <br>
 * Date : 2018-12-13<br>
 */
public class HostAndPathConfigurer extends AbstractSwaggerDocketConfigurer {

    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void configure(Docket docket) {
        String host = properties.getHost();
        if (!Utils.isBlank(host)) {
            docket.host(host);
        }

        String contextPath = properties.getContextPath();
        if (!Utils.isBlank(contextPath)) {
            docket.pathProvider(new RelativePathProvider(servletContext) {
                @Override
                protected String applicationPath() {
                    return contextPath;
                }

                @Override
                protected String getDocumentationPath() {
                    return contextPath;
                }
            });
        }
    }
}
