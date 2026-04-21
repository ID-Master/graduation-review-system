package cn.edu.cuhk.mkt.entity.ad;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * AD域组织DTO
 *
 * @author ldz@u-need.com
 * @date 2021/4/2
 */
@Data
public class AdOrgDTO implements Serializable {

    /**
     * 相当于Code
     */
    private String name;

    /**
     * 说明 eg 在职正式教职工
     */
    private String description;

    /**
     * 层级 eg OU=staff,OU=cuhksz,DC=CUHK,DC=EDU,DC=CN
     */
    private String distinguishedName;

    private String  id;

    private String parentId;

    private Boolean hasChildren;

    private String weight;

    /**
     * 子集
     */
    private List<AdOrgDTO> children;

}
