package com.RateManagement;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Repo.RateRepository;
import com.RateManagement.Service.RateService;
import com.RateManagement.Specification.RateSpecification;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RateManagementApplicationTests {

    @Mock
    private RateRepository rateRepository;

    @InjectMocks
    private RateService rateService;

    @Test
    public void testGetAllRates() {
        // Mock the page data
        Page<Rate> pageData = Mockito.mock(Page.class);

        // Mock the rate data
        Rate rate1 = new Rate();
        rate1.setRateId(1L);
        rate1.setBungalowId(1L);
        rate1.setValue(100.0);

        Rate rate2 = new Rate();
        rate2.setRateId(2L);
        rate2.setBungalowId(1L);
        rate2.setValue(150.0);

        List<Rate> rates = new ArrayList<>();
        rates.add(rate1);
        rates.add(rate2);

        // Mock the repository's behavior
        Specification<Rate> spec = RateSpecification.searchByCriteria(1L, LocalDate.now(), LocalDate.now(),
                2, 200.0, 1L, null);
        Pageable pageable = Mockito.mock(Pageable.class);
        when(rateRepository.findAll(spec, pageable)).thenReturn(pageData);
        when(pageData.getContent()).thenReturn(rates);

        // Call the service method
        Page<Rate> result = rateService.getAllRates(pageable, 1L, LocalDate.now(), LocalDate.now(),
                2, 200.0, 1L, null);

        // Verify the result
        assertEquals(rates.size(), result.getContent().size());
        assertEquals(rate1.getRateId(), result.getContent().get(0).getRateId());
        assertEquals(rate2.getRateId(), result.getContent().get(1).getRateId());
        assertEquals(rate1.getBungalowId(), result.getContent().get(0).getBungalowId());
        assertEquals(rate2.getBungalowId(), result.getContent().get(1).getBungalowId());
        assertEquals(rate1.getValue(), result.getContent().get(0).getValue());
        assertEquals(rate2.getValue(), result.getContent().get(1).getValue());

        // Verify the repository method was called
        Mockito.verify(rateRepository).findAll(spec, pageable);
    }
}