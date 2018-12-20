# Autumn-Web应用组件

## 主要功能

* 请求
  * 跟踪器过滤器`org.autumn.commons.web.request.AutumnTrackerFilter`，添加请求信息，跟踪当前请求等功能
  * 通过拦截器方式，在两种HTTP-RPC调用方式（`Feign`和`RestTemplate`）中传递请求信息
* 响应
  * 使用`org.autumn.commons.web.response.AutumnResponse`统一所有响应结果，包括返回码、返回描述、是否成功、跟踪ID等
  * 自动搜索实现了接口`org.autumn.commons.web.response.code.ResponseCode`的返回码
  * 返回描述国际化
  * 提供包装器接口，供个性化包装返回结果（并提供分页、数据绑定异常等实现）
* 异常处理
  * HandlerMethod执行异常：使用`@ControllerAdvice`和`@ExceptionHandler`统一处理
  * 其它过程中的异常，通过拦截`Response.sendError`重新转发，然后通过`ExceptionHandlerMapping`处理
* 请求参数的数据绑定
  * 请求参数和控制器方法参数绑定的扩展切入点`IPropertyValuesProvider`：绑定请添加属性、绑定后修改属性
  > 这两步均发生在数据校验前
  * 抽象的RSA解密绑定
  * 支持中括号`[]`形式的属性绑定
  * 异常处理国际化
* 数据校验
  * 校验日期，可自定义格式
  * 校验枚举值，可自定义枚举类，或者直接定义允许的取值
  * 校验信息国际化
* Swagger
  * 添加AutumnResponse的统一处理
  * 提取Swagger配置接口，并通过属性文件形式配置API等相关信息
  * 配置公共的请求参数
  * 提供请求参数文档扩展器接口，并提供分页参数的请求文档
  * 实现分页请求的响应文档
  * 国际化
  

## 修改日志

### 0.0.1

* 提供请求、响应、异常处理、数据绑定、数据校验、Swagger文档等增强功能

