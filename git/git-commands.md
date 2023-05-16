## Git-commands

### .gitignore cached 이슈
```bash
# 캐시된 목록 출력
git ls-files --cached

# 캐시된 내역 제거
git rm -r --cached .       # 모든 파일과 디렉토리 제거
git rm -r --cached ${PATH} # 특정 경로 또는 파일
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
alias gla='git log --branches --graph --decorate --date-order --date=format:"%Y-%m-%d %H:%M:%S" --pretty=format:"%C(auto)%d [%C(bold yellow)%h%C(reset)] - %C(white)%s %C(magenta)[%C(bold green)%an : %C(bold red)%ar%C(magenta)]"'
```
