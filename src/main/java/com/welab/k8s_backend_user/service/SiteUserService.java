package com.welab.k8s_backend_user.service;

import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.NotFound;
import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.domain.dto.SiteUserLoginDto;
import com.welab.k8s_backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.domain.event.SiteUserInfoEvent;
import com.welab.k8s_backend_user.domain.repository.SiteUserRepository;
import com.welab.k8s_backend_user.event.producer.KafkaMessageProducer;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;
import com.welab.k8s_backend_user.secret.jwt.props.TokenGenerator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();
        siteUserRepository.save(siteUser);
        SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity("Create", siteUser);
        kafkaMessageProducer.send(SiteUserInfoEvent.Topic, event);
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {
        SiteUser user = siteUserRepository.findByUserId(loginDto.getUserId());
        if (user == null) {
            throw new NotFound("아이디 또는 비밀번호를 확인하세요.");
        }
        if (!SecureHashUtils.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("비밀번호가 맞지 않습니다.");
        }

        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");
    }
}
