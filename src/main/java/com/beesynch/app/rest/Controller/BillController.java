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

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @GetMapping("/all-bills") // done
    public List<Bill> getAllBills() {
        return billRepo.findAll();
    }

    //01/19/2025
    @GetMapping("/bills-by-end-date")
    public Map<String, List<String>> getBillsGroupedByEndDate() {
        List<Object[]> results = billRepo.findBillDetails(); // Get raw results

        // Group by end_date (formatted as yyyy-MM-dd) and include title + due_time in the result
        return results.stream()
                .collect(Collectors.groupingBy(
                        result -> formatDateOnly((Date) result[1]), // Group by the formatted end_date (index 1)
                        Collectors.mapping(result -> formatBillDetails(result), Collectors.toList()) // Format bill details
                ));
    }

    // Helper function to extract the date portion for grouping
    private String formatDateOnly(Date date) {
        if (date == null) {
            return "Unknown Date"; // Handle null values
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date format (yyyy-MM-dd)
        return dateFormat.format(date);
    }

    // Helper function to format the bill details
    private String formatBillDetails(Object[] result) {
        String billName = (String) result[0]; // Bill name (index 0)
        Date dueTime = (Date) result[2]; // Due time (index 2)

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // Time format

        String formattedDueTime = (dueTime != null) ? timeFormat.format(dueTime) : "Unknown Due Time";

        // Combine details into a readable string
        return billName + " : " + formattedDueTime;
    }
}
