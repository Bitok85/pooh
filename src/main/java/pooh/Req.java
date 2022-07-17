package pooh;

public class Req {

    private static final int SMALL_REQ = 4;

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceNAme, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceNAme;
        this.param = param;
    }

    public static Req of(String content) {
        String[] parseArr = content.split(System.lineSeparator());
        String firstStr = parseArr[0];
        String[] firstArr = firstStr.split("/");
        String lastStr;
        if (parseArr.length > SMALL_REQ) {
            lastStr = parseArr[parseArr.length - 1];
        } else {
            if (firstArr.length > SMALL_REQ) {
                lastStr = firstArr[3].split(" ")[0];
            } else {
                lastStr = "";
            }
        }
        return new Req(
                firstArr[0].trim(),
                firstArr[1],
                firstArr[2].split(" ")[0],
                lastStr
        );
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
