package GrammarAnalyzer;

import java.util.*;

/**
 * @Description 将整理后的普通文法转换成LL(1)文法
 * @Author Stringing
 * @Date 2018/11/21 11:28
 */

public class LL1Transformer {

    private static List<Character> alphabet = new ArrayList<>();
    private static Map<Character, List<String>> newOrder;
    static {
        for(int i = 65; i <= 90; i++){
            alphabet.add((char)i);
        }
    }

    /**
     * 将整理过的普通文法转换为LL(1)文法
     * 结合消除左递归和消除回溯得到
     * @param grammar 整理过的普通文法
     */
    public static void transToLL1(Grammar grammar){
        //除去已经占用了的非终结符作为待用
        for(Character c : grammar.Vn){
            alphabet.remove(c);
        }
        removeLeftRecursion(grammar);
        removeLeftGene(grammar);
    }

    /**
     * 消除左递归（按Vn默认排序）
     * 用到了P69的消除规则和P70的代换算法
     * @param grammar 待消除左递归的文法
     */
    public static void removeLeftRecursion(Grammar grammar){
        int n = grammar.P.pformula.size();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < i; j++){
                //Pj代入Pi
                substitude(grammar.P.pformula, j, i);
            }
        }
        System.out.println(grammar);
        int p = 0;
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            //代入算法最后必定是非终结符排序的最后一个非终结符含有左递归
            if(p == grammar.P.pformula.size() - 1){
                //消除左递归
                Character nonterminal = entry.getKey();
                List<String> pfml = entry.getValue();
                List<String> alphas = new LinkedList<>();
                Iterator<String> it = pfml.iterator();
                boolean flag = false;
                while(it.hasNext()){
                    String alpha = it.next();
                    if(alpha.startsWith(String.valueOf(nonterminal))){
                        flag = true;
                        alphas.add(alpha.substring(1));
                        it.remove();
                    }
                }
                if(flag){
                    Character c = fetchOneCharacter();
                    for(int i = 0; i < pfml.size(); i++){
                        pfml.set(i, pfml.get(i) + c);
                    }
                    List<String> newAlpha = new ArrayList<>();
                    for(String alpha : alphas){
                        newAlpha.add(alpha + c);
                    }
                    newAlpha.add(null);
                    grammar.P.pformula.put(c, newAlpha);
                }
            }
            p++;
        }
    }

    /**
     * 将第j个产生式代入第i个
     * @param pformula 产生式的map
     * @param j 要代入的产生式序号
     * @param i 被代入的产生式序号
     */
    private static void substitude(Map<Character, List<String>> pformula, int j, int i) {
        Character nj = null, ni = null;
        List<String> pj = null, pi = null;
        int p = 0;
        for(Character key : pformula.keySet()){
            if(p == j){
                nj = key;
                pj = pformula.get(key);
            }
            if(p == i){
                ni = key;
                pi = pformula.get(key);
            }
            p++;
        }
        Map<Integer, List<String>> markMap = new LinkedHashMap<>();
        for(int k = 0; k < pi.size(); k++){
            String pf = pi.get(k);
            for(int x = 0; x < pf.length(); x++){
                if(pf.charAt(x) == nj){
                    List<String> mid = new ArrayList<>();
                    for(int w = 0; w < pj.size(); w++){
                        String tmp = pf;
                        tmp = tmp.replace(nj.toString(), pj.get(w));
                        mid.add(tmp);
                    }
                    List<String> tmpList = new ArrayList<>();
                    tmpList.addAll(mid);
                    markMap.put(k, tmpList);
                }else{
                }
            }
        }
        List<String> newAlphas = new ArrayList<>();
        for(int y = 0; y < pi.size(); y++){
            if(markMap.containsKey(y)){
                newAlphas.addAll(markMap.get(y));
            }else{
                newAlphas.add(pi.get(y));
            }
        }
        pformula.put(ni, newAlphas);
    }

    /**
     * 提取左因子
     * 用到了P71的提取规则
     * @param grammar 待提取左因子的文法
     */
    public static void removeLeftGene(Grammar grammar){
        newOrder = new LinkedHashMap<>();
        int p = 0;
        boolean flag = false;
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            if(p != grammar.P.pformula.size() - 1 && p != grammar.P.pformula.size() - 2){
                Character nonterminal = entry.getKey();
                List<String> alphas = entry.getValue();
                Map<String, List<String>> commons = new LinkedHashMap<>();
                for(String alpha : alphas){
                    String first = alpha.substring(0, 1);
                    String tail = alpha.substring(1);
                    List<String> tmp;
                    if(commons.containsKey(first)){
                        tmp = commons.get(first);
                    }else{
                        tmp = new LinkedList<>();
                    }
                    tmp.add(tail);
                    commons.put(first, tmp);
                }
                for(Map.Entry<String, List<String>> commonEntry : commons.entrySet()){
                    List<String> list = commonEntry.getValue();
                    if(list.size() >= 2){
                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).equals("")){
                                list.set(i, "ε");
                                break;
                            }
                        }
                    }
                }
                List<String> substitution = new ArrayList<>();
                Map<String, List<String>> newp = new LinkedHashMap<>();
                for(Map.Entry<String, List<String>> commonEntry : commons.entrySet()){
                    String key = commonEntry.getKey();
                    List<String> value = commonEntry.getValue();
                    if(value.size() >= 2)flag = true;
                    Character c = null;
                    if(value.size() == 1){
                        substitution.add(key + value.get(0));
                    }else{
                        c = fetchOneCharacter();
                        substitution.add(key + c);
                        newp.put(String.valueOf(c), value);
                    }
                    grammar.P.pformula.replace(nonterminal, substitution);
                    newOrder.put(nonterminal, substitution);
                    if(c != null) {
                        newOrder.put(c, value);
                    }
                }
            }else{
                newOrder.put(entry.getKey(),entry.getValue());
            }
            p++;
        }
        grammar.P.pformula = newOrder;
        if(flag)removeLeftGene(grammar);
    }

    /**
     * 从可用字母集中取一个出来作新引进的非终结符
     * @return 新引进的非终结符
     */
    private static Character fetchOneCharacter(){
        Character c = alphabet.get(0);
        alphabet.remove(0);
        return c;
    }
}
