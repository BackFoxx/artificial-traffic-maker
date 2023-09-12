package edonymyeon.trafficmaker.task;

import java.util.Map;

public abstract class Task {

    public static final String SERVER_URL_PREFIX = "http://43.201.69.141:8080";
    public static final String STATUS = "status";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";
    public static final String DEVICE_TOKEN = "deviceToken";

    /**
     * 작업을 수행합니다.
     *
     * @param resource 테스크 실행에 필요한 값들을 담습니다.
     * @return 테스크 수행 후 받은 결과를 담습니다.
     */
    public abstract Map<String, Object> execute(Map<String, Object> resource);
}
