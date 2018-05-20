package com.tmooc.work.DTO;

import lombok.*;


/**
 * @author northsailor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private Integer month;
    private Integer week;
    private Integer weekDay;
    private Integer answer;
    private Integer remote;
    private Integer count;
    private Integer validCount;
    private Integer broadcast;
    private Integer inputTitle;
    private Integer forward;
    private Integer other;
    private String emailPwd;
}
