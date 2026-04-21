import service from "@/util/request";
import url from './url'
// 连掉测试接口
export function payWayadd (data) {
  return service({
    url: `${url}/auth/login/pc`,
    method: "post",
    data
  });
}
// 测试
export function codeMajor (data) {
  return service({
    url: `${url}/sys/profiles/list-by-code/Major`,
    method: "get",
    // data,
  });
}
// 获取课程模板数据
export function getTemplateData (code) {
  return service({
    url: `${url}/biz/course-master/detail/student/` + code,
    method: "get"
    // data,
  });
}
// 获取课程模板数据
export function studentSubmit (data) {
  return service({
    url: `${url}/auth/student/submit`,
    method: "post",
    data,
  });
}
// 学生获取课程明细数据
export function getStudentTable (code) {
  return service({
    url: `${url}/biz/course-master/detail/student/` + code,
    method: "get",
    // data,
  });
}
// 学生获取课程明细数据
export function nextPreservation (data) {
  return service({
    url: `${url}/biz/course-master/next`,
    method: "post",
    data,
  });
}
// 文件oss上传
export function uploadSuess (data) {
  return service({
    url: `${url}/common/oss/upload`,
    method: "post",
    data,
  });
}
// 保存第七步
export function saveStudentCheckBox (data) {
  return service({
    url: `${url}/biz/course-master/student/check/submit`,
    method: "post",
    data,
  });
}
// submit提交
export function onSubmit (data) {
  return service({
    url: `${url}/biz/course-master/student/submit`,
    method: "post",
    data,
  });
}
// 获取模板数据
export function getCourseTemplateList (data) {
  return service({
    url: `${url}/biz/course-template/list-by-condition`,
    method: "post",
    data,
  });
}
// 获取Units分数最大值
export function getUnitsMaxVal (id) {
  return service({
    url: `${url}/biz/course-template/detail/` + id,
    method: "get",
  });
}
// 获取模板数据
export function getCourseCategory (data) {
  return service({
    url: `${url}/biz/course-category/get-by-condition`,
    method: "post",
    data,
  });
}