package cc.ding.server.pojo;

public class Word {
	int ranking;
	String word;
	String pos;

	public Word(int ranking, String word, String pos) {
		this.ranking = ranking;
		this.word = word;
		this.pos = pos;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return String.valueOf(ranking);
	}
}
