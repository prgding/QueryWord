package cc.ding.server.mapper;

import cc.ding.server.pojo.Word;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WordMapper {
	Word findOne(@Param("word") String word, @Param("pos") String pos);
	List<Word> findAll(String word);
}

