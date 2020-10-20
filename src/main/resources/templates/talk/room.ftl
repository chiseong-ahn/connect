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
        <div class="row">
            <div class="col-md-6">
            	<span>로그인 : {{user.empno}}</span><br />
            	<span>선택된 스페이스 : {{roomId}}</span>
            </div>
            <div class="col-md-6 text-right">
                <a class="btn btn-primary btn-sm" @click="logout">로그아웃</a>
            </div>
        </div>
        <div class="row">
        	<div class="col-md-2">
        		<ul>
        			<li><a href="/page/main" class="btn btn-default" type="button">상담채팅</a></li>
        			<li><a href="/page/template" class="btn btn-default" type="button">답변템플릿</a></li>
        			<li><a href="/page/helper" class="btn btn-default" type="button">답변도우미</a></li>
        			<li><a href="/admin/emp" class="btn btn-default" type="button">관리자메뉴</a></li>
        		</ul>
        	</div>
        	<div class="col-md-3">
		        <h4>상담대기</h4>
		        <span><button type="button" class="btn btn-primary">대기상담 가져오기 ({{readyCount}}건)</button></span>
		        <ul class="list-group">
		            <li class="list-group-item list-group-item-action" v-for="obj in chatrooms" v-bind:key="obj.id" v-on:click="join(obj.id)">
		            	<dt>[{{obj.id}}] 
		            		<span v-if="obj.empname == null" style="color:red">{{obj.customer}}</span>
		            		<span v-else>{{obj.customer}}</span>
		            		<span class="badge badge-info badge-pill">{{obj.empname}}</span>
		            		<span v-if="obj.isonline == true" class="badge badge-primary badge-pill">Online</span>
		            	</dt>
		                <dd>{{obj.msg}}</dd>
		            </li>
		        </ul>
		    </div>
		    <div class="col-md-3">
		    	<h4>상담채팅 - {{selectedRoom.customer}}</h4>
		    	<span v-if="roomId != baseroom"><button type="button" @click="leave()">채팅나가기</button></span>
		    	<span v-if="roomId != baseroom"><button type="button" @click="end()">상담종료</button></span>
		    	<ul  v-if="roomId != baseroom" class="list-group">
		            <li class="list-group-item" v-for="obj in messages">
		                <strong>[시스템:{{obj.sysmsg}}, 상담사:{{obj.isemp}}, 작성자:{{obj.speaker}}]</strong>
		                <br />&gt;&gt; {{obj.msg}}</a>
		            </li>
		       	</ul>
		       	<div v-if="roomId != baseroom">
		       		<div class="input-group-prepend">
		                <label class="input-group-text">내용</label>
		            </div>
		            <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage('MESSAGE')">
		            <div class="input-group-append">
		                <button class="btn btn-primary" type="button" @click="sendMessage('MESSAGE')">보내기</button>
		            </div>
		       	</div>
		    </div>
		    <div class="col-md-3">
		    	<h4>계약정보 </h4>
		    	<ul class="list-group">
		            <li class="list-group-item" v-for="obj in contracts">
		                {{obj.createdate}} - {{obj.workdate}}</a>
		            </li>
		       	</ul>
		    	<h4>이전 상담목록</h4>
		    	<ul class="list-group">
		            <li class="list-group-item" v-for="(obj, index) in histories" v-bind:key="obj.id" @click="getHistorySpeaks(obj.space, obj.startid, obj.endid)">
		                {{obj.createdate}} - {{obj.workdate}}
		                <ul v-if="obj.showSpeaks == true">
		            		<li v-for="(speak, idx) in obj.speaks">
		            			<strong>[시스템:{{speak.sysmsg}}, 상담사:{{speak.isemp}},작성자:{{speak.speaker}}]</strong>
		            			<br>>> {{speak.message}}
		            		</li>
		            	</ul>
		            </li>
		       	</ul>
		    </div>
		</div>
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
            	wsUri: "/ws-stomp",
            	ws: undefined,	//  웹소켓 객체.
            	user: {
            		emp: 0,
					cid: 0,
					speaker: 0,
					name: '',
					auth: 0,
					empno: '',
            		userno: 0
            	},		// 사용자 정보.
            	appid: 0,
            	baseroom: 'LOBBY',
            	readyCount: 0,
                chatrooms: [],
                messages: [],
                contracts: [],
                histories: [],
                roomId: '',
                selectedRoom: {},
                header: {},
                token: ''
            },
            created() {
				// 인증정보 조회.
            	this.getUserInfo(); 
                
                // 초기화
				this.init();
            },
            methods: {

				// 초기화
				init: function(){

					// 기존 연결 해제.
					this.disconnect();

					// 기존 데이터 초기화.
                	this.messages = [];
	                this.contracts = [];
	                this.histories = [];
	                this.roomName = '';
	                this.message = '';
	                this.selectedRoom = {};

					// 기본 스페이스(로비) 입장.
	            	this.baseJoin();
				},

				// 기본 스페이스 연결
				baseJoin: function(){
					this.roomId = this.baseroom;
	            	this.connect();
				},

				// 웹소켓 연결 및 스페이스 구독 설정.
                connect: function(){
                	var _this = this;
                	
                	// SockJs 객체 초기화.
                	var sock = new SockJS(this.wsUri);
                	
                	// Stomp 객체 초기화.
                	this.ws = Stomp.over(sock);
                	
                	// 웹소켓 연결.
                	this.ws.connect(
						{"token": _this.token}, 
						function(frame) {
							if(_this.roomId != ''){

								// 스페이스(room) 구독.
								_this.ws.subscribe(
									"/sub/chat/room/"+ _this.roomId, 
									function(message) {
										// 수신메세지 처리.
										var recv = JSON.parse(message.body);
										_this.recvMessage(recv);		
									}, 
									headers = {'token': _this.token}
								);
							}
	                    
	                	}, function(error) {
	                    	alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
	                    	location.href="/";
	                	}
					);
                },

				// 소켓 연결해제.
                disconnect: function(){
                	if(this.ws != undefined){
                		if(this.ws.connected){
                			this.ws.disconnect();
	                	}
                	}
                },
            
            	// 스페이스 목록 조회.
                findAllRoom: function() {
                	//var uri = '/chat/rooms';
                	var uri = '/talk/spaces';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
							this.chatrooms = response.data.list;
						}
                    }, function(e){
                    	
                    });
                },
               
				// 스페이스 입장(선택)
                join: function(roomId) {
                
                	rooms = this.chatrooms.filter(room => room.id == roomId);
                	if(rooms.length == 1){
                		this.selectedRoom = rooms[0];
                	}
                    
                    this.roomId = roomId;	// 스페이스 설정.
                    this.connect();			// 스페이스 소켓 연결 및 구독.
                },
                
                // 스페이스 나가기
                leave: function(){
                	var data = { 
						cid : this.user.cid,
						appid : this.appid,
						roomId : this.roomId, 
						message : ''
					}
					// 상담종료 메세지 전송.
					this.ws.send('/pub/talk/leave', {"token":this.token}, JSON.stringify(data));

					// 초기화
					this.init();
                },
                
                // 상담종료.
                end: function(){
                	var data = {
                		cid : this.user.cid,
                		appid : this.appid,
                		roomId : this.roomId, 
                		message : ''
                	}
					// 상담종료 메세지 전송.
                    this.ws.send('/pub/talk/end', {"token":this.token}, JSON.stringify(data));

					// 초기화
                    this.init();
                },
                
                // 대화 메세지 조회 
				/*
                getMessages: function(){
            		var _this = this;
            		
            		var uri = '/talk/spaces/' + this.roomId + '/speaks';
            		axios.get(uri, _this.header).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				//console.log(response.data.list);
            				_this.messages = response.data.list; 
            			}
            		}, function(err){
            			console.log(err);
            		});
            	},
				*/
            	            	
            	// 메세지 발송
                sendMessage: function() {               	
                	var data = {
                		cid: this.user.cid,
                		appid: this.appid,
                		roomId:this.roomId, 
                		message: this.message
                	}
                	
                    this.ws.send('/pub/talk/message', {"token":this.token}, JSON.stringify(data));
					this.message = ''
                },
                
                // 수신된 메세지노출  
                recvMessage: function(recvData) {
                	var type = recvData.type;		// 메세지 유형
                    

                    switch(type){
                    	case "JOIN" :		// 상담에 조인됨.
                    		break;
                    		
                    	case "SPEAKS" :		// 해당 상담의 대화내용 수신.
							if(recvData.data){
								this.messages = recvData.data.speaks; 
							}
                    		break;
                    		
                    	case "READSEMP" :	// 상담사가 이전글을 읽음.
                    		break;
                    		 
                    	case "MESSAGE" :	// 대화 메세지 수신.
							if(recvData.data){
								this.messages.push(recvData.data);
							}
                    		break;
                    		
                    	case "PREHISTORY" : // 이전 대화내용 수신.
							if(recvData.data){
								list = recvData.data.histories; 
								// 필수 기본값 설정.
								for(var i=0; i<list.length; i++){
									list[i].speaks = [];
									list[i].showSpeaks = false;
								}
								this.histories = list;							}
                    		break;
							
						case "RELOAD" :		// 상담목록 갱신 요청.
							break;
							
						case "END" : 		// 상담 종료.
							break;
                    
                    }
                },

                // 이전 상담목록 조회 
               	getHistory: function(){
               		var _this = this;
            		var uri = '/talk/spaces/' + this.roomId + '/history';
            		axios.get(uri, this.header).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				//console.log('getHistory list >> ');
            				//console.log(response.data.list);
            				
            				var list = response.data.list;
            				
            				// 필수 기본값 설정.
            				for(var i=0; i<list.length; i++){
            					list[i].speaks = [];
            					list[i].showSpeaks = false;
            				}
            				
            				_this.histories = list; 
            			}
            		}, function(err){
            			console.log(err);
            		});
               	},
               	
               	// 이전 상담 상세내용 조회 
               	getHistorySpeaks: function(roomId, startId, endId){
               		var uri = '/talk/spaces/' + roomId + '/history/speaks?startId=' + startId + '&endId=' + endId;
            		
            		var history = this.histories.filter(history => history.startid == startId && history.endid == endId)[0]
            		if(history.showSpeaks == true){
            			history.speaks = [];
            			history.showSpeaks = false;
            		}else{
	            		// 필수 기본값 설정.
	    				for(var i=0; i<this.histories.length; i++){
	    					this.histories[i].speaks = [];
	    					this.histories[i].showSpeaks = false;
	    				}
    				
						//console.log('header : ' + JSON.stringify(this.header.headers))
            			axios.get(uri, this.header).then(response => {
	            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
	            				history.speaks = response.data.list;
	            				history.showSpeaks = true;
	            			}
	            		}, function(err){
	            			console.log(err);
	            		});
            		}            		
               	},

				// 사용자 정보 조회.
            	getUserInfo: function() {
            		this.token = localStorage.accessToken;
            		
            		this.header = {
            			headers: {'Authorization' : 'Bearer ' + this.token}
            		}
            		            		
            		axios.get('/auth/user', this.header).then(response => {
            			if(response.data){
            				this.user = response.data;
            				console.log(JSON.stringify(this.user));
            				
            				this.findAllRoom();
            			}else{
            			
            				// 인증정보가 존재하지 않을 경우.
            				document.location.href = '/';
            			}
	                	
		            }, function(e){
		            	document.location.href = '/';
		            });
            	},

				// 로그아웃.
            	logout: function(){
            		localStorage.removeItem("accessToken");
            		document.location.href = "/";
            	},
            }
        });
    </script>
  </body>
</html>