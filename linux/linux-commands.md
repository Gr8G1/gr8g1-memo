## Linux

### 명령어
#### chmod [option] [mode] [file]

| Owner | Group | Other |
|---|---|---|
| rwx  |  rwx |  rwx |

**r: 파일 읽기(4) / w: 파일 쓰기(2) / x: 파일 실행(1)**

Ex: **-rwxr-xr-x**  
rwx  : 사용자 자신(Owner)의 사용 권한  
r-x  : 그다음 3개 문자 = 그룹 사용자의 사용 권한   
r-x  : 마지막 3개 문자 = 전체 사용자의 사용 권한

> Owner와 Group은 파일소유자자신과 자신이 속한그룹.   
> Other은 제3자, 웹사이트 방문객은 제3자로 nobody로 취급.
```
  # 설명 
  파일 권한 변경 명령어

  # 옵션
  [OPTION]
    -v	: 모든 파일에 대해 모드가 적용되는 진단(diagnostic) 메시지 출력.
    -f	: 에러 메시지 출력하지 않음.
    -c	: 기존 파일 모드가 변경되는 경우만 진단(diagnostic) 메시지 출력.
    -R	: 지정한 모드를 파일과 디렉토리에 대해 재귀적으로(recursively) 적용.
  [MODE]
    u,g,o,a : 소유자(u), 그룹(g), 그 외 사용자(o), 모든 사용자(a) 지정.
    +,-,=   : 현재 모드에 권한 추가(+), 현재 모드에서 권한 제거(-), 현재 모드로 권한 지정(=)
    r,w,x   : 읽기 권한(r), 쓰기 권한(w), 실행 권한(x)
    X       : "디렉토리" 또는 "실행 권한(x)이 있는 파일"에 실행 권한(x) 적용.
    s       : 실행 시 사용자 또는 그룹 ID 지정(s). "setuid", "setgid".
    t       : 공유모드에서의 제한된 삭제 플래그를 나타내는 sticky(t) bit.
    0~7     : 8진수(octet) 형식 모드 설정 값.
```

#### kill \[Option] \[PID]
```
  # 설명
  프로세스를 지정하고 신호(Signal)를 보내서 제어하는 명령어

  # 옵션
  [Option]
    -9	: 프로세스 아이디(PID)를 직접 지정하여 종료시 사용
    -l	: 신호(Signal)로 사용할 수 있는 신호(Signal) 목록 출력

  # 신호
  번호	신호(Signal)이름	신호(Signal)	의미
  1	SIGHUP HUP	hangup	 로그아웃등의 접속이 끊을 때 발생하는 신호(Signal)로 특정 실행 중인 프로그램이 사용하는 설정 파일을 변경시키고 변화된 내용을 적용할때 사용
  2	SIGINT INT	현재 작동중인 프로그램의 동작을 멈출때 사용되며, 일반적인 값은 CTRL + c 이다.
  9	SIGKILL	KILL	프로그램을 무조건 종료할 경우 사용
  11	SIGSEGV	SEGV	잘못된 메모리 관리시 생기는 신호(Signal)
  15	SIGTERM	TERM	실행중인 프로그램을 정상적인 종료방법으로 프로그램을 종료하는 신호(Signal)로 kill 명령에서 신호(Signal)를 지정하지 않으면 이 신호(Signal)를 사용하여 프로그램을 종료.
  18	SIGCONT	CONT	중지 되어 있는 프로그램을 재실행 하는데 사용되는 신호(Signal)
  19	SIGSTOP	STOP	프로그램을 중지 하는데 사용되는 신호(Signal)
  20	SIGTSTP	TSTP	터미널에서 중지되어 있는 신호(Signal)
```

#### ps [option]
```
  # 설명
  현재 실행중인 (사용자) 프로세스 출력

  #옵션
  [Option]
    -e (everything)	: 모든(사용자, 다른 사용자 포함) 프로세스 출력
    -f (full-format)	: 상세 내역 포함 출력
    -p (pid)		: 프로세스 아이디를 넘기면 특정 프로세스의 정보만 출력
    -u (username)		: 사용자 이름을 넘기면 해당 사용자가 실행 중인 프로세스를 출력
    -h (hierarchy)	: 부모와 자식 프로세스의 관계를 들여쓰기를 통해 시각화하여 출력
```

#### grep [option] [regexp] [file]
```
  # 설명
  파일에서 특정 패턴을 찾아서 그 라인(행)을 출력한다.

  # 옵션
  [Option]
    -c, --count			 : 패턴 매칭된 라인 수 출력
    -n, --line-number		 : 패턴 매칭된 라인(행)과 라인번호 함께 출력 (각 파일은 1행부터 시작됨) |
    -H, --with-filename		 : 패턴 매칭된 라인(행)과 파일 이름 함께 출력
    -h, --no-filename		 : 파일 이름 무시
    -i, --ignore-case		 : 문자열의 대소문자 무시
    -L, --files-without-match	 : 패턴 매칭되지 않는 파일 이름 출력
    -l, --files-with-mathces	 : 패턴 매칭되는 파일 이름 출력
    -v, --invert-match		 : 패턴 매칭되지 않는 라인(행) 출력
    -o, --only-matching		 : 패턴 매칭되는 단어 출력 (패턴이 abc이면 abcde에서 abc만 출력되는 방식)
    -e PATTERN, --regexp=PATTERN : 검사할 PATTERN을 명시함. (-로 시작하는 패턴 등에 사용하면 유용)
    -w, --word-regexp		 : 단어 단위로 매칭되는 라인(행) 출력
    -x, --line-regexp		 : 라인 단위로 매칭되는 라인(행) 출력
    -a, --text			 : 바이너리파일을 텍스트파일처럼 처리
    -A NUM, --after-context=NUM	 : 패턴 매칭된 이후의 라인을 NUM수 만큼 출력
    -B NUM, --before-context=NUM : 패턴 매칭된 이전의 라인을 NUM수 만큼 출력
    -C NUM, -NUM, --context=NUM	 : 패턴 매칭된 라인의 앞뒤 라인을 NUM수 만큼 출력 (매칭된 라인은 제외)
    -b, --byte-offest		 : 패턴 매칭된 라인에서 패턴까지의 바이트 수
    -E, --extened-regexp	 : 확장 정규식 사용
    -F, --fixed-strings		 : 정규식을 무시하고 문자그대로 인식
    -q, --quiet, --silent	 : 메시지 출력안함. 종료 상태만 반환
    -s, --no-messages		 : 존재하지 않거나 읽기 불가 파일로 인한 오류 메시지 출력안함
    -r, --recursive		 : 재귀적으로 하위 디렉토리도 검사
  그외... 
```

#### nohup [process_name] &
```
  # 설명
  쉘스크립트파일 (*.sh)을 데몬 형태로 실행시키는 프로그램
  터미널 세션이 끊겨도 실행을 멈추지 않고 동작하도록 한다.
  > 백그라운드 실행
  
  # 옵션
  [Process_NAME]
    
  # 사용법
  nohup [Process_NAME] & : 표준 출력(standard output)을 nohup.out 파일로 재지향(redirection) 상시 기록
  nohup [Process_NAME]  1> /dev/null 2 >&1 & : 1> /dev/null = 표준 출력을 사용하지 않겠다는 의미이고, 2>&1 은 표준 에러를 표준 출력과 같게 만드는 명령
```

#### tail [option] [filename]
```
  # 설명
  해당하는 파일의 마지막 부분을 읽어 내용을 확인할 수 있는 명령어
  
  # 옵션
  -f			: tail을 종료하지 않고 파일의 업데이트 내용을 실시간으로 계속 출력한다.
  -n (line count)	: 파일의 마지막줄부터 지정한 라인수까지의 내용을 출력한다.
  -c (byte count)	: 파일의 마지막부터 지정한 바이트만큼의 내용을 출력한다.
  -q			: 파일의 헤더와 상단의 파일 이름을 출력하지 않고 내용만 출력한다.
  -v			: 출력하기전에 파일의 헤더와 이름 먼저 출력한 후 파일의 내용을 출력한다.
    
  # 사용법
  tail -f filename.txt
```

#### ln [option] [origin-file] [link-file]
```
  # 설명
  symbolic link를 생성하는 명령어
  
  # 옵션
  -s: symbolic link를 생성
  -f: 대상 파일이 이미 존재하면 덮어쓰기
  -i: 대상 파일이 이미 존재하면 덮어쓸지 여부 선택
  -v: symbolic link를 생성할 때마다 생성된 파일 이름을 출력
  
  # 사용법
  ln -s /home/user1/file1 /home/user2/file2
```

#### truncate [option] [size] [file]
```
  # 설명
  파일의 크기를 변경하는 명령어
  > 일반적으로 파일의 크기를 줄이는데 사용 만약 파일의 크기를 0으로 줄이면, 파일의 내용이 모두 삭제된다.
  
  # 옵션
  -s, --size: 파일의 크기를 설정
  -c, --no-create: 파일이 존재하지 않으면 파일을 생성하지않는다
  -o, --io-blocks: 파일 크기를 I/O 블록 크기 단위로 지정
  -r, --reference: 지정한 파일의 크기와 동일하게 파일 크기를 조절

  # 사용법
  truncate -s 0 /example/log/error.log
```

#### Memory
```
  # 설명
  메모리 정보 / 메모리 사용량과 여유량 그리고 캐싱으로 사용되는 메모리가 얼마나 있는지 파악 할 수 있는 명령어
  
  # 명령어
  total		: 설치된 총 메모리 크기 / 설정된 스왑 총 크기
  used		: total에서 free, buff/cache를 뺀 사용중인 메모리. / 사용중인 스왑 크기
  free		: total에서 used와 buff/cahce를 뺀 실제 사용 가능한 여유 있는 메모리량 / 사용되지 않은 스왑 크기
  shared	: tmpfs(메모리 파일 시스템), ramfs 등으로 사용되는 메모리. 여러 프로세스에서 사용할 수 있는 공유 메모리
  buffers	: 커널 버퍼로 사용중인 메모리
  cache		: 페이지 캐시와 slab으로 사용중인 메모리
  buff/cache	: 버퍼와 캐시를 더한 사용중인 메모리
  available	: swapping 없이 새로운 프로세스에서 할당 가능한 메모리의 예상 크기. (예전의 -/+ buffers/cache이 사라지고 새로 생긴 컬럼)
  
  # 옵션
  -h			: 사람이 읽기 쉬운 단위로 출력한다.
  -b | -k | -m | -g	: 바이트, 키비바이트, 메비바이트, 기비바이트 단위로 출력한다.
  --tebi | --pebi	: 테비바이트, 페비바이트 단위로 출력한다.
  --kilo		: 킬로바이트, 메가바이트, 기기바이트, 페타바이트 단위로 출력한다.
  [--kilo, --mega | --giga | --tera | --peta]
  -w			: 와이드 모드로 cache와 buffers를 따로 출력한다.
  -c '반복'		: 지정한 반복 횟수 만큼 free를 연속해서 실행한다.
  -s '초' 		: 지정한 초 만큼 딜레이를 두고 지속적으로 실행한다.
  -t 			: 합계가 계산된 total 컬럼줄을 추가로 출력한다.

  # 사용법
  free -h : 
```

#### Changing
```
& (Ampersand)		: 이 명령은 프로세스/스크립트/명령어를 백그라운드로 보낸다.
&& (Logical AND)	: 이 연산자 다음에 오는 명령은, 이 연산자 앞의 명령이 성공적으로 실행되었을 경우에만 실행된다.
|| ( Logical OR)	: 이 연산자 앞의 명령이 실패한 경우에만, 이 연산자 뒤의 명령이 실행된다.
| (Pipe)		: 이 명령은 첫 번째 명령의 출력을 두 번째 명령의 입력으로 사용한다.
! (NOT)			: 명령 안의 표현식 부정
; (Semi-colon)		: 이 연산자 앞의 명령이 성공적으로 실행되지 않았더라도, 이 연산자 뒤의 명령은 실행된다.
>, >>, < (Redirection)	:  명령 또는 명령 그룹의 출력을 파일이나 스트림으로 리디렉션한다.
&&-|| (AND-OR)		: 이 연산자는 AND OR 연산자의 조합으로, if-else 문과 비슷하다.
\ (Concatenation)	: 쉘에서 여러 줄에 걸쳐 큰 명령을 연결하는 데 사용된다.
() (Precedence)		: 명령을 우선 순위 순서대로 실행할 수 있게 한다.
{} (Combination)	: 이 연산자 다음에 오는 명령은, 첫 번째 명령의 실행 결과에 따라 실행된다.
```
