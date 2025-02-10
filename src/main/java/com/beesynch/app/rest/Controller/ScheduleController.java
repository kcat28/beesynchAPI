package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Repo.ScheduleRepo;
import com.beesynch.app.rest.Repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/schedule")
    public class ScheduleController {

    @Autowired
    private ScheduleRepo ScheduleRepo;

    //Get Schedule from a task
    @GetMapping("/task/{taskId}")
    public Schedule findByTaskId(@PathVariable Long taskId) {
        return ScheduleRepo.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Schedule not found"));
    }


    //Modify Schedule from a task
    @PutMapping("/{schedule_id}")
    public Schedule updateScheduleFromTask(@PathVariable Long schedule_id, @RequestBody Schedule scheduleDetails){
        Schedule schedule = ScheduleRepo.findById(schedule_id)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));
        schedule.setUser_id(scheduleDetails.getUser_id());
        schedule.setRecurrence(scheduleDetails.getRecurrence());
        schedule.setDue_time(scheduleDetails.getDue_time());
        schedule.setEnd_date(scheduleDetails.getEnd_date());
        schedule.setStart_date(scheduleDetails.getStart_date());
        return ScheduleRepo.save(schedule);
    }
}
