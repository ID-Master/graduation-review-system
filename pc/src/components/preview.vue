<template>
  <div class="p-config page">
    <div class="riImg">
      <div class="rioneimg chenser">
        <img src="./title.png" alt="" />
      </div>
      <div class="rioneimgs">
        <img src="./title2.png" alt="" />
      </div>
      <div class="stRen">
        <i class="el-icon-user-solid sulid"></i>{{ nameCh }}
        <div class="suRtui">
          <div @click="$router.push('/list')">Query self inspection</div>
          <div @click="addout">Logout</div>
        </div>
      </div>
    </div>
    <el-button class="l-preview-back" type="primary" size="small" icon="el-icon-back" @click="handleBack"></el-button>
    <div class="p-container">
      <div class="tarRwi container-scroll">
        <div class="tarRis">
          <div id="exportPdf" ref="exportPdf">
            <div class="tarRcolor" id="eight">
              <div name="calc" class="calc tarRcolor-padding"></div>
              <nodet :text="systemInfo.noteOne" :shows="false" />
              <stuatsname :valueTotal="piData" />
              <div name="calc" class="calc tarRcolor-padding"></div>
            </div>

            <div v-for="(item, index) of contentTextList" :key="index">
              <template v-if="item.categoryCode === 'University Core'">
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="one">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <college-student
                    :tdData="UniversityCoreData"
                    :trData="trSchoolDataFilter(item)"
                    :row="0"
                    :txData="contentTextList"
                    class="target-node-item"
                    ref="university"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
              <template v-else-if="item.categoryCode === 'School Package'">
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="two">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <college-courses
                    :tdData="SchoolPackageData"
                    :trData="trSchoolDataFilter(item)"
                    :row="0"
                    :txData="contentTextList"
                    class="target-node-item"
                    ref="school"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
              <template v-else-if="item.categoryCode === 'Major Required Courses'">
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="three">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <core-courses
                    :tdData="MajorRequiredCoursesData"
                    :trData="trSchoolDataFilter(item)"
                    :txData="contentTextList"
                    :row="0"
                    ref="major"
                    class="target-node-item"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
              <template v-else-if="item.categoryCode === 'Major Elective Courses' && item.part === 'A'">
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="fore">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <professional-elective
                    :tdData="MajorElectiveCoursesDataA"
                    :trData="trSchoolDataFilter(item)"
                    :txData="contentTextList"
                    :is-major="isMajor"
                    :is-have-b="MajorElectiveCoursesDataB.length > 2"
                    :row="0"
                    ref="profess"
                    class="target-node-item"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
              <template v-else-if="item.categoryCode === 'Major Elective Courses' && item.part === 'B'">
                  <div name="calc" class="calc tar-margin"></div>
                  <div class="tarRcolor" id="five">
                    <div name="calc" class="calc tarRcolor-padding"></div>
                    <professionalElectiveB
                      :tdData="MajorElectiveCoursesDataB"
                      :trData="trSchoolDataFilter(item)"
                      :row="0"
                      :is-major="isMajor"
                      :txData="contentTextList"
                      class="target-node-item"
                      ref="professB"
                    />
                    <div name="calc" class="calc tarRcolor-padding"></div>
                  </div>
              </template>
              <template v-else-if="item.categoryCode === 'Free Elective'">
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="six">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <freeElectives
                    :txData="contentTextList"
                  />
                  <FreeElectiveData
                    :tdData="FreeElectiveData"
                    :trData="trSchoolDataFilter(item)"
                    :row="0"
                    :txData="contentTextList"
                    class="target-node-item"
                    ref="FreeE"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
              <template v-else>
                <div name="calc" class="calc tar-margin"></div>
                <div class="tarRcolor" id="one">
                  <div name="calc" class="calc tarRcolor-padding"></div>
                  <college-student
                    :tdData="courses[item.categoryCode]"
                    :trData="trSchoolDataFilter(item)"
                    :code="item.categoryCode"
                    :row="0"
                    :txData="contentTextList"
                    class="target-node-item"
                    ref="university"
                  />
                  <div name="calc" class="calc tarRcolor-padding"></div>
                </div>
              </template>
            </div>

            <div name="calc" class="calc tar-margin"></div>
            <div class="tarRcolor" id="seven">
              <div name="calc" class="calc tarRcolor-padding"></div>
              <statement
                :text="selfDeclaration || systemInfo.noteSeven"
                :text2="systemInfo.noteEight"
                :preview="true"
                :statementData="statementData"
                :isCheckBox="isCheckBox"
                :studentCheckFeedback="studentCheckFeedback"
                :teacherCheckBox="teacherCheckBox"
                :auditBtn="auditBtn"
              />
              <div name="calc" class="calc tarRcolor-padding"></div>
            </div>
          </div>
        </div>
      </div>
      <div v-if="$route.query.id" style="text-align: center;margin-top:20px;">
        <el-button type="primary" class="lancolor" @click="$router.back()" icon="el-icon-back" size="small">Back</el-button>
        <el-button type="primary" size="small" class="lancolor" @click="submission"
          >Submit</el-button
        >
      </div>
    </div>
  </div>
</template>
<script>
import moment from 'moment';
import nodet from './assembly/nodet.vue'
import stuatsname from './teachers/stuatsname.vue'
import collegeStudent from './puretable/collegeStudent'
import collegeCourses from './puretable/collegeCourses'
import coreCourses from './puretable/coreCourses'
import professionalElective from './puretable/professionalElective'
import professionalElectiveB from './puretable/professionalElectiveB'
import freeElectives from './puretable/freeElectives'
import FreeElectiveData from './puretable/FreeElectiveData'
import statement from './puretable/statement'
import {
  detailTeacher,
  detailTeacherByMajor,
  getSystemConfig,
  linkAdfsLogout,
} from '@/api/index'
import { onSubmit } from '@/api/student';

import Cookies from 'js-cookie'

export default {
  components: {
    nodet,
    stuatsname,
    'college-student': collegeStudent,
    'college-courses': collegeCourses,
    'core-courses': coreCourses,
    'professional-elective': professionalElective,
    professionalElectiveB,
    FreeElectiveData,
    freeElectives,
    statement,
  },
  data() {
    return {
      nameCh: '',
      isCheckBox: null,
      studentCheckFeedback: '',
      teacherCheckBox: false,
      auditBtn: false,
      //项目数据
      piData: {
        studentVO: {
          studentId: '',
          nameEn: ''
        },
      },
      // 大学核心课程`
      UniversityCoreData: [],
      // 学院课程
      SchoolPackageData: [],
      // 专业必修科目
      MajorRequiredCoursesData: [],
      // 专业选修科目
      MajorElectiveCoursesDataA: [],
      MajorElectiveCoursesDataB: [],
      // 自由选修
      FreeElectiveData: [],
      // 声明 Declaration
      statementData: {},
      trSchoolData: [
        {
          type: 'courseCode',
          text: 'Course Code',
        },
        {
          type: 'title',
          text: 'Course Title',
        },
        {
          type: 'Units',
          text: 'Units',
        },
        {
          type: 'selfCheck',
          text: 'Self-check(✔/NR/IP)',
        },
        {
          type: 'progress',
          text: 'Progress',
        },
        {
          type: 'Minor',
          text: 'Minor<br/>(Minor Required-/Minor Elective)',
        },
        {
          type: 'Remark',
          text: 'Remark<br/>(Credit Transfer for equivalent course/Course substitution)',
        },
      ],
      contentTextList: [],
      systemInfo: {},
      selfDeclaration: '',
      /** 是否课程预览 */
      isMajor: false,
      progressOption: ['', '已完成修读', '正在修读', '未修读'],
      /** 其他课程数据，对应课程编码 */
      courses: {},
    }
  },
  computed: {
    partBLength() {
      if (this.isMajor) {
        return 0;
      } else {
        return 2;
      }
    },
  },
  created() {
    this.nameCh = localStorage.getItem('nameCh')
    this.goDetails();
    this.getSystemInfo()
  },
  methods: {
    trSchoolDataFilter(node) {
      if (node && node.standard === 1) {
        // 排除的列
        let excludeRows = ['minor', 'progress', 'Minor'];

        if (node.type === 2) {
          excludeRows = ['minor', 'Units', 'selfCheck', 'Minor'];
        }

        return this.trSchoolData.filter((tr) => !excludeRows.includes(tr.type));
      }

      return this.trSchoolData.filter((tr) => tr.type !== 'progress');
    },
    async submission() {
      if (!this.$route.query.id) {
        return;
      }

      let signatureUrl = this.statementData.signatureUrl;
      let signatureDate = this.statementData.signatureDate;
      let englishSchoolReport = this.statementData.englishSchoolReport;
      console.log('------>',signatureUrl,signatureDate,englishSchoolReport);
      if(signatureUrl == null || signatureUrl == ''){
        this.$message.error('请选择上传签名后再提交！');
        return;
      }
      // if(englishSchoolReport == null || englishSchoolReport == ''){
      //   this.$message.error('请选择上传最新版英文成绩单后再提交！');
      //   return;
      // }
      if(signatureDate == null ||  signatureDate == ''){
        this.$message.error('请选择日期后再提交！');
        return;
      }
      let params = {
        id: this.$route.query.id,
        fileId: '',
        signatureUrl: signatureUrl,
        englishSchoolReport: englishSchoolReport,
        signatureDate: signatureDate,
      }

      let res = await onSubmit(params)
      if (res.code == 200) {
        this.$router.push({
          name: 'result',
          query: {
            indexs: 9
          },
        });
      } else {
        this.$router.push({
          name: 'result',
          query: {
            indexs: 10
          },
        });
      }
    },
    async getSystemInfo() {
      const result = await getSystemConfig();
      this.systemInfo = result.data || {};
    },
    empty() {
      this.UniversityCoreData = []
      this.SchoolPackageData = []
      this.MajorRequiredCoursesData = []
      this.MajorElectiveCoursesDataA = []
      this.MajorElectiveCoursesDataB = []
      this.FreeElectiveData = []
      this.courses = {};
    },
    // 查看自查表
    async goDetails() {
      let res = null;
      if (this.$route.query.id) {
        res = await detailTeacher(this.$route.query.id);
      } else {
        this.isMajor = true;
        res = await detailTeacherByMajor({
          major: this.$route.query.major,
          grade: this.$route.query.grade,
        });
      }
      if (res.code == 200 && res.data !== null) {
        this.contentTextList = res.data.courseCategoryVOList
        this.selfDeclaration = res.data.selfDeclaration;
      } else {
        this.$message({
          type: 'error',
          message: res.message,
        })
        return
      }
      this.piData = res.data
      this.auditBtn = res.data.status == 1 ? false : true
      const arr = res.data.courseDetailVOList || []
      this.empty()
      this.isCheckBox = this.piData.studentCheckBox
      this.studentCheckFeedback = this.piData.studentCheckFeedback || '';
      this.teacherCheckBox = this.piData.teacherCheckBox == 1 ? true : false
      arr.forEach((e) => {
        switch (e.selfCheck) {
          case '0':
            e.selfCheck = ''
            break
          case '1':
            e.selfCheck = '✔'
            break
          case '2':
            e.selfCheck = 'NR'
            break
          case '3':
            e.selfCheck = 'IP'
            break
        }

        const data = {
          courseCode: e.courseCode || '',
          title: e.courseTitle || '',
          Units: e.units || '',
          selfCheck: e.selfCheck || '',
          Remark: e.remark || '',
          Minor: e.minor || '',
          progress: e.progress ? this.progressOption[e.progress] : '',
        };

        if (e.courseCategoryCode == 'University Core') {
          this.UniversityCoreData.push(data)
        } else if (e.courseCategoryCode == 'School Package') {
          this.SchoolPackageData.push(data)
        } else if (e.courseCategoryCode == 'Major Required Courses') {
          this.MajorRequiredCoursesData.push(data)
        } else if (e.courseCategoryCode == 'Major Elective Courses' && e.part == 'A') {
          this.MajorElectiveCoursesDataA.push(data)
        } else if (e.courseCategoryCode == 'Major Elective Courses' && e.part == 'B') {
          this.MajorElectiveCoursesDataB.push(data)
        } else if (e.courseCategoryCode == 'Free Elective') {
          this.FreeElectiveData.push(data)
        } else {
          if (this.courses[e.courseCategoryCode]) {
            this.courses[e.courseCategoryCode].push(data);
          } else {
            this.courses[e.courseCategoryCode] = [data];
          }
        }
      })

      const date = moment().format('YYYY-MM-DD');
      const isStatus = res.data.status;
      console.log("====> preview isStatus", isStatus);
      let signatureDate = date;
      // 草稿、驳回状态以当前为填写时间，其它状态则以已填写
      if(![0,2].includes(isStatus)) {
        signatureDate = res.data.signatureDate;
      }
      console.log("====> preview signatureDate", signatureDate);
      this.statementData = {
        englishSchoolReport: res.data.englishSchoolReport,
        signatureUrl: res.data.signatureUrl,
        signatureDate: signatureDate,
        officerCheckedDate: res.data.officerCheckedDate
          ? res.data.officerCheckedDate
          : date,
        teacherCheckBox: res.data.teacherCheckBox == 1 ? true : false,
      }
      this.$nextTick(() => {
        const universities = this.$refs.university;
        if (universities) {
          if (universities instanceof Array) {
            universities.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            universities.setUnitsVal();
          }
        }

        const schools = this.$refs.school;
        if (schools) {
          if (schools instanceof Array) {
            schools.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            schools.setUnitsVal();
          }
        }

        const majors = this.$refs.major;
        if (majors) {
          if (majors instanceof Array) {
            majors.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            majors.setUnitsVal();
          }
        }

        const professes = this.$refs.profess;
        if (professes) {
          if (professes instanceof Array) {
            professes.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            professes.setUnitsVal();
          }
        }

        const professBs = this.$refs.professB;
        if (professBs) {
          if (professBs instanceof Array) {
            professBs.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            professBs.setUnitsVal();
          }
        }

        const frees = this.$refs.FreeE;
        if (frees) {
          if (frees instanceof Array) {
            frees.forEach((data) => {
              data.setUnitsVal();
            });
          } else {
            frees.setUnitsVal();
          }
        }
      })
      this.shows = false
    },
    async addout() {
      this.$confirm('此操作将退出返回登陆页面?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$message({
            type: 'success',
            message: '退出成功!',
          })
          this.outlogin()
        })
    },
    async outlogin() {
      Cookies.remove('XSRF-TOKEN')
      window.localStorage.clear()
      linkAdfsLogout()
    },
    handleBack() {
      this.$router.back();
    },
  },
}
</script>
<style scoped>
.p-config {
  background: #fff;
}

.riImg {
  background: #6a1d72;
  width: 100vw;
  height: 80px;
  position: fixed;
  z-index: 999;
  top: 0;
}

.rioneimg {
  height: 60px;
  padding: 10px;
  display: inline-block;
  vertical-align: middle;
}
.rioneimgs {
  height: 80px;
  display: inline-block;
  vertical-align: middle;
}
.rioneimgs img {
  height: 100%;
  margin-left: 10px;
}

.chenser {
  background: #ddaa43;
}
.rioneimg img {
  height: 100%;
}
.ritext {
  text-align: center;
  width: 350px;
  display: inline-block;
  color: #eee;
}
.ritext div:first-child {
  letter-spacing: 6px;
  font-family: 'Book Antiqua';
}

.stRen {
  position: absolute;
  top: 30px;
  right: 50px;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  z-index: 99999;
  height: 30px;
  overflow: hidden;
}
.stRen:hover {
  height: 100px;
}
.stRen span {
  vertical-align: middle;
}

.suRtui {
  border: 1px solid #6a1d72;
  color: #6a1d72;
  background: #fff;
  border-radius: 6px;
  font-weight: 600;
  padding: 8px 10px;
  margin-top: 10px;
}
.sulid {
  font-size: 20px;
  vertical-align: middle !important;
  margin-right: 10px;
}

.p-container {
  padding: 100px 40px;
}

.lancolor {
  border: none;
  background: #3a62d7 !important;
  padding: 10px 30px !important;
}
.lancolor:hover {
  background: #284fc1 !important;
}

.l-preview-back {
  position: fixed;
  top: 100px;
  left: 20px;
  background: #6a1d72;
  border-color: #6a1d72;
}
</style>
