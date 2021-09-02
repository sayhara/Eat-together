package com.eattogether.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        log.info("processors count:{}",processors);
        executor.setCorePoolSize(processors); // 프로세서 수만큼 코어사이즈 수 설정 (수영장의 튜브개념)
        executor.setMaxPoolSize(processors*2); // 남는 튜브가 없을 때 새로 빌려오는 개념
        executor.setQueueCapacity(50); // 수영장의 줄 개념
        executor.setKeepAliveSeconds(60); // 새로 빌려온 튜브를 빌릴 수 있는 시간
        executor.setThreadNamePrefix("AsyncExecutor-"); // 이름
        executor.initialize();
        return executor;
    }

}
