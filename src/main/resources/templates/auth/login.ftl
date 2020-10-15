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
							<select id="cid" name="cid" v-model="cid">
								<option value="1">서울도시가스</option>
								<option value="2">인천도시가스</option>
								<option value="3">제주도시가스</option>
							</select>
			            </li>
			            <li class="list-group-item">
							<input type="text" v-model="empno" />
			            </li>
			            <li class="list-group-item">
							<input type="text" v-model="pwd" />
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
            	cid: 1,
            	empno: 'csmaster1',
            	pwd: '1212',
            	appid: 2,
            	header: {},
            	isShow: false
            },
            created() {
            	this.init();
            },
            methods: {
            
            	// 인증여부 확인
            	init: function(){
            		var accessToken = localStorage.accessToken;
            		console.log('accessToken : ' + typeof accessToken);
            		
            		if(typeof accessToken != "undefined" && accessToken != "undefined" && accessToken != null && accessToken != ""){
            			this.header = {
	            			headers: {'Authorization' : 'Bearer ' + accessToken}
	            		}
	            		
	            		axios.get('/auth/user', this.header).then(response => {
		                	if(response.data){
		                		this.moveTalk();
		                	}
			            });
            		}
            		this.isShow = true;
            		
            	},
            
            	// 로그인
            	onSubmit: function(){
            		var header = {'Content-Type': 'multipart/form-data'};
            		var data = new FormData();
            			data.append("cid", this.cid);
            			data.append("empno", this.empno);
            			data.append("pwd", this.pwd);
            		
            		localStorage.removeItem("accessToken");
            		axios.post('/auth/login', data, header).then(response => {
	                	console.log(response.data);
	                	if(response.data.login == true){
	                		// 로그인 성공시 로컬스토리지에 인증토큰과 기관코드를 등록한다.
	                		localStorage.accessToken = response.data.token;
	                		
	                		this.moveTalk();
	                	}
		            });
            	},
            	
            	// 상담톡 페이지로 이동
            	moveTalk: function(){
            		document.location.href = "/talk";
            	}
            }
        });
    </script>
  </body>
</html>