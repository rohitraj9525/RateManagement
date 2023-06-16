package com.RateManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.RateService;
import java.util.*;
@SpringBootTest
public class RateManagementApplicationTests {

    @Mock
    private RateRepository rateRepository;

    @InjectMocks
    private RateService rateService;

    
}