# 디버깅 히스토리

## X-Frame-Option 이슈.
- sock.js에서 iframe을 사용하는데 서버와의 도메인이 맞지않을경우 해당 이슈 발생.
```javascript
socket:1 Refused to display 'https://cstalk-dev.gasapp.co.kr/ws/iframe.html#3snsj4qc' in a frame because it set 'X-Frame-Options' to 'sameorigin'.
socket:1 
```
> 조치
- Apache 설정 변경.
/etc/apache2/conf-available/security.conf

