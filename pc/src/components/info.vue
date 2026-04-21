<template>
  <div ref="exportPdf" :id="infoId">
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
      <template v-else-if="item.categoryCode === 'Major Elective Courses'">
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="fore">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <professional-elective
            :tdData="MajorElectiveCoursesDataA"
            :trData="trSchoolDataFilter(item)"
            :txData="contentTextList"
            :is-have-b="MajorElectiveCoursesDataB.length > 2"
            :row="0"
            ref="profess"
            class="target-node-item"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div
          v-if="MajorElectiveCoursesDataB.length > 2"
          class="tarRcolor"
          id="five"
        >
          <div name="calc" class="calc tarRcolor-padding"></div>
          <professionalElectiveB
            :tdData="MajorElectiveCoursesDataB"
            :trData="trSchoolDataFilter(item)"
            :row="0"
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
          <freeElectives :txData="contentTextList" />
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
        <div class="tarRcolor" :id="`one${index}`">
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
        :text="selfDeclaration"
        :text2="systemInfo.noteEight"
        :statementData="statementData"
        :isCheckBox="isCheckBox"
        :studentCheckFeedback="studentCheckFeedback"
        :teacherCheckBox="teacherCheckBox"
        :auditBtn="auditBtn"
      />
      <div name="calc" class="calc tarRcolor-padding"></div>
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
import statement from './puretable/statement'
import freeElectives from './puretable/freeElectives'
import FreeElectiveData from './puretable/FreeElectiveData'
import {
  detailTeacher,
  getSystemConfig,
} from '@/api/index'

export default {
  components: {
    nodet,
    stuatsname,
    statement,
    freeElectives,
    'college-student': collegeStudent,
    'college-courses': collegeCourses,
    'core-courses': coreCourses,
    'professional-elective': professionalElective,
    professionalElectiveB,
    FreeElectiveData,
  },
  props: {
    infoId: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      isCheckBox: null,
      studentCheckFeedback: null,
      teacherCheckBox: false,
      systemInfo: {},
      selfDeclaration: '',
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
      piData: {
        studentVO: {
          studentId: '',
          nameEn: ''
        },
      },
      auditBtn: false,
      contentTextList: [],
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
      progressOption: ['', '已完成修读', '正在修读', '未修读'],
      /** 其他课程数据，对应课程编码 */
      courses: {},
    };
  },
  created() {
    this.getSystemInfo();
    this.goDetails();
  },
  methods: {
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
      let res = await detailTeacher(this.infoId)
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
      this.studentCheckFeedback = this.piData.studentCheckFeedback
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
      
      this.statementData = {
        englishSchoolReport: res.data.englishSchoolReport || '',
        signatureUrl: res.data.signatureUrl || '',
        signatureDate: res.data.signatureDate,
        officerCheckedDate: res.data.officerCheckedDate
          ? res.data.officerCheckedDate
          : new Date().getFullYear() +
            '-' +
            (new Date().getMonth() + 1) +
            '-' +
            new Date().getDate(),
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

        this.handleInitEle();
      })
    },
    handleInitEle() {
      /** 获取可计算高度的元素 */
      const calcs = document.getElementById(this.infoId).getElementsByClassName('calc');
      this.height = 0;
      this.pageHeight = 1848; // 一页的高度
      for (let i = 0; i < calcs.length; i++) {
        const ident = calcs[i].getAttribute('desc');
        if (ident === 'content') {
          this.height += 20;
          console.log('---------content==========', calcs[i], calcs[i].children, 20, this.height);
          const nodes = calcs[i].children;
          for (let j = 0; j < nodes.length; j++) {
            this.handleNodeHeight(nodes[j]);
          }
        } else {
          this.handleNodeHeight(calcs[i]);
        }
      }

      this.$nextTick(() => {
        this.printPdf();
      });
    },
    handleNodeHeight(node) {
      if (node.offsetHeight + this.height < this.pageHeight) {
        // 添加这个节点仍不会超出一页高度
        this.height += node.offsetHeight
        console.log('========================', node, node.offsetHeight, this.height);
      } else {
        // 加入这个节点后高度超出一页，这页剩余部分填充一个空白div
        const ident = node.getAttribute('desc');
        const marginTop = this.pageHeight - this.height;
        let eleStyle = node.style;
        if (ident === 'tr') {
          eleStyle = node.parentNode.parentNode.style;
        } else if (ident === 'tfoot') {
          eleStyle = node.parentNode.style;
        }

        console.log('--------------------', node, node.offsetHeight, this.height, this.pageHeight - this.height);
        eleStyle.marginTop = `${marginTop}px` // 这页剩余高度
        
        this.height = 0 // 填充了空白后，这页就结束了，因此重置height
        this.height = node.offsetHeight;
      }
    },
    printPdf() {
      let studentVO = this.piData.studentVO
      let pdfNameArr = []
      let studentId = studentVO.studentId ? studentVO.studentId : ''
      if (studentId !== '') {
        pdfNameArr.push(studentId)
      }
      let studentName = studentVO.nameEn ? studentVO.nameEn : studentVO.nameCh
      if (studentName !== '') {
        pdfNameArr.push(studentName)
      }
      pdfNameArr.push('self-check-form')
      let pdfName = pdfNameArr.join('-')
      this.$nextTick(async () => {
        this.$PDFSave(this.$refs.exportPdf, pdfName);
      })
    },
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
  },
}
</script>
<style scoped>
.tarRcolor {  
  background: #fff;
  padding: 0 60px;
}
.tarRcolor-padding {
  height: 20px;
}
.tar-margin {
  height: 40px;
}
</style>
