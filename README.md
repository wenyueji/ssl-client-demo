# https双向认证client端请求代码示例

demo是根据CA证书签发client证书请求，客户端只需根据client.jks访问即可

### server端实现思路：
* 生成CA证书
* 生成server证书
* CA签发server证书
* 生成CA.truststore
* 生成server.jks
* 配置文件中开启ssl，添加key-store、trust-store的相关配置，开启双向认证配置：client-auth: need

### client端请求：
参考本demo即可