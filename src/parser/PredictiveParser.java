package parser;

import javafx.util.Pair;

import java.util.*;

/**
 * @Description 预测分析程序
 * @Author Stringing
 * @Date 2018/11/25 20:13
 */
public class PredictiveParser {

    public static Map<Character, Set<Character>> firsts = new HashMap<>();
    private static List<Character> alphaHistory = null;
    public static Map<Character, Set<Character>> follows = new HashMap<>();
    private static PredictiveTable pt;

    public static void createFirstForGrammar(Grammar grammar){
        for(Character c : grammar.Vn){
            alphaHistory = new ArrayList<>();
            firsts.put(c, createFirstSet(c, grammar));
            alphaHistory = null;
        }
    }

    /**
     * 根据P构造某个文法符号X的First终结首符集
     * @param grammar LL(1)文法
     * @return First集
     */
    private static Set<Character> createFirstSet(Character x, Grammar grammar){
        if(firsts.containsKey(x))
            return firsts.get(x);
        Set<Character> first = new HashSet<>();
        if(grammar.Vt.contains(x)){
            //rule 1
            first.add(x);
        }else if(x == 'ε'){
            first.add('ε');
        }else {
            //rule 2
            traverseAlphas(first, x, grammar);
        }
        return first;
    }

    /**
     * 遍历非终结符A求First(A)
     * @param first First集
     * @param x 非终结符
     * @param grammar LL(1)文法
     */
    private static void traverseAlphas(Set<Character> first, Character x, Grammar grammar){
        if(alphaHistory.contains(x)){
            return;
        }else{
            alphaHistory.add(x);
        }
        for(String alpha : grammar.P.pformula.get(x)){
            if(alpha.equals("ε")){
                first.add('ε');
                continue;
            }
            Character n = alpha.charAt(0);
            if(grammar.Vt.contains(n)){
                first.add(n);
            }else{
                traverseAlphas(first, n, grammar);
            }
        }
    }

    /**
     * 得到某符号串α的First(α)，必须在已经求得Vn所有元素的First集后才能调用。
     * @param alpha 符号串α
     * @param grammar LL(1)文法
     * @return 指定符号串α的First(α)
     */
    public static Set<Character> createFirstSetForAlpha(String alpha, Grammar grammar){
        //rule 3
        Set<Character> first = new HashSet<>();
        char[] alpha_array = alpha.toCharArray();
        int i = 0;
        Set<Character> tmp = createFirstSet(alpha_array[i], grammar);
        first.addAll(tmp);
        while(tmp.contains("ε") && i < alpha_array.length){
            tmp = createFirstSet(alpha_array[++i], grammar);
            tmp.remove('ε');
            first.addAll(tmp);
        }
        if(i == alpha_array.length){
            first.add('ε');
        }
        return first;
    }

    /**
     * 创建每个非终结符的Follow集，知道每个Follow集不再增大为止
     * @param grammar LL(1)文法
     */
    public static void createFollowForGrammarUtilNoIncrement(Grammar grammar){
        createFollowForGrammar(grammar);
        List<Integer> records = new LinkedList<>();
        for(Map.Entry<Character, Set<Character>> entry : follows.entrySet()){
            records.add(entry.getValue().size());
        }
        while(true){
            createFollowForGrammar(grammar);
            int i = 0;
            int count = 0;
            for(Map.Entry<Character, Set<Character>> entry : follows.entrySet()){
                if(entry.getValue().size() != records.get(i)){
                    count++;
                    records.set(i, entry.getValue().size());
                }
                i++;
            }
            if(count != 0){
                createFollowForGrammar(grammar);
            }else{
                break;
            }
        }
    }

    /**
     * 创建Follow集的一次规则执行
     * @param grammar LL(1)文法
     */
    public static void createFollowForGrammar(Grammar grammar){
        for(Character c : grammar.Vn){
            createFollowSet(c, grammar);
        }
    }

    /**
     * 根据Vn, P和First集构造Follow集
     * @param x 文法符号
     * @param grammar LL(1)文法
     */
    private static void createFollowSet(Character x, Grammar grammar){
        //rule 1
        addFollowElement(x, '#');
        //rule 2
        for(String alpha : grammar.P.pformula.get(x)){
            if(alpha.length() == 1)continue;
            char[] alpha_array = alpha.toCharArray();
            for(int i = 1; i < alpha_array.length; i++){
                if(grammar.Vn.contains(alpha_array[i])){
                    if(i != alpha_array.length - 1){
                        //rule 2
                        Set<Character> first = createFirstSetForAlpha(alpha.substring(i + 1), grammar);
                        if(first.remove('ε')){
                            addFollowSet(alpha_array[i], follows.get(x));
                        }
                        addFollowSet(alpha_array[i], first);
                    }else{
                        //rule 3
                        addFollowSet(alpha_array[i], follows.get(x));
                    }
                }
            }
        }
    }

    /**
     * 给非终结符x对应的Follow集添加元素
     * @param x 非终结符
     * @param c 所添加的元素(终结符)
     */
    private static void addFollowElement(Character x, char c) {
        Set<Character> follow = follows.get(x);
        if(follow == null){
            follow = new HashSet<>();
        }
        follow.add(c);
        follows.put(x, follow);
    }

    /**
     * 给非终结符x对应的Follow集添加集合所有元素
     * @param x 非终结符
     * @param s 所添加的元素集合
     */
    private static void addFollowSet(Character x, Set<Character> s) {
        Set<Character> follow = follows.get(x);
        if(follow == null){
            follow = new HashSet<>();
        }
        follow.addAll(s);
        follows.put(x, follow);
    }

    /**
     * 构造预测分析表，在构造完First和Follow集后调用
     * @param grammar LL(1)文法
     */
    public static void createPredictiveTable(Grammar grammar){
        pt = new PredictiveTable(grammar);
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            for(String alpha : entry.getValue()){
                Set<Character> first = createFirstSetForAlpha(alpha, grammar);
                for(Character c : first){
                    if(c == 'ε')continue;
                    pt.addPformula(entry.getKey(), c, entry.getKey(), alpha);
                }
                if(first.contains('ε')){
                    Set<Character> follow = follows.get(entry.getKey());
                    for(Character c : follow){
                        pt.addPformula(entry.getKey(), c, entry.getKey(), alpha);
                    }
                }
            }
        }
    }

    /**
     * 分析单个表达式语法是否正确
     * @param expression 表达式
     * @return 语法正确为true，反之为false
     */
    public static boolean parse(Grammar grammar, String expression){
        Stack<Character> ntStack = new Stack<>();
        ntStack.push('#');
        ntStack.push(grammar.S);
        expression += '#';
        boolean flag = true;
        Character x, a;
        int p = 0;
        while(flag){
            a = expression.charAt(p);
            x = ntStack.pop();
            if(grammar.Vt.contains(x)){
                if(x == a){
                    p++;
                }else{
                    return false;
                }
            }else if(x == '#'){
                if(x == a){
                    flag = false;
                }else{
                    return false;
                }
            }else if(pt.getAlpha(x, a) != null){
                String alpha = pt.getAlpha(x, a);
                if(!alpha.equals("ε")) {
                    for (int i = alpha.length() - 1; i >= 0; i--) {
                        ntStack.push(alpha.charAt(i));
                    }
                }
            }else{
                return false;
            }
        }
        return true;
    }

    public static List<Pair<String, Boolean>> parseAll(Grammar grammar, List<Pair<String, String>> typeList){
        List<Pair<String, Boolean>> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(Pair<String, String> p : typeList){
            if(Character.isDigit(p.getKey().charAt(0))){
                //先不管10进制之外的进制
                if(p.getKey().equals("1") || p.getKey().equals("0"))
                    sb.append("i");
            }else{
                if(p.getKey().equals(";")){
                    String expression = sb.toString();
                    result.add(new Pair<>(expression, parse(grammar, expression)));
                    sb = new StringBuilder();
                }else{
                    sb.append(p.getKey());
                }
            }
        }
        return result;
    }

    public static void printPredictiveTable(){
        pt.printTable();
    }
}