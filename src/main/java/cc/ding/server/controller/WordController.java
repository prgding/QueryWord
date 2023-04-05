package cc.ding.server.controller;

import cc.ding.server.service.WordService;
import jakarta.servlet.http.HttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
public class WordController extends HttpServlet {
    @Autowired
    private WordService wordService;

    @RequestMapping("/get_passage")
    public String getPassage(@RequestParam("passage") String passage) throws IOException {
        wordService.process(passage);
        return "success.jsp";
    }
}
