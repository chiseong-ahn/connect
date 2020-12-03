<!doctype html>
<html lang="en">
  <head>
    <title>Websocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
      [v-cloak] {
          display: none;
      }
    </style>
  </head>
  <body>
    <div class="container" id="app" v-cloak>
        <div class="row">
            <div class="col-md-6">
                <h4>{{roomName}} <span class="badge badge-info badge-pill">{{userCount}}</span></h4>
            </div>
            <div class="col-md-6 text-right">
                <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
                <a class="btn btn-info btn-sm" href="/chat/room">채팅방 나가기</a>
            </div>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" v-model="message" v-on:keypress.enter="sendMessage('TALK')">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="sendMessage('TALK')">보내기</button>
            </div>
        </div>
        <ul class="list-group">
            <li class="list-group-item" v-for="obj in messages">
                {{obj.speaker}} - {{obj.msg}}</a>
            </li>
        </ul>
        <br><Br>
        <h3>이전상담내역</h3>
         <ul class="list-group">
            <li class="list-group-item" v-for="history in histories">
                {{history.createdate}} - {{history.workdate}}</a>
            </li>
        </ul>
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    <script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    <script>
        // websocket & stomp initialize
        var sock = new SockJS("/ws-stomp");
        var ws = Stomp.over(sock);
        // vue.js
        var vm = new Vue({
            el: '#app',
            data: {
                roomId: '',
                roomName: '',
                message: '',
                messages: [],
                token: '',
                userCount: 0,
               	histories: [],
               	speaker: ''
            },
            created() {
                this.roomId = localStorage.getItem('wschat.roomId');
                this.roomName = localStorage.getItem('wschat.roomName');
                this.getMessages();		// 이전 대화내용 조회 
                this.getHistory();		// 이전 상담목록 조회 
                
                var _this = this;
                axios.get('/chat/user').then(response => {
                    _this.token = response.data.token;
                    ws.connect({"token":_this.token}, function(frame) {
                    	console.log('subscribe : /sub/chat/room/' + _this.roomId);
                        ws.subscribe("/sub/chat/room/"+_this.roomId, function(message) {
                            var recv = JSON.parse(message.body);
                            _this.recvMessage(recv);
                        });
                    }, function(error) {
                        alert("서버 연결에 실패 하였습니다. 다시 접속해 주십시요.");
                        location.href="/chat/room";
                    });
                    
                    
                });
            },
            methods: {
            	getMessages: function(){
            		var _this = this;
            		
            		var uri = '/talk/1/spaces/' + this.roomId + '/speaks';
            		axios.get(uri).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				console.log(response.data.list);
            				_this.messages = response.data.list; 
            			}
            		}, function(err){
            			console.log(err);
            		});
            	},
                sendMessage: function(type) {
                
                	//var uri = '/pub/chat/message';
                	var uri = '/pub/talk/message';
                	
                	var data = {
                		type:type, 
                		roomId:this.roomId, 
                		message:this.message, 
                		msg:this.message
                	}
                	
                    ws.send(uri, {"token":this.token}, JSON.stringify(data));
                    this.message = '';
                },
                //
                recvMessage: function(recv) {
                	console.log("recvMessage > ");
                	console.log(recv);
                    this.userCount = recv.userCount;
                    this.messages.unshift({"type":recv.type,"sender":recv.sender,"msg":recv.msg, "message":recv.message})
                },
               	getHistory: function(){
               		var _this = this;
            		
            		var uri = '/talk/1/spaces/' + this.roomId + '/history';
            		axios.get(uri).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				console.log('getHistory list >> ');
            				console.log(response.data.list);
            				_this.histories = response.data.list; 
            			}
            		}, function(err){
            			console.log(err);
            		});
               	}
            }
        });
    </script>
  </body>
</html>
