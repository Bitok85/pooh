package pooh;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queueMap = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String httpRequest = req.httpRequestType();
        if (!"GET".equals(httpRequest) && !"POST".equals(httpRequest)) {
            throw new IllegalArgumentException("Input correct httpType");
        }
        if ("POST".equals(httpRequest)) {
            postReq(req);
        } else {
            String param = getReq(req).orElse("");
            if (param.equals("")) {
                return new Resp("", NO_DATA);
            } else {
                return new Resp(param, REQUEST_PASSED);
            }
        }
        return new Resp("", NO_DATA);
    }

    private Optional<String> getReq(Req req) {
        ConcurrentLinkedQueue<String> queue = queueMap.get(req.getSourceName());
        if (queue == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(queue.poll());
    }


    private void postReq(Req req) {
        if (queueMap.computeIfPresent(
                req.getSourceName(), (source, queue) -> queue.add(req.getParam()) ? queue : null) == null) {
            ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
            queue.add(req.getParam());
            queueMap.put(req.getSourceName(), queue);
        }
    }
}
