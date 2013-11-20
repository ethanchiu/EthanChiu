package com.lidroid.xutils.sample.entities;
/**
 * @Description: TODO
 * @author: ethan.qiu@sosino.com
 * @date: Nov 20, 2013
 */
public class TestEntity {
	
	String id;
	String name;
	int age;
	
	
	
//	public TestEntity(String id, String name, int age) {
//		super();
//		this.id = id;
//		this.name = name;
//		this.age = age;
//	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "TestEntity [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	
	
	
	
	
}
