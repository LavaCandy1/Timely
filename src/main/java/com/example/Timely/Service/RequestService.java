package com.example.Timely.Service;

import java.util.List;
import java.sql.Date;

import org.springframework.stereotype.Service;
import com.example.Timely.Models.ExtraClassRequest;
import com.example.Timely.Repository.ExtraClassRequestRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ExtraClassRequestRepo extraClassRepo;

    

    public void submitExtraClassRequest(ExtraClassRequest request) {
        request.setStatus("PENDING");
        request.setCreatedAt(new Date(System.currentTimeMillis()));
        System.out.println("Saving new extra class request: " + request);
        extraClassRepo.save(request);
    }

    public List<ExtraClassRequest> getAllExtraClassRequests() {
        return extraClassRepo.findAll();
    }

    public void rejectExtraClassRequest(Long id) {
        if (id == null) {
            return;
        }
        if (extraClassRepo.existsById(id)) {
            extraClassRepo.findById(id).ifPresent(request -> {
                request.setStatus("REJECTED");
                extraClassRepo.save(request);
            });
        }
    }

    public void approveExtraClassRequest(Long id) {
        if (id == null) {
            return;
        }
        if (extraClassRepo.existsById(id)) {
            extraClassRepo.findById(id).ifPresent(request -> {
                request.setStatus("APPROVED");
                extraClassRepo.save(request);
            });
        }
    }
}