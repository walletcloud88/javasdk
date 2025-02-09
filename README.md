### maven包安装
```
    <repositories>
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/walletcloud88/javasdk</url>
        </repository>
    </repositories>


    <dependencies>
        <dependency>
            <groupId>com.wallet.api</groupId>
            <artifactId>javasdk</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
```

### 基础参数配置
```
        String appId = "";
        String host = "";
        String lang = "en";                 // support en\cn
        String version = "1.0";
        String privateKey = "";
        String platformPublicKey = "";
```
* RSA公私钥获取平台：https://www.metools.info/code/c80.html
* 密钥长度：2048 bit
* 密钥格式：PKCS#8
* 私钥密码：无
