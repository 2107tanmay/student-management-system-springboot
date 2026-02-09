package com.example.student_management_sys_demo.services;

import com.example.student_management_sys_demo.model.ChangeRequest;
import com.example.student_management_sys_demo.model.User;
import com.example.student_management_sys_demo.repository.ChangeRequestRepository;
import com.example.student_management_sys_demo.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class ChangeRequestService {

    private final ChangeRequestRepository changeRequestRepository;
    private final UserRepository userRepository;

    public ChangeRequestService(ChangeRequestRepository changeRequestRepository, UserRepository userRepository) {
        this.changeRequestRepository = changeRequestRepository;
        this.userRepository = userRepository;
    }

    public ChangeRequest createRequest(Long requesterUserId, String fieldName, String newValue) {
        User requester = userRepository.findById(requesterUserId)
                .orElseThrow(() -> new NoSuchElementException("Requester not found"));
        ChangeRequest request = new ChangeRequest();
        request.setRequester(requester);
        request.setFieldName(fieldName);
        request.setNewValue(newValue);
        return changeRequestRepository.save(request);
    }

    public List<ChangeRequest> getRequestsForUser(Long requesterUserId) {
        return changeRequestRepository.findByRequesterId(requesterUserId);
    }
}
