package com.example.jSearch.utils;


import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class NLPUtils {
    private static Word2Vec word2Vec;
    private static StanfordCoreNLP pipeline;
    private static Set<String> stopwords = new HashSet<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours",
            "ourselves", "you", "your", "yours", "yourself", "yourselves",
            "he", "him", "his", "himself", "she", "her", "hers", "herself",
            "it", "its", "itself", "they", "them", "their", "theirs", "themselves",
            "what", "which", "who", "whom", "this", "that", "these", "those", "am",
            "is", "are", "was", "were", "be", "been", "being", "have", "has", "had",
            "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if",
            "or", "because", "as", "until", "while", "of", "at", "by", "for", "with",
            "about", "against", "between", "into", "through", "during", "before", "after",
            "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over",
            "under", "again", "further", "then", "once", "here", "there", "when", "where",
            "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some",
            "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s",
            "t", "can", "will", "just", "don", "should", "ll", "now"));

    static {
        //word2Vec = WordVectorSerializer.readWord2VecModel(new File("models/word2vecmodel.txt"));

        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");

        pipeline = new StanfordCoreNLP(props);
    }

    public static Word2Vec getWord2Vec() {
        return word2Vec;
    }

    public static List<String> lemmatize(String input) {

        List<String> lemmas = new LinkedList<>();

        Annotation document = new Annotation(input.toLowerCase());

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                lemmas.add(token.get(CoreAnnotations.LemmaAnnotation.class));
            }
        }
        return lemmas.stream()
                .filter(el -> !stopwords.contains(el) && !el.isEmpty() && !el.matches("[,?$'a-z]"))
                .collect(Collectors.toList());
    }

}
