package GrammarAnalyzer;

import java.util.*;

/**
 * @Description 将整理后的普通文法转换成LL(1)文法
 * @Author Stringing
 * @Date 2018/11/21 11:28
 */

public class LL1Transformer {

    private static List<Character> alphabet = new ArrayList<>();
    static {
        for(int i = 65; i <= 90; i++){
            alphabet.add((char)i);
        }
    }

    /**
     * 将整理过的普通文法转换为LL(1)文法
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
        int p = 0;
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            //代入算法最后必定是非终结符排序的最后一个非终结符含有左递归
            if(p == grammar.P.pformula.size() - 1){
                //每次取字母表当前第一个字母表示所谓的S'
                Character c = alphabet.get(0);
                alphabet.remove(0);
                //消除左递归
                Character nonterminal = entry.getKey();
                List<String> pfml = entry.getValue();
                String alpha = pfml.get(0).substring(1);
                pfml.remove(0);
                for(int i = 0; i < pfml.size(); i++){
                    pfml.set(i, pfml.get(i) + c);
                }
                List<String> newAlpha = new ArrayList<>();
                newAlpha.add(alpha + c);
                newAlpha.add(null);
                grammar.P.pformula.put(c, newAlpha);
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
        for(int k = 0; k < pi.size(); k++){
            String pf = pi.get(k);
            for(int x = 0; x < pf.length(); x++){
                if(pf.charAt(x) == nj){
                    List<String> front = pi.subList(0, k);
                    List<String> tail = pi.subList(k + 1, pi.size());
                    List<String> mid = new ArrayList<>();
                    List<String> total = new ArrayList<>();
                    for(int w = 0; w < pj.size(); w++){
                        String tmp = pf;
                        tmp = tmp.replace(nj.toString(), pj.get(w));
                        mid.add(tmp);
                    }
                    total.addAll(front);
                    total.addAll(mid);
                    total.addAll(tail);
                    pformula.put(ni, total);
                    break;
                }
            }
        }
    }

    /**
     * 提取左因子
     * @param grammar 待提取左因子的文法
     */
    public static void removeLeftGene(Grammar grammar){
        int p = 0;
        for(Map.Entry<Character, List<String>> entry : grammar.P.pformula.entrySet()){
            if(p != grammar.P.pformula.size() - 1 && p != grammar.P.pformula.size() - 2){
                List<String> alphas = entry.getValue();
                Map<String, List<String>> commoms = new LinkedHashMap<>();
                for(String alpha : alphas){
                    String first = alpha.substring(0, 1);
                    String tail = alpha.substring(1);
                    List<String> tmp;
                    if(commoms.containsKey(first)){
                        tmp = commoms.get(first);
                    }else{
                        tmp = new LinkedList<>();
                    }
                    if(!tail.equals("")) {
                        tmp.add(alpha.substring(1));
                    }
                    commoms.put(first, tmp);
                }
                for(Map.Entry<String, List<String>> commonEntry : commoms.entrySet()){

                }
            }
            p++;
        }
    }
}
