<template>
  <div class="sud">
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
          <div @click="handleLinkToConfig">Configuration</div>
          <div @click="handleLinkToReport">Report</div>
          <div @click="addout">Logout</div>
        </div>
      </div>
    </div>
    <div style="overflow: scroll;padding: 0 10px">
      <div class="richTap">
        <div :class="!activeName ? 'richTapadd richflo' : 'richflo'">
          Query self inspection
        </div>
      </div>
      <div v-if="!shows" class="tarRmar tarRis">
        <el-button-group>
          <el-button
            size="small"
            round
            class="riColor"
            @click="detailSave"
            :disabled="auditBtn"
            icon="el-icon-document-checked"
            >Save</el-button
          >
          <el-button
            size="small"
            @click="openSendEmail"
            round
            class="riColor"
            :disabled="auditBtn"
            icon="el-icon-back"
            >Return</el-button
          >
        </el-button-group>
        <el-button-group>
          <el-button
            class="riColorchen"
            icon="el-icon-download"
            size="small"
            @click="printPdf"
            round
            >Export</el-button
          >
          <el-button
            size="small"
            @click=";(activeName = false), (shows = true)"
            round
            class="riColorchen"
            icon="el-icon-close"
            >Back</el-button
          >
        </el-button-group>
      </div>
    
      <div class="banxin" v-show="activeName">
        <tarpush :mydata="tableData" @addpush="addlist" :meiju="meiju"></tarpush>
      </div>
      <div style="margin: 0 auto" v-show="!activeName">
        <div v-show="shows">
          <div class="banxin">
            <div class="riButton">
              <el-row :gutter="24">
                <el-col :span="3">
                  <el-date-picker
                    size="small"
                    v-model="formData.grade"
                    type="year"
                    value-format="yyyy"
                    placeholder="Grade">
                  </el-date-picker>
                </el-col>
                <el-col :span="3">
                  <el-select v-model="formData.expectedGraduationTerm" size="small" style="width: 100%;" placeholder="Expected Graduation Term" clearable>
                    <el-option v-for="(year, key) of schoolYear" :key="key" :label="year" :value="key"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="6">
                  <el-select
                    v-model="formData.major"
                    placeholder="Major"
                    size="small"
                    clearable
                    style="width: 100%;"
                  >
                    <el-option
                      v-for="item in meiju"
                      :key="item.value"
                      :label="item.value"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-select
                    v-model="formData.value"
                    placeholder="Student I.D"
                    size="small"
                    style="width: 100%;"
                    clearable
                    @change="formData.input = ''"
                  >
                    <el-option
                      v-for="item in options1"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="8">
                  <el-input
                    :disabled="!formData.value"
                    placeholder="Student I.D"
                    v-model="formData.input"
                    size="small"
                    clearable
                  />
                </el-col>
              </el-row>
              <el-row :gutter="24" style="margin-top: 10px;">
                <el-col :span="6">
                  <el-select v-model="formData.status" size="small" style="width: 100%;" placeholder="Status" clearable>
                    <el-option label="Unfilled" :value="0"></el-option>
                    <el-option label="Submitted" :value="1"></el-option>
                    <el-option label="Rejected" :value="2"></el-option>
                    <el-option label="Acknowledged" :value="3"></el-option>
                    <el-option label="Invalid" :value="4"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="6">
                  <el-select
                    v-model="formData.studentCheckBox"
                    placeholder="Self-Declaration"
                    size="small"
                    style="width: 100%;"
                    clearable
                  >
                    <el-option
                      v-for="item in options2"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  <el-select
                    v-model="formData.timeType"
                    placeholder="Time"
                    size="small"
                    style="width: 100%;"
                    clearable
                  >
                    <el-option label="Submit Time" :value="0"></el-option>
                    <el-option label="Check Time" :value="1"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="8">
                  <el-date-picker
                    v-model="formData.startEnd"
                    type="datetimerange"
                    size="small"
                    style="width: 100%;"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    range-separator="~"
                    start-placeholder="Start"
                    end-placeholder="End">
                  </el-date-picker>
                </el-col>
              </el-row>
              <el-row :gutter="24" style="margin-top: 10px;">
                <el-col :span="24">
                  <div class="align-right">
                    <el-button-group>
                      <el-button size="small" class="riColor" icon="el-icon-circle-check" @click="handleAcknowledged" round >Acknowledged</el-button>
                      <el-button size="small" class="riColor" icon="el-icon-circle-check" @click="handleInvalid" round >Invalid</el-button>
                      <el-button size="small" class="riColor" icon="el-icon-circle-check" @click="handleReject" round >Reject</el-button>
                      <el-button size="small" class="riColor" icon="el-icon-circle-check" @click="handleSendEmail" round >Email</el-button>
                    </el-button-group>
                    <el-button-group>
                      <el-button size="small" class="riColorchen" icon="el-icon-download" @click="handlePdfMultiple" round>Export</el-button>
                      <el-button size="small" class="riColorchen" icon="el-icon-download" @click="exportTableList" round>Export Summary</el-button>
                    </el-button-group>
                    <el-button-group>
                      <el-button class="riColorchen" icon="el-icon-search" size="small" @click="recordList" round>Query</el-button>
                      <el-button class="riColorchen" icon="el-icon-delete" size="small" @click="cleanQuery" round>Reset</el-button>
                    </el-button-group>
                </div>
                </el-col>
              </el-row>
            </div>
            <div class="riButton rirow ripatop">
              <el-table
                ref="multipleTable"
                :data="stmydata.records"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange"
              >
                <el-table-column type="selection" width="100" align="center">
                </el-table-column>
                <el-table-column
                  label="Graduation Audlt Self-checkForm"
                  min-width="425"
                  align="center"
                >
                  <template slot-scope="scope">
                    <span class="font-decoration" @click="goDetails(scope.row)">{{
                      scope.row.graduationForm ? scope.row.graduationForm : '暂无记录'
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="major"
                  label="Major"
                  min-width="180"
                  align="center"
                  show-overflow-tooltip
                />
                <el-table-column
                  prop="grade"
                  label="Grade"
                  min-width="60"
                  align="center"
                />
                <el-table-column
                  prop="studentId"
                  label="Student I.D"
                  min-width="110"
                  align="center"
                  sortable
                >
                </el-table-column>
                <el-table-column
                  prop="nameCh"
                  label="Name"
                  align="center"
                  min-width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="contactTel"
                  label="Contact Tel"
                  align="center"
                  min-width="120"
                  show-overflow-tooltip
                >
                </el-table-column>
                <el-table-column
                  prop="studentCheckBox"
                  label="Self-Declaration"
                  width="120"
                  align="center"
                >
                  <template slot-scope="scope">
                    <span>{{ scope.row.studentCheckBox == 1 ? 'Y' : 'N' }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="statusText"
                  label="Status"
                  align="center"
                  show-overflow-tooltip
                >
                </el-table-column>
              </el-table>
              <div class="rimar">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="recordList"
                  :current-page.sync="current"
                  :page-sizes="[10, 20, 30, 50, 100, stmydata.total]"
                  :page-size.sync="size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="stmydata.total"
                >
                </el-pagination>
              </div>
            </div>
          </div>
          <template v-if="pdfVisible">
            <div v-for="(item, index) of multipleSelection" :key="index" class="tarRwi container-table">
              <div class="tarRis">
                <info ref="infoPdf" :infoId="item.id"></info>
              </div>
            </div>
          </template>
        </div>
        <div v-show="!shows">
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
                    <div v-if="MajorElectiveCoursesDataB.length > 2" class="tarRcolor" id="five">
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
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 驳回弹窗 -->
    <el-dialog
      title="Send Email"
      @opened="foundE()"
      :visible.sync="returnFormVisible"
      center
    >
      <el-form
        :model="returnForm.emailVO"
        :rules="returnRules"
        ref="returnForm"
        label-width="80px"
      >
        <el-form-item label="To" prop="mailTo">
          <el-input
            v-model="returnForm.emailVO.mailTo"
            style="width: 100%"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="Subject" prop="subject">
          <el-input
            v-model="returnForm.emailVO.subject"
            style="width: 100%"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="Content">
          <div
            id="websiteEditorElem"
            ref="websiteEditorElem"
            style="height: 350px; background: #ffffff"
          ></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="returnFormVisible = false" type="info"
          >Cancel</el-button
        >
        <el-button type="primary" @click="handleConfirm">Confirm</el-button>
      </div>
    </el-dialog>
    <div id="pdfBxo" ref="pdfBxo" />
  </div>
</template>

<script>
import tarpush from './teachers/tarpush.vue'
import nodet from './assembly/nodet.vue'
import stuatsname from './teachers/stuatsname.vue'
import elements from './teachers/elements.vue'
import puretable from './puretable'
import collegeStudent from './puretable/collegeStudent'
import collegeCourses from './puretable/collegeCourses'
import coreCourses from './puretable/coreCourses'
import professionalElective from './puretable/professionalElective'
import professionalElectiveB from './puretable/professionalElectiveB'
import statement from './puretable/statement'
import freeElectives from './puretable/freeElectives'
import FreeElectiveData from './puretable/FreeElectiveData'
import E from 'wangeditor'
import Cookies from 'js-cookie'
import info from './info.vue';

import {
  codeMajor,
  categoryList,
  coursemasterList,
  detailTeacher,
  rejectTeacher,
  batchSendEmail,
  exportExcel,
  detailSave,
  linkAdfsLogout,
  getSchoolYear,
  updateStatus,
  updateAcknowledged,
  getSystemConfig,
} from '@/api/index'
export default {
  components: {
    tarpush,
    nodet,
    stuatsname,
    elements,
    puretable,
    statement,
    freeElectives,
    'college-student': collegeStudent,
    'college-courses': collegeCourses,
    'core-courses': coreCourses,
    'professional-elective': professionalElective,
    professionalElectiveB,
    FreeElectiveData,
    info,
  },
  data() {
    return {
      isCheckBox: null,
      studentCheckFeedback: null,
      teacherCheckBox: false,
      nameCh: '',
      activeName: false,
      row: '',
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
      //表格
      formData: {
        major: '',
        value: '',
        input: '',
        studentCheckBox: '',
        startEnd: [],
        timeType: '',
        expectedGraduationTerm: '',
        grade: '',
        startTime: '',
        endTime: '',
      },
      //枚举
      options1: [
        {
          value: 'Student I.D',
          label: 'Student I.D',
        },
        {
          value: 'Name',
          label: 'Name',
        },
      ],
      options2: [
        {
          value: '1',
          label: 'Y',
        },
        {
          value: '0',
          label: 'N',
        },
      ],

      // 假数据表头数据源
      trData: [
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
      tableData: {},
      multipleSelection: [],
      //分页
      current: 1,
      //多少
      size: 10,
      // 切换状态
      shows: true,
      //枚举
      meiju: [],
      //学生毕业档案
      stmydata: {},
      //项目数据
      piData: {
        studentVO: {
          studentId: '',
          nameEn: ''
        },
      },
      // 声明 Declaration
      statementData: {},
      /** 是否批量发送邮件 */
      isBatchEmail: false,
      // 驳回弹窗显示隐藏
      returnFormVisible: false,
      // 驳回参数
      returnForm: {
        id: '',
        action: 'reject',
        emailVO: {
          content: null,
          mailTo: '',
          subject: '',
        },
      },

      returnRules: {
        mailTo: [{ required: true, message: '请正确填写', trigger: 'blur' }],
        subject: [{ required: true, message: '请正确填写', trigger: 'blur' }],
      },
      phoneEditor: null, //  富文本实例
      auditBtn: false,
      contentTextList: [],
      height: 0,
      pageHeight: 0,
      /** 学年选项 */
      schoolYear: [],
      systemInfo: {},
      selfDeclaration: '',
      /** 列表pdf导出 */
      pdfVisible: false,
      progressOption: ['', '已完成修读', '正在修读', '未修读'],
      /** 其他课程数据，对应课程编码 */
      courses: {},
    }
  },
  created() {
    this.nameCh = localStorage.getItem('nameCh')
    this.payWay()
    this.recordList()
    this.getSystemInfo();
  },

  methods: {
    async getSystemInfo() {
      const result = await getSystemConfig();
      this.systemInfo = result.data || {};
    },
    openSendEmail(){
      let studentVO = this.piData.studentVO;
      this.returnForm.emailVO.mailTo = studentVO.email;
      this.isBatchEmail = false;
      this.returnFormVisible = true;
    },
    async detailSave() {
      let param = {
        id: this.returnForm.id,
        action: 'agree',
        officerCheckedDate: this.statementData.officerCheckedDate,
        teacherCheckBox: this.statementData.teacherCheckBox ? 1 : 0,
      }
      let res = await detailSave(param)
      if (res.code == 200) {
        this.$message({
          type: 'success',
          message: '保存成功',
        })
        this.recordList()
        this.shows = true
      } else {
        this.$message({
          type: 'error',
          message: res.message
        })
      }
    },
    handlePdfMultiple() {
      if (this.multipleSelection.length === 0) {
        this.$message.error('请选择数据后再操作');
        return;
      }

      this.pdfVisible = true;
      // const eles = this.$refs.infoPdf;

      // for(let i = 0; i < eles.length; i++) {
      //   eles[i].printPdf();
      // }
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
      document.getElementById('pdfBxo').innerHTML = '';
      this.$nextTick(async () => {
        this.$PDFSave(this.$refs.exportPdf, pdfName);
      })
    },
    handleInitEle() {
      /** 获取可计算高度的元素 */
      const calcs = document.getElementById('exportPdf').getElementsByClassName('calc');
      this.height = 0;
      this.pageHeight = 1848; // 一页的高度
      for (let i = 0; i < calcs.length; i++) {
        const ident = calcs[i].getAttribute('desc');
        if (ident === 'content') {
          this.height += 20;
          const nodes = calcs[i].children;
          for (let j = 0; j < nodes.length; j++) {
            this.handleNodeHeight(nodes[j]);
          }
        } else {
          this.handleNodeHeight(calcs[i]);
        }
      }
    },
    handleNodeHeight(node) {
      if (node.offsetHeight + this.height < this.pageHeight) {
        // 添加这个节点仍不会超出一页高度
        this.height += node.offsetHeight
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

        eleStyle.marginTop = `${marginTop}px` // 这页剩余高度
        
        this.height = 0 // 填充了空白后，这页就结束了，因此重置height
        this.height = node.offsetHeight;
      }
    },
    async exportTableList() {
      let val = this.multipleSelection
      if (val.length >= 1) {
        const ids = val.map((e) => e.id)
        let param = { ids }
        let res = await exportExcel(param)
      } else {
        this.$message({
          type: 'error',
          message: '请选择数据后再操作',
        })
      }
    },
    handleSendEmail() {
      if (this.multipleSelection.length > 0) {
        this.returnForm.emailVO.mailTo = this.multipleSelection.map(x => x.email).join(';');
        this.isBatchEmail = true;
        this.returnFormVisible = true;
      } else {
        this.$message({
          type: 'error',
          message: '请选择数据后再操作',
        })
      }
    },
    handleReject() {
      if (this.multipleSelection.length > 0) {
        this.$confirm('确定驳回选中数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const result = await updateStatus({
            ids: this.multipleSelection.map(x => x.id),
            status: 2,
          });

          if (result.code == 200) {
            this.$message({
              type: 'success',
              message: '操作成功',
            })
            this.recordList()
          } else {
            this.$message({
              type: 'error',
              message: result.message || '操作失败'
            })
          }
        });
      } else {
        this.$message({
          type: 'error',
          message: '请选择数据后再操作',
        })
      }
    },
    handleAcknowledged() {
      if (this.multipleSelection.length > 0) {
        this.$confirm('确定承认选中数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const result = await updateAcknowledged({
            ids: this.multipleSelection.map(x => x.id),
            teacherCheckBox: 1,
          });

          if (result.code == 200) {
            this.$message({
              type: 'success',
              message: '操作成功',
            })
            this.recordList()
          } else {
            this.$message({
              type: 'error',
              message: result.message || '操作失败'
            })
          }
        });
      } else {
        this.$message({
          type: 'error',
          message: '请选择数据后再操作',
        })
      }
    },
    handleInvalid() {
      if (this.multipleSelection.length > 0) {
        this.$confirm('确定废弃选中数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const result = await updateStatus({
            ids: this.multipleSelection.map(x => x.id),
            status: 4,
          });

          if (result.code == 200) {
            this.$message({
              type: 'success',
              message: '操作成功',
            })
            this.recordList()
          } else {
            this.$message({
              type: 'error',
              message: result.message || '操作失败'
            })
          }
        });
      } else {
        this.$message({
          type: 'error',
          message: '请选择数据后再操作',
        })
      }
    },
    //学生分页列表
    async recordList() {
      let data = {
        condition: {
          fieldName: this.formData.value,
          keyword: this.formData.input,
          major: this.formData.major,
          studentCheckBox: this.formData.studentCheckBox,
          timeType: this.formData.timeType,
          expectedGraduationTerm: this.formData.expectedGraduationTerm,
          grade: this.formData.grade,
          status: this.formData.status,
          startTime: this.formData.startEnd.length === 2 ? this.formData.startEnd[0] : '',
          endTime: this.formData.startEnd.length === 2 ? this.formData.startEnd[1] : '',
        },
        current: this.current, //分页
        size: this.size,
        sorts: [
          {
            asc: true,
            field: '',
          },
        ],
      }

      let res = await coursemasterList(data)
      this.stmydata = res.data
    },
    async cleanQuery() {
      this.formData = {
        major: '',
        value: '',
        input: '',
        studentCheckBox: '',
        startEnd: [],
        timeType: '',
        expectedGraduationTerm: '',
        grade: '',
        startTime: '',
        endTime: '',
      };
      this.recordList();
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
    },
    handleLinkToConfig() {
      this.$router.push('/config');
    },
    handleLinkToReport() {
      this.$router.push('/report');
    },
    async outlogin() {
      Cookies.remove('XSRF-TOKEN')
      window.localStorage.clear()
      linkAdfsLogout()
      // let res = await authLogout()
      // this.$router.push({ path: '/' })
    },
    // 添加删除返回刷新
    addlist(data) {
      this.plateList(data)
    },
    async codeMa() {
      let res = await codeMajor()
      this.meiju = res.data

      const result = await getSchoolYear();
      this.schoolYear = result.data;
    },
    //登录
    async payWay() {
      let datas = {
        current: 1,
        size: 10,
      }
      this.plateList(datas)
      this.codeMa()
    },
    //查询模板信息
    async plateList(data) {
      let res = await categoryList(data)
      this.tableData = res.data
    },
    handleClick(tab, event) {
      console.log(tab, event)
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
    async goDetails(data) {
      this.returnForm.id = data.id
      let res = await detailTeacher(data.id)
      if (res.code == 200 && res.data !== null) {
        this.$set(this.returnForm.emailVO, 'mailTo', res.data.studentVO.email)
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
      this.shows = false
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    handleSelectionChange(val) {
      this.pdfVisible = false;
      this.multipleSelection = [];
      this.$nextTick(() => {
        this.multipleSelection = val
      });
    },
    //学生资料分页
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.current = 1
      this.recordList()
    },

    // 驳回确认
    handleConfirm() {
      this.$refs['returnForm'].validate((valid) => {
        if (valid) {
          this.returnForm.emailVO.content = this.phoneEditor.txt.html()
          if (this.isBatchEmail) {
            batchSendEmail(this.returnForm.emailVO).then(res => {
              if (res.code == 200) {
                this.$message.success('邮件发送成功')
                this.returnFormVisible = false
              } else {
                this.$message.error(res.message || '邮件发送失败')
              }
            });
          } else {
            rejectTeacher(this.returnForm).then((res) => {
              if (res.code == 200) {
                this.$message.success('操作成功！')
                this.goDetails(this.returnForm)
                this.returnFormVisible = false
              } else {
                this.$message.error(res.message)
              }
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },

    // 创建富文本
    foundE() {
      this.phoneEditor = new E(this.$refs.websiteEditorElem)
      this.phoneEditor.create()
      // 富文本内容
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
.el-date-editor.el-input {
  width: auto;
}
.container-table {
  position: fixed;
  height: 1px;
  overflow: hidden;
}
.container-scroll {
  overflow-y: scroll;
  height: calc(100vh - 90px - 44px - 100px);
}
.align-right {
  float: right;
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
.suRtui > div {
  line-height: 30px;
}
.sulid {
  font-size: 20px;
  vertical-align: middle !important;
  margin-right: 10px;
}
.font-decoration {
  color: #6a1d72;
  text-decoration: underline;
  cursor: pointer;
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
  height: 150px;
}
.stRen span {
  vertical-align: middle;
}
.stDmsi {
  margin: 10px 0;
}
.stDfou {
  margin: 10px 0;
  font-weight: 600;
}
#abb {
  width: 100vw;
  height: 100vh;
  overflow-y: scroll;
  overflow: hidden;
}

.riButton {
  padding: 20px 0;
}
.ripatop {
  padding-top: 0px;
}
.riColor {
  background: #6a1d72;
  border: 1px solid #6a1d72;
  color: #fff;
  font-family: 'Book Antiqua';
}

.riColor:focus,
.riColor:hover {
  background: #531659;
  border: 1px solid #531659;
  color: #fff;
  font-family: 'Book Antiqua';
}

.riColorchen {
  background: #e6a823 !important;
  border: 1px solid #e6a823;
  color: #fff !important;
  font-family: 'Book Antiqua';
}

.riColorchen:hover {
  background: #d29b23 !important;
  border: 1px solid #d29b23;
  color: #fff !important;
  font-family: 'Book Antiqua';
}

.riright {
  text-align: right;
}

.riwidth {
  width: 20%;
  display: inline-block;
  vertical-align: middle;
}

.riwidths {
  width: 79%;
  display: inline-block;
  vertical-align: middle;
}

.rimar {
  padding: 20px;
  background: #fff;
  text-align: right;
}
.rirow {
  border-radius: 4px;
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
.banxin {
  width: 85%;
  margin: 0 auto;
  min-width: 1300px;
}
.tarRwi {
  background: #eee;
}
.tarRis {
  width: 1300px;
  margin: 0 auto;
}
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
.tarRmar {
  padding: 30px 0;
}
.richTap {
  margin: 0 auto;
  margin-top: 100px;
  width: 85%;
  min-width: 1300px;
}
.richTap div {
  display: inline-block;
  margin-right: 10px;
  font-weight: 600;
  padding: 10px 0;
}
.richTap div:hover {
  color: #6a1d72;
  border-bottom: 2px solid #6a1d72;
}
.richTapadd {
  color: #6a1d72;
  border-bottom: 2px solid #6a1d72;
}
.richflo {
  /* float: right; */
  text-align: right;
}
</style>
<style>
.content {
  padding: 10px 0;
}

.content p {
  margin: 0;
  padding: 6px 0;
}
.sud .el-table th {
  background-color: #6a1d72 !important;
  color: #fff;
}
.sud .el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background: #6a1d72;
  color: #fff;
  border: 1px solid #eee;
}
.iptText {
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 2px solid black;
}
.richTap .el-tabs__item.is-active {
  color: #6a1d72;
}
.richTap .el-tabs__active-bar {
  background-color: #6a1d72;
}
@media print {
  body {
    -webkit-print-color-adjust: exact;
    -moz-print-color-adjust: exact;
    -ms-print-color-adjust: exact;
    print-color-adjust: exact;
  }
}
.el-dialog .el-button {
  padding: 10px 30px !important;
}
/* @media screen and (max-width: 1400px) {
  .el-select {
    width: 200px !important;
  }
} */
</style>
<style>
body {
  font-size: 16px;
}

p {
  padding: 10px 0;
  margin: 0;
}

.noName {
  margin: 0 !important;
}

.noName div {
  margin: 0 !important;
}
</style>