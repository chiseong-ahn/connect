<!doctype html>
<html lang="en">
  <head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
      [v-cloak] {
          display: none;
      }
      	.container {
		    max-width: 100% !important;
		}
    </style>
  </head>
  <body>
    
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    <script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    
    <script>
        var vm = new Vue({
            el: '#app',
            data: {
            	socket: {
            		ws: undefined,							// 웹소켓 객체.
            		subscribe: undefined,					// 조인(구독) 객체.
            		connectEndPoint: "http://localhost:8080/ws-stomp",			// 연결 end point
	            	sendEndPoint: '/pub/socket/message',	// 메세지 전송 end point
	            	roomPrefix: '/sub/chat/room/',			// 조인(구독)룸 end point의 prefix
	            	header: {},								// 공통 header
	            	lobbyName: 'LOBBY1',					// 서울도시가스 조인(구독) 기본룸 Id (인천도시가스 - LOBBY2)
	            	roomId: '',								// 현재 조인(구독)중인 방
            	},
            	companyId: 1,								// 가스사 Id
            	token: 'dfdfjsdfslfesjdlksfs',				// 인증토큰
				
            },
            created() {
            },
            
            mounted() {
            	// 웹소켓 연결.
            	this.connect();
            },
            
            methods: {
            
            	/**************************************************************
                * 웹소켓 연결
                **************************************************************/
                connect: function(){
                
                	// SockJs 객체 초기화.
                	var sock = new SockJS(this.socket.connectEndPoint);
                	
                	// Stomp 객체 초기화.
                	this.socket.ws = Stomp.over(sock);
                	
                	// 웹소켓 연결.
                	this.socket.ws.connect(
						{"Authorization": "Bearer " + this.token},		// 연결시 전달할 헤더.
						this.connectSuccessCallback, 					// 성공시 홀출되는 함수.
						this.connectFailCallback						// 실패시 호출되는 함수.
					);
                },
                
                
                /**************************************************************
                * 웹소켓 닫기
                **************************************************************/
                disconnect: function(){
                	if(this.socket.ws){
                		this.socket.ws.disconnect();						// 웹소켓 닫기
                	}
                },
                
                
                /**************************************************************
                * 웹소켓 연결성공 처리.
                **************************************************************/
                connectSuccessCallback: function(){
                	// 기본룸 조인.
                	// this.join(this.socket.lobbyName);
                	
                	// 기본룸 조인해제.
                	// this.unjoin();
                	
                	// 일반룸 조인.
                	this.join("111");
                	
                	// 일반룸 조인해제.
                	// this.unjoin();
                	
                	// 연결 해제.
                	// this.disconnect();
                },
                

                /**************************************************************
                * 웹소켓 연결실패 처리.
                **************************************************************/
                connectFailCallback: function(errorMessage){
                	alert("연결하는데 실패하였습니다.");
                	console.log('Error : ' + errorMessage);
                },
                
                
                /**************************************************************
                * 조인(구독).
                **************************************************************/
                join: function(roomId){
                	this.socket.roomId = roomId;
                	
                	uri = this.socket.roomPrefix + roomId;
                	
                	// 구독
                	this.socket.subscribe = this.socket.ws.subscribe(
                		uri, 								// 구독 URI
                		this.receiveMessage, 				// 구독 성공시 호출되는 함수.	 
                		headers = {'token': this.token}		// 구독 요청시 전달하는 헤더.
                	);
                },
                

                /**************************************************************
                * 조인(구독) 해제.
                **************************************************************/
                unjoin: function(){
                	if(this.socket.subscribe){
                		this.socket.subscribe.unsubscribe(); // 구독 해제.
                	}
                },
                
                
                /**************************************************************
                * 서버로 부터 메세지 수신.
                **************************************************************/
                receiveMessage: function(recvData){
                	var payload = JSON.parse(recvData.body);
					// console.log(payload);
					
					var eventName = payload.eventName;		// 이벤트 구분.
					var roomId = payload.roomId;			// 룸 아이디.
					var data = payload.data;				// 각 메세지에 대한 기타 정보데이터.
					
					// 각 이벤트에 따른 처리.
					switch(eventName){
						case "JOINED" :	// 조인완료 수신.
							// todo
							// 메세지 전송.
		                	payload = {etc: 'test'}
		            		this.sendMessage("MESSAGE", payload);
            		
							break;
							
						case "ROOM_DETAIL" : 	// 방 상세정보 수신.
							// todo
							break;
							
						case "MESSAGE" : 		//	대화 메세지 수신.
							// todo
							break;
						
						case "MESSAGES" : 		// 이전대화 목록 수신.
							// todo
							break;
						
						case "END" :			// 상담 종료 수신.
							// todo
							break;
					
						case "RELOAD" :			// 상담목록 갱신요청 수신.
							// todo
							break;
							
						case "ERROR" :			// 에러 발생정보 수신.
							// todo
							break;
					}
                },
                
                /**************************************************************
                * 서버로 메세지 발송.
                **************************************************************/
                sendMessage: function(eventName, data){
                	uri = this.socket.sendEndPoint,
                	header = {},
                	payload = {
                		'eventName': eventName,
                		'companyId': this.companyId,
                		'roomId': this.socket.roomId,
                		'token': this.token,
                		'data': data
                	}
                	this.socket.ws.send(uri, header, JSON.stringify(payload));
                },
                
                
                
            }
        });
    </script>
  </body>
</html>