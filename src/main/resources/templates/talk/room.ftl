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
            	<span>선택된 스페이스 : {{roomId}}</span><br />
            	<span>Token : {{token}}</span><br />
            	<span>Socket : </span><Br />
            </div>
            <div class="col-md-6 text-right">
                <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
            </div>
        </div>
        <div class="row">
        	<div class="col-md-2">
        		<ul>
        			<li><a href="/page/main" class="btn btn-default" type="button">상담채팅</a></li>
        			<li><a href="/page/template" class="btn btn-default" type="button">답변템플릿</a></li>
        			<li><a href="/page/helper" class="btn btn-default" type="button">답변도우미</a></li>
        			<li><a href="/page/admin" class="btn btn-default" type="button">관리자메뉴</a></li>
        		</ul>
        	</div>
        	<div class="col-md-3">
		        <h4>상담대기</h4>
		        <span><button type="button" class="btn btn-primary">대기상담 가져오기 ({{readyCount}}건)</button></span>
		        <ul class="list-group">
		            <li class="list-group-item list-group-item-action" v-for="obj in chatrooms" v-bind:key="obj.id" v-on:click="enterRoom(obj.id)">
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
		    	<ul class="list-group">
		            <li class="list-group-item" v-for="obj in messages">
		                <strong>[시스템:{{obj.sysmsg}}, 상담사:{{obj.isemp}}, 작성자:{{obj.speaker}}]</strong>
		                <br />>> {{obj.msg}}</a>
		            </li>
		       	</ul>
		       	<div v-if="roomId != ''">
		       		<div class="input-group-prepend">
		                <label class="input-group-text">내용</label>
		            </div>
		            <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage('TALK')">
		            <div class="input-group-append">
		                <button class="btn btn-primary" type="button" @click="sendMessage('TALK')">보내기</button>
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
		            			<br>>> {{speak.msg}}
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
    	//var sock = new SockJS("/ws-stomp");
        //var ws = Stomp.over(sock);
        
        var vm = new Vue({
            el: '#app',
            data: {
            	ws: undefined,	//  웹소켓 객
            	token: '',
            	cid: 0,
            	readyCount: 0,
                room_name : '',
                chatrooms: [],
                messages: [],
                contracts: [],
                histories: [],
                roomId: '',
                roomName: '',
                message: '',
                selectedRoom: {}
            },
            created() {
            	this.cid = 1; 
                this.findAllRoom();
                
                var _this = this;
                axios.get('/chat/user').then(response => {
	                _this.token = response.data.token;
	            });
	            
	            //console.log('ws - 1 : ' + JSON.stringify(ws));
            },
            methods: {
            
            	// 스페이스 목록 조회.
                findAllRoom: function() {
                	//var uri = '/chat/rooms';
                	var uri = '/talk/' + this.cid + '/spaces';
                    axios.get(uri).then(response => {
                        // prevent html, allow json array
                        if(Object.prototype.toString.call(response.data.list) === "[object Array]")
                            this.chatrooms = response.data.list;
                    });
                },
               
				// 스페이스 입장(선택)
                enterRoom: function(roomId) {
                	console.log(this.ws);
                	if(this.ws != undefined){
                		if(this.ws.connected){
                			console.log('disconnect success!');
	                		this.ws.disconnect();
	                	}
                	}
                	
                	rooms = this.chatrooms.filter(room => room.id == roomId);
                	if(rooms.length == 1){
                		this.selectedRoom = rooms[0];
                	}                	
                    
                    this.roomId = roomId;	// 스페이스 설정.
                    this.connect();			// 스페이스 소켓 연결 및 구독.
                    this.getMessages();		// 이전 채팅메세지 조회.
                    this.getHistory();		// 이전 상담목록 조회.
                },
                
                // 웹소켓 연결 및 스페이스 구독 설정.
                connect: function(){
                	var _this = this;
                	
                	// SockJs 객체 초기화.
                	var sock = new SockJS("/ws-stomp");
                	
                	// Stomp 객체 초기화.
                	this.ws = Stomp.over(sock);
                	
                	// 웹소켓 연결.
                	this.ws.connect({"token": _this.token}, function(frame) {
	                	console.log('socket connected!');
	                	
	                	// 스페이스(room) 구독.
	                	_this.ws.subscribe("/sub/chat/room/"+ _this.roomId, function(message) {
	                		console.log('message : >> ');
	                		console.log(message);
	                		
	                        var recv = JSON.parse(message.body);
	                        _this.recvMessage(recv);
	                    });
	                    
	                }, function(error) {
	                    alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
	                    location.href="/";
	                });
                },
                
                // 대화 메세지 조회 
                getMessages: function(){
            		var _this = this;
            		
            		var uri = '/talk/' + this.cid + '/spaces/' + this.roomId + '/speaks';
            		axios.get(uri).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				console.log(response.data.list);
            				_this.messages = response.data.list; 
            			}
            		}, function(err){
            			console.log(err);
            		});
            	},
            	
            	// 대화 메세지 전송
                sendMessage: function(type) {
                	var uri = '/pub/talk/message';
                	
                	var data = {
                		cid: this.cid,
                		type:type, 
                		roomId:this.roomId, 
                		message:this.message, 
                		msg:this.message
                	}
                	
                    this.ws.send(uri, {"token":this.token}, JSON.stringify(data));
                    console.log('send message success!');
                    this.message = '';
                },
                
                // 수신된 메세지노출  
                recvMessage: function(recv) {
                	console.log("recvMessage > ");
                	console.log(recv);
                    this.userCount = recv.userCount;
                    this.messages.push({"type":recv.type,"sender":recv.sender,"msg":recv.msg, "message":recv.message})
                },
                
                // 이전 상담목록 조회 
               	getHistory: function(){
               		var _this = this;
            		var uri = '/talk/' + this.cid + '/spaces/' + this.roomId + '/history';
            		axios.get(uri).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				console.log('getHistory list >> ');
            				console.log(response.data.list);
            				
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
               		console.log('check');
               		var _this = this;
            		var uri = '/talk/' + this.cid + '/spaces/' + roomId + '/history/speaks';
            		var data = {
            			params: {
            				'startId': startId, 
            				'endId': endId
            			}
            		}
            		
            		var history = _this.histories.filter(history => history.startid == startId && history.endid == endId)[0]
            		if(history.showSpeaks == true){
            			history.speaks = [];
            			history.showSpeaks = false;
            		}else{
	            		// 필수 기본값 설정.
	    				for(var i=0; i<_this.histories.length; i++){
	    					_this.histories[i].speaks = [];
	    					_this.histories[i].showSpeaks = false;
	    				}
    				
            			axios.get(uri, data).then(response => {
	            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
	            				history.speaks = response.data.list;
	            				history.showSpeaks = true;
	            			}
	            		}, function(err){
	            			console.log(err);
	            		});
            		}
            		
            		
               	}
                
            }
        });
    </script>
  </body>
</html>