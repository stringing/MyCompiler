package parser;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    public static List<String> transToReversePolish(List<Pair<String, String>> typeList){
        List<String> results = new ArrayList<>();
        for(Pair<String, String> p : typeList){
            if(p.getKey().equals(";")){
                // TODO 弹出表达式
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

        }
    }

}
