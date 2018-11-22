package GrammarAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 将整理后的普通文法转换成LL(1)文法
 * @Author Stringing
 * @Date 2018/11/21 11:28
 */

public class LL1Transformer {

    /**
     * 将整理过的普通文法转换为LL(1)文法
     * @param grammar 整理过的普通文法
     */
    public static void transToLL1(Grammar grammar){
        removeLeftRecursion(grammar);
        removeLeftGene(grammar);
    }

    /**
     * 消除左递归
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

    }
}
