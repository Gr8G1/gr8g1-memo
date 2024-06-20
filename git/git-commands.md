## Git-commands

### .gitignore cached 이슈
```bash
# 캐시된 목록 출력
git ls-files --cached

# 캐시된 내역 제거
git rm -r --cached .         # 모든 파일과 디렉토리 제거
git rm -r --cached ${PATH}   # 특정 경로 또는 파일
```

### 서브모듈 관련
- 저장소 내려받기

```
# 저장소 내려받기
git clone <REMOTE-URL>

# 저장소 & 서브모듈 내려받기
git clone --recurse-submodules <REMOTE-URL>
```

- 초기화 & 업데이트
```
git submodule update --init --remote --rebase
  --init: 서브모듈을 초기화 (서브모듈을 처음 가져올 때 사용)
  --recursive: 중첩된 서브모듈까지 모두 초기화하고 업데이트
  --remote: 서브모듈의 원격 저장소에서 최신 커밋을 가져온다
  --rebase: 서브모듈의 변경 사항을 메인 프로젝트의 브랜치에 리베이스한다.
```

- 추가
```
git submodule add <REMOTE-URL> <경로|디렉토리명>
  - 브랜치 변경 방법
    : git config -f .gitmodules submodule.<경로|디렉토리명>.branch <브랜치명>
    : .gitsubmodule 파일 수정
git submodule add -b <브랜치명> <REMOTE-URL> <경로|디렉토리명>
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
# 서브모듈 변경사항 상태(status) 간략 표기
git config --global diff.submodule log

# 서브모듈의 변경사항이 커밋되지 않은 경우, 메인 프로젝트의 푸시 거부 설정
git config --global push.recurseSubmodules check
 
** 사용자 지정 Git 명령어 추가 **
# 서브모듈 업데이트
git config --global alias.subup 'submodule update --remote --rebase'

# 메인 & 서브모듈 브랜치 변경
git config --global alias.all '!''f() { git $@; git submodule foreach git $@; }; f'
  - '!': Git 명령을 정의
  - 'f() { ... }': 셸 함수 정의
  -  Git 명령 부분: $@: 사용자 입력값 치환
```
