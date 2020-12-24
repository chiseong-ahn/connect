# SCGLAB 채팅상담 서버 환경.

## Apache 설정
> 기본경로
- 메인 : /usr/local/apache2
- 프로젝트 : /data/project/cstalk/apache


> Proxy 설정
- 톰캣의 websocket으로 통신하기 위한 설정.
- /usr/local/apache2/httpd.conf
```
// 주석 해제 - line 127.
LoadModule proxy_module modules/mod_proxy.so

// 주석 해제 - line 137.
LoadModule proxy_wstunnel_module modules/mod_proxy_wstunnel.so
```

> 가상 호스트 설정
- /data/project/gasapp-cstalk/apache/conf/httpd-ssl.conf
```
<VirtualHost *:443>
    ServerName cstalk-dev.gasapp.co.kr
    DocumentRoot /data/project/gasapp-cstalk/apache/htdocs

    JkMount /api/* gasapp-cstalk
    jkMount /ws gasapp-cstalk
    jkMount /pub/socket/* gasapp-cstalk
    jkMount /sub/socket/* gasapp-cstalk
    jkMount /webjars/* gasapp-cstalk
    jkMount /auth/* gasapp-cstalk
    #jkMount /example/* gasapp-cstalk
    jkMount /swagger* gasapp-cstalk
    jkMount /v3/* gasapp-cstalk

    # 업로드한 파일에 대해 서비스를 위한 Alias
    Alias /attach/ /data/project/gasapp-cstalk/apache/data/attach/

    <Directory /data/project/gasapp-cstalk/apache/data/attach>
        Options All
        AllowOverride All
        Require all granted
    </Directory>

    SSLProxyEngine On
    
    # Websocket 연동 설정.
    RewriteEngine On
    RewriteCond %{HTTP:Upgrade} =websocket [NC]
    RewriteRule /(.*) ws://127.0.0.1:8115/$1 [P,L]
    #RewriteCond %{HTTP:Upgrade} !=websocket [NC]
    #RewriteRule /(.*) http://127.0.0.1:8115/$1 [P,L]

    ProxyPass /ws/ http://127.0.0.1:8115/ws/
    ProxyPassReverse /ws/ http://127.0.0.1:8115/ws/

# 121.171.80.14 scg private ip

    <LocationMatch ^/(index.html)?$>
        Header set Cache-Control "max-age=0, no-cache, no-store, must-revalidate"
        Header set Pragma "no-cache"
        Header set Expires "Thu, 1 Jan 1970 00:00:00 GMT"
    </LocationMatch>

    <IfModule expires_module>
        ExpiresActive On
        <Location /attach/>
            ExpiresByType image/gif "access plus 2 month"
            ExpiresByType image/jpeg "access plus 2 month"
            ExpiresByType image/png "access plus 2 month"
        </Location>
        <Location /static/>
            ExpiresByType text/css "access plus 1 month"
            ExpiresByType application/javascript "access plus 1 month"
        </Location>
    </IfModule>

    SetEnvIf Request_URI . cstalk-log
    SetEnvIf Request_URI ^/favicon.ico$ no-log !cstalk-log
    SetEnvIf Request_URI ^/static/* static-log !cstalk-log

    SetEnvIf X-MEMBER "(\d+);?" X-MEMBER=$1
    SetEnvIf X-COMPANY "(\d+);?" X-COMPANY=$1

    LogFormat "%>s %{%Y-%m-%d %H:%M:%S}t.%{msec_frac}t %{Host}i %p %h %{X-COMPANY}e %{X-MEMBER}e %D %b \"%r\" \"%{Referer}i\" \"%{User-Agent}i\"" gasapp
    CustomLog "|/usr/local/apache2/bin/rotatelogs -l /data/log/apache2/cstalk-dev.access_log.%Y%m%d 86400" gasapp env=cstalk-log
    CustomLog "|/usr/local/apache2/bin/rotatelogs -l /data/log/apache2/cstalk-dev.static.access_log.%Y%m%d 86400" gasapp env=static-log

    SSLEngine on
    SSLCertificateFile "/usr/local/apache2/conf/extra/ssl/STAR.gasapp.co.kr.crt"
    SSLCertificateKeyFile "/usr/local/apache2/conf/extra/ssl/STAR.gasapp.co.kr.key"
    SSLCertificateChainFile "/usr/local/apache2/conf/extra/ssl/chainca.crt"

    BrowserMatch "MSIE [2-5]" \
         nokeepalive ssl-unclean-shutdown \
         downgrade-1.0 force-response-1.0
</VirtualHost>
```
> Tomcat 연동(AJP)
- /usr/local/apache2/conf/workers.properties
```
// line 1.
worker.list=gasapp-cstalk

// line 121.
worker.gasapp-cstalk.type=ajp13
worker.gasapp-cstalk.host=127.0.0.1
worker.gasapp-cstalk.port=8112
worker.gasapp-cstalk.socket_timeout=60
worker.gasapp-cstalk.connection_pool_timeout=600
worker.gasapp-cstalk.retries=1
```

> 인증서
- /data/project/gasapp-cstalk/apache/conf/httpd-ssl.conf
```
SSLEngine on 
SSLCertificateFile "/usr/local/apache2/conf/extra/ssl/STAR.gasapp.co.kr.crt" 
SSLCertificateKeyFile "/usr/local/apache2/conf/extra/ssl/STAR.gasapp.co.kr.key" 
SSLCertificateChainFile "/usr/local/apache2/conf/extra/ssl/chainca.crt" 
```

> 설정파일 테스트
```
[gasapp@gasapp-dev bin]$ ./apachectl configtest
Syntax OK
```

## Apache 서비스 제어
> 서비스 상태확인
- /usr/local/apache2/bin/apachectl
```
[gasapp@gasapp-dev bin]$ ./apachectl status
httpd (pid 12302) already running
```
> 서비스 중지
```
# ./apachectl stop
```

> 서비스 구동
```
# ./apachectl start
```

> 서비스 재구동
```
# ./apachectl restart   // 강제 재시작.
# ./apachectl graceful  // 자동으로 configtest 과 같이 설정파일을 검사하며, 이전 작업이 종료된 후 재시작 된다.
```


[< 서버환경 구성으로 돌아가기](./env.md)