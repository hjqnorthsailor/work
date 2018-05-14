package com.tmooc.work.DTO;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private Integer month=1;
    private Integer week=1;
    private Integer weekDay=1;
    private Integer answer=0;
    private Integer remote=0;
    private Integer count=0;
    private Integer validCount=0;
    private Integer broadcast=0;
    private Integer inputTitle=0;
    private Integer forward=0;
    private Integer other=0;
    private String emailPwd;
}
