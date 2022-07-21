package pooh;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topicMap
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String httpRequest = req.httpRequestType();
        if (!"GET".equals(httpRequest) && !"POST".equals(httpRequest)) {
            throw new IllegalArgumentException("Input correct httpType");
        }
        if ("POST".equals(httpRequest) && topicCheck(req)) {
            postReq(req);
        } else {
            putTopic(req);
            String getReq = subscribeOrGet(req).orElse("");
            if (!EMPTY_VALUE.equals(getReq)) {
                return new Resp(getReq, REQUEST_PASSED);
            }
        }
        return new Resp(EMPTY_VALUE, NO_DATA);
    }


    private void postReq(Req req) {
        topicMap.computeIfPresent(req.getSourceName(), (topic, subs) -> {
            subs.forEach((sub, queue) -> queue.add(req.getParam()));
            return subs;
        });
    }

    private void putTopic(Req req) {
        topicMap.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
    }

    private Optional<String> subscribeOrGet(Req req) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> subs = topicMap.get(req.getSourceName());
        String param = req.getParam();
        if (subs.putIfAbsent(param, new ConcurrentLinkedQueue<>()) != null) {
            return Optional.ofNullable(subs.get(param).poll());
        }
        return Optional.empty();
    }

    private boolean topicCheck(Req req) {
        return topicMap.get(req.getSourceName()) != null;
    }
}
