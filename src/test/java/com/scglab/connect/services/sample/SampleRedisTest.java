package com.scglab.connect.services.sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleRedisTest {

    @Autowired
    private SampleRedisRepository sampleRedisRepository;

    @After
    public void tearDown() throws Exception {
        this.sampleRedisRepository.deleteAll();
    }

    @Test
    public void 기본_등록_조회기능() {
        //given
        String id = "test1";
        LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
        Sample sample = Sample.builder()
                .id(id)
                .amount(1000L)
                .refreshTime(refreshTime)
                .build();

        //when
        this.sampleRedisRepository.save(sample);
        System.out.println(this.sampleRedisRepository.findAll());

        //then
        Sample savedSample = this.sampleRedisRepository.findById(id).get();
        assertThat(savedSample.getAmount()).isEqualTo(1000L);
        assertThat(savedSample.getRefreshTime()).isEqualTo(refreshTime);
    }
}