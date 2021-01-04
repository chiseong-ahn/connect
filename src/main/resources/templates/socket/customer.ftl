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
    <div class="container" id="app" v-cloak>
        <div class="col-md-3">
	    	<h4>고객용 채팅</h4>
			<select v-model="companyId">
				<option value="1">서울도시가스</option>
				<option value="2">인천도시가스</option>
			</select>
			<input type="text" v-model="gasappMemberNumber" />
			<button type="button" @click="connect">연결</button>
			<button type="button" @click="review">리뷰 메기기</button>
	    	<ul class="list-group">
	            <li class="list-group-item" v-for="obj in messages">
	            	<strong>[시스템:{{obj.isSystemMessage}}, 상담사:{{obj.isEmployee}}, 고객:{{obj.isCustomer}}, 작성자:{{obj.speakerName}}]</strong>
	                <template v-if="obj.messageType == 1">
	                	<a :href="getImageUrl(obj.messageDetail)" target="_blank"><img :src="getThumbnailUrl(obj.messageDetail)" /></a>
	                </template>
	                <template v-else>
	                	[읽음카운트 : {{obj.noReadCount}}] <template v-if="obj.noReadCount > 0"><button type="button" @click="deleteMessage(obj.id)">[삭제]</button></template>
	                	<br />&gt;&gt; {{obj.message}} {{obj.createDate}}
	                </template>
	            </li>
	       	</ul>
	       	<div>
		       	<div>
			       	<div>
			            <textarea class="form-control" v-model="message" placeholder="내용을 입력하세요."></textarea>
			            <div class="input-group-append">
			                <button class="btn btn-primary" type="button" @click="sendDialog()">보내기</button>
			            </div>
			       	</div>
				</div>
			</div>
	    </div>
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    <script src="/webjars/sockjs-client/1.0.0/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    
    <script>
        var vm = new Vue({
            el: '#app',
            data: {
            	socket: {
            		//host: "//localhost",
            		host: "//cstalk-local.gasapp.co.kr",
            		//host: "//cstalk-dev.gasapp.co.kr",
            		
            		port: 8080,
            		//port: 80,
            		
            		ws: undefined,							// 웹소켓 객체.
            		subscribe: undefined,					// 조인(구독) 객체.
					subscribePrivate: undefined,					// 조인(구독) 객체.
            		connectEndPoint: "/ws",			// 연결 end point
	            	sendEndPoint: '/pub/socket/message',	// 메세지 전송 end point
	            	roomPrefix: '/sub/socket/room/',			// 조인(구독)룸 end point의 prefix
	            	sessionEndPoint: '/user/session/message',	// 개인메세지를 받을 구독주소.
	            	roomId: '',								// 현재 조인(구독)중인 방
            	},
            	companyId: "1",								// 가스사 Id
            	header: {},				// 공통 header
            	profile: {},
            	messages: [],			// 대화메세지
				message: "",			// 작성하는 메세지
				imgPrefix: 'https://cstalk-dev.gasapp.co.kr/attach/',
				gasappMemberNumber: 3825,
				secretKey: '$CSTALK#_20210104'
            },
            created() {
            },
            
            mounted() {
            },
            
            methods: {
            	/**************************************************************
                * 웹소켓 연결
                **************************************************************/
                connect: function(){
                
                	console.log('connect!');
                
                	// SockJs 객체 초기화.
                	var connectUrl = "";
                	if(this.socket.port == 80){
                		connectUrl = this.socket.host + this.socket.connectEndPoint;
                	}else{
                		connectUrl = this.socket.host + ":" + this.socket.port + this.socket.connectEndPoint;
                	}
                	
                	var sock = new SockJS(connectUrl);
                	
                	// Stomp 객체 초기화.
                	this.socket.ws = Stomp.over(sock);
                	
                	// 웹소켓 연결.
                	this.socket.ws.connect(
						{"companyId": this.companyId, "gasappMemberNumber": this.gasappMemberNumber, "secretKey": this.secretKey},		// 연결시 전달할 헤더.
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
                	this.join(this.socket.lobbyName);
                },
                

                /**************************************************************
                * 웹소켓 연결실패 처리.
                **************************************************************/
                connectFailCallback: function(errorMessage){
                	alert("연결하는데 실패하였습니다.\n reason - " + errorMessage);
                	console.log('Error : ' + errorMessage);
                },
                
                /**************************************************************
                * 조인(구독).
                **************************************************************/
                join: function(roomId){
                	
            		// 개인메시지를 받을 구독.
            		this.socket.subscribePrivate = this.socket.ws.subscribe(
            			this.socket.sessionEndPoint, 
            			this.receiveMessage
            		);
                	
                	// 조인방 이름 생성.(예, /sub/chat/room/93)
                	var uri = this.socket.roomPrefix + this.socket.roomId;
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
					//console.log(payload);
					
					var target = payload.target;
					
					var eventName = payload.eventName;		// 이벤트 구분.
					var roomId = payload.roomId;			// 룸 아이디.
					var data = payload.data;				// 각 메세지에 대한 기타 정보데이터.
					
					// 각 이벤트에 따른 처리.
					switch(eventName){
					
						case "LOGINED" : // 로그인 됨.
							break;
							
					
						case "JOINED" :	// 조인완료 수신.
							// todo
							// 구독
							this.socket.roomId = data.profile.roomId;
		                	this.socket.subscribe = this.socket.ws.subscribe(
		                		this.socket.roomPrefix + this.socket.roomId, // 구독 URI
		                		this.receiveMessage, 				// 구독 성공시 호출되는 함수.	 
		                	);
							
		                	break;
							
						case "ROOM_DETAIL" : 	// 방 상세정보 수신.
							// todo
							break;
							
						case "ASSIGNED" :		// 상담사 배정완료 수신.
							// todo
							// this.findRooms();
							// this.join(roomId);
							
							break;
							
						case "READ_MESSAGE" :	// 메시지 읽음.
							console.log('READ_MESSAGE 처리')
							
							var startId = data.startId;
							var endId = data.endId;
								
							filteredMessages = this.messages.filter(message => message.id >= startId && message.id <= endId);
							if(filteredMessages.length >= 1){
								for(var i=0; i<filteredMessages.length; i++){
									filteredMessages[i].noReadCount = 0;
								}
							}  
							
							
							break;
						
						case "MESSAGE" : 		//	대화 메세지 수신.
							// todo
							
							// 메시지 노출.
							this.messages.push(data.message);
							
							// 고객의 메세지일 경우에 읽음 메시지 전송.
							if(data.message.isEmployee == 1){
								// 읽음 알림.
								data = {
			                		speakerId: this.profile.speakerId,
			                		startId: data.message.id,
			                		endId: data.message.id
			                	}
			            		this.sendMessage("READ_MESSAGE", data);
							}
							
							break;
						
						case "MESSAGE_LIST" : 		// 이전대화 목록 수신.
							// todo
							messages = data.messages;
							messages.reverse()		// 배열 뒤집기.
							this.messages = messages;
							break;
							
						case "DELETE_MESSAGE" :		// 메시지 삭제.
							
							if(data.success == true){
								var id = data.id;
								var index = this.messages.findIndex(function(message) {
									return message.id === id
								})
								console.log('index : ' + index);
								
								if(index > -1){
									this.messages.splice(index, 1);
								}
							}
							
						case "END" :			// 상담 종료 수신.
							// todo
							this.findRooms(); 
							this.join(this.socket.lobbyName);
							
							
							break;
					
						case "RELOAD" :			// 상담목록 갱신요청 수신.
							// todo
							this.findRooms();
							
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
                		'data': data
                	}
                	this.socket.ws.send(uri, header, JSON.stringify(payload));
                },
                
                end: function(){
                	if(confirm("상담을 종료하시겠습니까?")){
                		this.sendMessage("END", {})
                	}
                },
                
                // 대화메세지 보내기
                sendDialog: function(){
                	data = {
                		messageType: 0,
                		message: this.message,
                		messageDetail: '',
                		templateId: '1',
                	}
            		this.sendMessage("MESSAGE", data);
            		this.message = '';
                },
                
                // 메시지 삭제
				deleteMessage: function(id){
					if(confirm('메시지를 삭제하시겠습니까?')){
						data = {
	                		id: id
	                	}
	            		this.sendMessage("DELETE_MESSAGE", data);
					}
					
				},
				
				// 상담종료.
				closeRoom: function(roomId){
					if(confirm('상담을 종료하시겠습니까?')){
						/*
						var uri = '/api/room/' + roomId + '/closeRoom';
						axios.post(uri, {}, this.header).then(response => {
							// prevent html, allow json array
							room = response.data;
							console.log(room);
							
							this.findRooms();
							
							
						}, function(e){
							console.log("error : " + e.message);
						});	
						*/
						
						data = {roomId: roomId}
	            		this.sendMessage("END", data);
					}
				},
				
				getImageUrl: function(url){
            		return this.imgPrefix + url;
            	},
				
				getThumbnailUrl: function(url){
            		console.log(url);
            		var thumbUrl = '';
            		
            		if(url){
            			_div = url.lastIndexOf('/');
            			_front = url.substring(0, _div);
            			_rear = url.substring((_div+1), url.length);
            			thumbUrl = this.imgPrefix + _front + '/thumb_' + _rear;
            		}
            		
            		return thumbUrl;
            	},
            	
            	review: function(){
            		if(confirm('리뷰를 남기시겠습니까?')){
						data = {
	                		reviewScore: 2
	                	}
	            		this.sendMessage("REVIEW", data);
					}
            	}
                
            }
        });
    </script>
  </body>
</html>