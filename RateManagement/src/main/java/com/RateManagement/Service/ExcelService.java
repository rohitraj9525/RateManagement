package com.RateManagement.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RateManagement.Entity.Rate;
import com.RateManagement.Helper.DownloadHelper;
import com.RateManagement.Repo.RateRepository;

@Service
public class ExcelService 
{
	  @Autowired
	  RateRepository repository;

	  public ByteArrayInputStream load() {
	    List<Rate> rateExcel = repository.findAll();

	    ByteArrayInputStream in = DownloadHelper.rateToExcel(rateExcel);
	    return in;
	  }

	}

