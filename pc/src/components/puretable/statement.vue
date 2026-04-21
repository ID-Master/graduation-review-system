<template>
  <div>
    <div name="calc" class="calc">
      <div style="padding-top: 20px;" v-html="text"></div>
      <div style="padding: 10px 0;">
        <div>
          <el-radio disabled v-model="isOpen" :label="true">是 Yes</el-radio>
          <!-- <el-radio disabled v-model="isOpen" :label="true">是，本人申报在上述学期毕业。Yes, I am declaring graduation in above-mentioned term.</el-radio> -->
        </div>
        <div>
          <el-radio disabled v-model="isOpen" :label="false">否 No</el-radio>
          <!-- <el-radio disabled v-model="isOpen" :label="false">否，本人不申报在上述学期毕业。No, I am NOT declaring graduation in above-mentioned term.</el-radio> -->
        </div>
      </div>
    </div>
    <!-- <div v-if="!isOpen" name="calc" class="calc">不申报原因 Reason for not declaring:</div> -->
    <!-- <div v-if="!isOpen" name="calc" class="calc">{{ studentCheckFeedback }}</div> -->
    <div name="calc" class="calc" v-html="text2">
      <!--
      <div class="stDfou">声明 Declaration</div>
      <p>Note 备注:</p>
      <p>
        以下签名表明你已确认本表格所填信息是真实，完整以及准确无误的，并且完全清楚任何虚假信息、遗漏或者失实表述均可能导致延迟毕业或其他后果。
      </p>
      <p>
        The signature below indicates that you confirm all information given in
        this form is true, complete and accurate. You are fully aware that any
        false information, omission or misrepresentation contained may lead to
        delay graduation or other consequences.
      </p>
      -->
    </div>
    <div class="noName-padding calc" name="calc"></div>
    <div name="calc" class="calc">
      <el-row :gutter="24">
        <el-col :span="12">
          <div class="state-title">学生签名：</div>
          <div class="state-title">Signature</div>
          <div class="state-content">
            <a v-if="statementData.signatureUrl" href="javascript: void;" @click="handleOpen(statementData.signatureUrl)">{{ `signature${statementData.signatureUrl.substring(statementData.signatureUrl.lastIndexOf('.'))}` }}</a>
            <template v-else>-</template>
          </div>
        </el-col>
        <!-- <el-col :span="8">
          <div class="state-title">最新版英文成绩单：</div>
          <div class="state-title">The most updated unofficial/official transcript</div>
          <div class="state-content">
            <a v-if="statementData.englishSchoolReport" href="javascript: void;" @click="handleOpen(statementData.englishSchoolReport)">{{ `transcript${statementData.englishSchoolReport.substring(statementData.englishSchoolReport.lastIndexOf('.'))}` }}</a>
          </div>
        </el-col> -->
        <el-col :span="12">
          <div class="state-title">日期：</div>
          <div class="state-title">Date</div>
          <div class="state-content">
            {{ statementData.signatureDate }}
          </div>
        </el-col>
      </el-row>
    </div>
    <template v-if="!preview">
      <div name="calc" class="calc stDfou">For SME Office use only</div>
      <div name="calc" class="calc" style="display: flex">
        <div style="flex: 1">
          <el-checkbox v-model="statementData.teacherCheckBox" :disabled="auditBtn">Acknowledged</el-checkbox>
        </div>
        <div>
          <span>Date:</span>
          <!-- <input
            type="text"
            class="iptText"
            v-model="statementData.officerCheckedDate"
            style="width: 300px"
          /> -->
          <el-date-picker
            v-model="statementData.officerCheckedDate"
            type="date"
            placeholder=""
            style="width: 310px;"
            value-format="yyyy-MM-dd"
            :disabled="auditBtn"
          />
        </div>
      </div>
    </template>
  </div>
</template>
<script>
export default {
  name: 'collegeStudent',
  props: {
    statementData: {},
    isCheckBox: {
      type: Number,
      default: 0,
    },
    studentCheckFeedback: {
      type: String,
      default: '',
    },
    teacherCheckBox: {
      type: Boolean,
      default: false,
    },
    auditBtn: {
      type: Boolean,
      default: 1,
    },
    preview: {
      type: Boolean,
      default: false,
    },
    text: {
      type: String,
      default: '',
    },
    text2: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      isOpen: true,
      teacherCheckBoxChecked: false,
    }
  },
  watch: {
    isCheckBox(val, oldVal) {
      this.isOpen = val == 0 ? false : true
    },
    // teacherCheckBox(val, oldVal) {
    //   this.teacherCheckBoxChecked = val == 0 ? false : true
    // },
  },
  methods: {
    handleOpen(url) {
      window.open(url);
    }
  },
}
</script>
<style scoped>
p {
  margin: 0;
  padding: 6px 0;
}
.stDfou {
  padding: 10px 0;
  font-weight: 600;
}
.supush {
  flex: 1;
  display: flex;
  align-items: center;
  border-bottom: 1px solid;
  justify-content: center;
}
.sutext {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.noName-padding {
  height: 30px;
  padding: 0 !important;
}
</style>
<style>
.el-radio {
  line-height: 30px;
}

.el-radio__label {
  font-size: 16px;
  font-weight: 700;
}

.el-radio__input.is-checked+.el-radio__label {
  color: #d61518 !important;
}

.el-radio__input.is-checked .el-radio__inner {
  border-color: #d61518 !important;
  background: #d61518 !important;
}

.state-title {
  font-size: 14px;
}
.state-content {
  line-height: 50px;
  border-bottom: 1px solid #ddd;
}
</style>
