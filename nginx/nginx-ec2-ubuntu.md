## AWS EC2(Ubuntu)에 Nginx 설치

### On EC2
```bash
# nginx 설치 
$ sudo apt-get install nginx

# nginx 설치 확인
$ nginx -v

# nginx 활성화 & 실행
$ sudo systemctl enable nginx
$ sudo service nginx start

# 사용중인 포트 번호 확인
$ sudo lsof -i :{port}

# nginx 실행 확인
$ service nginx status 

# nginx 설정 파일 분석(오류 검증)
$ sudo nginx -t
```

### html - DocumentRoot
**'/var/www/html/'** 디렉토리는 Apache 웹 서버에서 기본으로 설정된 DocumentRoot , 이 디렉토리는 웹 서버가 제공하는 웹 페이지와 컨텐츠를 저장하는 기본 위치이다.
만약 우분투에서 Apache 웹 서버를 설치하고 활성화했다면, /var/www/html/ 디렉토리에 저장된 파일들이 사용자가 브라우저에서 요청한 URL에 따라 웹 서버를 통해 전송된다. 
이 디렉토리에는 HTML, CSS, JavaScript, 이미지 파일 등의 웹 컨텐츠를 저장할 수 있다.

> /var/www/html/ 디렉토리는 일반적으로 웹 서버의 기본 디렉토리로 사용되지만, 웹 서버 설정에서 DocumentRoot 디렉토리를 변경할 수도 있다.

#### Nginx도 같은 경로를 사용하나?
nginx는 Apache와 달리 /var/www/html/ 디렉토리를 사용하지 않는다.
DocumentRoot를 설정하는 방법이 Apache와 다르며, 기본적으로 /usr/share/nginx/html/ 디렉토리가 DocumentRoot로 사용된다. 
이 디렉토리도 Apache의 /var/www/html/ 디렉토리와 마찬가지로 웹 서버가 제공하는 웹 페이지와 컨텐츠를 저장하는 기본 위치이다.

> 그러나 Nginx의 DocumentRoot 디렉토리는 웹 서버의 설정 파일에서 변경할 수 있다. 설정 파일에서 root 디렉티브를 사용하여 새로운 디렉토리 경로를 지정할 수 있다.

### Nginx server block
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

### Forward vs Reverse
Nginx 설정 파일을 보면 Forward(순방향) proxy 임을 확인할 수 있다.

- Forward Proxy
  - 클라이언트가 외부 인터넷에 접근하기 위해 프록시 서버를 거쳐서 요청을 보내는 방식
  - 클라이언트는 직접 외부 인터넷에 접속하지 않고, 프록시 서버를 통해 요청을 보내기 때문에 클라이언트의 IP 주소를 감추거나 인터넷 사용 제한 등의 기능을 수행할 수 있다.
- Reverse Proxy
  - 서버가 클라이언트에게 서비스를 제공하기 위해 프록시 서버를 사용하는 방식
  - 클라이언트는 프록시 서버에 요청을 보내고, 프록시 서버는 이 요청을 다시 백엔드 서버에 전달한다. 
    - 이때 백엔드 서버의 IP 주소를 클라이언트에게 노출시키지 않기 때문에 보안적인 측면에서 유리하다. 
    - 또한 프록시 서버에서 캐싱을 이용하면 백엔드 서버의 부하를 줄일 수 있습니다.

### Nginx VS Apache
Apache는 프로세스를 fork하거나 쓰레드를 사용하지만,
Nginx는 CPU와 관계없이 IO들을 전부 Event Listener로 미루기 때문에 흐름이 끊기지 않고 응답이 빠른 작업이 가능하다.
추가적으로 Nginx는 Apache와 달리 동시접속자 수가 많아져도 추가적인 생성비용이 들지 않고, 메모리적인 측면에서 Nginx가 System Resource를 적게 처리한다는 장점이 있다.

### Nginx 설정(구성) 변경
```bash
# 특정 파일 찾기 : sudo find / -name nginx.conf

# 설정(구성) 변경
$ sudo vim /etc/nginx/nginx.conf

# nginx.conf
http {
    ...
    
    # server_names_hash_bucket_size 64; # 해시 버킷 메모리 이슈 발생 시 주석 제거 
    
    ...
    
    ##
    # Virtual Host Configs
    ##

    include /etc/nginx/conf.d/*.conf;
    include /etc/nginx/sites-enabled/*; # 도메인 파일 등록 (Symlinks) 경로 설정 확인
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
set $service_url http://{퍼블릭 IPv4 주소}:{port};
```

### 서버 블록 생성 (listen 80;)
```bash
# sites-available/{domain}
$ sudo vi /etc/nginx/sites-available/{domain}

server {
    listen 80;
    # listen [::]:80; IPv6
    server_name {domain...};
    
    # Load configuration files for the default server block.
    include /etc/nginx/conf.d/service-url.inc;
  
    location / {
        if ($http_x_forwarded_proto != 'https') {
            return 301 https://$host$request_uri;
        }
        
        include /etc/nginx/proxy_params;
        
        proxy_pass $service_url;
    }
}
```
> listen 지시문에 의해 80 포트로 들어오는 요청에 대해 server_name 값과 정확하게 일치하는 서버 블록을 찾으려고 시도한다.
> http -> https 요청 변경은 EC2 로드 밸런서 및 대상 그룹 설정이 **선행**되어야한다.
> 

### nginx 로그 레벨
- debug: 디버그 수준의 정보
- info: 정보 수준의 정보
- notice: 일반적인 정보 수준의 정보
- warn: 경고 수준의 정보
- error: 에러 수준의 정보
- crit: 심각한 에러 수준의 정보
- alert: 심각한 상황을 나타내는 정보
- emerg: 시스템 멈춤을 나타내는 정보

```bash
access_log /var/log/nginx/proxy/access.log info;
error_log /var/log/nginx/proxy/error.log error;
```

### 파일 활성화(symbolic link)
```bash
$ sudo ln -s /etc/nginx/sites-available/{domain} /etc/nginx/sites-enabled/
```

### 기본 구성 파일 삭제
```bash
# sites-available & sites-enabled
$ sudo rm /etc/nginx/sites-available/default && sudo rm /etc/nginx/sites-enabled/default
```

### certbot 
certbot은 Let's Encrypt 인증 기관에서 발급한 SSL/TLS 인증서를 편리하게 생성, 발급 및 갱신할 수 있는 오픈소스 도구이다.
일반적으로 SSL/TLS 인증서를 발급받으려면 유료 인증 기관에서 인증서를 발급받아야하는데, 
Let's Encrypt 인증 기관에서는 무료로 인증서를 발급하고 있으며, certbot은 이를 쉽게 생성하고 관리할 수 있는 방법을 제공한다.
다양한 웹 서버 플랫폼 (Apache, Nginx 등)에서 작동하며, 몇 가지 명령어를 사용하여 SSL/TLS 인증서를 자동으로 생성하고 설치할 수 있다. 
또한 생성한 인증서의 만료일이 가까워지면 자동으로 갱신하는 기능도 제공한다.
이를 통해 웹 사이트를 안전하게 보호하고 검색 엔진에서 검색 결과 상위에 노출될 수 있도록 HTTPS 프로토콜을 사용할 수 있다.

> document: [https://certbot.eff.org](https://certbot.eff.org/instructions?ws=nginx&os=ubuntufocal)

#### ACME (Automatic Certificate Management Environment) 프로토콜 
브라우저에서 HTTPS를 지원하는 웹사이트에서 사용하는 SSL/TLS 인증서 발급 및 관리 프로토콜

ACME 프로토콜은 Let's Encrypt라는 공인 인증 기관에서 만들어졌으며, 기존의 SSL/TLS 인증서 발급 방식보다 보다 쉽고 빠르게 SSL/TLS 인증서를 발급 받을 수 있도록 지원한다.

#### 인증서 발급 절차
1. 웹사이트에서 ACME 클라이언트를 통해 인증서 발급 요청
2. 인증기관 서버가 요청을 검증 후 도메인 소유 확인을 위해 도메인 인증서 생성을 요청
3. 클라이언트는 발급 요청 도메인의 웹 서버에 인증서를 생성하고 이를 확인 받는다.
4. 클라이언트는 인증기관 서버에 인증서가 잘 생성되었다는 것을 확인 후 인증서 발급
5. 발급받은 인증서를 서버에 설치하여 HTTPS 암호화 통신이 가능해진다.

ACME 프로토콜은 간단하고 자동화된 인증서 발급 방식을 지원하여 많은 웹사이트에서 사용되고 있다. 
무료로 SSL/TLS 인증서를 발급 받을 수 있어서 SSL/TLS 보안 연결을 사용하는 모든 웹사이트에서 권장되고 있다.

```bash
# core 설치
$ sudo snap install core; sudo snap refresh core
# snap: 리눅스 운영 체제에서 애플리케이션을 더 쉽게 설치하고 관리하기 위한 패키지 매니저

# certbot 재설치 : certbot 명령어 사용시 snap 적용되게 하기 위함이라 공식 가이드상 안내되어있다.
$ sudo apt-get remove certbot
$ sudo snap install --classic certbot

# certbot 활성화
$ sudo ln -s /snap/bin/certbot /usr/bin/certbot

# certbot 도메인 적용
$ sudo certbot --nginx -d {domain} -d {domain} ...

# certbot auto renewal 적용 확인
$ sudo certbot renew --dry-run

# 시스템에서 실행 중인 모든 타이머(unit files) 목록 출력
$ systemctl list-timers
```

> sudo certbot --nginx 도메인 등록시 healthcheck 경로 이슈 발생 가능
> 이슈 발생시 하단 서버 블록 **location ~ /.well-known/acme-challenge/** 추가 후 재검증 진행

### **location 우선순위**
1. = : 정확히 일치
2. ^~ : URI 경로의 접두사가 매칭
3. ~ : 정규표현식(case-sensitive)
4. ~* : 정규표현식(case-insensitive)
5. / : 기본

#### 서버 블록 생성 (listen 443 ssl;)
```bash
# certbot webroot 경로 생성
$ sudo mkdir -p /var/www/certbot/.well-known/acme-challenge

# Remote Host 사용 파일 편집시 업로드 권한 설정 선행
$ sudo chmod 777 /etc/nginx/sites-available/{domain}

# sites-available/{domain}
$ sudo vi /etc/nginx/sites-available/{domain}

server {
    listen 80;
    listen 443 ssl;
    
    server_name {domain...};
    
    access_log /var/log/nginx/proxy/access.log;
    error_log /var/log/nginx/proxy/error.log;
    
    include /etc/nginx/conf.d/service-url.inc;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;
    ssl_certificate /etc/letsencrypt/live/{domain}/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/{domain}/privkey.pem;
    
    # certbot 인증서 발급 (healthcheck 기본 경로)
    location = /.well-known/acme-challenge/ {
        default_type "text/plain";
        root /var/www/certbot;
        
        try_files $uri = 404;
        
        break;
    }
    
    location / {
        proxy_pass $service_url;
    }   
}
```
