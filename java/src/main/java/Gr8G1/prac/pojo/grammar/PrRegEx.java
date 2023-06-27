package Gr8G1.prac.pojo.grammar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrRegEx {
  /*
   * # RegEx
   *
   * ! Method 사용 주의
   *  String replace(char oldChar, char newChar),
   *  String replace(CharSequnce target, CharSequence replacement)
   *   - 인자로 주어진 이전 문자열을 새로운 문자열로 치환한다. (**정규식 패턴 사용불가**)
   *
   * Methods
   *  boolean matches(String regex)
   *   - 인자로 주어진 정규식에 매칭되는 값이 있는지 확인한다.
   *  String replaceAll(String regex, String replacement)
   *   - 문자열내에 있는 정규식 regex와 매치되는 모든 문자열을 replacement 문자열로 치환된 문자열을 반환한다.
   *  String[] split(String regex)
   *   - 인자로 주어진 정규식과 매치되는 문자열을 구분자로 분할한다.
   *  String join(CharSequence delimiter, CharSequence... elements)
   *   - 분할된 문자열을 구문자로 이은 문자열을 반환한다.
   *
   * Pattern Class
   *  - 정규식의 컴파일된 표현
   *  - Pattern 클래스는 공개된 생성자를 제공하지 않는다.
   *  - 패턴을 생성하려면 Pattern객체를 반환하는 정적 compile 메소드를 호출해야 한다.
   *  - 이 메소드는 첫 번째 인자로 정규식 문자열을 받아들인다.
   *
   * Matcher Class
   *  - 패턴을 해석하고 입력 문자열에 대해 일치 작업을 수행하는 엔진
   *  - Pattern 클래스와 마찬가지로 Matcher는 어떤 공개된 생성자도 정의하고 있지 않다.
   *  - Matcher객체는 Pattern 객체의 matcher 메소드를 호출해서 얻을 수 있다.
   *
   * PatternSyntaxException 객체
   *  - 정규식 패턴의 문법 오류를 나타내는 unchecked 예외
   *
   * Cheat Sheet
   *  - Java 정규식의 "\" -> "\\[정규식구문]" ***Escape*** 처리 필수
   *  - global flag 존재하지 않는다. (replaceAll 메소드 활용 처리 필수)
   *
   * ~ flags
   *  - Pattern.CANON_EQ : None
   *    - 표준화된 매칭 모드 a를 나타내는 유니코드 "\u00E5"와 a와 상단고리 유니코드 "a\u030A"를 같다고 매칭한다.
   *  - Pattern.CASE_INSENSITIVE : (?i)
   *    - 대소문자를 구분하지 않는다.
   *  - Pattern.COMMENTS : (?x)
   *    - 공백과 주석을 무시한다.
   *  - Pattern.MULTILINE : (?m)
   *    -  다중행 모드 사용하여 모든 ^와 $가 인식된다. (Default: 입력값 전체를 하나의 시작과 끝으로 인식)
   *  - Pattern.DOTALL : (?s)
   *    - "."이 개행문자까지 포함하는 모든 문자로 매칭된다.
   *  - Pattern.LITERAL : None
   *    - 메타문자와 이스케이프된 문자를 일반 문자로 취급한다 (CASE_INSENSITIVE와 UNICODE_CASE는 기능이 유지)
   *  -  Pattern.UNICODE_CASE : (?u)
   *    - 대소문자 매칭이 유니코드 표준을 따른다. (Default : US-ASCII 문자 집합)
   *  -  Pattern.UNIX_LINES : (?d)
   *    - ^와 $를 처리시 UNIX 개행을 사용한다.
   *
   * ~ Anchors
   *  - ^start
   *    - start 로 시작하는 문자열과 매칭한다.
   *  - end$
   *    - end로 끝나는 문자열과 매칭한다.
   *  - ^start end$
   *    - start end와 정확하게 일치하는 문자열을 매칭한다.
   *  - abc
   *    - abc 문자열과 매칭한다.
   *
   * ~ Quantifiers
   *  - abc*
   *    - ab 그리고 0개 이상의 c 를 포함한 문자열과 매칭한다.
   *  - abc+
   *    - ab 그리고 1개 이상의 c 를 포함한 문자열과 매칭한다.
   *  - abc?
   *    - ab 그리고 0개 또는 1개의 c 를 포함한 문자열과 매칭한다.
   *  - abc{2}
   *    - ab 그리고 2개의 c 를 포함한 문자열과 매칭한다.
   *  - abc{2,}
   *    - ab 그리고 2개 이상의 c 를 포함한 문자열과 매칭한다.
   *  - abc{2,5}
   *    - ab 그리고 2개 이상 5개 이하의 c 를 포함한 문자열과 매칭한다.
   *  - a(bc)*
   *    - a 그리고 0개 이상의 bc를 포함한 문자열과 매칭한다.
   *  - a(bc){2,5}
   *    - a 그리고 2개 이상 5개 이하의 bc를 포함한 문자열과  매칭한다.
   *  - ab*?
   *    - a 그리고 b를 0개 이상 포함된 문자열중 가장 적게 일치하는 문자열과 매칭한다.
   *
   * ~ OR
   *  - a(b|c) or a[bc]
   *    - a 그리고 b 또는 c를 포함한 문자열과 매칭한다.
   *
   * ~ Character
   *  - \d
   *    - 숫자 하나와 매칭한다.
   *  - \D
   *    - 숫자가 아닌 문자 하나와 매칭한다.
   *  - \w
   *    - 문자(알파벳, 언더바, 숫자 : [a-zA-Z_0-9]) 하나와 매칭한다.
   *  - \W
   *    - 문자(알파벳, 언더바, 숫자 : [a-zA-Z_0-9]) 아닌 문자 하나와 매칭한다.
   *  - \s
   *    - 공백문자([\t\n\x0B\f\r]) 하나와 매칭한다.
   *  - \S
   *    - 공백문자가 아닌 문자 하나와 매칭한다.
   *  - .
   *    - 모든 문자 하나와 매칭한다.
   *  - \b
   *    - 단어 경계를 매칭한다.
   *  - \B
   *    - 단어가 아닌것의 경계를 매칭한다.
   *  - \A
   *    - 입력의 시작 부분을 매칭한다.
   *  - \G
   *    - 이전 매치의 끝을 매칭한다.
   *  - \z
   *    - 입력의 끝을 매칭한다.
   *  - \Z
   *    - 입력의 끝이지만 종결자가 있는 경우를 매칭한다.
   *
   * ~ Grouping and capturing
   *  - a(bc)
   *    - bc 캡쳐 그룹을 생성한다.
   *  - a(?:bc)
   *    - (?:) <- 캡쳐 그룹생성을 무시한다.
   *  -  a(?<name>bc)
   *    - (?<name>) <- 캡쳐 그룹에 이름을 지정한다
   *
   * ~ Bracket expressions — []
   *  - [abc]
   *    - a 또는 b 또는 c 를 포함하는 문자열과 매칭한다. (a|b|c || [a-c])
   * - [^abc]
   *    - a 또는 b 또는 c 를 포함하는 않는 문자열과 매칭한다. (^ 부정을 의미)
   *  - [a-fA-F0-9]
   *    - 16진수의 문자 하나와 매칭하고 대소문자를 구분하지 않는다.
   *
   *
   * ~ Greedy and Lazy match
   *  - Quantifiers(*, +, {})은 Greedy 연산자이기 때문에, 최대한 많은 매칭을 진행한다.
   *    예를 들어, <.+>는 <alpabet> abcdefg ... </alpabet> <- 전체를 매칭한다.
   *    <alpabet> 만을 매칭하기 위해선 '?' 를 사용하여 Lazy match 로 변경할 수 있다.
   */

  static boolean containsWhitespace(String str) {
    return str.matches(".*?\\s.*?");
  }

  public static void main(String[] args) {
    String str = "a...b b...a \n" + "b   a";
    System.out.println(str.matches("(?ims).*?a(.{3})b.*?"));
    System.out.println(str.replaceAll("(?ims).*?a(.{3})b.*?", "[]"));
    System.out.println(str.matches("(?ims).*b(.{3})a.*"));
    System.out.println(str.replaceAll("(?ims)b(.{3})a", "[]"));

    String apb = "abcdefghijklmnopqrstuvwxyz";
    System.out.println(apb.replaceAll("\\w", "-"));

    String staPattern = ";01234;56789;";
    String pattern = ";(?<foo>\\d{5});(?<bar>\\d{5});";
    Pattern regex = Pattern.compile(pattern);
    Matcher matcher = regex.matcher(staPattern);
    boolean success = matcher.find();

    String foo = success ? matcher.group("foo") : null;
    String bar = success ? matcher.group("bar") : null;

    System.out.println(foo +" "+ bar);

    // 비밀번호 정규표현식
    // toChars: 33 ~ 47 : !@#$%^&*()_\\-\"',.+/
    String password = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-\"',.+/])[A-Za-z\\d!@#$%^&*()_\\-\"',.+/]{8,}$";
    Pattern passwordPattern = Pattern.compile(password);
    Matcher matchPassword = passwordPattern.matcher("y9!Ru0B-");
    System.out.println("matchPassword = " + matchPassword.find());

    boolean all = Pattern.compile("^[a-zA-Z가-힣\\d\\s]+$").matcher("abc1 ABC2 한글3").find();
    System.out.println("all = " + all);

    String t1 = "타이틀, String";
    String s1 = t1.replaceAll(",\\s*\\w+$", "");
    System.out.println("s1 = " + s1);

    String t2 = "캘린더 제목1, String, Optional";
    String s2 = t2.replaceAll(",\\s*Optional$", "");
    System.out.println("s2 = " + s2);

    String my_string = "ihrhbakrfpndopljhygc";
    String[] split = my_string.split("(?<=\\G.{4})");
    // (?<=...): 긍정형 전방탐색 구문
    //  - 즉, 일치하는 패턴을 찾을 때 이전에 일치한 위치를 찾는 것
    // \G: 이전 일치한 위치에서 시작하는 것을 의미
    //  - \G는 전체 문자열의 시작 부분과 첫 번째 일치하지 않는 위치 이후의 위치를 구분한다.
    // .{4}: 임의의 문자 4개를 나타내는 패턴
    //  - .은 어떤 문자든 하나를 의미하고, {4}는 앞에 오는 패턴이 정확히 4번 반복되어야 함을 나타낸다.
  }
}
