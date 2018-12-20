package parser;

import javafx.util.Pair;

import java.util.*;

/**
 * @Description 逆波兰表达式转换
 * @Author Stringing
 * @Date 2018/12/19 21:16
 */
public class ReversePolishTransformer {
    //符号栈s1
    private static Stack<Variable> s1 = new Stack<>();
    //中间结果栈s2
    private static Stack<Variable> s2 = new Stack<>();

    /**
     * 将表达式转换为逆波兰表达式
     * @param typeList 词法分析器的提词结果
     * @return 逆波兰表达式结果列表(结果是从栈里pop的为倒序)
     */
    public static List<List<Variable>> transToReversePolish(List<Pair<String, String>> typeList){
        List<List<Variable>> results = new ArrayList<>();
        for(Pair<String, String> p : typeList){
            List<Variable> result = new ArrayList<>();
            if(p.getKey().equals(";")){
                while(!s1.isEmpty()){
                    s2.push(s1.pop());
                }
                while(!s2.isEmpty()){
                    result.add(s2.pop());
                }
                results.add(result);
            }else {
                if (p.getKey().equals("0")) {
                    s2.push(new Variable(p.getValue()));
                } else if (p.getKey().equals("1")) {
                    s2.push(new Variable(Integer.valueOf(p.getValue())));
                } else {
                    processOperator(p.getKey());
                }
            }
        }
        return results;
    }

    private static void processOperator(String op) {
        if(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")) {
            if (s1.isEmpty() || s1.peek().getStrVal().equals("(")) {
                s1.push(new Variable(op));
            } else if ((op.equals("*") || op.equals("/")) && (s1.peek().getStrVal().equals("+") || s1.peek().getStrVal().equals("-"))) {
                s1.push(new Variable(op));
            } else {
                s2.push(s1.pop());
                processOperator(op);
            }
        }else if(op.equals("(")){
            s1.push(new Variable(op));
        }else if(op.equals(")")){
            while(!s1.peek().getStrVal().equals("(")){
                s2.push(s1.pop());
            }
            s1.pop();
        }
    }

}
