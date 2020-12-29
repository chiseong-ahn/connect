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
    <div v-if="isShow" class="container" id="app">
        <div class="row">
           <div class="col-md-3">
		    	<h4>로그인</h4>
		    	<form v-on:submit.prevent="onSubmit">
			    	<ul class="list-group">
			            <li class="list-group-item">
							<select id="companyId" name="companyId" v-model="companyId">
								<option value="1">서울도시가스</option>
								<option value="2">인천도시가스</option>
								<option value="3">제주도시가스</option>
							</select>
			            </li>
			            <li class="list-group-item">
							<input type="text" v-model="loginName" />
			            </li>
			            <li class="list-group-item">
							<input type="text" v-model="password" />
			            </li>
			            <li class="list-group-item">
							<input type="text" v-model="name" />
			            </li>
			            <li class="list-group-item">
			            	<button type="submit">로그인</button>
			            </li>
			       	</ul>
		    	</form>
		    </div>
		</div>
    </div>
    <!-- JavaScript -->
    <script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
    <script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
    
    <script>
    	var vm = new Vue({
            el: '#app',
            data: {
            	companyId: 1,
            	loginName: 'csmaster1',
            	password: '1212',
            	name: '서울도시가스',
            	header: {},
            	isShow: false,
            	accessToken: '',
            	refreshToken: ''
            },
            created() {
            	this.init();
            },
            methods: {
            
            	// 인증여부 확인
            	init: function(){
            		this.accessToken = localStorage.accessToken;
            		//this.refreshToken = localStorage.refreshToken;
            		
            		_this = this;
            		if(typeof this.accessToken != "undefined" && this.accessToken != "undefined" && this.accessToken != null && this.accessToken != ""){
            			this.header = {
	            			headers: {'Authorization' : 'Bearer ' + this.accessToken}
	            		}
	            		
	            		axios.get('auth/profile', this.header).then(response => {
	            			
	            			if(response.data.id > 0){
		                		this.moveTalk();
		                	}
			            },function(e){
			            	if(e.response.status == 401){
			            		
			            	}
			            });
            		}
            		this.isShow = true;
            		
            	},
            	
            	// 토큰만료에 따른 토큰 갱신요청
            	refreshToken: function(){
            		var header = {
            			headers: {
            				'Content-Type': 'multipart/form-data',
            				'Authorization' : 'Bearer ' + this.accessToken
            			}
            		};
            		var data = new FormData();
            			data.append("refreshToken", this.refreshToken);
            			
            		axios.post('/api/auth/refresh', data, header).then(response => {
	                	console.log(response.data);
	                	if(response.data.result == true){
	                		// 로그인 성공시 로컬스토리지에 인증토큰과 기관코드를 등록한다.
	                		localStorage.accessToken = response.data.accessToken;
	                		//localStorage.refreshToken = response.data.refreshToken;
	                		//this.moveTalk();
	                		
	                		console.log("refresh token success!");
	                	}
		            });
            	},
            
            	// 로그인
            	onSubmit: function(){
            		var header = {'Content-Type': 'multipart/form-data'};
            		
            		/*
            		var data = new FormData();
            			data.append("companyId", this.companyId);
            			data.append("loginName", this.loginName);
            			data.append("password", this.password);
            			data.append("name", this.name);
            		*/
            		
            		var data = {
					    "companyId": "" + this.companyId,
					    "loginName": this.loginName,
					    "password": this.password,
					    "name": this.name
					}
					
            		localStorage.removeItem("accessToken");
            		axios.post('/auth/login', data, header).then(response => {
            			console.log(response.data.token);
	                	if(!response.data.token){
	                		alert(response.data.reason);
	                		return;
	                	}
                		// 로그인 성공시 로컬스토리지에 인증토큰과 기관코드를 등록한다.
                		localStorage.accessToken = response.data.token;
                		//localStorage.refreshToken = response.data.refreshToken;
                		this.moveTalk();
	                	
		            });
            	},
            	
            	// 상담톡 페이지로 이동
            	moveTalk: function(){
            		document.location.href = "/api/socket";
            	}
            }
        });
    </script>
  </body>
</html>