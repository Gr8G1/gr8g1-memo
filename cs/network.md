## 네트워크

#### 네트워크 통신방식
|     타입     |  통신대상   |         범위         | IPv4 | IPv6 |     Ex     |
|:------------:|:-----------:|:--------------------:|:----:|:----:|:----------:|
|  유니캐스트  |     1:1     |    전체 네트워트     |  O   |  O   |    http    |
| 브로드캐스트 |    1:All    | 서브넷(로컬네트워크) |  O   |  X   |    ARP     |
|  멀티캐스트  |   1:Group   |     정의된 구간      |  O   |  O   |    방송    |
|  애니캐스트  |     1:1     |    전체 네트워크     | 일부 |  O   | 6 to 4 DNS |

### 인터넷 프로토콜 스위트(Internet Protocol Suite)

인터넷에서 통신을 할 때 사용되는 프로토콜들의 집합
TCP/IP 프로토콜 스위트라고도 불리며, TCP와 IP를 비롯하여 다른 프로토콜들을 포함하고 있다.

#### TCP/IP 4 계층
| 계층  |           이름           |                                       -                                       |
|:-----:|:------------------------:|:-----------------------------------------------------------------------------:|
|   4   |        응용 계층         | NS, TFTP, TLS/SSL, FTP, HTTP, IMAP, IRC, NNTP, POP3, SIP, SMTP, SNMP, SSH ... |
|   3   |        전송 계층         |                                 TCP, UDP ...                                  |
|   2   |       인터넷 계층        |                                IP (IPv4, IPv6)                                |
|   1   | 네트워크 인터페이스 계층 |     이더넷, Wi-Fi, 토큰링, PPP, SLIP, FDDI, ATM, 프레임 릴레이, SMDS ...      |

### TCP(Transmission Control Protocol)

- 연결 방식: 연결형 서비스 (패킷을 전송하기 위한 논리적 경로를 배정)
- 패킷 교환 방식: 가상 회선 방식
- 전송 순서: 순서 보장
- 수신 확인: 수신 상태 확인
- 통신 방식: 1:1
- 신뢰성: 높음
- 속도: 느림

#### TCP 특징
1. 연결 지향 방식이다.
2. 3-way handshaking 과정을 통해 연결을 설정하고 4-way handshaking을 통해 해제한다.
3. 흐름 제어 및 혼잡 제어.
4. 높은 신뢰성을 보장한다.
5. UDP 보다 속도가 느리다.
 
**전이중(Full-Duplex), 점대점(Point to Point) 방식**
  > 연속성보다 신뢰성있는 전송이 중요할 때에 사용하는 프로토콜

#### TCP 3-way handshaking
통신을 하기 전 3-way handshaking을 통해서 연결 설정을 맺고, 연결을 끝낼 때도 4-way handshaking을 통해서 통신이 이루어지기 때문에 신뢰성이 있는 프로토콜이라고 부른다.
- Ex: Host A(Client) <=> HostB(Server)
  - SYN(Sequence Number) FIN 전송 <=> ACK(Acknowledge Character) 확인[긍정] 응답 문자 주고받으며 연결 설정은 맺는다.

#### TCP는 패킷을 어떻게 추적 및 관리하나?
A, B, C라는 패킷들이 발신지에서 출발하여 수신지으로 간다고 할때 패킷들이 A, B, C에 순차적으로 도착하는 기대 상황에서 B가 분실되었다고해도
A, B, C라는 패킷에 1, 2, 3이라는 번호를 부여하여 패킷을 나누어 보내고 분실 확인과 같은 처리를하여 목적지에서 재조립 한다.
위 방식으로 TCP는 패킷을 패킷들을 추적 관리한다.

#### 패킷(Packet)이란?
  - 인터넷 내에서 데이터를 보내기 위한 경로배정(라우팅)을 효율적으로 하기 위해서
    데이터를 여러 개의 조각들로 나누어 전송을 하는데 이때, 이 조각을 패킷이라고 한다.

### UDP(User Datagram Protocol)

- 연결방식: 비연결형 (패킷을 전송하기 위한 논리적 경로가 없다)
- 패킷 교환 방식: 데이터그램 방식
- 전송 순서: 순서 미보장
- 수신 확인: 수신 상태 미확인
- 통신 방식: 1:N | 1:N | 1N:N
- 신뢰성: 낮음
- 속도: 빠름
  > 데이터그램: 독립적인 관계를 지니는 패킷
  > 신뢰성보다는 연속성이 중요할때 사용한다.

#### UDP 특징
1. 비연결형 서비스로 데이터그램 방식을 제공한다
2. 정보를 주고 받을 때 정보를 보내거나 받는다는 신호절차를 거치지 않는다.
3. UDP 헤더의 CheckSum 필드를 통해 최소한의 오류만 검출한다.
4. 신뢰성이 낮다
5. TCP 보다 속도가 빠르다

### IP(Internet Protocol)

#### IP 구조
IP 주소는 32bit(4byte) 길이로 구성된 논리적인 주소체계로서 OOO.OOO.OOO.OOO (IPv4 기준: 192.168.123.123)로 표기, '.' 으로 구분된 Octet(8bit / 1byte) 4개가 조합되어 IP주소를 나타나게 되는 것.
실제 IP는 2진수로 표기되어 xxxxxxxx.xxxxxxxx.xxxxxxxx.xxxxxxxx (x: 0 | 1)와 같은 형태로 구분되어 사람이 이해하고 외우기 어렵기에 때문에 10진수 표기법으로 사용되는것이다.
**dot-decimal notice**로 불리기도 한다.

#### MAC(Media Access Control) : 물리적 주소 체계
MAC 주소 또는 Physical Address라고도 한다.
LAN(Local Area Network) 또는 Ethernet(컴퓨터 네트워크 기술의 하나로, 일반적으로 LAN, MAN 및 WAN에서 가장 많이 활용되는 기술 규격이다)에서 통신을 하기 위하여 사용된다.
MAC 주소는 자신이 속한 네트워크(LAN) 안에서만 통신이 되며, 이후 네트워크를 빠져나가는 장치인 라우터를 지나게 되면 IP를 이용하여 통신하게 된다.

#### ARP(Address Resolution Protocol) : 주소 결정 프로토콜
네트워크 상에서 IP 주소를 물리적 네트워크 주소로 대응(bind)시키기 위해 사용되는 프로토콜이다.
IP 호스트 A가 IP 호스트 B에게 IP 패킷을 전송하려고 할 때 IP 호스트 B의 물리적 네트워크 주소를 모른다면,
ARP를 사용하여 목적지 IP 주소 B와 브로드캐스팅 물리적 네트워크 주소 XXXXXXXXXXXX를 가지는 ARP 패킷을 네트워크 상에 전송한다.
IP 호스트 B는 자신의 IP 주소가 목적지에 있는 ARP 패킷을 수신하면 자신의 물리적 네트워크 주소를 A에게 응답한다.
이와 같은 방식으로 수집된 IP 주소와 이에 해당하는 물리적 네트워크 주소 정보는 각 IP 호스트의 ARP 캐시라 불리는 메모리에 테이블 형태로 저장된 다음, 패킷을 전송할 때에 다시 사용된다.
ARP와는 반대로, IP 호스트가 자신의 물리 네트워크 주소는 알지만 IP 주소를 모르는 경우, 서버로부터 IP주소를 요청하기 위해 RARP를 사용한다.

#### Subnet mask
IP를 사용할 때는 Subnet mask라는 것이 무조건 따라오게 된다.  
Subnet mask는 Network ID 와 Host ID를 구분해주는 역할을 하여 Network ID를 올리거나 낮출 수 있고, 반대로 Host ID를 늘리거나 줄일 수 있다.

#### Network ID / Host ID
- IP 주소에는 Network ID와 Host ID가 존재하고 있다.
- Network ID / Host ID
  - IP Class 개념에 따라 다르게 설정된다.
  - A Class인 경우
    - 처음 octet(8bit)이 Network ID이며 나머지 octet(24bit)이 Host ID로 사용된다.  
      가장 왼쪽 bit가 0으로 고정되어 시작하기 때문에 네트워크는 0 ~ 127까지 할당되고
      네트워크 할당은 128곳에 사용가능하고, 호스트 수는 최대 16,777,214개 이다.

- IP Class 개념
- IP Class의 경우 A,B,C,D,E Class로 나누어 Network ID와 Host ID를 구분하게 된다.

> 한국인터넷정보센터: https://xn--3e0bx5euxnjje69i70af08bea817g.xn--3e0b707e/jsp/resources/ipv4Info.jsp

### Domain

scheme:[//[user[:password]@host[:port]][/path][?query][#fragment]
URI(Uniform Resource Identifier) https://www.example.com:8080/ex(index.html)?q=1#index1
URL(Uniform Resource Locator) = https://www.example.com:8080/(index.html)

#### 구조
- scheme(protocol): http(s?)://
- user[:password]: 데이터가 있는 서버에 접근하기 위해 필요한 ID와 PASSWORD
- host: www.example.com:
  - www: subdomain
  - domain: example.com
- port: :8080
- url-path | path
  - directory: /ex
  - file: index.html
  - query: ?q=1
- fragment
  - #index1

#### DNS(Domain Name System)란?
사람이 읽을 수 있는 도메인 이름(예: www.example.com)을 머신이 읽을 수 있는 IP 주소(예: 192.0.2.44)로 변환하는 역활한다.

#### 도메인 관리
- ICANN 비영리 단체 (전체 도메인 관리)
- registry(도메인 관리기간: 각 나라별)와 registrar(중개업체(인가))가 있다.

#### 도메인 종류
- gTLD: generic Top Level Domain
  - com, .net, .org, .edu, .gov, .int, .mil, .biz, .name, .info ...
- ccTLD: country code Top Level Domain
  - .kr, .us, .ca, .jp ...

### Well-known port
- 포트_목록: [https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers](https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers)

### 그밖에 용어
- ISP: Internet service provider
