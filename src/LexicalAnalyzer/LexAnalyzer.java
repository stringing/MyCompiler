package LexicalAnalyzer;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description 词法分析器
 * @Author Stringing
 * @Date 2018/11/6 12:57
 */
public class LexAnalyzer {

    //所有的状态
    enum Status{
        START(0), ALPHA_NUM(1), NOT_ALPHA_NUM(2), ZERO_TO_NINE(3),
        NOT_ZERO_TO_NINE(4), ZERO(5), ZERO_TO_SEVEN(6), NOT_ZERO_TO_SEVEN(7),
        ZERO_X(8), ZERO_TO_F(9), NOT_ZERO_TO_F(10), OPRATER_DELIMITER(11),
        KEYWORD(12), INVALID_CHAR(13);

        int status;

        Status(int status){this.status = status;}
    }

    //状态图
    private static class StatusGraph{
        Status start;
        Status currentStatus;
        Stack<Character> s;

        public StatusGraph(){
            start = Status.START;
            currentStatus = start;
            s = new Stack<>();
        }


        //读入每个字符，同时根据状态图判断每次所到的状态
        public int processInput(char c){
            if(currentStatus == Status.NOT_ALPHA_NUM || currentStatus == Status.NOT_ZERO_TO_NINE
                    || currentStatus == Status.NOT_ZERO_TO_SEVEN || currentStatus == Status.NOT_ZERO_TO_F
                    || currentStatus == Status.INVALID_CHAR){
                return -1;
            }
            s.push(c);
            if(currentStatus == Status.START){
                fromStatus0(c);
            }else if(currentStatus == Status.ALPHA_NUM){
                fromStatus1(c);
            }else if(currentStatus == Status.ZERO_TO_NINE){
                fromStatus3(c);
            }else if(currentStatus == Status.ZERO){
                fromStatus5(c);
            }else if(currentStatus == Status.ZERO_TO_SEVEN){
                fromStatus6(c);
            }else if(currentStatus == Status.ZERO_X){
                fromStatus8(c);
            }else if(currentStatus == Status.ZERO_TO_F){
                fromStatus9(c);
            }
            return 0;
        }

        //写到列表中
        private void addType(int type, char c){
            s.pop();
            typeList.add(new Pair<>(String.valueOf(type), printStack(s)));
            if(c != ' '){
                typeList.add(new Pair<>(String.valueOf(c), "-"));
            }
            s.clear();
            currentStatus = Status.START;
        }

        private void fromStatus9(char c) {
            if(!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f'))){
                if(hasChar(OPERATORS_AND_DELIMITERS, c)){
                    addType(3, c);
                }else {
                    currentStatus = Status.NOT_ZERO_TO_F;
                }
            }
        }

        private void fromStatus8(char c) {
            if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')){
                currentStatus = Status.ZERO_TO_F;
            }else{
                currentStatus = Status.NOT_ZERO_TO_F;
            }
        }

        private void fromStatus6(char c) {
            if(Character.isDigit(c)) {
                int num = Integer.parseInt(String.valueOf(c));
                if (num < 0 || num > 7) {
                    currentStatus = Status.NOT_ZERO_TO_SEVEN;
                }
            }else if(hasChar(OPERATORS_AND_DELIMITERS, c)){
                addType(2, c);
            }else{
                currentStatus = Status.NOT_ZERO_TO_SEVEN;
            }
        }

        private void fromStatus5(char c) {
            if(Character.isDigit(c)){
                int num = Integer.parseInt(String.valueOf(c));
                if(num >= 1 && num <= 7){
                    currentStatus = Status.ZERO_TO_SEVEN;
                }
            }else if(hasChar(OPERATORS_AND_DELIMITERS, c)){
                addType(1, c);
            }else if(c == 'x'){
                currentStatus = Status.ZERO_X;
            }else{
                currentStatus = Status.NOT_ZERO_TO_NINE;
            }
        }

        private void fromStatus3(char c) {
            if(!Character.isDigit(c)){
                if(hasChar(OPERATORS_AND_DELIMITERS, c)){
                    addType(1, c);
                }else{
                    currentStatus = Status.NOT_ZERO_TO_NINE;
                }
            }
        }

        private void fromStatus1(char c) {
            if(!Character.isAlphabetic(c) && !Character.isDigit(c)){
                if(hasChar(OPERATORS_AND_DELIMITERS, c)) {
                    s.pop();
                    String word = printStack(s);
                    if(hasWord(KEYWORDS, word)){
                        typeList.add(new Pair<>(word, "-"));
                    }else{
                        typeList.add(new Pair<>("0", printStack(s)));
                    }
                    if(c != ' ') {
                        typeList.add(new Pair<>(String.valueOf(c), "-"));
                    }
                    s.clear();
                    currentStatus = Status.START;
                }else{
                    currentStatus = Status.NOT_ALPHA_NUM;
                }
            }
        }

        private void fromStatus0(char c) {
            if(Character.isAlphabetic(c)){
                currentStatus = Status.ALPHA_NUM;
            }else if(Character.isDigit(c)){
                if(Integer.parseInt(String.valueOf(c)) == 0)
                    currentStatus = Status.ZERO;
                else
                    currentStatus = Status.ZERO_TO_NINE;
            }else if(hasChar(OPERATORS_AND_DELIMITERS, c)){
                if(c != ' ')
                    currentStatus = Status.INVALID_CHAR;
                else
                    s.pop();
            }else{
                currentStatus = Status.INVALID_CHAR;
            }
        }

        private String printStack(Stack<Character> q) {
            StringBuilder sb = new StringBuilder();
            for(char c : q){
                sb.append(c);
            }
            return sb.toString();
        }

        private boolean hasWord(String[] keywords, String word) {
            for(String tmp : keywords){
                if(tmp.equals(word))
                    return true;
            }
            return false;
        }

        private boolean hasChar(char[] operatorsAndDelimiters, char c) {
            for(char tmp : operatorsAndDelimiters){
                if(tmp == c)
                    return true;
            }
            return false;
        }
    }

    private static final char[] OPERATORS_AND_DELIMITERS = {'+', '-', '*', '/', '>', '<', '=', '(', ')', ';', ' ', '\n', '\t'};
    private static final String[] KEYWORDS = {"if", "then", "else", "while", "do"};
    private static List<Pair<String, String>> typeList = new ArrayList<>();
    private static StatusGraph graph = new StatusGraph();

    public static void main(String[] args) throws IOException {
        File program = new File("resource/program.txt");
        lexProcess(program);
        printTypes();
    }

    //扫描函数的入口
    private static void lexProcess(File program) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(program)));
        String line = null;
        List<Pair<String, String>> typeList = new ArrayList<>();
        int lineNum = 1;
        while((line = br.readLine()) != null){
            scan(line, lineNum++);
        }
    }

    //逐个扫描每个字符
    private static void scan(String line, int lineNum){
        Queue<Character> q = new LinkedBlockingQueue(32);
        char[] characters = line.toCharArray();
        for(int i = 0; graph.processInput(characters[i]) != -1 && i < characters.length - 1; i++);
    }

    //打印出最后结果
    private static void printTypes(){
        for(Pair p : typeList){
            System.out.println("<" + p.getKey() + "," + p.getValue() + ">");
        }
    }
}