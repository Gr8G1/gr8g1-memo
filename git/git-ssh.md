## GitHub SSH 키 교체 절차 (macOS)

### 1) 로컬 SSH 디렉터리 확인

```bash
ls -al ~/.ssh
```

확인 포인트:

- 기존 개인키: `~/.ssh/id_ed25519`
- 기존 공개키: `~/.ssh/id_ed25519.pub`
- 설정 파일: `~/.ssh/config`

### 2) 현재 ssh-agent 상태 확인

```bash
ssh-add -l
```

예상 결과:

- `The agent has no identities.` → 에이전트에 키가 안 올라간 상태. 정상일 수 있음.

### 3) 현재 GitHub용 SSH 설정 확인

```bash
cat ~/.ssh/config
```

예시 (`~/.ssh/config` 내용):

```text
# Host github.com
#   HostName github.com
#   User git
#   IdentityFile ~/.ssh/id_ed25519
#   AddKeysToAgent yes
#   UseKeychain yes
```

---

### 새 SSH 키 생성

기존 키를 덮어쓰지 않고 새 파일명으로 생성한다. 먼저 `id_ed25519_` 뒤에 붙일 **키 식별 이름**을 정한다(공백 없이 영문·숫자·underscore 권장). 예: `github_macbook`, `work_laptop`.

아래 명령의 `YOUR_KEY_NAME`은 그 이름으로 **그대로 치환**한다. 이후 단계의 경로도 모두 같은 이름을 쓴다.

```bash
ssh-keygen -t ed25519 -C "YOUR_GITHUB_EMAIL" -f ~/.ssh/id_ed25519_YOUR_KEY_NAME
```

설명:

- `YOUR_GITHUB_EMAIL` → GitHub 계정 이메일로 변경
- `YOUR_KEY_NAME` → 위에서 정한 SSH 키 식별 이름으로 변경(예: `github_macbook`이면 파일은 `~/.ssh/id_ed25519_github_macbook`)
- 패스프레이즈는 설정 권장

생성 결과:

- 개인키: `~/.ssh/id_ed25519_YOUR_KEY_NAME`
- 공개키: `~/.ssh/id_ed25519_YOUR_KEY_NAME.pub`

---

### 새 키를 ssh-agent에 등록

**1. ssh-agent 실행**

```bash
eval "$(ssh-agent -s)"
```

**2. 새 키 추가**

```bash
ssh-add --apple-use-keychain ~/.ssh/id_ed25519_YOUR_KEY_NAME
```

위 명령이 동작하지 않으면:

```bash
ssh-add -K ~/.ssh/id_ed25519_YOUR_KEY_NAME
```

**3. 등록 확인**

```bash
ssh-add -l
```

예상 결과: 새 키 fingerprint가 출력되어야 함.

---

### 공개키 복사

```bash
pbcopy < ~/.ssh/id_ed25519_YOUR_KEY_NAME.pub
```

클립보드에 공개키가 복사된 상태.

---

### GitHub에 새 SSH 키 등록

**경로:** GitHub → 프로필 사진 → Settings → SSH and GPG keys

**절차**

1. **New SSH key** 클릭
2. 입력:
   - **Title:** GitHub에서 구분할 라벨(위 키 이름과 같게 하거나, 예: `MacBook-GitHub`)
   - **Key type:** Authentication
   - **Key:** 방금 복사한 공개키 붙여넣기
3. **Add SSH key** 클릭

주의: 기존 키는 바로 삭제하지 말고, 새 키 동작 확인 후 삭제하는 것이 안전.

---

### 로컬 SSH 설정을 새 키로 변경

기존 `~/.ssh/config`에서 GitHub용 `IdentityFile`을 새 키로 변경.

**최종 권장 설정:**

```text
Host github.com
  HostName github.com
  User git
  IdentityFile ~/.ssh/id_ed25519_YOUR_KEY_NAME
  IdentitiesOnly yes
  AddKeysToAgent yes
  UseKeychain yes
```

**덮어쓰기 방식**

```bash
cat > ~/.ssh/config <<'EOF'
Host github.com
HostName github.com
User git
IdentityFile ~/.ssh/id_ed25519_YOUR_KEY_NAME
IdentitiesOnly yes
AddKeysToAgent yes
UseKeychain yes
EOF

chmod 600 ~/.ssh/config
```

---

### 연결 테스트

**1. GitHub SSH 연결 테스트**

```bash
ssh -T git@github.com
```

예상 결과: GitHub 계정명과 함께 인증 성공 메시지 출력.

**2. Git 원격 확인**

```bash
git remote -v
```

예상 결과: `git@github.com:...` 형태면 SSH 사용 중.

**3. 원격 접근 확인**

```bash
git ls-remote
```

예상 결과: 원격 ref 목록이 출력되면 정상.

---

### 기존 GitHub SSH 키 삭제

새 키가 정상 동작하는 것이 확인되면 GitHub 웹에서 기존 SSH 키 삭제.

**경로:** GitHub → Settings → SSH and GPG keys

**조치:** 기존에 등록돼 있던 이전 키 삭제.

원칙: 새 키 정상 동작 확인 후 기존 키 삭제.

---

### 로컬 기존 키 백업

기존 키는 바로 삭제하지 말고 이름만 바꿔 백업.

```bash
mv ~/.ssh/id_ed25519 ~/.ssh/id_ed25519_old_backup
mv ~/.ssh/id_ed25519.pub ~/.ssh/id_ed25519_old_backup.pub
```

---

### 기존 키를 ssh-agent에서 제거

```bash
ssh-add -d ~/.ssh/id_ed25519 2>/dev/null
ssh-add -l
```

예상 결과: 새 키만 남아 있거나, 필요 시 다시 추가 가능.

---

### 최종 확인 체크리스트

- 새 SSH 키 생성 완료
- 새 공개키 GitHub 등록 완료
- `~/.ssh/config`가 새 키를 가리키도록 변경 완료
- `ssh -T git@github.com` 성공
- `git ls-remote` 성공
- GitHub의 기존 SSH 키 삭제 완료
- 로컬 기존 키는 백업 파일명으로 변경 완료

---

### 문제 발생 시 점검 포인트

**1. `ssh -T git@github.com` 실패**

확인:

- GitHub에 공개키 등록했는지
- `~/.ssh/config`의 `IdentityFile` 경로가 맞는지
- `ssh-add -l`에 새 키가 보이는지

**2. 여전히 옛 키를 쓰는 것 같음**

```bash
ssh -vT git@github.com
```

확인: verbose 로그에서 실제로 사용 중인 `IdentityFile` 확인.

**3. config 파일 권한 문제**

```bash
chmod 600 ~/.ssh/config
chmod 700 ~/.ssh
```

---

### 권장 운영 원칙

- GitHub용 SSH 키는 다른 용도와 분리해서 파일명 구분.
- 기존 키는 바로 삭제하지 말고 최소 1회 정상 테스트 후 정리.
- 개인키는 메신저, 클라우드 메모, 문서 파일에 복사해 두지 않기.
- 키 파일과 비밀번호/설명 문서를 같은 폴더에 두지 않기.
