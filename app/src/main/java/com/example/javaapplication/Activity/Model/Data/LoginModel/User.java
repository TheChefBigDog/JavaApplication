package com.example.javaapplication.Activity.Model.Data.LoginModel;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("faceDetection")
	private Object faceDetection;

	@SerializedName("branchId")
	private String branchId;

	@SerializedName("employeeRegisterId")
	private Object employeeRegisterId;

	@SerializedName("roleId")
	private int roleId;

	@SerializedName("employeeId")
	private int employeeId;

	@SerializedName("scoreKpiDay")
	private Object scoreKpiDay;

	@SerializedName("branchParent")
	private Object branchParent;

	@SerializedName("isActive")
	private boolean isActive;

	@SerializedName("branch")
	private String branch;

	@SerializedName("userId")
	private String userId;

	@SerializedName("parentId")
	private Object parentId;

	@SerializedName("lastLoginTime")
	private long lastLoginTime;

	@SerializedName("scoreKpiMonth")
	private Object scoreKpiMonth;

	@SerializedName("handphone")
	private Object handphone;

	@SerializedName("defaultUserPassDuration")
	private int defaultUserPassDuration;

	@SerializedName("name")
	private String name;

	@SerializedName("roleName")
	private String roleName;

	@SerializedName("branchType")
	private String branchType;

	@SerializedName("userType")
	private String userType;

	@SerializedName("email")
	private String email;

	@SerializedName("roleGroupId")
	private int roleGroupId;

	@SerializedName("username")
	private String username;

	public void setFaceDetection(Object faceDetection){
		this.faceDetection = faceDetection;
	}

	public Object getFaceDetection(){
		return faceDetection;
	}

	public void setBranchId(String branchId){
		this.branchId = branchId;
	}

	public String getBranchId(){
		return branchId;
	}

	public void setEmployeeRegisterId(Object employeeRegisterId){
		this.employeeRegisterId = employeeRegisterId;
	}

	public Object getEmployeeRegisterId(){
		return employeeRegisterId;
	}

	public void setRoleId(int roleId){
		this.roleId = roleId;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setEmployeeId(int employeeId){
		this.employeeId = employeeId;
	}

	public int getEmployeeId(){
		return employeeId;
	}

	public void setScoreKpiDay(Object scoreKpiDay){
		this.scoreKpiDay = scoreKpiDay;
	}

	public Object getScoreKpiDay(){
		return scoreKpiDay;
	}

	public void setBranchParent(Object branchParent){
		this.branchParent = branchParent;
	}

	public Object getBranchParent(){
		return branchParent;
	}

	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

	public boolean isIsActive(){
		return isActive;
	}

	public void setBranch(String branch){
		this.branch = branch;
	}

	public String getBranch(){
		return branch;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setParentId(Object parentId){
		this.parentId = parentId;
	}

	public Object getParentId(){
		return parentId;
	}

	public void setLastLoginTime(long lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public long getLastLoginTime(){
		return lastLoginTime;
	}

	public void setScoreKpiMonth(Object scoreKpiMonth){
		this.scoreKpiMonth = scoreKpiMonth;
	}

	public Object getScoreKpiMonth(){
		return scoreKpiMonth;
	}

	public void setHandphone(Object handphone){
		this.handphone = handphone;
	}

	public Object getHandphone(){
		return handphone;
	}

	public void setDefaultUserPassDuration(int defaultUserPassDuration){
		this.defaultUserPassDuration = defaultUserPassDuration;
	}

	public int getDefaultUserPassDuration(){
		return defaultUserPassDuration;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public String getRoleName(){
		return roleName;
	}

	public void setBranchType(String branchType){
		this.branchType = branchType;
	}

	public String getBranchType(){
		return branchType;
	}

	public void setUserType(String userType){
		this.userType = userType;
	}

	public String getUserType(){
		return userType;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setRoleGroupId(int roleGroupId){
		this.roleGroupId = roleGroupId;
	}

	public int getRoleGroupId(){
		return roleGroupId;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}