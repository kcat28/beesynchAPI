package com.beesynch.app.rest.Controller;
import com.beesynch.app.rest.DTO.BillDTO;
import com.beesynch.app.rest.DTO.ScheduleDTO;
import com.beesynch.app.rest.Models.Bill;
import com.beesynch.app.rest.Repo.BillRepo;
import com.beesynch.app.rest.Service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

        if (request.getSchedules() == null || request.getSchedules().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: schedules must not be empty.");
        }

            for(ScheduleDTO schedule : request.getSchedules() ){
                System.out.println("Due Time: " + schedule.getDueTime());

                if (schedule.getDueTime() == null) {
                    return ResponseEntity.badRequest().body("Error: due_time must not be null in the schedule.");
                }

                if (schedule.getStartDate() == null ) { // removed: || schedule.getEndDate() == null
                    return ResponseEntity.badRequest().body("Error: startDate is required in schedule.");
                }
            }

        try {
            Bill bill = billService.createFullTask(request);
            return ResponseEntity.ok(bill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/bills-by-end-date")
    public Map<String, List<Map<String, String>>> getBillsGroupedByEndDate() {
        return billRepo.findAll().stream()
                .flatMap(bill -> bill.getSchedule().stream() // Flatten the schedules
                        .map(schedule -> Map.entry(
                                schedule.getEnd_date() != null ? schedule.getEnd_date().toString() : "No End Date",
                                Map.of(
                                        "title", bill.getBill_name(),
                                        "img_path", bill.getImg_path() != null ? bill.getImg_path() : "",
                                        "due_time", schedule.getDue_time() != null ? schedule.getDue_time().toString() : "No Due Time"
                                )
                        )))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey, // Group by end date
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()) // Map values
                ));
    }


    @GetMapping("/getAllBills")
    public List<Bill> getAllBills(){
        return billRepo.findAll();
    }
}
