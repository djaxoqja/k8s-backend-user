package com.welab.k8s_backend_user.service.probe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProbeService {
    public void validateLiveness() {
        // TODO: 만일 서비스를 재시작 해야 하는 상황이면 exception 발생
    }

    public void validateReadiness() {
        // TODO: 만일 서비스 수행일 일시 중지해야 하는 상황이면 exception 발생
    }
}
