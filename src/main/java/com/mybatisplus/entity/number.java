package com.mybatisplus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class number {
   private int i = 0;
   public int addnumber(){
       this.i  =this.i+1;
       return this.i;
   }
}
