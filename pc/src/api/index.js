import service from "@/util/request";
import url from './url'
import website from "./website";
import axios from "axios";
// 登录
export function authgetUserInfo () {
  return service({
    url: `${url}/auth/getUserInfo`,
    method: "get",
  });
}
// 登录账号
export function payWayadd (data) {
  return service({
    url: `${url}/auth/login/pc`,
    method: "post",
    data
  });
}
//新增模板信息
export function categoryInsert (data) {
  return service({
    url: `${url}/biz/course-category/insert`,
    method: "post",
    data,
  });
}
//获取详情
export function categoryDetail (id) {
  return service({
    url: `${url}/biz/course-category/detail/` + id,
    method: "get",
  });
}
/** 复制成新的数据 */
export function categoryCopy(data) {
  return service({
    url: `${url}/biz/course-category/copy`,
    method: "post",
    data,
  });
}
//获取修改
export function categoryUpdate (data) {
  return service({
    url: `${url}/biz/course-category/update`,
    method: "post",
    data,
  });
}
//删除模板信息
export function templateRemove (data) {
  return service({
    url: `${url}/biz/course-category/remove-by-ids`,
    method: "post",
    data,
  });
}
//删除子模板信息
export function templateRemoves (data) {
  return service({
    url: `${url}/biz/course-template/remove-by-ids`,
    method: "post",
    data,
  });
}

//获取模板列表
export function templateList (data) {
  return service({
    url: `${url}/biz/course-template/list`,
    method: "post",
    data,
  });
}
//获取模板详情
export function templateDetail (id) {
  return service({
    url: `${url}/biz/course-template/detail/` + id,
    method: "get",
  });
}
//获取枚举
export function codeMajor (data) {
  return service({
    url: `${url}/sys/profiles/list-by-code/Major`,
    method: "get",
    // data,
  });
}
//获取子列表
export function categoryList (data) {
  return service({
    url: `${url}/biz/course-category/list`,
    method: "post",
    data,
  });
}
//获取当前学生课程分类
export function currentCategoryList (data) {
  return service({
    url: `${url}/biz/course-category/get-current-list`,
    method: "post",
    data,
  });
}
//添加子类表
export function templateInsert (data) {
  return service({
    url: `${url}/biz/course-template/insert`,
    method: "post",
    data,
  });
}
export function templateUpdate (data) {
  return service({
    url: `${url}/biz/course-template/update`,
    method: "post",
    data,
  });
}
//退出
export function authLogout () {
  return service({
    url: `${url}/auth/logout`,
    method: "get",

  });
}

export function approvalrecordList (data) {
  return service({
    url: `${url}/biz/approval-record/list`,
    method: "post",
    data,
  });
}
//教师端学生列表
export function coursemasterList (data) {
  return service({
    url: `${url}/biz/course-master/list`,
    method: "post",
    data,
  });
}
//教师端学生详情
export function detailTeacher (data) {
  return service({
    url: `${url}/biz/course-master/detail/teacher/${data}`,
    method: "get",
  });
}
/** 课程预览 */
export function detailTeacherByMajor(data) {
  return service({
    url: `${url}/biz/course-master/view/teacher`,
    method: "post",
    data,
  });
}
//驳回
export function rejectTeacher (data) {
  return service({
    url: `${url}/biz/course-master/teacher/approve`,
    method: "post",
    data
  });
}

export function linkAdfsLogin () {
  window.location.href = `${website}/oauth2/authorization/adfs`;
}
export function linkAdfsLogout () {
  window.location.href = "https://sts.cuhk.edu.cn/adfs/ls/?wa=wsignout1.0";
}
export function getAdfsUser () {
  return service({
    url: `${website}/adfs/getInfo`,
    method: "get",
  })
}

//导出教师表格excel
export function exportList (data) {
  return service({
    url: `${url}/biz/course-master/export`,
    method: "post",
    responseType: "arraybuffer",
    data
  });
}
export function exportExcel(data) {
  axios({
    method: "post",
    url: `${url}/biz/course-master/export`,
    data: data,
    responseType: "blob"
  })
  .then(res => {
    console.log(decodeURI(res.headers[`filename`]));
    const link = document.createElement("a");
    let blob = new Blob([res.data], { type: "application/vnd.ms-excel" });
    link.style.display = "none";
    link.href = URL.createObjectURL(blob);
    link.setAttribute("download", decodeURI('课程数据报表.xlsx'));
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  })
  .catch(error => {
    this.$message({
      type: 'error',
      title: "错误",
      message: '系统数据错误',
    })
  });
}

//Save
export function detailSave (data) {
  return service({
    url: `${url}/biz/course-master/teacher/approve`,
    method: "post",
    data
  });
}

/** 系统配置保存 */
export function systemConfig(data) {
  return service({
    url: `${url}/sys/info/update`,
    method: "post",
    data
  });
}

/** 获取系统配置 */
export function getSystemConfig(data) {
  return service({
    url: `${url}/sys/info/get`,
    method: "get",
    data
  });
}

/** 获取学年 */
export function getSchoolYear(data) {
  return service({
    url: `${url}/biz/course-master/student/expected-list`,
    method: "get",
    data
  });
}

// 文件oss上传
export function uploadSuess (data) {
  return service({
    url: `${url}/biz/course-master/student/import`,
    method: "post",
    data,
  });
}

// 批量发送邮件
export function batchSendEmail (data) {
  return service({
    url: `${url}/biz/course-master/teacher/email`,
    method: "post",
    data
  });
}

/** 修改数据状态 */
export function updateStatus(data) {
  return service({
    url: `${url}/biz/course-master/detail/teacher/update`,
    method: "post",
    data
  });
}

/** 修改acknowledged状态 */
export function updateAcknowledged(data) {
  return service({
    url: `${url}/biz/course-master/detail/teacher/acknowledged`,
    method: "post",
    data
  });
}

/** 专业配置 */
export function majorList(data) {
  return service({
    url: `${url}/sys/profiles/list`,
    method: "post",
    data
  });
}

/** 新增、修改专业配置 */
export function modifyMajor(data) {
  return service({
    url: `${url}/sys/profiles/${data.id ? 'update' : 'insert'}`,
    method: "post",
    data
  });
}

/** 删除专业配置 */
export function deleteMajor(id) {
  return service({
    url: `${url}/sys/profiles/remove/${id}`,
    method: "get",
  });
}

/** 报表 */
export function report(data) {
  return service({
    url: `${url}/biz/course-master/detail/data/report-list`,
    method: "post",
    data,
  });
}
