package cc.ding.server.service.impl;

import cc.ding.nlp.Main;
import cc.ding.server.mapper.WordMapper;
import cc.ding.server.service.WordService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class WordServiceImpl implements WordService {
    private final WordMapper wordMapper;

    public WordServiceImpl(WordMapper wordMapper) {
        this.wordMapper = wordMapper;
    }

    @Override
    public void process(String passage) throws IOException {
        ArrayList<ArrayList<String>> allWordsList = Main.process(passage);

        System.out.println("===========service.output===========");
        System.out.println("allWordsList = " + allWordsList);
        allWordsList.forEach(oneWordList -> {
            System.out.println(oneWordList);
            String userInput = oneWordList.get(0);
            String pos = oneWordList.get(1);
            String lemma = oneWordList.get(2);
        });
    }
}