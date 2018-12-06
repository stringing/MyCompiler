package parser;

import javafx.util.Pair;

import java.util.*;

/**
 * @Description 算符优先分析
 * @Author Stringing
 * @Date 2018/12/6 10:34
 */
public class OperatorPrecedenceParser {
    private static PrecedenceTable pt;
    private static Map<Character, Set<Character>> Firstvts = new HashMap<>();
    private static Map<Character, Set<Character>> Lastvts = new HashMap<>();
    private static Map<Pair<Character, Character>, Boolean> F = new HashMap<>();
    private static Stack<Pair<Character, Character>> stack = new Stack<>();

    /**
     * 将符号对[P,a]置为真并压入栈
     * @param P 非终结符
     * @param a 终结符
     */
    private static void insert(Character P, Character a){
        Pair<Character, Character> tmp = new Pair<>(P, a);
        if(!F.get(tmp)){
            F.put(tmp, true);
            stack.push(tmp);
        }
    }

    /**
     * 构建所有非终结符的FIRSTVT集
     * @param grammar 文法
     */
    public static void createFirstvts(Grammar grammar){
        for(Character P : grammar.Vn){
            for(Character a : grammar.Vt){
                F.put(new Pair<>(P, a), false);
            }
        }
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            for(String alpha : entry.getValue()){
                if(grammar.Vt.contains(alpha.charAt(0))){
                    insert(entry.getKey(), alpha.charAt(0));
                }else if(alpha.length() > 1 && grammar.Vn.contains(alpha.charAt(0)) && grammar.Vt.contains(alpha.charAt(1))){
                    insert(entry.getKey(), alpha.charAt(1));
                }
            }
        }
        while(!stack.isEmpty()){
            Pair<Character, Character> tmp = stack.pop();
            for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
                for(String alpha : entry.getValue()){
                    if(alpha.charAt(0) == tmp.getKey()){
                        insert(entry.getKey(), tmp.getValue());
                    }
                }
            }
        }
        for(Map.Entry<Pair<Character, Character>, Boolean> entry : F.entrySet()){
            Character key = entry.getKey().getKey();
            Character value = entry.getKey().getValue();
            Set<Character> firstvt;
            if(entry.getValue()){
                if(Firstvts.containsKey(key)){
                    firstvt = Firstvts.get(key);
                }else{
                    firstvt = new HashSet<>();
                }
                firstvt.add(value);
                Firstvts.put(key, firstvt);
            }
        }
    }

    /**
     * 构建所有非终结符的LASTVT集
     * @param grammar 文法·
     */
    public static void createLastvts(Grammar grammar){
        for(Character P : grammar.Vn){
            for(Character a : grammar.Vt){
                F.put(new Pair<>(P, a), false);
            }
        }
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            for(String alpha : entry.getValue()){
                if(grammar.Vt.contains(alpha.charAt(alpha.length() - 1))){
                    insert(entry.getKey(), alpha.charAt(alpha.length() - 1));
                }else if(alpha.length() > 1 && grammar.Vn.contains(alpha.charAt(alpha.length() - 1)) && grammar.Vt.contains(alpha.charAt(alpha.length() - 2))){
                    insert(entry.getKey(), alpha.charAt(1));
                }
            }
        }
        while(!stack.isEmpty()){
            Pair<Character, Character> tmp = stack.pop();
            for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
                for(String alpha : entry.getValue()){
                    if(alpha.charAt(alpha.length() - 1) == tmp.getKey()){
                        insert(entry.getKey(), tmp.getValue());
                    }
                }
            }
        }
        for(Map.Entry<Pair<Character, Character>, Boolean> entry : F.entrySet()){
            Character key = entry.getKey().getKey();
            Character value = entry.getKey().getValue();
            Set<Character> lastvt;
            if(entry.getValue()){
                if(Lastvts.containsKey(key)){
                    lastvt = Lastvts.get(key);
                }else{
                    lastvt = new HashSet<>();
                }
                lastvt.add(value);
                Lastvts.put(key, lastvt);
            }
        }
    }

    /**
     * 根据FIRSTVT和LASTVT集构建优先表
     * @param grammar
     */
    public static void createPrecedenceTable(Grammar grammar){
        pt = new PrecedenceTable(grammar);
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            for(String alpha : entry.getValue()){
                char[] alpha_array = alpha.toCharArray();
                for(int i = 0; i < alpha_array.length - 1; i++){
                    if((grammar.Vt.contains(alpha_array[i]) && grammar.Vt.contains(alpha_array[i + 1]))){
                        pt.setPrecedence(alpha_array[i], alpha_array[i + 1], '=');
                    }
                    if(i < alpha_array.length - 2 && grammar.Vt.contains(alpha_array[i]) && grammar.Vt.contains(alpha_array[i + 2])
                            && grammar.Vn.contains(alpha_array[i + 1])){
                        pt.setPrecedence(alpha_array[i], alpha_array[i + 2], '=');
                    }
                    if(grammar.Vt.contains(alpha_array[i]) && grammar.Vn.contains(alpha_array[i + 1])){
                        Set<Character> firstvt = Firstvts.get(alpha_array[i + 1]);
                        for(Character b : firstvt){
                            pt.setPrecedence(alpha_array[i], b, '<');
                        }
                    }
                    if(grammar.Vt.contains(alpha_array[i + 1]) && grammar.Vn.contains(alpha_array[i])){
                        Set<Character> lastvt = Lastvts.get(alpha_array[i]);
                        for(Character a : lastvt){
                            pt.setPrecedence(a, alpha_array[i + 1],   '>');
                        }
                    }
                }
            }
        }
    }

    public static boolean parse(Grammar grammar, String expression){
        int k = 0;
        int p = 0;
        int j = 0;
        Character[] S = new Character[64];
        S[k] = '#';
        Character a = null;
        Character Q = null;
        while(a != '#'){
            a = expression.charAt(p++);
            if(grammar.Vt.contains(S[k])){
                j = k;
            }else{
                j = k - 1;
            }
            while(pt.compare(S[j], a) == 1){
                do{
                    Q = S[j];
                    if(grammar.Vt.contains(S[j - 1])){
                        j -= 1;
                    }else{
                        j -= 2;
                    }
                }while(pt.compare(S[j], Q) == 0 || pt.compare(S[j], Q) == 1);
                k = j + 1;
//                S[k]
            }
        }
        return false;
    }

    /**
     * 打印F关系集
     */
    public static void printF(){
        for(Map.Entry<Pair<Character, Character>, Boolean> entry : F.entrySet()){
            System.out.println("F[" + entry.getKey().getKey() + "," + entry.getKey().getValue() + "] = " + entry.getValue());
        }
    }

    /**
     * 打印FIRSTVT集
     */
    public static void printFirstvts(){
        System.out.println("FIRSTVT:");
        for(Map.Entry<Character, Set<Character>> entry : Firstvts.entrySet()){
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * 打印LASTVT集
     */
    public static void printLastvts(){
        System.out.println("LASTVT:");
        for(Map.Entry<Character, Set<Character>> entry : Lastvts.entrySet()){
            System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * 打印优先表
     */
    public static void printPrecedenceTable(){
        pt.printTable();
    }

}