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

// 주석 해제 - line 106.
LoadModule proxy_balancer_module modules/mod_proxy_balancer.so

// 주석 해제 - line 143.
LoadModule slotmem_shm_module modules/mod_slotmem_shm.so

// 주석 해제 - line 147.
LoadModule lbmethod_byrequests_module modules/mod_lbmethod_byrequests.so
```

> 가상 호스트, Socket proxy, 로드밸런싱 설정.
- /data/project/gasapp-cstalk/apache/conf/httpd-ssl.conf

> Tomcat 연동(AJP)
- /usr/local/apache2/conf/workers.properties
```
// line 1.
worker.list=gasapp-cstalk1,gasapp-cstalk2,loadbalancer-cstalk

// line 4. 로드밸런싱 설정.
worker.loadbalancer-cstalk.type=lb
worker.loadbalancer-cstalk.balance_workers=gasapp-cstalk1,gasapp-cstalk2

// line 121.
worker.gasapp-cstalk1.type=ajp13
worker.gasapp-cstalk1.host=127.0.0.1
worker.gasapp-cstalk1.port=8112
worker.gasapp-cstalk1.socket_timeout=60
worker.gasapp-cstalk1.connection_pool_timeout=600
worker.gasapp-cstalk1.retries=1

worker.gasapp-cstalk2.type=ajp13
worker.gasapp-cstalk2.host=127.0.0.1
worker.gasapp-cstalk2.port=8122
worker.gasapp-cstalk2.socket_timeout=60
worker.gasapp-cstalk2.connection_pool_timeout=600
worker.gasapp-cstalk2.retries=1
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
[gasapp@gasapp-dev bin]$ ./apachectl -t
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