package imdb.exceptions;

public class NoRowFoundException extends Exception {


	private static final long serialVersionUID = -1144266575190407512L;

	public NoRowFoundException(String msg) {
		super(msg);
	}

}
