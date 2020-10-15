<!doctype html>
<html lang="en">
  <head>
    <title>상담톡 &gt; 관리자메뉴 &gt; 계정 관리</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
      	.container {
		    max-width: 100% !important;
		}
    </style>
  </head>
  <body>
    <div class="container" id="app">
        <div class="row">
            <div class="col-md-6 text-right">
                <a class="btn btn-primary btn-sm" href="/logout">로그아웃</a>
            </div>
        </div>
        <div class="row">
        	<div class="col-md-1">
        		<ul>
        			<li><a href="/talk" class="btn btn-default" type="button">상담채팅</a></li>
        			<li><a href="/templates" class="btn btn-default" type="button">답변템플릿</a></li>
        			<li><a href="/helper" class="btn btn-default" type="button">답변도우미</a></li>
        			<li><a href="/admin/emps" class="btn btn-default" type="button">관리자메뉴</a></li>
        		</ul>
        	</div>
        	<div class="col-md-3">
		        <ul class="list-group">
		            <li class="list-group-item list-group-item-action">
		            	현재 진행 상황 및 기간 별 통
		            </li>
		            <li class="list-group-item list-group-item-action">
		            	계정 관리
		            </li>
		            <li class="list-group-item list-group-item-action">
		            	답변템플릿 카테고리 관리 
		            </li>
		            <li class="list-group-item list-group-item-action">
		            	자동 메세지 관리 
		            </li>
		            <li class="list-group-item list-group-item-action">
		            	관심고객관리 
		            </li>
		        </ul>
		    </div>
		    <div class="col-md-7">
		    	<h4>계정 관리 | <small>계정연동</small></h4>
		    	<h5>총 {{count}}명</h5> 
		    	<table width="100%">
		    		<thead>
		    			<tr>
		    				<th>No.</th>
		    				<th>이름</th>
		    				<th>사번</th>
		    				<th>소속</th>
		    				<th>직급</th>
		    				<th>권한</th>
		    				<th>상담상태</th>
		    				<th>수정</th>
		    			</tr>
		    		</thead>
		    		<tbody>
		    			<tr v-for="(emp, index) in emps" v-key="emp.id">
		    				<td>{{count - index}}</td>
		    				<td>{{emp.name}}</td>
		    				<td>{{emp.empno}}</td>
		    				<td></td>
		    				<td></td>
		    				<td>
		    					<select v-if="emp.isUpdate" id="{{emp.id}}_auth" name="auth" v-model="emp.auth">
		    						<option value="">권한</option>
		    						<option value="2">관리자</option>
								    <option value="4">상담사</option>
								    <option value="7">조회자</option>
								    <option value="9">Guest</option>
								</select>
								<span v-else>{{emp.auth}}</span>
		    				</td>
		    				<td> 
		    					<select v-if="emp.isUpdate" id="{{emp.id}}_state" name="state" v-model="emp.state">
									<option value="">상담상태</option>
									<option value="0">상담중</option>
								    <option value="1">휴식중</option>
								    <option value="2">회의중</option>
								    <option value="3">콜집중</option>
								    <option value="5">퇴근</option>
								    <option value="6">점심</option>
								    <option value="9">기타</option>
								</select>
								<span v-else>{{emp.state}}</span>
		    				</td>
		    				<td>
		    					<div v-if="emp.isUpdate"><button type="button" @click="save(emp.id)">저장</button>|<button type="button" @click="cancel">취소</button></div>
		    					<div v-else><button type="button" @click="update(emp.id)">수정</button></div>
		    				</td>
		    			</tr>
		    		</tbody>
		    	</table>
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
            	count: 0,
        		emps: [],
        		header: {},
                token: ''
            },
            created() {
            	
            	this.getUserInfo();
            	 
            	
            },
            methods: {
            
            	// 로그아웃.
            	logout: function(){
            		localStorage.removeItem("accessToken");
            		document.location.href = "/";
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
            				
            				this.getEmps();
            				
            			}else{
            			
            				// 인증정보가 존재하지 않을 경우.
            				document.location.href = '/';
            			}
	                	
		            }, function(e){
		            	document.location.href = '/';
		            });
            	},
            
            	// 계정목록 조회.
                getEmps: function(){
            		var _this = this;
            		
            		var uri = '/admin/emps';
            		axios.get(uri, this.header).then(response => {
            			if(Object.prototype.toString.call(response.data.list) === "[object Array]"){
            				console.log(response.data.list);
            				this.count = response.data.total;
            				
            				if(this.count > 0){
            					list = response.data.list
	            				for(var i=0; i<list.length; i++){
	            					list[i].isUpdate = false;
	            				}
	            				_this.emps = list;
            				}
            				 
            			}
            		}, function(err){
            			console.log(err);
            		});
            	},
            	
            	// 수정폼 변경.
            	update: function(id){
            		console.log(this.emps.filter(emp => emp.id = id).length);
            		emp = this.emps.filter(emp => emp.id = id)[0];
            		emp.isUpdate = true;
            	},
            	
            	// 수정정보 저장.
            	save: function(id){
            		emp = this.emps.filter(emp => emp.id = id)[0];
            		var uri = '/admin/emps';
            		
            		// convert json to form data           		
            		var formData = new FormData();
            		for(var key in emp){
            			formData.append(key, emp[key]);
            		}
            		
            		var header = {
            			headers: {
	            			'Content-Type': 'multipart/form-data',
	            			'Authorization' : 'Bearer ' + this.token
	            		}
            		};
            		
            		axios.put(uri, formData, header).then(response => {
            			if(response.data.result == true){
            				alert('저장되었습니다.');
            				this.getEmps();
            			}else{
            				alert('저장하는데 실패하였습니다.');
            			}
            			
            		}, function(err){
            			console.log(err);
            		});
				},  
            	
            	// 수정취소.
            	cancel: function(id){
            		emp = this.emps.filter(emp => emp.id = id)[0];
            		emp.isUpdate = false;
            	}
            }
        });
    </script>
  </body>
</html>
