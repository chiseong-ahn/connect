# 웹소켓

## Websocket + STOMP + SockJs + Redis(Pub/Sub)
- 참고 https://stomp.github.io

## 서버환경
| 서버 | Host | End point |
|:---:|---|---|
| 개발 | cstalk-dev.gasapp.co.kr | /ws |
| 스테이지 | cstalk-stage.gasapp.co.kr | /ws |
| 운영 | cstalk.gasapp.co.kr | /ws |

## Frontend
### 1. 연결 | Connect
```javascript
// SockJs 객체 초기화.
var connectUrl = "http://[도메인]:[포트]/[엔드포인트]";
// var connectUrl = "http://cstalk-dev.gasapp.co.kr/ws"

// sockJs 객체 생성.
var sock = new SockJS(connectUrl);

// 웹소켓 객체 초기화.
this.socket.ws = Stomp.over(sock);

// 멤버(상담사) 연결.
// token(인증토큰) - 인증 API를 통해 취득.
this.socket.ws.connect(
    {"Authorization": "Bearer " + token},		// 연결시 전달할 헤더.
    fnSuccessCallback, 					// 성공시 홀출되는 함수.
    fnFailCallback					// 실패시 호출되는 함수.
);

// 고객(가스앱 회원) 연결.
// companyId - 가스사 관리번호(서울-1, 인천-2)
// gasappMemberNumber - 가스앱 고객 관리번호
// secretKey - 인증에 필요한 임의 키. [key_YYYYMMDD]
this.socket.ws.connect(
    {"companyId": "1", "gasappMemberNumber": 3825, "secretKey": "$CSTALK#_20210104"},	// 연결시 전달할 헤더.
    fnSuccessCallback, 			// 성공시 홀출되는 함수.
    fnFailCallback					// 실패시 호출되는 함수.
);
```

### 2. 구독 | Subscribe
- 개인 채널과 대기룸 채널은 최초 1회 연결.
- 채팅룸 채널은 구독하여 대화하고 채팅방을 나갈경우 구독해제한다.
```javascript
// 개인메시지 수신전용 구독(Session 기반)
this.socket.ws.subscribe(
    "/user/session/message",     // 구독채널명
    fnReceiveMessage,           // 메세지 수신함수
    headers                     // 구독시 전송할 헤더 
);

// 대기 룸 구독.
this.socket.ws.subscribe(
    "/sub/socket/room/LOBBY[회사번호]",     // 구독채널명
    fnReceiveMessage,           	 // 메세지 수신함수
    headers                     	 // 구독시 전송할 헤더 
);

// 채팅 룸 구독.(고객과의 대화방)
this.socket.subscribe = this.socket.ws.subscribe(
    "/sub/socket/room/[룸 번호]",  // 구독채널명
    fnReceiveMessage,           // 메세지 수신함수
    headers                     // 구독시 전송할 헤더 
);
```

### 3. 메세지 전송 | Send
```javascript
this.socket.ws.send(
    "/pub/socket/message",   // 메시지 발송URI
    {},                     // 전송할 헤더
    payload                 // 전송할 데이터
)
```

### 4. 메세지 수신 | Receive
```javascript
fnReceiveMessage(recvData){
    console.log(recvData);      // 수신된 메시지 객체.
}
```

### 5. 구독해제 | Unsubscribe
```javascript
this.socket.subscribe.unsubscribe();
```

### 6. 연결해제 | Disconnect
```javascript
this.socket.ws.disconnect();
```

## 수신 메시지 유형
### 1. 룸에 조인 됨.
```json
{
  "eventName" : "JOINED",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {
    "profile" : {
      "id" : 1,
      "companyId" : "1",
      "roomId" : "147",
      "isAdmin" : 0,
      "isMember" : 1,
      "isCustomer" : 0,
      "loginName" : null,
      "speakerId" : 177,
      "name" : "서울도시가스",
      "sessionId" : "crlmngar",
      "companyUseConfigJson" : null
    }
  }
}
```

### 2. 메시지 읽음.
```json
{
  "eventName" : "READ_MESSAGE",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {
    "endId" : 3096,
    "startId" : 3094,
    "speakerId" : 211,
    "roomId" : "147"
  }
}
```

### 3. 메시지 목록 수신.
```json
{
  "eventName" : "MESSAGE_LIST",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {
    "messages" : [ {
      "id" : 3093,
      "joinMessageId" : 3093,
      "createDate" : "2020-12-18 15:31:37",
      "updateDate" : "2020-12-18 15:33:24",
      "speakerId" : 211,
      "speakerName" : "이유진",
      "roomId" : 147,
      "roomName" : "안치성",
      "companyId" : "1",
      "messageType" : 0,
      "noReadCount" : 0,
      "isSystemMessage" : 0,
      "message" : "1111",
      "messageAdminType" : 0,
      "isEmployee" : 0,
      "isCustomer" : 1,
      "isOnline" : 0,
      "messageDetail" : ""
    }]
  }
}
```

### 4. 메시지 수신.
```json
{
  "eventName" : "MESSAGE",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {
    "message" : {
      "id" : 3097,
      "joinMessageId" : 0,
      "createDate" : "2020-12-18 15:38:37",
      "updateDate" : "2020-12-18 15:38:37",
      "speakerId" : 211,
      "speakerName" : "이유진",
      "roomId" : 147,
      "roomName" : "안치성",
      "companyId" : "1",
      "messageType" : 0,
      "noReadCount" : 1,
      "isSystemMessage" : 0,
      "message" : "고객이 발송한 메시지 내용",
      "messageAdminType" : 0,
      "isEmployee" : 0,
      "isCustomer" : 1,
      "isOnline" : 1,
      "messageDetail" : ""
    }
  }
}
```

### 5. 메시지 삭제 됨.
```json
{
  "eventName" : "DELETE_MESSAGE",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {
    "success" : true,
    "id" : 3098		// 삭제된 메시지 id
  }
}
```

### 6. 상담 종료.
```json
{
  "eventName" : "END",
  "companyId" : "1",
  "roomId" : "147",
  "data" : {}
}
```

### 7. 룸 목록 갱신 요쳥.
```json
{
  "eventName" : "RELOAD",
  "companyId" : "1"
}
```

### 8. 고객만족도 등록결과.
```json
{
  "eventName": "REVIEW",
  "companyId": "1",
  "roomId": "153",
  "target": "SELF",
  "data": {
    "success": true // true-성공, false-실패
  }
}
```

### 8. 에러 발송.
```json
{
    "eventName" : "ERROR",
    "companyId" : "1",
    "roomId" : "147",
    "data" : {
        "reason" : "서버에 연결할 수 없습니다."	// 에러 발생 사유
    }
}
```


## 발신 메시지 유형
### 1. 시작메시지 요청.
```json
{
  "eventName":"START_MESSAGE",
  "data":{}
}
```

### 2. 메시지 발송.
```json
{
  "eventName":"MESSAGE",
  "data":{
	"messageType":0,	// 메시지유형(0-일반텍스트, 1-이미지)
	"message":"1111",	// 메시지 내용
	"messageDetail":""	// 메시지 추가내용(이미지일 경우 이미지경로)
  }
}
```

### 3. 메시지 더보기.
```json
{
    "eventName":"MESSAGE_LIST",
    "data":{
	"messageAdminType":0,
	"startId":"31"		// 더보기 할 메시지 시작번호
    }
}
```

### 4. 메시지 읽음.
```json
{
    "eventName": "READ_MESSAGE",
    "data": {
        "startId": 3081,	// 읽은메시지의 시작번호
        "endId": 3083		// 읽은메시지의 마지막번호
    }
}
```

### 5. 메시지 삭제
```json
{
    "eventName": "DELETE_MESSAGE",
    "data": {
        "id": 10	// 삭제할 메시지 id
    }
}
```

### 6. 상담 종료.
```json
{
    "eventName": "END",
    "data": {}
}
```

### 7. 리뷰 남기기.
```json
{
  "eventName":"REVIEW",
  "data":{
    "reviewScore":3               // 리뷰 점수.
  }
}
```

[< 목록으로 돌아가기](manual.md)
