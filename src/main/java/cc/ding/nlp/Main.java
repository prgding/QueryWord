package cc.ding.nlp;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Main {
    private static final String ARTICLE_A = "a";
    private static final String ARTICLE_AN = "an";
    private static final String ARTICLE_THE = "the";
    private static WordnetStemmer stemmer;
    private static final StanfordCoreNLP pipeline;

    static {
        System.out.println("静态方法执行了");
        // 建立 WordNet 数据库的路径
        URL url = Main.class.getClassLoader().getResource("dict");

        // 建立 WordNet 数据库
        IDictionary dict;
        if (url != null) {
            dict = new Dictionary(url);
            try {
                dict.open();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stemmer = new WordnetStemmer(dict);
        }

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    public static ArrayList<ArrayList<String>> process(String passage) throws IOException {
        System.out.println("============ nlp.output ============");
        // 创建一个 List 用于 返回
        ArrayList<ArrayList<String>> allWordsList = new ArrayList<>();

        // 去掉标点符号
        String cleanText = passage.replaceAll("[^\\w\\s']+", "");
        Annotation document = new Annotation(cleanText);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                ArrayList<String> oneWordList = new ArrayList<>();
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                System.out.println("NLP == " + word + ", " + pos + ", " + lemma);

                // 词性还原
                // NLP 处理所有的动词还原，所有的名词复数
                String targetWord = pos.contains("VB") || pos.contains("NNS") || pos.equals("RB") ? lemma : word;

                // WordNet 处理形容词比较级、最高级
                List<String> stems;
                if (pos.equals("JJR") || pos.equals("JJS")) {
                    // 查询形容词的原级
                    if (stemmer != null) {
                        try {
                            stems = stemmer.findStems(word, POS.ADJECTIVE);
                            targetWord = stems.get(0);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("没有 " + word + " 单词");
                            targetWord = null;
                        }

                    }
                } else if (pos.contains("NN")) {
                    // NNS 查询复数名词原型
                    // NNPS 查询复数专有名词原型
                    if (stemmer != null) {
                        try {
                            stems = stemmer.findStems(word, POS.NOUN);
                            targetWord = stems.get(0);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("没有 " + word + " 单词");
                            targetWord = null;
                        }
                    }
                }

                Map<String, String> posMap = new HashMap<>();
                posMap.put(ARTICLE_A, "Article");
                posMap.put(ARTICLE_AN, "Article");
                posMap.put(ARTICLE_THE, "Article");
                posMap.put("VB", "Verb");
                posMap.put("MD", "Verb");
                posMap.put("CC", "Conjunction");
                posMap.put("IN", "Preposition");
                posMap.put("TO", "T");
                posMap.put("PRP", "Pronoun");
                posMap.put("DT", "Determiner");
                posMap.put("RB", "Adverb");
                posMap.put("CD", "Number");
                posMap.put("NN", "Noun");
                posMap.put("EX", "Existential");
                posMap.put("JJ", "Adjective");
                posMap.put("UH", "Interjection");

                // 用遍历实现 contains 操作
                for (String key : posMap.keySet()) {
                    if (pos.contains(key)) {
                        pos = posMap.get(key);
                        break;
                    }
                }
                System.out.println(("查询 == " + targetWord + ", " + pos));
                oneWordList.add(word);
                oneWordList.add(pos);
                oneWordList.add(targetWord);
                allWordsList.add(oneWordList);
            }
        }
        return allWordsList;
    }
}
