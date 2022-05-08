package fortin.truefalse.multiplayer.quiz;

public class QuizPojo {

	String _question;
	String _answer;
	String category_name;

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public QuizPojo(String _question, String _answer, String category_name) {
		super();
		this._question = _question;

		this._answer = _answer;
		this.category_name = category_name;
	}

	// private variables
	int _id;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_question() {
		return _question;
	}

	public void set_question(String _question) {
		this._question = _question;
	}

	public String get_answer() {
		return _answer;
	}

	public void set_answer(String _answer) {
		this._answer = _answer;
	}

	// Empty constructor
	public QuizPojo() {

	}
}