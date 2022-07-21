package pooh;

public interface Service {

    String REQUEST_PASSED = "200";
    String NO_DATA = "204";
    String EMPTY_VALUE = "";

    Resp process(Req req);
}
