package com.bupt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class IdAndNameAndCode {
    private Integer id;
    private String name;
    private String code;
    @Override
    public boolean equals(Object obj) {
        
        /** 对象是 null 直接返回 false **/
        if (obj == null) {
            return false;
        }
        /** 对象是当前对象，直接返回 true **/
        if (this == obj) {
            return true;
        }
        /** 判断对象类型是否是User **/
        if (obj instanceof IdAndNameAndCode) {
            IdAndNameAndCode vo = (IdAndNameAndCode) obj;
            /** 比较每个属性的值一致时才返回true **/
            /** 有几个对象就要比较几个属性 **/
            if (vo.name.equals(this.name) && vo.code.equals(this.code)&&vo.id.equals(this.id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重写hashcode方法，返回的hashCode一样才再去比较每个属性的值
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode() * this.getId().hashCode() * this.getCode().hashCode();
    }

}
