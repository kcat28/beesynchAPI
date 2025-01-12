package com.beesynch.app.rest.Controller;
import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.Bill;
import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.Task;
import com.beesynch.app.rest.Repo.BillRepo;
import com.beesynch.app.rest.Service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


//related to a hive
//has bill id, hive id, bill name, amount, and bill_status
// has schedule and notification - jep

@RestController
@RequestMapping("/bills")
public class BillController {

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private BillService billService;

    @PostMapping("/createFullBill")
    public ResponseEntity<?> createBill (@RequestBody BillDTO request){
        System.out.println("Received Schedule: " + request.getSchedules());

        if (request.getSchedules() != null) {
            for(ScheduleDTO schedule : request.getSchedules() ){
                System.out.println("Due Time: " + schedule.getDueTime());

                // Normal Validation
                if (request.getSchedules() == null || request.getSchedules().isEmpty()) {
                    return ResponseEntity.badRequest().body("Error: schedules must not be empty.");
                }

                if (schedule.getDueTime() == null) {
                    return ResponseEntity.badRequest().body("Error: due_time must not be null in the schedule.");
                }

                if (schedule.getStartDate() == null ) { // removed: || schedule.getEndDate() == null
                    return ResponseEntity.badRequest().body("Error: startDate is required in schedule.");
                }
            }
        }
        try {
            Bill bill = billService.createFullTask(request);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
