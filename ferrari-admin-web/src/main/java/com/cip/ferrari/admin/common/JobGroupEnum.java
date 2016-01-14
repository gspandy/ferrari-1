/**
 * 
 */
package com.cip.ferrari.admin.common;

/**
 * @author yuantengkai
 * job组枚举
 */
public enum JobGroupEnum {
	
	DEFAULT("默认"),
	WEDDING("结婚"),
	BABY("亲子"),
	HOME("家装"),
	BEAUTY("丽人");
	
	private String desc;
	
	private JobGroupEnum(String desc){
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static JobGroupEnum match(String name){
		for (JobGroupEnum item : JobGroupEnum.values()) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return null;
	}

}
