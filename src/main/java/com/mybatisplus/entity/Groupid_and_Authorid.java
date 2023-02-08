package com.mybatisplus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Groupid_and_Authorid {
    private String GroupId;
    private String Authorid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groupid_and_Authorid that = (Groupid_and_Authorid) o;

        if (GroupId != null ? !GroupId.equals(that.GroupId) : that.GroupId != null) return false;
        return Authorid != null ? Authorid.equals(that.Authorid) : that.Authorid == null;
    }

    @Override
    public int hashCode() {
        int result = GroupId != null ? GroupId.hashCode() : 0;
        result = 31 * result + (Authorid != null ? Authorid.hashCode() : 0);
        return result;
    }
}
