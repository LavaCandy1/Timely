package com.example.Timely.Models.dto.timetableDTO;

import java.util.List;

import com.example.Timely.Models.ClassSlot;

import lombok.Data;

@Data
public class UpdateClassDTO {

    List<String> ids;
    ClassSlot slot;

}
