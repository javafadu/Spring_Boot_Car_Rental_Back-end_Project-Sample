package com.greenrent.service;

import com.greenrent.domain.User;
import com.greenrent.helper.ExcelReportHelper;
import com.greenrent.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {

    UserRepository userRepository;

    // Report1 : get all user informations

    public ByteArrayInputStream getUserReport() throws IOException {
        List<User> users = userRepository.findAll();
       return ExcelReportHelper.getUsersExcelReport(users);

    }
}
