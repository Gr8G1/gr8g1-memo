## Git-commands

### .gitignore cached 이슈
```bash
# 캐시된 목록 출력
git ls-files --cached

# 캐시된 내역 제거
git rm -r --cached .         # 모든 파일과 디렉토리 제거
git rm -r --cached ${PATH}   # 특정 경로 또는 파일
```

### Alias
```bash
# git repo commands
alias grc='gh repo create --public --source=. --remote=upstream'

# git commands simplified
alias la='ls -al'
alias gst='git status'
alias gra='git restore .'
alias gba='git branch -a'
alias gla='git log --graph --decorate --date-order --date=format:"%Y-%m-%d %H:%M:%S" --pretty=format:"%C(auto)%d [%C(bold yellow)%h%C(reset)] - %C(white)%s %C(magenta)[%C(bold green)%an : %C(bold red)%ar%C(magenta)]"'
```

### 서브모듈 관련
- 추가
```
git submodule add <REMOTE-URL> <경로|디렉토리명>

git submodule add -b <브랜치명> <REMOTE-URL> <경로|디렉토리명>
  - <REMOTE-URL>: [https|ssh] 경로
  - 브랜치 변경: git config -f .gitmodules submodule.<경로|디렉토리명>.branch <브랜치명>
```

- 초기화 & 업데이트
``` 
git submodule init
git submodule update --init --recursive
git submodule update --remote --rebase
```

- 삭제
``` 
git submodule deinit -f <서브모듈 경로>
rm -rf <서브모듈 디렉토리>
rm .gitmodules
  - 하위 서브모듈 없을경우 삭제 
git rm --cached <서브모듈 디렉토리>
```

- 상태확인
```
git submodule status
```

- 설정
```
git config --global diff.submodule log
  - 서브모듈 변경사항 상태(status) 간략 표기
git config --global push.recurseSubmodules check
  - 서브모듈의 변경사항이 커밋되지 않은 경우, 메인 프로젝트의 푸시 거부 설정

* 사용자 지정 Git 명령어 추가
git config --global alias.subup 'submodule update --remote --rebase'
  - 서브모듈 업데이트(fetch)
git config --global alias.all '!''f() { git $@; git submodule foreach git $@; }; f'
  - '!': Git 명령을 정의
  - 'f() { ... }': 셸 함수 정의
  -  Git 명령 부분: $@: 사용자 입력값 치환
```
