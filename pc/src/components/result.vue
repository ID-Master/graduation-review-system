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
          <div @click="addout">Logout</div>
        </div>
      </div>
    </div>
    <div class="p-container">
      <div v-if="indexs == 9">
        <div class="stIjiao-c">
          <i class="el-icon-success stIjioi"></i>
          <div class="suRd">Submitted success fully</div>
          <span>waiting for review by relevant departments</span>
          <!-- <div class="suRcs" @click="handlePreview">Preview</div> -->
          <div class="suRcs" @click="$router.back()">Back</div>
          <div class="suRcs" @click="addout">Log out</div>
        </div>
      </div>
      <div v-if="indexs == 10">
        <div class="stIjiao-c">
          <i class="el-icon-warning stIjioi"></i>
          <div class="suRd">Failed</div>
          <div class="suRvas">
            <div>Detail:</div>
            <div>Network Error</div>
          </div>
          <div class="suRcs" @click="$router.back()">Back</div>
          <div class="suRcs">Return to Resubmit</div>
        </div>
      </div>
      <!-- <div style="text-align: center;margin-top:20px;">
        <el-button type="primary" class="lancolor" @click="$router.back()" icon="el-icon-back" size="small">Back</el-button>
      </div> -->
    </div>
  </div>
</template>
<script>
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
      /** 是否课程预览 */
      isMajor: false,
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
    indexs() {
      return this.$route.query.indexs;
    },
  },
  created() {
    this.nameCh = localStorage.getItem('nameCh')
  },
  methods: {
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
.stIjiao-c {
  text-align: center;
}
.stIjioi {
  font-size: 120px;
  color: #6a1d72;
}
.stIjiois {
  font-size: 120px;
  color: #f56c6c;
}
.suRd {
  font-size: 25px;
  font-weight: 600;
  margin-bottom: 10px;
}
.suRcs {
  margin: 0 auto;
  margin-top: 30px;
  width: 500px;
  border: 1px solid #90c3fa;
  color: #3291f8;
  height: 60px;
  line-height: 60px;
  font-size: 17px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  box-shadow: 2px 2px 5px #71b2f7;
}
.suRcs:hover {
  border: 1px solid #1d84f4;
  color: #1d84f4;
  height: 60px;
  line-height: 60px;
  font-size: 17px;
  border-radius: 6px;
  font-weight: 600;
  box-shadow: 2px 2px 5px #2c88e9;
}
.suRvas {
  width: 520px;
  height: 160px;
  border: 2px solid #adadad;
  border-radius: 6px;
  text-align: left;
  padding: 10px;
  font-size: 16px;
  display: inline-block;
}
.suRvas div:first-child {
  font-weight: 600;
  padding-bottom: 10px;
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
</style>
