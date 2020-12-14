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
            	<span>로그인 : {{profile.loginName}} </span><br />
            	<span>조인룸 : {{socket.roomId}}</span><br />
            	<span>선택룸 : {{selectedRoom.id}}</span>
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
		        <ul>
		        	<li><button type="button" @click="swapList(0);">대기목록({{readyCount}})</button></li>
		        	<li><button type="button" @click="swapList(1);">진행목록</button></li>
		        	<li><button type="button" @click="swapList(2);">종료목록</button></li>
		        </ul>
		        <ul v-if="showRooms == 0" class="list-group">
		        	<h3>대기상담 목록</h3>
		            <li class="list-group-item list-group-item-action" v-for="obj in readyRooms" v-bind:key="obj.id">
		            	<dt>[{{obj.id}}] 
		            		<span v-if="obj.isBlockCustomer > 0" style="color:red">{{obj.customerName}}</span>
		            		<span v-else>{{obj.customerName}}님</span>
		            		<span class="badge badge-info badge-pill">{{obj.memberName}}</span>
		            		<span v-if="obj.isOnline == 1" class="badge badge-primary badge-pill">Online</span>
							<span v-if="obj.noReadCount > 0 " class="badge badge-info badge-pill">{{obj.noReadCount}}</span>
							<br />{{obj.lastMessage}}
		            	</dt>
		                <dd>
		                	<button type="button" @click="">챗봇대화</button>
		                	<button type="button" @click="matchRoom(obj.id)">상담하기</button>
		                	<button type="button" @click="">이관</button>
		                </dd>
		                
		            </li>
		        </ul>
		        <ul v-if="showRooms == 1" class="list-group">
		        	<h3>진행상담 목록</h3>
		            <li class="list-group-item list-group-item-action" v-for="obj in activeRooms" v-bind:key="obj.id">
		            	<dt>[{{obj.id}}] 
		            		<span v-if="obj.isBlockCustomer > 0" style="color:red">{{obj.customerName}}</span>
		            		<span v-else>{{obj.customerName}}님</span>
		            		<span class="badge badge-info badge-pill">{{obj.memberName}}</span>
		            		<span v-if="obj.isOnline == 1" class="badge badge-primary badge-pill">Online</span>
							<span v-if="obj.noReadCount > 0 " class="badge badge-info badge-pill">{{obj.noReadCount}}</span>
							<br />{{obj.lastMessage}}
		            	</dt>
		                <dd>
		                	<button type="button" @click="alert(obj.joinHistoryJson)">챗봇대화</button>
		                	<button type="button" @click="join(obj.id)">상담하기</button>
		                	<button type="button" @click="">이관</button>
		                	<button type="button" @click="closeRoom(obj.id)">종료</button>
		                </dd>
		            </li>
		        </ul>
		        <ul v-if="showRooms == 2" class="list-group">
		        	<h3>종료상담 목록</h3>
		            <li class="list-group-item list-group-item-action" v-for="obj in finishRooms" v-bind:key="obj.id" v-on:click="join(obj.id)">
		            	<dt>[{{obj.id}}] 
		            		<span v-if="obj.isBlockCustomer > 0" style="color:red">{{obj.customerName}}</span>
		            		<span v-else>{{obj.customerName}}</span>
		            		<span class="badge badge-info badge-pill">{{obj.memberName}}</span>
		            		<span v-if="obj.isOnline == 1" class="badge badge-primary badge-pill">Online</span>
							<span v-if="obj.noReadCount > 0 " class="badge badge-info badge-pill">{{obj.noReadCount}}</span>
		            	</dt>
		                <dd>{{obj.lastMessage}}</dd>
		            </li>
		        </ul>
		    </div>
		    <div class="col-md-3">
		    	<h4>상담채팅 - </h4>
		    	<span v-if="selectedRoom.id != socket.lobbyName"><button type="button" @click="leave()">채팅나가기</button></span>
		    	<ul  v-if="selectedRoom.id != socket.lobbyName" class="list-group">
		            <li class="list-group-item" v-for="obj in messages">
		            	<strong>[시스템:{{obj.isSystemMessage}}, 상담사:{{obj.isEmployee}}, 고객:{{obj.isCustomer}}, 작성자:{{obj.speakerName}}]</strong>
		                <br />&gt;&gt; {{obj.message}} {{obj.createDate}}</a>
		            </li>
		       	</ul>
		       	<div v-if="selectedRoom.id != socket.lobbyName">
			       	<div>
				       	<div v-if="profile.id == selectedRoom.memberId">
				            <textarea class="form-control" v-model="message" placeholder="내용을 입력하세요."></textarea>
				            <div class="input-group-append">
				                <button class="btn btn-primary" type="button" @click="sendDialog()">보내기</button>
				            </div>
				       	</div>
				       	<div v-else>
				            <textarea disabled class="form-control" v-model="message" placeholder="내용을 입력하세요."></textarea>
				            <div class="input-group-append">
				                <button disabled class="btn btn-primary" type="button" @click="sendDialog()">보내기</button>
				            </div>
				            <div>
								<button type="button" class="btn btn-info" @click="transferRoom()">내 상담으로 가져오기</button>
							</div>
				       	</div>
						<div v-if="socket.roomId != socket.lobbyName">
							<button type="button" class="btn btn-info" @click="warnSwear">욕설/비속어 경고메세지</button>
							<button type="button" class="btn btn-info" @click="warnInsult">부적절한 대화 경고메세지</button>
						</div>
					</div>
				</div>
		    </div>
		    <div class="col-md-3">
		    	<h4>계약정보 </h4>
		    	<select v-model="useContractNum">
		    		<template v-for="(obj, count) in contracts">
			    		<option v-bind:value="obj.useContractNum">{{obj.newAddress}}</option>
			    	</template>
		    	</select>
		    	<ul class="list-group">
		            <li>사용계약번호 : {{selectedContract.contractInfo.useContractNum}}</li>
		            <li>계약상태 : {{selectedContract.contractInfo.contractStatus}}</li>
		            <li>계약자성명 : {{selectedContract.contractInfo.customerName}}</li>
		            <li>휴대폰번호 : </li>
		            <li>주소 : </li>
		            <li>검침기준일 : {{selectedContract.contractInfo.gmtrBaseDay}}</li>
		            <li>청구서 : {{selectedContract.contractInfo.billSendMethod}}</li>
		            <li>납부방법 : {{selectedContract.contractInfo.paymentType}}</li>
		            <li>최근 계량기 교체일 : </li>
		            <li>최근 안전점검일자 : </li>
		            <li>잔여캐시 : </li>
		            <li>해당 고객센터 : {{selectedContract.contractInfo.centerName}} {{selectedContract.contractInfo.centerPhone}}</li>
		       	</ul>
		       	<select v-model="requestYm">
		    		<template v-for="(sub, count) in selectedContract.history">
			    		<option v-bind:value="sub.requestYm">{{sub.requestYm}}</option>
			    	</template>
		    	</select>
		    	<ul>
		    		<li>납기일 : {{contractBil.paymentDeadline}}</li>
		    		<li>총 청구요금 : {{contractBil.allPayAmounts}}</li>
		    		<li>당월소계 : {{contractBil.chargeAmt}}</li>
		    		<li>- 기본요금 : {{contractBil.basicRate}}</li>
		    		<li>- 사용요금 : {{contractBil.useRate}}</li>
		    		<li>- 감면금액 : {{contractBil.discountAmt}}</li>
		    		<li>- 계량기 교체비용 : {{contractBil.replacementCost}}</li>
		    		<li>- 부가세 : {{contractBil.vat}}</li>
		    		<li>- 정산금액 : {{contractBil.adjustmentAmt}}</li>
		    		<li>- 절사금액 : {{contractBil.cutAmt}}</li>
		    		<li>미납소계 : {{contractBil.previousUnpayAmounts}}</li>
		    		<template v-for="(prev, idx) in contractBil.previousUnpayInfos">
		    			<li >{{prev.requestYm}} : {{prev.unpayAmtAll}}</li>
		    		</template>
		    		<li>납부상태 : {{contractBil.payMethod}}</li>
		    		<li>입금전용계좌 : </li>
		    		<li>
		    			<ul>
		    				<li>
		    					<select v-model="selectedAccount">
		    						<template v-for="(acc, idx2) in contractBil.virtualAccount.accounts">
						    			<option v-bind:value="acc.account">{{acc.name}}</option>
						    		</template>
						    	</select>
		    				</li>
		    				<li>계좌번호 : {{selectedAccount}}</li>
		    			</ul>
		    		</li>
		    	</ul>
		    	
		    	
		    	
		       	<h4>상담목록</h4>
		    	<ul class="list-group">
		            <li class="list-group-item" v-for="(obj, index) in histories" v-bind:key="obj.id" @click="toggleHistory(index)">
		                {{obj.create_date}} - {{obj.end_date}}
		                <ul v-if="obj.toggle">
		            		<li v-for="(message, idx) in obj.messages">
		            			<strong>[시스템:{{message.isSystemMessage}}, 상담사:{{message.isEmployee}}, 고객:{{message.isCustomer}}, 작성자:{{message.speakerName}}]</strong>
		                <br />&gt;&gt; {{message.message}} {{message.createDate}}</a>
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
    <script src="/webjars/sockjs-client/1.0.0/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
    
    <script>
        var vm = new Vue({
            el: '#app',
            data: {
            	socket: {
            		//host: "//localhost",
            		//host: "//cstalk-local.gasapp.co.kr",
            		host: "//cstalk-dev.gasapp.co.kr",
            		
            		//port: 8080,
            		port: 80,
            		
            		ws: undefined,							// 웹소켓 객체.
            		subscribe: undefined,					// 조인(구독) 객체.
            		connectEndPoint: "/ws",			// 연결 end point
	            	sendEndPoint: '/pub/socket/message',	// 메세지 전송 end point
	            	roomPrefix: '/sub/socket/room/',			// 조인(구독)룸 end point의 prefix
	            	lobbyName: 'LOBBY1',					// 서울도시가스 조인(구독) 기본룸 Id (인천도시가스 - LOBBY2)
	            	roomId: '',								// 현재 조인(구독)중인 방
            	},
            	companyId: 1,								// 가스사 Id
            	header: {},				// 공통 header
            	token: '',				// 인증토큰
            	profile: {},
            	readyCount: 0,			// 대기상담 카운트
            	showRooms: 1,			// 노출할 상담영역(0-대기, 1-진행, 2-종료)
				readyRooms: [],			// 대기상담목록
				activeRooms: [],		// 진행상담목록
				finishRooms: [],		// 종료상담목록
				histories: [],			// 이전상담목록
				contracts: [],			// 사용계약목록
				contractBil: {
					virtualAccount:{
						accounts: []
					}
				},		// 사용계약 결제상세
				selectedAccount: '',	// 선택된 전용계좌
				selectedContract: {
				  "contractInfo": {
				    "gmtrBaseDay": "",
				    "contractStatus": "",
				    "telNumber": {
				      "num1": "",
				      "num3": "",
				      "num2": ""
				    },
				    "meterNum": "",
				    "centerCode": "",
				    "centerPhone": "",
				    "useContractNum": "",
				    "billSendMethod": "",
				    "customerName": "",
				    "productName": "",
				    "centerName": "",
				    "paymentType": ""
				  },
				  "history": [
				    {
				      "deadlineFlag": "",
				      "requestYm": ""
				    },
				    {
				      "deadlineFlag": "",
				      "requestYm": ""
				    }
				  ]
				},	// 선택된 사용계약상세정보
				useContractNum: "",		// 사용계약번호
				requestYm: '',			// 결제연월
				messages: [],			// 대화메세지
				message: "",			// 작성하는 메세지
				selectedRoom: {},	// 선택된 룸 정보
            },
            created() {
            },
            
            mounted() {
            	this.init();
            },
            
            methods: {
            	/**************************************************************
                * 초기화
                **************************************************************/
                init: function(){
                	this.getProfile();
                },	
            
            	/**************************************************************
                * 멤버정보 조회
                **************************************************************/
            	getProfile: function() {
            		this.token = localStorage.accessToken;
            		//this.token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiY29tcGFueUlkIjoiMSIsImNvbXBhbnlVc2VDb25maWdKc29uIjpudWxsLCJjb21wYW55TmFtZSI6IuyEnOyauOuPhOyLnOqwgOyKpCIsImlzQWRtaW4iOjEsImlzQ3VzdG9tZXIiOjAsImF1dGhMZXZlbCI6MiwibG9naW5OYW1lIjoiY3NtYXN0ZXIxIiwic3RhdGUiOjMsInByb2ZpbGVJbWFnZUlkIjowLCJzcGVha2VySWQiOjE3NywibmFtZSI6IuyEnOyauOuPhOyLnOqwgOyKpCIsImNyZWF0ZURhdGUiOiIyMDE5LTExLTA4IDE0OjEwOjA5IiwidXBkYXRlRGF0ZSI6IjIwMjAtMTEtMjQgMTQ6MjY6NDQiLCJ1cGRhdGVNZW1iZXJJZCI6IjEiLCJpYXQiOjE2MDYyODMxNjcsImV4cCI6MTYwNjM2OTU2N30.5FmZjUqz120n6vmmb-XJw8wxkNHvlJH4TmbeoEvTs7M";
            		
            		this.header = {
            			headers: {'Authorization' : 'Bearer ' + this.token}
            		}

					if(this.token == ""){
						// 인증정보가 존재하지 않을경우.
						//document.location.href = '/api/login';
						console.log("error : " + this.token);
						
					}else{
	            		axios.get('/api/auth/profile', this.header).then(response => {
	            			if(response.data){
	            				this.profile = response.data;
	            				console.log(JSON.stringify(this.profile));
	
								//인증되었을 경우 소켓 연결.
								this.connect();
								
	            			}else{
	            			
	            				// 인증정보가 존재하지 않을 경우.
	            				//document.location.href = '/api/login';
	            				console.log("error2");
	            			}
		                	
			            }, function(e){
			            	localStorage.removeItem("accessToken");
			            	console.log("error3");
			            	document.location.href = '/api/login';
			            });
			           }
            	},
            	
            	/**************************************************************
                * 로그아웃
                **************************************************************/
            	logout: function(){
            		localStorage.removeItem("accessToken");
            		document.location.href = "/api/login";
            	},
                
            
            	/**************************************************************
                * 웹소켓 연결
                **************************************************************/
                connect: function(){
                
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
                	this.join(this.socket.lobbyName);
                	
                	// 상담목록 조회.
                	this.findRooms();
                },
                

                /**************************************************************
                * 웹소켓 연결실패 처리.
                **************************************************************/
                connectFailCallback: function(errorMessage){
                	alert("연결하는데 실패하였습니다.");
                	console.log('Error : ' + errorMessage);
                },
                
                /**************************************************************
                * 타 상담사는 조인하지 않고 대화내용만 노출(구독하지 않음).
                **************************************************************/
                showRoom: function(roomId){
                
                	// 기본룸 조인.
                	this.join(this.socket.lobbyName);
                
                	console.log("내 상담이 아니기에 조인하지 않고 관련정보만 조회.");
                	// 대기룸이 아닐경우.
                	if(roomId != this.socket.lobbyName){
                	
                		// 이전 대화목록 조회.
                		this.getMessages(roomId);
                			
	                	// 이전 상담목록 조회
	                	this.getHistory(roomId);
	                	
	                	// 계약정보 조회
	                	this.getContracts();
	                }
                },
                
                /**************************************************************
                * 조인(구독).
                **************************************************************/
                join: function(roomId){
                	
                	rooms = this.activeRooms.filter(room => room.id == roomId);
                	if(rooms.length == 1){
                	
                		this.selectedRoom = rooms[0];
                		console.log("selectedRoom : " + JSON.stringify(this.selectedRoom));
                	
                		// 나의 상담인지 확인.
                		if(rooms[0].memberId != this.profile.id){
                			// 내 상담이 아닐경우 조인하지 않음.
                			this.showRoom(roomId);
                			return;
                		}
                	
                	}
                	// 조인방 이름 생성.(예, /sub/chat/room/93)
                	var uri = this.socket.roomPrefix + roomId;
                	
                	// 기존에 연결된 조인(구독)이 있을경우.
                	if(this.socket.subscribe){
                		// 조인(구독) 해제.
                		this.unjoin();
                	}
                	
                	this.socket.roomId = roomId;
                	
                	// 구독
                	this.socket.subscribe = this.socket.ws.subscribe(
                		uri, 								// 구독 URI
                		this.receiveMessage, 				// 구독 성공시 호출되는 함수.	 
                		headers = {'token': this.token}		// 구독 요청시 전달하는 헤더.
                	);

                	// 대기룸이 아닐경우.
                	if(this.socket.roomId != this.socket.lobbyName){
	                	// 이전 상담목록 조회
	                	this.getHistory(this.socket.roomId);
	                	
	                	// 계약정보 조회
	                	this.getContracts();
	                }
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
					console.log(payload);
					
					var target = payload.target;
					
					if(target == 'ALL' || target == 'MEMBER'){
						var eventName = payload.eventName;		// 이벤트 구분.
						var roomId = payload.roomId;			// 룸 아이디.
						var data = payload.data;				// 각 메세지에 대한 기타 정보데이터.
						
						// 각 이벤트에 따른 처리.
						switch(eventName){
						
							case "JOINED" :	// 조인완료 수신.
								// todo
								// 메세지 전송.
			                	data = {
			                		speakerId: '',
			                		messageType: 0,
			                		message: '상담사의 메시지입니다.',
			                		messageDetail: '',
			                		templateId: '',
			                	}
			            		//this.sendMessage("MESSAGE", data);
	            		
								break;
								
							case "ROOM_DETAIL" : 	// 방 상세정보 수신.
								// todo
								break;
								
							case "ASSIGNED" :		// 상담사 배정완료 수신.
								// todo
								// this.findRooms();
								// this.join(roomId);
								
								break;
							
							case "MESSAGE" : 		//	대화 메세지 수신.
								// todo
								this.messages.push(data.message);
								
								break;
							
							case "MESSAGE_LIST" : 		// 이전대화 목록 수신.
								// todo
								messages = data.messages;
								messages.reverse()		// 배열 뒤집기.
								this.messages = messages;
								break;
							
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
                
                // 진행중인 상담 조회 조회
                findRooms: function(roomId) {
                	this.findReadyRooms();
                	this.findActiveRooms(roomId);
                	this.findEndRooms();
                },
                
                // 대기중인 상담목록 조회
                findReadyRooms: function(){
                	var uri = '/api/room?queryId=findReadyState';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
							this.readyRooms = response.data;
							
							console.log(">> 대기상담 목록");
							console.log(this.readyRooms);
							
							this.readyCount = this.readyRooms.length;
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
                },
                
                // 진행중인 상담목록 조회
                findActiveRooms: function(roomId){
                	var uri = '/api/room?queryId=findIngState';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
							this.activeRooms = response.data;
							
							console.log(">> 진행중인 상담 목록");
							console.log(this.activeRooms);
							
							if(roomId){
								this.join(roomId)
							}
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
                },
                
                // 종료된 상담목록 조회
                findEndRooms: function(){
                	var uri = '/api/room?queryId=findCloseState';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
							this.finishRooms = response.data;
							
							console.log(">> 종료상담 목록");
							console.log(this.finishRooms);
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
                },
                
                // 대기상담 가져오기
                assign: function(){
                	
                },
                
                // 채팅나가기
                leave: function(){
                	this.join(this.socket.lobbyName);
                },
                
                // 상담 종료하기
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
                
                
                // 욕설/비속어 경고 메세지 전송.
				warnSwear: function(){
					console.log("욕설/비속어 경고 메세지 전송.");
				},
				
				// 부적절한 대화 경고 메세지 발송.
				warnInsult: function(){
				
				},
				
				// 이전 상담대화 목록 조회.
				getMessages: function(roomId){
					var uri = '/api/message?roomId=' + roomId + '&queryId=findByRoomIdAll';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
							this.messages = response.data;
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
				},
				
				// 이전 상담목록 조회.
				getHistory: function(roomId){
					var uri = '/api/room/' + roomId + '/findSearchJoinHistory';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
							//this.histories = response.data;
							histories = response.data;
							for(var i=0; i<histories.length; i++){
								histories[i].toggle = false;
							}
							this.histories = histories;
							
							console.log(">> 이전상담 목록");
							console.log(this.histories);
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
				},
				
				// 이전 상담 대화내용 조회
				toggleHistory: function(index){
					console.log('history index : ' + index);
					this.histories[index].toggle = !this.histories[index].toggle;
				},
				
				// 사용계약정보 목록 조회
				getContracts: function(){
				
					console.log(this.selectedRoom);
					var gasappMemberNumber = this.selectedRoom.gasappMemberNumber;
					console.log('gasappMemberNumber : ' + gasappMemberNumber);
					
					var uri = '/api/customer/' + gasappMemberNumber + '/contracts';
                    axios.get(uri, this.header).then(response => {
                        // prevent html, allow json array
						if(Object.prototype.toString.call(response.data) === "[object Array]"){
						
							// 사용계약 목록 설정.
							this.contracts = response.data;
							
							// 사용계약 상세정보 초기화.
							this.useContractNum = this.contracts[0].useContractNum;  
							
						}
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
				},
				
				// 사용계약 상세정보 조회
				getContractDetail: function(){
				
					var gasappMemberNumber = this.selectedRoom.gasappMemberNumber;
					console.log('gasappMemberNumber : ' + gasappMemberNumber);
					
					var uri = '/api/customer/' + gasappMemberNumber + '/contracts/' + this.useContractNum;
                    axios.get(uri, this.header).then(response => {
                    
                    	// 사용계약 상세정보 설정.
                    	this.selectedContract = response.data;
						console.log(this.selectedContract);
						
						// 월별 선택 초기화.
						if(this.selectedContract.history.length > 0){
							this.requestYm = this.selectedContract.history[0].requestYm;
						}
						
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
				},
				
				// 사용계약 결제상세정보 조회.
				getContractBilDetail: function(){
					var gasappMemberNumber = this.selectedRoom.gasappMemberNumber;
					console.log('gasappMemberNumber : ' + gasappMemberNumber);
					
					var uri = '/api/customer/' + gasappMemberNumber + '/contracts/' + this.useContractNum + '/bil?requestYm=' + this.requestYm + '&deadlineFlag=';
                    axios.get(uri, this.header).then(response => {
                    	this.contractBil = response.data;
                    	
                    	// 전용계좌 초기화
                    	if(this.contractBil.virtualAccount.accounts.length > 0){
                    		this.selectedAccount = this.contractBil.virtualAccount.accounts[0].account;
                    	}
                    	console.log(this.contractBil);
                    }, function(e){
                    	console.log("error : " + e.message);
                    });
				},
				
				
				// 대기상담 가져오기
				matchRoom: function(roomId){
					if(confirm('상담을 시작하시겠습니까?')){
						var uri = '/api/room/' + roomId + '/matchRoom';
						axios.post(uri, {}, this.header).then(response => {
							// prevent html, allow json array
							room = response.data;
							
							this.showRooms = 1;
							this.findRooms(room.id);
							
						}, function(e){
							console.log("error : " + e.message);
						});
					}
					
				},
				
				// 내 상담으로 이관하기
				transferRoom: function(){
					if(confirm('내 상담으로 이관하시겠겠습니까?')){
						var uri = '/api/room/' + this.selectedRoom.id + '/transferRoom?memberId=' + this.profile.id + '&transferType=toMember';
						axios.post(uri, {}, this.header).then(response => {
							// prevent html, allow json array
							room = response.data;
							
							this.showRooms = 1;
							this.findRooms(room.id);
							
							alert('내 상담으로 이관되었습니다.');
							
						}, function(e){
							console.log("error : " + e.message);
						});
					}
				},
				
				// 상담목록 노출.
				swapList: function(num){
					this.showRooms = num;
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
                
            }, watch: {
				useContractNum: function (val) {
					this.useContractNum = val;
					this.getContractDetail();
			    },
			    requestYm: function(val) {
			    	this.requestYm = val;
			    	this.getContractBilDetail();
			    },
			}
        });
    </script>
  </body>
</html>