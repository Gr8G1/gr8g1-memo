## AWS EC2(Ubuntu)에 nginx 설치

### On EC2
```bash
# nginx 설치 
$ sudo apt-get install nginx

# nginx 설치 확인
$ nginx -v

# nginx 활성화 & 실행
$ sudo systemctl enable nginx
$ sudo service nginx start
$ service nginx status 

# nginx 설정 파일 분석
$ nginx -t
```

### html - DocumentRoot
**'/var/www/html/'** 디렉토리는 Apache 웹 서버에서 기본으로 설정된 DocumentRoot , 이 디렉토리는 웹 서버가 제공하는 웹 페이지와 컨텐츠를 저장하는 기본 위치이다.
만약 우분투에서 Apache 웹 서버를 설치하고 활성화했다면, /var/www/html/ 디렉토리에 저장된 파일들이 사용자가 브라우저에서 요청한 URL에 따라 웹 서버를 통해 전송된다. 
이 디렉토리에는 HTML, CSS, JavaScript, 이미지 파일 등의 웹 컨텐츠를 저장할 수 있다.

> /var/www/html/ 디렉토리는 일반적으로 웹 서버의 기본 디렉토리로 사용되지만, 웹 서버 설정에서 DocumentRoot 디렉토리를 변경할 수도 있다.

#### nginx도 같은 경로를 사용하나?
nginx는 Apache와는 기본 설정에서 nginx는 Apache와 달리 /var/www/html/ 디렉토리를 사용하지 않는다.
DocumentRoot를 설정하는 방법이 Apache와 다르며, 기본적으로 /usr/share/nginx/html/ 디렉토리가 DocumentRoot로 사용된다. 
이 디렉토리도 Apache의 /var/www/html/ 디렉토리와 마찬가지로 웹 서버가 제공하는 웹 페이지와 컨텐츠를 저장하는 기본 위치이다.

> 그러나 nginx의 DocumentRoot 디렉토리는 웹 서버의 설정 파일에서 변경할 수 있다. 설정 파일에서 root 디렉티브를 사용하여 새로운 디렉토리 경로를 지정할 수 있다.

### nginx server block
nginx 웹 서버를 사용할 때 서버 블록을 사용해 구성 세부 정보를 캡슐화하고 단일 서버에서 둘 이상의 도메인을 호스팅할 수 있다. 
- 단일 사이트 호스팅: /var/www/html 활용 
- 여러 사이트를 호스팅할 경우: /var/www/{domain}/html 같은 방식으로 디렉토리 구조를 수정해 사용 가능하다.

### Nginx 파일 및 디렉토리
- 콘텐츠
  - /var/www/html : index.html || index.nginx-debian.html
- 서버 설정(구성)
  - /etc/nginx/nginx.conf:
- 서버 블록 설정(구성)
  - /etc/nginx/sites-available/
- 서버 블록 (활성화) 설정(구성)
  - /etc/nginx/sites-enabled
- 서버 로그
  - /var/log/nginx/access.log
- 에러 로그 
  - /var/log/nginx/error.log

### nginx 설정(구성) 변경
```bash
# 특정 파일 찾기 : sudo find / -name nginx.conf

# 설정(구성) 변경
$ sudo vim /etc/nginx/nginx.conf

# nginx.conf
http {
    ...
    
    # server_names_hash_bucket_size 64; -> # 해시 버킷 메모리 이슈 발생 시 주석 제거 
    
    ...
}
```

### Proxy 설정
```bash
# log, error 저장 디렉토리 생성
$ sudo mkdir /var/log/nginx/proxy

# 설정 추가
$ sudo vi /etc/nginx/proxy_params

# proxy_params
proxy_set_header Host $http_host;                               # 요청 헤더의 Host 필드를 프록시 서버로 전달한다.
proxy_set_header X-Real-IP $remote_addr;                        # 클라이언트의 실제 IP 주소를 프록시 서버로 전달한다.
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;    # 클라이언트의 IP 주소를 포함한 전체 X-Forwarded-For 헤더를 프록시 서버로 전달한다.
proxy_set_header X-Forwarded-Proto $scheme;                     # 요청 URL의 프로토콜을 프록시 서버로 전달
proxy_set_header X-NginX-Proxy true;                            # nginx 프록시 서버에서 요청이 처리된 것임을 나타내는 X-NginX-Proxy 헤더를 추가한다.

client_max_body_size 256M;              # 클라이언트 요청의 최대 크기를 256MB로 제한한다.
client_body_buffer_size 1m;             # 클라이언트 요청 바디의 버퍼 사이즈를 1MB로 설정한다.
proxy_buffering on;                     # 프록시 서버가 응답을 버퍼링하도록 설정한다.
proxy_buffers 256 16k;                  # 응답을 위한 버퍼의 개수와 사이즈를 설정한다.
proxy_buffer_size 128k;                 # 각 버퍼의 사이즈를 128KB로 설정한다.
proxy_busy_buffers_size 256k;           # 사용 중인 버퍼의 최대 크기를 256KB로 제한한다.
proxy_temp_file_write_size 256k;        # 임시 파일에 쓰기 위한 최대 크기를 256KB로 제한한다.
proxy_max_temp_file_size 1024m;         # 임시 파일의 최대 크기를 1024MB로 제한한다.

proxy_temp_file_write_size 256k;        # 임시 파일에 쓰기 위한 최대 크기를 256KB로 제한한다.
proxy_max_temp_file_size 1024m;         # 임시 파일의 최대 크기를 1024MB로 제한한다.

proxy_connect_timeout 300;              # 각각 프록시 서버와의 연결, 요청 전송, 응답 수신에 대한 타임아웃 시간을 설정
proxy_send_timeout 300;
proxy_read_timeout 300;

proxy_intercept_errors on;              # 프록시 서버에서 발생하는 에러를 감지하여 처리할지 여부를 결정
```

### service-url.inc 설정(구성) 변경
```bash
# 설정(구성) 변경
$ sudo vim /etc/nginx/conf.d/service-url.inc

# service-url.inc
set $service_url https://{EC2 Public IP or Domain}:{Port};
```

### 서버 블록 생성
```bash
# 도메인 기준
$ sudo vi /etc/nginx/sites-available/{domain}

server {
    listen 80;
    listen [::]:80;
    server_name {domain} www.{domain};
    # root /usr/share/nginx/html;
    
    access_log /var/log/nginx/proxy/access.log;
    error_log /var/log/nginx/proxy/error.log;
    
    # Load configuration files for the default server block.
    include /etc/nginx/conf.d/service-url.inc;
  
    location / {
        include /etc/nginx/proxy_params;
        
        proxy_pass $service_url;
    }
}
```
> listen 지시문에 의해 80 포트로 들어오는 요청에 대해 server_name 값과 정확하게 일치하는 서버 블록을 찾으려고 시도한다.
