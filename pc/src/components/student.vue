<template>
  <div id="stu" v-loading.fullscreen.lock="fullscreenLoading">
    <div :class="{ sotpd: isActive }">
      <div class="riImg">
        <div class="rioneimg chenser">
          <img src="./title.png" alt="" />
        </div>
        <div class="rioneimgs">
          <img src="./title2.png" alt="" />
        </div>
      </div>
      <div class="step-auto">
        <el-steps :active="active" v-if="isActive" align-center>
          <el-step v-for="(item, index) in stepList" :key="index" :title="item.code"
            @click.native="togglePage(index, item.code)" style="cursor: pointer" />
        </el-steps>
      </div>
      <div class="stRen">
        <i class="el-icon-user-solid sulid"></i><span>{{ storageName }}</span>
        <div class="suRtui" @click="addout">Logout</div>
      </div>
    </div>
    <div :style="{ height: isActive ? '200px' : 0 }"></div>
    <div class="banxin">
      <div v-if="isActive">
        <div v-if="currentCode == 'Note'">
          <nodet @nextStep="nextStep()" :text="systemInfo.noteOne" />
        </div>
        <div v-if="currentNode.isStandard">
          <telar :tdData="uCoreTdData" :trData="uCoreTrDataFilter" :tableData="tableData"
            :totalUnitsPassed="totalUnitsPassed" :totalUnitsInProcess="totalUnitsInProcess" :couresList="couresList"
            :isStatus="isStatus" :currentDetail="currentDetail" @getTotalNum="getTotalNum" @refresh="refreshScore"
            ref="telar" />
          <div class="stBotton">
            <el-button class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode == 'School Package'">
          <schoolPackage :tdData="uCoreTdData" :trData="uCoreTrDataFilter" :tableData="tableData"
            :totalUnitsPassed="totalUnitsPassed" :totalUnitsInProcess="totalUnitsInProcess" :couresList="couresList"
            :currentDetail="currentDetail" :isStatus="isStatus" @getTotalNum="getTotalNum" @refresh="refreshScore"
            ref="school" />
          <div class="stBotton">
            <el-button class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode == 'Major Required Courses'">
          <majorRequiredcourses :tdData="uCoreTdData" :trData="uCoreTrDataFilter" :tableData="tableData"
            :totalUnitsPassed="totalUnitsPassed" :totalUnitsInProcess="totalUnitsInProcess" :couresList="couresList"
            :currentDetail="currentDetail" :isStatus="isStatus" @getTotalNum="getTotalNum" @refresh="refreshScore"
            ref="major" />
          <div class="stBotton">
            <el-button type="primary" class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode == 'Major Elective Courses'">
          <majorElectives :tdData="uCoreTdData" :trData="uCoreTrDataFilter" :tableDataA="tableDataA"
            :tableDataB="tableDataB" :totalUnitsA="totalUnitsA" :totalUnitsB="totalUnitsB" :couresList="couresList"
            :couresListA="couresListA" :couresListMerge="couresListMerge" :couresListB="couresListB"
            :totalUnitsPassedA="totalUnitsPassedA" :totalUnitsPassedB="totalUnitsPassedB"
            :totalUnitsInProcessA="totalUnitsInProcessA" :totalUnitsInProcessB="totalUnitsInProcessB"
            :currentDetail="currentDetail" :currentDetailB="currentDetailB" :isStatus="isStatus"
            @getTotalNumA="getTotalNumA" @getTotalNumB="getTotalNumB" @special="handleSpecialMerge" ref="electives" />
          <div class="stBotton">
            <el-button type="primary" class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode == 'Free Elective'">
          <reeElective :tdData="uCoreTdData" :trData="uCoreTrDataFilter" :tableData="tableData"
            :totalUnitsPassed="totalUnitsPassed" :totalUnitsInProcess="totalUnitsInProcess"
            :couresListMerge="couresListMergeFree" @special="handleSpecialMergeFree" :couresList="couresList"
            :isStatus="isStatus" :currentDetail="currentDetail" :stepCode="'Free Elective'" @getTotalNum="getTotalNum"
            ref="reel" />
          <div class="stBotton">
            <el-button type="primary" class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode === 'Self Declaration'">
          <div style="margin-top: 20px;" v-html="systemInfo.noteSeven"></div>
          <div style="padding: 10px 0;">
            <div>
              <el-radio :disabled="[1, 3].includes(isStatus)" v-model="studentCheckBox" :label="true">是 Yes</el-radio>
              <!-- <el-radio :disabled="[1, 3].includes(isStatus)" v-model="studentCheckBox" :label="true">是，本人申报在上述学期毕业。Yes, I
                am declaring graduation in above-mentioned term.</el-radio> -->
            </div>
            <div>
              <el-radio :disabled="[1, 3].includes(isStatus)" v-model="studentCheckBox" :label="false">否 No</el-radio>
              <!-- <el-radio :disabled="[1, 3].includes(isStatus)" v-model="studentCheckBox" :label="false">否，本人不申报在上述学期毕业。No,
                I am NOT declaring graduation in above-mentioned term.</el-radio> -->
            </div>
          </div>
          <!-- <div>
            <el-input v-if="!studentCheckBox" type="textarea" :disabled="[1, 3].includes(isStatus)" rows="4" v-model="studentCheckFeedback"
              placeholder="不申报原因。Reason for not declaring."></el-input>
          </div> -->
          <div class="stBotton">
            <el-button type="primary" class="huicolor" @click="returnSuperior()">Previous</el-button>
            <el-button type="primary" class="lancolor" @click="nextStep()">Next</el-button>
          </div>
        </div>
        <div v-if="currentCode == 'Submit'">
          <suBmit ref="children" :text="systemInfo.noteEight" :isStatus="isStatus" :signatureDate="signatureDate"
            :signatureUrl="signatureUrl" :englishSchoolReport="englishSchoolReport" @save="handleSaveNote8" />
          <div class="stBotton">
            <el-button type="primary" class="lancolor" @click="handlePreview">Preview</el-button>
            <el-button v-if="false" type="primary" class="lancolor" @click="submission">Submit</el-button>
          </div>
        </div>
      </div>
      <div v-if="submitState == 'success'">
        <div class="stIjiao">
          <i class="el-icon-success stIjioi"></i>
          <div class="suRd">Submitted success fully</div>
          <span>waiting for review by relevant departments</span>
          <div class="suRcs" @click="handlePreview">Preview</div>
          <div class="suRcs" @click="goLogin">Log out</div>
        </div>
      </div>
      <div v-if="submitState == 'error'">
        <div class="stIjiao">
          <i class="el-icon-warning stIjioi"></i>
          <div class="suRd">Failed</div>
          <div class="suRvas">
            <div>Detail:</div>
            <div>Network Error</div>
          </div>
          <div class="suRcs">Return to Resubmit</div>
        </div>
      </div>
      <el-dialog :visible.sync="dialogVisible" width="30%" center>
        <span class="confirm">Are you sure to Submit</span>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submission">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  getStudentTable,
  nextPreservation,
  onSubmit,
  getCourseTemplateList,
  getCourseCategory,
  saveStudentCheckBox,
} from '@/api/student'
import { getSystemConfig, linkAdfsLogout, currentCategoryList } from '@/api/index'
import nodet from './assembly/nodet.vue'
import telar from './assembly/telar.vue'
import schoolPackage from './assembly/schoolPackage.vue'
import majorRequiredcourses from './assembly/majorRequiredcourses.vue'
import majorElectives from './assembly/majorElectives.vue'
import reeElective from './assembly/reeElective.vue'
import suBmit from './assembly/suBmit.vue'
import Cookies from 'js-cookie'
import _ from 'lodash';

export default {
  components: {
    nodet,
    telar,
    schoolPackage,
    majorRequiredcourses,
    majorElectives,
    reeElective,
    suBmit,
  },
  data() {
    return {
      isActive: true,
      storageName: '',
      stepList: [],
      id: '',
      active: 0,
      currentCode: 'Note',
      tableData: [],
      uCoreTdData: [],
      uCoreTrData: [
        {
          type: 'courseCode',
          text: 'Coures Code',
          width: '170',
        },
        {
          type: 'courseTitle',
          text: 'Coures Title',
          width: '320',
        },
        {
          type: 'units',
          text: 'Units',
          width: '60',
        },
        {
          type: 'selfCheck',
          text: 'Self-check(✔/NR/IP)',
          width: '170',
        },
        {
          type: 'progress',
          text: 'Progress',
          width: '170',
        },
        {
          type: 'minor',
          text: 'Minor<br/>(Minor Required-/Minor Elective)',
          width: '150',
        },
        {
          type: 'remark',
          text: 'Remark<br/>(Credit Transfer for equivalent course/Course substitution)',
        },
      ],
      tableDataA: [],
      tableDataB: [],
      totalUnitsPassed: 0,
      totalUnitsPassedA: 0,
      totalUnitsPassedB: 0,
      totalUnitsInProcess: 0,
      totalUnitsInProcessA: 0,
      totalUnitsInProcessB: 0,
      totalUnitsA: '',
      totalUnitsB: '',
      couresList: [],
      couresListA: [],
      couresListMerge: [],
      couresListMergeFree: [],
      couresListB: [],
      dialogVisible: false,
      tips: null,
      isStatus: 0,
      nextId: '',
      fullscreenLoading: false,
      signatureDate: new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate(),
      signatureUrl: null,
      englishSchoolReport: null,
      studentCheckBox: '',
      // 不申报，填写原因
      studentCheckFeedback: '',
      currentDetail: {},
      currentDetailB: {},
      systemInfo: {},
      submitState: '',
    }
  },
  created() {
    this.getStorageName()
    this.getSystemInfo()
    this.getCategories();
  },
  computed: {
    currentNode() {
      return this.stepList.find((step) => step.code === this.currentCode) || {};
    },
    currentIndex() {
      const index = this.stepList.findIndex((step) => step.code === this.currentCode);
      return index === undefined ? null : index;
    },
    totalScore() {
      if (this.currentDetail.title) {
        return this.currentDetail.title.replace(/[^0-9]/ig, "");
      } else {
        return null;
      }
    },
    totalScoreB() {
      return 9;
    },
    limitScore() {
      const title = this.currentDetail.title;
      if (!title) {
        return null;
      }

      const score = title.substring(title.indexOf('(') + 1, title.indexOf(')')).replace('units', '').trim()
      const minMax = score.split('-');

      return minMax.length > 0 ? minMax[minMax.length - 1] : null;
    },
    partLimitScore() {
      const title = this.currentDetail.categoryName;
      if (!title) {
        return null;
      }

      const score = title.substring(title.indexOf('(') + 1, title.indexOf(')')).replace('units', '').trim()
      const minMax = score.split('-');

      return minMax.length > 0 ? minMax[minMax.length - 1] : null;
    },
    limitScoreB() {
      const title = this.currentDetailB.title;
      if (!title) {
        return null;
      }

      const score = title.substring(title.indexOf('(') + 1, title.indexOf(')')).replace('units', '').trim()
      const minMax = score.split('-');

      return minMax.length > 0 ? minMax[minMax.length - 1] : null;
    },
    uCoreTrDataFilter() {
      if (this.currentNode && this.currentNode.isStandard) {
        // 排除的列
        let excludeRows = ['minor', 'progress'];

        if (this.currentDetail.type === 2) {
          excludeRows = ['minor', 'units', 'selfCheck'];
        }

        return this.uCoreTrData.filter((tr) => !excludeRows.includes(tr.type));
      }

      return this.uCoreTrData.filter((tr) => tr.type !== 'progress');
    },
  },
  watch: {
    totalUnitsInProcess(val) {
      if (this.totalScore && val && val > this.totalScore && this.currentCode !== 'Major Elective Courses') {
        this.$store.dispatch('setExceedTotalScore', true);
        this.showHint(this.totalScore);
      }
    },
    totalUnitsInProcessA(val) {
      if (this.totalScore && val && val > this.totalScore && this.currentCode === 'Major Elective Courses') {
        this.$store.dispatch('setExceedTotalScore', true);
        this.showHint(this.totalScore);
      }
    },
    totalUnitsInProcessB(val) {
      if (this.totalScoreB && val && val > this.totalScoreB) {
        this.$store.dispatch('setExceedTotalScore', true);
        this.showHint(this.totalScoreB);
      }
    }
  },
  methods: {
    async getCategories() {
      const info = JSON.parse(localStorage.getItem('user-info') || '{}');

      const result = await currentCategoryList({});

      if (result.code === 200) {
        const datas = (result.data || []).map((data) => {
          return { code: data.categoryCode, isStandard: data.standard === 1 };
        });

        this.stepList = _.concat([{ code: 'Note' }], datas, [{ code: 'Self Declaration' }, { code: 'Submit' }]);
      }
    },
    async getSystemInfo() {
      const result = await getSystemConfig();
      this.systemInfo = result.data || {};
    },
    showHint(score) {
      // 超过总分提示
      this.$message.error(`该课程不能超过总分：${score}`);
    },
    getStorageName() {
      this.storageName =
        localStorage.getItem('nameEn') == ''
          ? localStorage.getItem('nameCh')
          : localStorage.getItem('nameEn')
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then((_) => {
          done()
        })
        .catch((_) => { })
    },
    async submission() {
      let signatureUrl = this.$refs.children.url;
      let signatureDate = this.$refs.children.date;
      let englishSchoolReport = this.$refs.children.url1;
      if (signatureUrl == null || signatureUrl == '') {
        this.$message.error('请选择上传签名后再提交！');
        return;
      }
      // if(englishSchoolReport == null || englishSchoolReport == ''){
      //   this.$message.error('请选择上传最新版英文成绩单后再提交！');
      //   return;
      // }
      if (signatureDate == null || signatureDate == '') {
        this.$message.error('请选择日期后再提交！');
        return;
      }
      let params = {
        id: this.id,
        fileId: this.$refs.children.fileId,
        signatureUrl: signatureUrl,
        englishSchoolReport: englishSchoolReport,
        signatureDate: signatureDate,
      }

      let res = await onSubmit(params)
      if (res.code == 200) {
        this.submitState = 'success';
        this.isActive = false
        this.dialogVisible = false
      } else {
        this.submitState = 'error';
        this.$message.error(res.message || '请求失败')
      }
    },
    goLogin() {
      this.outlogin();
    },
    onSubmit() {
      this.dialogVisible = true
    },
    async getTableData(code) {
      const res = await getStudentTable(code)
      const data = res.data;

      if (data) {
        this.id = data.id;
        this.isStatus = data.status;  // 状态（0：草稿，1：待审核，2、已驳回，3、已完成）
        if (code == 'Submit') {
          // 草稿、驳回状态以当前为填写时间，其它状态则以已填写
          if(![0,2].includes(this.isStatus)) {
            this.signatureDate = data.signatureDate ? data.signatureDate : new Date().getFullYear() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getDate();
          }
          this.signatureUrl = data.signatureUrl;
          this.englishSchoolReport = data.englishSchoolReport;
        }
        this.studentCheckBox = data.studentCheckBox == 1 ? true : false;
        this.studentCheckFeedback = data.studentCheckFeedback || '';
        let resList = data.courseDetailVOList;
        if (resList !== null && resList.length > 0) {
          if (this.couresList != null && this.couresList.length > 0) {
            this.couresList.forEach((item) => {
              resList.forEach((item1) => {
                if (item1.courseCode == item.courseCode) {
                  // item.units = item1.units
                  item.remark = item1.remark
                  item.minor = item1.minor || ''
                  item.progress = item1.progress || ''
                  item.selfCheck = item1.selfCheck
                }
                item.courseTemplateId = item.id
              })
            })
            this.couresListA.forEach((item) => {
              resList.forEach((item1) => {
                if (item1.courseCode == item.courseCode && item1.part == item.part) {
                  // item.units = item1.units
                  item.remark = item1.remark
                  item.minor = item1.minor || ''
                  item.selfCheck = item1.selfCheck
                }
                item.courseTemplateId = item.id
              })
            })
            this.couresListB.forEach((item) => {
              resList.forEach((item1) => {
                if (item1.courseCode == item.courseCode && item1.part == item.part) {
                  // item.units = item1.units
                  item.remark = item1.remark
                  item.minor = item1.minor || ''
                  item.selfCheck = item1.selfCheck
                }
                item.courseTemplateId = item.id
              })
            })
            this.couresListMerge.forEach((item) => {
              resList.forEach((item1) => {
                if (item1.courseCode == item.courseCode && item1.part == item.part) {
                  // item.units = item1.units
                  item.remark = item1.remark
                  item.minor = item1.minor || ''
                  item.selfCheck = item1.selfCheck
                }
                item.courseTemplateId = item.id
              })
            })
          } else {
            this.couresList = resList.filter(x => !this.couresListMergeFree.map(x1 => x1.courseCode).includes(x.courseCode));
          }
          this.couresListMergeFree.forEach((item) => {
            (resList || []).forEach((item1) => {
              if (item1.courseCode == item.courseCode) {
                item.units = item1.units
                item.remark = item1.remark
                item.minor = item1.minor || ''
                item.selfCheck = item1.selfCheck
              }
              item.courseTemplateId = item.id
            })
          })
        }
      }

      this.tableData = this.couresList;
      this.tableDataA = this.couresListA;
      this.tableDataB = this.couresListB;
      this.refreshScore();
      this.fullscreenLoading = false;
    },
    empty() {
      this.totalUnitsPassed = 0
      this.totalUnitsInProcess = 0
      this.totalUnitsPassedA = 0
      this.totalUnitsPassedB = 0
      this.totalUnitsInProcessA = 0
      this.totalUnitsInProcessB = 0
      this.tableData = []
      this.tableDataA = []
      this.tableDataB = []
    },
    async next() {
      if (this.currentCode == 'Major Elective Courses') {
        // 节点6限制最高分、最低分
        const totalA = this.totalUnitsPassedA + this.totalUnitsInProcessA;
        if (this.limitScore && totalA > this.limitScore) {
          this.$message.error(this.currentDetail.title);
          return false;
        }

        const totalB = this.totalUnitsPassedB + this.totalUnitsInProcessB;
        if (this.limitScoreB && totalB > this.limitScoreB) {
          this.$message.error(this.currentDetailB.title);
          return false;
        }

        if (this.partLimitScore && (totalA + totalB) > this.partLimitScore) {
          this.$message.error(this.currentDetail.categoryName);
          return false;
        }
      }

      if (['University Core', 'School Package', 'Major Required Courses'].includes(this.currentCode) && (this.totalUnitsPassed + this.totalUnitsInProcess) > this.limitScore) {
        this.$message.error(this.currentDetail.title);
        return false;
      }

      let params = {}
      if (this.currentCode == 'Major Elective Courses') {
        this.tableDataA.forEach((item) => {
          item.courseCategoryCode = this.currentCode;
          item.frontShow = 1
        })
        this.tableDataB.forEach((item) => {
          item.courseCategoryCode = this.currentCode;
          item.frontShow = 1
        })
        let arr = this.tableDataA.concat(this.tableDataB).concat(this.couresListMerge)
        params = {
          courseDetailVOList: arr,
        }
      } else {
        params = {
          courseDetailVOList: this.tableData.concat(this.couresListMergeFree),
        }
        this.tableData.forEach((item) => {
          item.courseCategoryCode = this.currentCode;
          item.frontShow = 1
        })
      }

      if (params.courseDetailVOList.length == 0) {
        return;
      }

      if (this.currentCode === 'Free Elective') {
        let errorCount = 0
        params.courseDetailVOList.filter((data) => data.courseCode.indexOf('FREE_ELECTIVE') === -1).every((data) => {
          if (data.courseCode) {
            if (/^[A-Z]{3}\d{4}$/.test(data.courseCode)) {
              // 分数需大于0
              if (data.units && data.units > 0) {
                if (!(data.selfCheck && data.selfCheck > 0)) {
                  errorCount = 3;
                }
              } else {
                errorCount = 2;
              }
            } else {
              errorCount = 1;
            }
          } else {
            return true;
          }
        });

        if (errorCount === 1) {
          this.$message.error('课程编码格式输入有误，请按照成绩单中的课程编码格式输入，如GEW2001。The course code format is incorrect, please enter it as the one shown in the transcript (3 CAPITAL letters + 4 numbers), such as GEW2001.');
          return false;
        } else if (errorCount === 2) {
          this.$message.error('请根据课程在成绩单上对应的学分在“Unit”一栏填写正确的学分。Please correctly fill in the unit based on the corresponding course units shown in the transcript.');
          return false;
        } else if (errorCount === 3) {
          this.$message.error('请在对应课程后的“Self-Check”栏点击下拉符号选择课程的修读情况(√/NR/IP)。Please click the drop-down symbol in the "Self-Check" column after the corresponding course to select the course status (√/NR/IP).');
          return false;
        }
      }

      if (this.currentDetail.type === 2 && params.courseDetailVOList.some((data) => !data.progress)) {
        // 验证进度必选
        this.$message.error('请在此模块对应的课程后的“Progress”栏点击下拉符号选择该门课程的修读进度');
        return false;
      }

      let res = await nextPreservation(params)
      if (res.code == 200) {
        this.$message({
          message: '保存成功',
          type: 'success',
        })
      } else {
        this.$message({
          message: res.message,
          type: 'error',
        })
        return false;
      }
    },
    setCurrentPage(code) {
      localStorage.setItem('current-code', code)
      this.currentCode = code;
      this.active = this.currentIndex;
      document.body.scrollTop = document.documentElement.scrollTop = 0

      if (code === 'Free Elective') {
        this.$nextTick(() => {
          this.$refs.reel.addLineThree();
        });
      }
    },
    getTotalNum(data) {
      this.tableData = data
    },
    getTotalNumA(data) {
      this.tableDataA = data
    },
    getTotalNumB(data) {
      this.tableDataB = data
    },
    //计算分数
    refreshScore() {
      this.totalUnitsPassed = 0;
      this.totalUnitsInProcess = 0;
      this.totalUnitsPassedA = 0;
      this.totalUnitsPassedB = 0;
      this.totalUnitsInProcessA = 0;
      this.totalUnitsInProcessB = 0;
      this.couresList.concat(this.couresListMerge).concat(this.couresListMergeFree).forEach((item) => {
        let units = item.units
        let selfCheck = item.selfCheck
        if (['1', '2'].includes(selfCheck)) {
          this.totalUnitsPassed += Number(units)
          if (item.part == 'A') {
            this.totalUnitsPassedA += Number(units)
          }
          if (item.part == 'B') {
            this.totalUnitsPassedB += Number(units)
          }
        }
        if (['3'].includes(selfCheck)) {
          this.totalUnitsInProcess += Number(units)
          if (item.part == 'A') {
            this.totalUnitsInProcessA += Number(units)
          }
          if (item.part == 'B') {
            this.totalUnitsInProcessB += Number(units)
          }
        }
      });
    },
    async nextStep() {
      this.fullscreenLoading = true;

      //先保存数据 判断取下一步的逻辑判断
      if (!['Note', 'Submit'].includes(this.currentCode)) {
        if ([0, 2].includes(this.isStatus)) {
          const result = await this.next();

          if (result === false) {
            this.fullscreenLoading = false;
            return;
          }

          const note7Result = await this.handleSaveNote7();

          if (note7Result === false) {
            return;
          }
        }
      }

      const data = this.stepList[this.currentIndex + 1];
      if (!['Note'].includes(data.code)) {
        this.couresList = [];
        this.tableData = [];
        if (data.code !== 'Submit') {
          await this.getCourseTemplateList(data.code)
        } else {
          // 保存第七步
          const result = await this.handleSaveNote7();

          if (result === false) {
            return;
          }
        }
        await this.getCourseCategory(data.code);
        await this.getTableData(data.code)
      }
      this.setCurrentPage(data.code);
      this.fullscreenLoading = false;
    },
    async handleSaveNote7() {
      if (this.currentCode !== 'Self Declaration') {
        return true;
      }

      if ([1, 3].includes(this.isStatus)) {
        return;
      }

      // if (!this.studentCheckBox && !this.studentCheckFeedback) {
      //   this.fullscreenLoading = false;
      //   this.$message.error('不申报原因。Reason for not declaring.');
      //   return false;
      // }

      const result = await saveStudentCheckBox({
        id: this.id,
        studentCheckBox: this.studentCheckBox ? 1 : 0,
        studentCheckFeedback: this.studentCheckBox ? '' : this.studentCheckFeedback,
      });

      if (!result.success) {
        this.$message.error(result.message || '保存失败');
        this.fullscreenLoading = false;
        return false;
      } else {
        this.$message.success('保存成功');
        return true;
      }
    },
    async handleSaveNote8(data) {
      if (!this.id) {
        return;
      }

      const result = await saveStudentCheckBox({
        id: this.id,
        ...data,
      });

      if (!result.success) {
        this.$message.error(result.message || '保存失败');
        this.fullscreenLoading = false;
        return false;
      } else {
        this.$message.success('保存成功');
        return true;
      }
    },
    emptyFraction() {
      this.totalUnits = 0
      this.totalUnitsA = ''
      this.totalUnitsB = ''
    },
    async returnSuperior() {
      const current_data = this.stepList[this.currentIndex];
      if (!['Self Declaration', 'Submit'].includes(current_data.code)) {
        if ([0, 2].includes(this.isStatus)) {
          const result = await this.next();

          if (result === false) {
            return;
          }

          const note7Result = await this.handleSaveNote7();

          if (note7Result === false) {
            return;
          }
        }
      }
      const data = this.stepList[this.currentIndex - 1];
      this.setCurrentPage(data.code);
      if (data.code !== 'Note') {
        if ([1, 3].includes(this.isStatus)) {
          await this.getCourseTemplateList(data.code)
          await this.getTableData(data.code)
        }
        await this.getCourseTemplateList(data.code)
        await this.getTableData(data.code)
        await this.getCourseCategory(data.code);
      }
    },
    async togglePage(inx, code) {
      this.fullscreenLoading = true;
      
      if (code !== 'Note' && this.active != inx) {
        //先保存数据
        if (!['Note'].includes(this.currentCode)) {
          if ([0, 2].includes(this.isStatus)) {
            const result = await this.next();

            if (result === false) {
              this.fullscreenLoading = false;
              return;
            }

            const note7Result = await this.handleSaveNote7();

            if (note7Result === false) {
              return;
            }
          }
        }
        await this.getCourseCategory(code);
        await this.getCourseTemplateList(code);
        await this.getTableData(code);
        await this.setCurrentPage(code);
      } else {
        this.setCurrentPage(code)
      }
      this.fullscreenLoading = false;
    },
    /** 获取课程表单的模板数据列表 */
    async getCourseTemplateList(categoryCode) {
      let param = {
        major: '',
        categoryCode,
      }
      this.couresList = []
      const { code, success, data, message } = await getCourseTemplateList(param)
      if (code == 200 && success) {
        this.couresListMerge = [];
        this.couresListMergeFree = [];
        this.couresList = data || [];
        this.couresList.forEach((item) => { item.courseTemplateId = item.id })
        if (categoryCode === 'Major Elective Courses') {
          this.couresListA = data.filter((item) => { return item.part === 'A' });
          this.couresListA.forEach((item) => { item.courseTemplateId = item.id })
          this.couresListB = data.filter((item) => { return item.part === 'B' });
          this.couresListB.forEach((item) => { item.courseTemplateId = item.id })
          this.couresListMerge.push({ courseCode: "MAJOR_ELECTIVE_COURSES_PART_A01", courseCategoryCode: categoryCode, units: 0, selfCheck: '1', part: 'A' });
          this.couresListMerge.push({ courseCode: "MAJOR_ELECTIVE_COURSES_PART_A02", courseCategoryCode: categoryCode, units: 0, selfCheck: '3', part: 'A' });
          this.couresListMerge.push({ courseCode: "MAJOR_ELECTIVE_COURSES_PART_B01", courseCategoryCode: categoryCode, units: 0, selfCheck: '1', part: 'B' });
          this.couresListMerge.push({ courseCode: "MAJOR_ELECTIVE_COURSES_PART_B02", courseCategoryCode: categoryCode, units: 0, selfCheck: '3', part: 'B' });
        } else if (categoryCode === 'Free Elective') {
          this.couresListMergeFree.push({ courseCode: "FREE_ELECTIVE_01", courseCategoryCode: categoryCode, units: 0, selfCheck: '1' });
          this.couresListMergeFree.push({ courseCode: "FREE_ELECTIVE_02", courseCategoryCode: categoryCode, units: 0, selfCheck: '3' });
          this.couresListMergeFree.push({ courseCode: "FREE_ELECTIVE_03", courseCategoryCode: categoryCode, units: 0, selfCheck: '1' });
          this.couresListMergeFree.push({ courseCode: "FREE_ELECTIVE_04", courseCategoryCode: categoryCode, units: 0, selfCheck: '3' });
        }
        this.fullscreenLoading = false;
      } else {
        this.fullscreenLoading = false;
      }
    },
    handleSpecialMerge(couresListMerge) {
      this.couresListMerge = couresListMerge;
      this.refreshScore();
    },
    handleSpecialMergeFree(couresListMergeFree) {
      this.couresListMergeFree = couresListMergeFree;
      this.refreshScore();
    },
    async getCourseCategory(categoryCode) {
      let param = {
        major: '',
        categoryCode,
      }
      const { code, success, data, message } = await getCourseCategory(param)
      if (code == 200 && success && data !== null) {
        if (data instanceof Array && data.length > 0) {
          this.currentDetail = data[0];

          if (data.length > 1) {
            this.currentDetailB = data[1];
          }
        } else {
          this.currentDetail = {};
        }
      }
    },
    //退出
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
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消',
          })
        })
    },
    async outlogin() {
      Cookies.remove('XSRF-TOKEN')
      window.localStorage.clear()
      linkAdfsLogout()
    },
    async handlePreview() {
      let signatureUrl = this.$refs.children.url;
      let signatureDate = this.$refs.children.date;
      let englishSchoolReport = this.$refs.children.url1;
      if (signatureUrl == null || signatureUrl == '') {
        this.$message.error('请选择上传签名后再提交！');
        return;
      }
      // if(englishSchoolReport == null || englishSchoolReport == ''){
      //   this.$message.error('请选择上传最新版英文成绩单后再提交！');
      //   return;
      // }
      if (signatureDate == null || signatureDate == '') {
        this.$message.error('请选择日期后再提交！');
        return;
      }

      let id = this.id;
      if (!id) {
        let res = await getStudentTable('Submit')
        id = res.data.id;
      }
      this.$router.push(`/preview?id=${id}`);
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
html,
body,
#stu {
  padding: 0;
  margin: 0;
  background: #fff;
  font-family: 'Book Antiqua';
}

.el-step__icon-inner {
  display: none;
}

.el-step__icon {
  width: 17px;
  height: 17px;
}

#stu {
  width: 100vw;
  margin: 0 auto;
  overflow: scroll;
}

#stu::-webkit-scrollbar {
  display: none;
  /* Chrome Safari */
}

.stTitle {
  /* border-top: 2px solid #b6b5b5; */
  position: relative;
  display: flex;
  margin-top: 20px;
  margin-left: calc(-100% / 7 / 2 + 10px);
  margin-right: calc(-100% / 7 / 2 + 10px);
}

.stTitle>div {
  flex: 1;
  text-align: center;
}

.riImg {
  background: #6a1d72;
  width: 100vw;
  height: 80px;
}

.stldiv {
  margin-top: -10px;
}

.rioneimg {
  height: 60px;
  padding: 10px;
  display: inline-block;
  vertical-align: middle;
}

.chenser {
  background: #ddaa43;
}

.rioneimg img {
  height: 100%;
}

.stPadd {
  position: relative;
  /* width: 80%;
  min-width: 1300px; */
  margin: 0 auto;
}

.stov {
  overflow: hidden;
}

.stDiv {
  cursor: pointer;
  width: 12px;
  height: 12px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #b6b5b5;
  background: #fff;
  position: relative;
  z-index: 99999;
}

/* .stDiv:hover {
  width: 40px;
  height: 10px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #1989fa;
  background: #fff;
  position: relative;
  z-index: 99999;
} */

.stDivs {
  width: 12px;
  height: 12px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #1989fa;
  background: #fff;
  position: relative;
  z-index: 99999;
}

.stTitle .stldiv {
  width: calc(100% / 7);
}

.stDivname {
  padding: 10px 6px;
  cursor: pointer;
}

.stpoleft {
  background: #fff;
}

.noName {
  text-align: center;
  margin: 0;
}

.noFowei {
  font-weight: 600;
}

.noText {
  text-decoration: underline;
}

.noName div {
  margin-bottom: 0;
}

.noName div:nth-child(2n) {
  margin-bottom: 0;
}

p {
  padding: 6px 0;
  margin: 0;
  /* margin-left: 46px; */
}

.noPbc {
  margin: 30px 0;
  padding: 0;
  margin-left: 46px;
}

.noMar {
  margin-bottom: 0;
}

.noDIv {
  padding-bottom: 200px;
  /* width: 100%; */
  width: 80%;
  min-width: 1300px;
}

.sotpd {
  position: fixed;
  height: 200px;
  background: #fff;
  z-index: 2;
  width: 100vw;
}

.noBott {
  padding: 30px 0;
  text-align: center;
}

.stBotton {
  text-align: center;
  padding: 50px 0;
}

.huicolor {
  background: #c2c7cc;
  border: none;
  color: #fff;
}

.huicolor:hover {
  background: #afb5bb;
}

.rioneimgs {
  height: 80px;
  display: inline-block;
  vertical-align: middle;
}

.rioneimgs img {
  height: 100%;
}

.lancolor {
  border: none;
  background: #3a62d7 !important;
  padding: 10px 30px !important;
}

.lancolor:hover {
  background: #284fc1 !important;
}

.banxin {
  width: 80%;
  min-width: 1300px;
  margin: 0 auto;
  padding: 0 10px;
}

.stBotton .huicolor {
  margin-right: 150px;
  color: #909399;
  background: #f4f4f5;
  border-color: #d3d4d6;
  padding: 10px 30px;
}

.stBotton .lancolor {
  background: #3a62d7;
  padding: 10px 30px;
}

.stBotton .lancolor:hover {
  background: #284fc1;
}

.stIjiao {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -0);
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

.sulid {
  font-size: 20px;
  vertical-align: middle !important;
  margin-right: 10px;
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

.stxian {
  position: absolute;
  border-top: 2px solid #b6b5b5;
  top: 17px;
  left: 27px;
  width: calc(100% - 42px);
  z-index: 999;
}

.text-color {
  font-weight: bold !important;
}

.step-auto {
  /* width: 85%; */
  /* min-width: 1300px; */
  margin: 30px auto;
  /* margin-top: 30px;
  margin-bottom: 30px; */
}

.el-step.is-horizontal .el-step__line {
  top: 8px;
}

.subnt {
  margin: 0 !important;
}

.confirm {
  text-align: center;
  display: block;
}

.el-step__head.is-finish,
.el-step__title.is-process {
  color: #6a1d72 !important;
  border-color: #6a1d72 !important;
}

.el-step__title.is-finish,
.el-step__title.is-process {
  color: #6a1d72 !important;
}

.el-input__inner {
  font-size: 12px !important;
  height: 35px !important;
  line-height: 35px !important;
}

.el-input__icon {
  line-height: 35px !important;
}

.el-input-number {
  line-height: 15px !important;
}

.el-input-number.is-controls-right .el-input-number__decrease,
.el-input-number.is-controls-right .el-input-number__increase {
  line-height: 16px !important;
}

/* .el-step__main {
  display: none;
} */

.el-step__title {
  margin-top: 6px;
  line-height: 20px !important;
}

.el-radio__label {
  font-size: 16px;
  line-height: 30px;
  font-weight: 700;
}

.el-radio__input.is-checked+.el-radio__label {
  color: #d61518 !important;
}

.el-radio__input.is-checked .el-radio__inner {
  border-color: #d61518 !important;
  background: #d61518 !important;
}
</style>
