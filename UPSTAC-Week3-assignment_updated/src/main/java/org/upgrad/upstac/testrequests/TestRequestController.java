package org.upgrad.upstac.testrequests;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.users.User;

import java.util.List;
import java.util.Optional;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;


@RestController
public class TestRequestController {

    Logger log = LoggerFactory.getLogger(TestRequestController.class);


    @Autowired
    private TestRequestService testRequestService;

    @Autowired
    private UserLoggedInService userLoggedInService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;


    @PostMapping("/api/testrequests")
    public TestRequest createRequest(@RequestBody CreateTestRequest testRequest) {
        try {
            validateTestRequestInput(testRequest);
            User user = userLoggedInService.getLoggedInUser();
            TestRequest result = testRequestService.createTestRequestFrom(user, testRequest);
            return result;
        }  catch (AppException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
    private void validateTestRequestInput(CreateTestRequest testRequest){
        if ( testRequest.getName() == "" ) throw new AppException("Name to be Entered");
        if ( testRequest.getAge() == null ) throw new AppException("Age to be Entered");
        if ( testRequest.getEmail() == "" ) throw new AppException("Email to be Entered");
        if ( testRequest.getPhoneNumber()=="" ) throw new AppException("Phone Number to be Entered");
        if ( testRequest.getAddress()=="" ) throw new AppException("Address to be Entered");
        if ( testRequest.getPinCode()==null ) throw new AppException("Pin Code to be Entered");
    }

    @GetMapping("/api/testrequests")
    public List<TestRequest> requestHistory() {

        User user = userLoggedInService.getLoggedInUser();
        return testRequestService.getHistoryFor(user);


    }

    @GetMapping("/api/testrequests/{id}")
    public Optional<TestRequest> getById(@PathVariable Long id) {


        return testRequestQueryService.getTestRequestById(id);


    }



}
