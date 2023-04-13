package Gr8G1.prac.pojo.oop.temp;

import java.util.Arrays;

public class PrInheritance {
  public static void main(String[] args) {
    UnitedStates US = new UnitedStates(new Lang("en"));
    Korea KO = new Korea(new Lang("ko"));
    Seoul SE = new Seoul(new Lang("ko-se"));

    System.out.println(US.lang.lang);
    System.out.println(US.greetings());
    System.out.println(KO.lang.lang);
    System.out.println(KO.greetings());
    System.out.println(KO.consonant);
    System.out.println(SE.lang.lang);
    System.out.println(SE.greetings());
    System.out.println(SE.getGrammar());
  }
}

class Lang {
  String lang;

  public Lang(String lang) {
    this.lang = lang;
  }
}

class UnitedStates {
  Lang lang; // UnitedStates <- Lang 포함 관계

  char[] consonant = {'A', 'B', 'C', 'D'};
  char[] vowel = {'E', 'I', 'O', 'U'};

  public UnitedStates(Lang lang) {
    this.lang = lang;
  }

  public String greetings() {
    return "Hello. :)";
  }
}

class Korea {
  Lang lang; // Korea <- Lang 포함 관계

  char[] consonant = {'ㄱ', 'ㄴ', 'ㄷ', 'ㄹ'};
  char[] vowel = {'ㅏ', 'ㅑ', 'ㅓ', 'ㅕ'};

  public Korea(Lang lang) {
    this.lang = lang;
  }

  public String greetings() {
    return "안녕하세요. :)";
  }
}

class Seoul extends Korea { // Seoul -> Korea = 상속 관계
  public Seoul(Lang lang) {
    super(lang);
  }

  @Override
  public String greetings() { // 메소드 오버라이딩
    return "반가워요. :)";
  }
  public String getGrammar() {
    return Arrays.toString(super.consonant) + " " + Arrays.toString(super.vowel);
  }
}
