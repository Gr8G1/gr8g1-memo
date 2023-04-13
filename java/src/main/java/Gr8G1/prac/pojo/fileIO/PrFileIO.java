package Gr8G1.prac.pojo.fileIO;

import java.io.*;

public class PrFileIO {
  static final String INPUT_PATH = "fileIO.txt"; // 상대경로
  static final String OUTPUT_PATH = "fileIO_OUT.txt"; // 상대경로

  static void fileIs() {
    BufferedInputStream bis = null;

    try {
      bis = new BufferedInputStream(new FileInputStream(INPUT_PATH));

      // ! 한글 출력 불가
      // int i;
      // while ((i = bis.read()) != -1) System.out.print((char) i);

      // ! 한글 출력 가능
      byte[] readBuffer = new byte[bis.available()];
      while (bis.read(readBuffer) != -1) System.out.println(new String(readBuffer));

    } catch (Exception e) {
      System.out.println("파일 읽기 실패: " + e.getMessage());
    } finally {
      try {
        assert bis != null;
        bis.close();
      } catch (Exception e) {
        System.out.println("파일 닫기 실패: " + e.getMessage());
      }
    }
  }

  static void fileOs() {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;

    try {
      bis = new BufferedInputStream(new FileInputStream(INPUT_PATH));
      bos = new BufferedOutputStream(new FileOutputStream(OUTPUT_PATH));

      byte[] readBuffer = new byte[bis.available()];
      while (bis.read(readBuffer, 0, readBuffer.length) != -1) bos.write(readBuffer);

    } catch (Exception e) {
      System.out.println("파일 쓰기 실패:" + e.getMessage());
    } finally {
      try {
        assert bos != null;
        bos.close();
        bis.close();

      } catch (Exception e) {
        System.out.println("파일 닫기 실패: " + e.getMessage());
      }
    }
  }

  static void fileReader() {
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(INPUT_PATH));

      String string;
      do {
        string = br.readLine(); // 한줄씩 읽기
        if (string != null) System.out.println(string);
      } while (string != null);


    } catch (Exception e) {
      System.out.println("파일 읽기 실패" + e.getMessage());
    } finally {
      try {
        assert br != null;
        br.close();
      } catch (Exception e) {
        System.out.println("파일 닫기 실패" + e.getMessage());
      }
    }
  }

  static void fileWriter() {
    BufferedReader br = null;
    BufferedWriter bw = null;

    try {
      br = new BufferedReader(new FileReader(INPUT_PATH));
      bw = new BufferedWriter(new FileWriter(OUTPUT_PATH));

      String string;
      do {
        string = br.readLine();

        if (string != null) {
          bw.write(string);
          bw.newLine();
          System.out.println(string);
        }
      } while (string != null);
    } catch (Exception e) {
      System.out.println("파일 쓰기 실패: " + e.getMessage());
    } finally {
      try {
        assert br != null;
        br.close();
        assert bw != null;
        bw.close();
      } catch (Exception e) {
        System.out.println("파일 닫기 실패: " + e.getMessage());
      }
    }
  }

  static void files() throws IOException {
    // 파일 생성
    // File f = new File("./", "IO_Test.txt");

    // if (f.createNewFile()) {
    //   System.out.println(f.getPath());
    //   System.out.println(f.getParent());
    //   System.out.println(f.getCanonicalPath());
    //   System.out.println(f.canWrite());
    // }

    // 파일 변경
    File root = new File("./");
    File[] list = root.listFiles();

    assert list != null;
    for (File file : list) {
      String fileName = file.getName();
      System.out.println(fileName);

      if (fileName.endsWith("txt") && fileName.startsWith("test")) file.renameTo(new File(root, "File" + fileName));
    }

    // 파일 삭제
    // if ([FILE_PATH].exists()) {
    //   if ([FILE_PATH].isDirectory()) {
    //     File[] lists = [FILE_PATH].listFiles();
    //
    //     assert lists != null;
    //     for (File file : lists) {
    //       if (file.delete()) System.out.println(file.getName() + " 삭제성공");
    //       else System.out.println(file.getName() + " 삭제실패");
    //     }
    //   }
    //
    //   if ([FILE_PATH].delete()) System.out.println("파일삭제 성공");
    //   else System.out.println("파일삭제 실패");
    // }
  }

    public static void main (String[]args) throws IOException {
      files();

      // fileIs();
      // fileOs();

      // fileReader();
      // fileWriter();
    }
  }
