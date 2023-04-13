package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Stack;

public class BrowserStack {
  public static ArrayList<Stack> browserStack(String[] actions, String start) {
    ArrayList<Stack> result = new ArrayList<>() {{
      add(new Stack<String>());
      add(new Stack<String>() {{
        add(start);
      }});
      add(new Stack<String>());
    }};

    Stack<String> prevStack = result.get(0);
    Stack<String> current = result.get(1);
    Stack<String> nextStack = result.get(2);

    for (String s : actions) {
      switch (s.codePointAt(0)) {
        case 45: // "-1"
          if (!prevStack.isEmpty()) {
            nextStack.push(current.pop());
            current.push(prevStack.pop());
          }
          break;
        case 49: // "1"
          if (!nextStack.isEmpty()) {
            prevStack.push(current.pop());
            current.push(nextStack.pop());
          }
          break;
        default: // Char
          prevStack.push(current.pop());
          current.push(s);
          nextStack.clear();
          break;
      }
    }

    return result;
  }
}
