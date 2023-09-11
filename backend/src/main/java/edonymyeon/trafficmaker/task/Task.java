package edonymyeon.trafficmaker.task;

import java.util.Map;

public abstract class Task {
    /**
     * 작업을 수행합니다.
     *
     * @param resource 테스크 실행에 필요한 값들을 담습니다.
     * @return 테스크 수행 후 받은 결과를 담습니다.
     */
    public abstract Map<String, Object> execute(Map<String, Object> resource);
}
