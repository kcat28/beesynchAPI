package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.Schedule;
import com.beesynch.app.rest.Repo.ScheduleRepo;
import com.beesynch.app.rest.Repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/schedule")
    public class ScheduleController {

    @Autowired
    private ScheduleRepo ScheduleRepo;

//    //Assign Schedule to a task
//    @PostMapping("/task/{taskId}")
//    public Schedule addScheduleToTask(@PathVariable Long taskId, @RequestBody Schedule scheduleDetails) {
//        Task task = taskRepo.findById(taskId)
//                .orElseThrow(() -> new RuntimeException("Task not found."));
//        return ScheduleRepo.save(scheduleDetails);
//    } black out na since schedule creation relies on TaskController via Service

    //Get Schedule from a task
    @GetMapping("/task/{taskId}")
    public Schedule findByTaskId(@PathVariable Long taskId){
        return ScheduleRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Schedule not found."));
    }

    //Modify Schedule from a task
    @PutMapping("/{taskId}")
    public Schedule updateScheduleFromTask(@PathVariable Long schedule_id, @RequestBody Schedule scheduleDetails){
        Schedule schedule = ScheduleRepo.findById(schedule_id)
                .orElseThrow(() -> new RuntimeException("Task not found."));
        schedule.setUser_id(scheduleDetails.getUser_id());
        schedule.setRecurrence(scheduleDetails.getRecurrence());
        schedule.setDue_time(scheduleDetails.getDue_time());
        schedule.setEnd_date(scheduleDetails.getEnd_date());
        schedule.setStart_date(scheduleDetails.getStart_date());
        return ScheduleRepo.save(schedule);
    }
}
