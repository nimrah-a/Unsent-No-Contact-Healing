package com.unsent.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unsent.model.*;
import com.unsent.repo.*;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.util.*;

@RestController @RequestMapping("/api")
public class UnsentController {
 private static final Set<String> KINDS=Set.of("journey","boundary","reason","letter","contact","reflection","coping","card","card-done","contact-log","future","goal","pause","preference");
 private final JournalRepository items;
 private final CheckinRepository checkins;
 private final ObjectMapper mapper;
 public UnsentController(JournalRepository items,CheckinRepository checkins,ObjectMapper mapper){this.items=items;this.checkins=checkins;this.mapper=mapper;}
 public record ItemInput(@NotBlank String kind,Long journeyId,LocalDate eventDate,@NotBlank @Size(max=160) String title,String body,String details){}
 public record CheckinInput(@NotNull Long journeyId,@NotNull LocalDate date,boolean honored,String note){}
 @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok","app","Unsent");}
 @GetMapping("/items") public List<JournalItem> all(@RequestParam(required=false) String kind,@RequestParam(required=false) Long journeyId){
   if(kind!=null && !KINDS.contains(kind)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown item type");
   List<JournalItem> list=kind==null?items.findAllByOrderByCreatedAtDesc():items.findByKindOrderByCreatedAtDesc(kind);
   return journeyId==null?list:list.stream().filter(i->Objects.equals(journeyId,i.getJourneyId())).toList();
 }
 @GetMapping("/items/{id}") public JournalItem get(@PathVariable Long id){return items.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 @PostMapping("/items") @ResponseStatus(HttpStatus.CREATED) public JournalItem create(@jakarta.validation.Valid @RequestBody ItemInput input){return save(null,input);}
 @PutMapping("/items/{id}") public JournalItem update(@PathVariable Long id,@jakarta.validation.Valid @RequestBody ItemInput input){return save(id,input);}
 private JournalItem save(Long id,ItemInput input){
   if(!KINDS.contains(input.kind())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown item type");
   JournalItem item=id==null?new JournalItem():get(id);
   if(input.details()!=null){try{mapper.readTree(input.details());}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Details must be JSON");}}
   item.setKind(input.kind());item.setJourneyId(input.journeyId());item.setEventDate(input.eventDate());item.setTitle(input.title().trim());item.setBody(input.body()==null?"":input.body());item.setDetails(input.details()==null?"{}":input.details());item.setUpdatedAt(Instant.now());return items.save(item);
 }
 @DeleteMapping("/items/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){if(!items.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND);items.deleteById(id);}
 @GetMapping("/checkins") public List<DailyCheckin> checkins(@RequestParam Long journeyId){return checkins.findByJourneyIdOrderByDateDesc(journeyId);}
 @PostMapping("/checkins") public DailyCheckin log(@jakarta.validation.Valid @RequestBody CheckinInput input){
   DailyCheckin checkin=checkins.findByJourneyIdAndDate(input.journeyId(),input.date()).orElseGet(DailyCheckin::new);
   checkin.setJourneyId(input.journeyId());checkin.setDate(input.date());checkin.setHonored(input.honored());checkin.setNote(input.note()==null?"":input.note());return checkins.save(checkin);
 }
 @DeleteMapping("/checkins/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deleteCheckin(@PathVariable Long id){if(!checkins.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND);checkins.deleteById(id);}
 @GetMapping("/future/ready") public List<JournalItem> ready(){return items.findByKindOrderByCreatedAtDesc("future").stream().filter(i->i.getEventDate()==null||!i.getEventDate().isAfter(LocalDate.now())).toList();}
 @GetMapping("/stats") public Map<String,Object> stats(@RequestParam(required=false) Long journeyId){
   List<JournalItem> all=items.findAll();Map<String,Long> counts=new TreeMap<>();for(JournalItem i:all){if(journeyId==null||Objects.equals(journeyId,i.getJourneyId()))counts.merge(i.getKind(),1L,Long::sum);}
   List<DailyCheckin> logs=journeyId==null?List.of():checkins.findByJourneyIdOrderByDateDesc(journeyId);
   long honored=logs.stream().filter(DailyCheckin::isHonored).count();
   int streak=0;LocalDate day=LocalDate.now();Set<LocalDate> dates=new HashSet<>();logs.stream().filter(DailyCheckin::isHonored).forEach(c->dates.add(c.getDate()));
   if(!dates.contains(day))day=day.minusDays(1);
   while(dates.contains(day)){streak++;day=day.minusDays(1);}
   return Map.of("counts",counts,"checkins",logs.size(),"honored",honored,"currentStreak",streak);
 }
}
