# 웹소켓
## Websocket + STOMP + SockJs
- 참고 https://stomp.github.io

### 연결 | Connect
```
// SockJs 객체 초기화.
var connectUrl = "http://[도메인]:[포트]/[엔드포인트]";
var sock = new SockJS(connectUrl);

// Stomp 객체 초기화.
socket = Stomp.over(sock);

// 웹소켓 연결.
socket.connect(
    {},		// 연결시 전달할 헤더.
    fnSuccessCallback, 					// 성공시 홀출되는 함수.
    fnFailCallback						// 실패시 호출되는 함수.
);
```

### 구독 | Subscribe
```
socket.subscribe(
    "sub/socket/1",        // 구독채널명
    fnReceiveMessage,      // 메세지 수신함수
    headers                // 구독시 전송할 헤더 
);
```

### 메세지 전송 | Send
```
socket.send(
    "pub/socket/message",   // 메시지 발송URI
    headers,                // 전송할 헤더
    payload                 // 전송할 데이터
)
```

### 메세지 수신 | Receive
```
fnReceiveMessage(recvData){
    console.log(recvData);      // 수신된 메시지 객체.
}
```

### 구독해제 | Unsubscribe
```
socket.subscribe.unsubscribe();
```

### 연결해제 | Disconnect
```
socket.disconnect();
```