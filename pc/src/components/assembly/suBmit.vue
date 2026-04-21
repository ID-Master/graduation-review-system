<template>
  <div class="stPadd">
    <div v-html="text"></div>
    <!--
    <div class="noName">
      <div class="text-color">声明 Declaration</div>
      <div>Note 备注:</div>
      <div>
        以下签名表明你已确认本表格所填信息是真实，完整以及准确无误的，并且完全清楚任何虚假信息、遗漏或者失实表述均可能导致延迟毕业或其他后果。
      </div>
      <p>
        The signature below indicates that you confirm all information given in
        this form is true, complete and accurate. You are fully aware that any
        false information, omission or misrepresentation contained may lead to
        delay graduation or other consequences.
      </p>
    </div>
    <div>
      <div>
        <el-checkbox v-model="checked" :disabled="[1, 3].includes(isStatus)"></el-checkbox>
        <span style="margin-left: 10px">根据填写情况，本人申报本学期毕业</span>
      </div>

      <p>
        According to the filling situation, I declare my graduation this
        semester
      </p>
    </div>
    -->
    <div class="subott">
      <el-row :gutter="24">
        <el-col :span="12">
          <div class="subott-title">学生签名：</div>
          <div class="subott-title">Signature</div>
          <div class="subott-content">
            <a v-if="url" href="javascript: void;" @click="handleOpen(url)">{{ `signature${url.substring(url.lastIndexOf('.'))}` }}</a>
            <a v-else href="javascript: void;"></a>
            <template v-if="[0, 2].includes(isStatus)">
              <el-upload
                :action="action"
                :show-file-list="false"
                :http-request="onOssUpload"
                :on-success="handleSuccess"
                list-type="picture"
                :before-upload="beforeAvatarUpload"
              >
                <el-button
                  size="small"
                  class="sulans"
                  >Upload</el-button
                >
              </el-upload>
            </template>
          </div>
        </el-col>
        <!-- <el-col :span="8">
          <div class="subott-title">最新版英文成绩单：</div>
          <div class="subott-title">The most updated unofficial/official transcript</div>
          <div class="subott-content">
            <a v-if="url1" href="javascript: void;" @click="handleOpen(url1)">{{ `transcript${url1.substring(url1.lastIndexOf('.'))}` }}</a>
            <a v-else href="javascript: void;"></a>
            <template v-if="[0, 2].includes(isStatus)">
              <el-upload
                :action="action"
                :show-file-list="false"
                :http-request="onOssUpload1"
                :on-success="handleSuccess1"
                list-type="picture"
                :before-upload="beforeAvatarUpload"
              >
                <el-button
                  size="small"
                  class="sulans"
                  >Upload</el-button
                >
              </el-upload>
            </template>
          </div>
        </el-col> -->
        <el-col :span="12">
          <div class="subott-title">日期：</div>
          <div class="subott-title">Date</div>
          <div class="subott-content">
            <el-date-picker
              v-model="date"
              type="date"
              placeholder=""
              value-format="yyyy-MM-dd"
              disabled
              @change="handleDateChange"
              v-if="[0,2].includes(isStatus)"
            />
            <div v-else>{{ date }}</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import moment from 'moment';
import { uploadSuess, saveStudentCheckBox } from '@/api/student'
import eltable from './eltable.vue'
export default {
  props: {
    //总数
    valueTotal: {
      type: String,
      default: '1',
    },
    isStatus: {
      type: Number,
      default: 0,
    },
    signatureDate: {
      type: String,
      default: '',
    },
    signatureUrl: {
      type: String,
      default: '',
    },
    englishSchoolReport: {
      type: String,
      default: '',
    },
    studentCheckBox: {
      type: Boolean,
      default: false,
    },
    text: {
      type: String,
      default: '',
    },
  },
  components: { eltable },
  data() {
    return {
      // action: 'http://106.54.209.225:10050',  // 测试环境（私服）
      action: 'http://10.20.217.41:10080', // 正式环境（港中大）
      fileList: [],
      fileId: '',
      id: '',
      srcList: [],
      date: moment().format('YYYY-MM-DD'),
      url: '',
      url1: '',
      srcList1: [],
      fileId1: '',
    }
  },
  methods: {
    handleOpen(url) {
      window.open(url);
    },
    async onOssUpload(param) {
      const { action, filename, file } = param
      const form = new FormData()
      form.append(filename || 'file', file)
      let res = await uploadSuess(form)
      if (res.code == 200) {
        this.fileId = res.data.id
        this.url = res.data.fileUrl
        this.srcList.push(res.data.fileUrl)
        this.$emit('save', {
          signatureUrl: this.url,
        });
      }
    },
    handleSuccess(response, file, fileList) {},
    beforeAvatarUpload(file) {
      /** 文件大小 单位 mb */
      const size = Number(file.size || 0) / 1024 / 1024;

      // 限制10M图片不能上传
      if (size >= 10) {
        this.$message.error('上传的图片大小需小于10M');
        return false;
      }

      return true;
    },
    async onOssUpload1(param) {
      const { action, filename, file } = param
      const form = new FormData()
      form.append(filename || 'file', file)
      let res = await uploadSuess(form)
      if (res.code == 200) {
        this.fileId1 = res.data.id
        this.url1 = res.data.fileUrl
        this.srcList1.push(res.data.fileUrl)
        this.$emit('save', {
          englishSchoolReport: this.url1,
        });
      }
    },
    handleDateChange() {
      this.$emit('save', {
        signatureDate: this.date,
      });
    },
    handleSuccess1(response, file, fileList) {},
  },
  mounted() {},
  watch: {
    signatureUrl: {
      deep: true,
      immediate: true,
      handler(newVal) {
        this.url = newVal;
        this.srcList.push(newVal);
      },
    },
    englishSchoolReport: {
      deep: true,
      immediate: true,
      handler(newVal) {
        this.url1 = newVal;
        this.srcList1.push(newVal);
      },
    },
    signatureDate: {
      deep: true,
      immediate: true,
      handler(newVal) {
        this.date = newVal;
      },
    },
  },
}
</script>

<style  scoped>
.stPadd {
  margin: 0 auto;
  text-align: left;
}
p {
  padding: 6px 0;
  margin: 0;
  margin-left: 28px;
}
.noName {
  text-align: left;
}
.noName div {
  margin-bottom: 0;
}
.sunames {
  width: 40%;
  display: inline-block;
  vertical-align: middle;
  height: 100px;
}
.sutext {
  display: inline-block;
  width: 100px;
  vertical-align: middle;
  font-weight: 600;
}
.subott {
  margin-top: 50px;
  position: relative;
}
.sutext div {
  padding: 5px 0;
}
.sulans {
  color: #3291f8;
  border: 1px solid #3291f8;
}
.sulans:hover {
  color: #1d84f4;
  border: 1px solid #1d84f4;
}
.supush {
  position: relative;
  width: calc(100% - 220px);
  display: inline-block;
  vertical-align: middle;
  border-bottom: 1px solid #979797;
  padding-bottom: 10px;
  height: 32px;
}
.supusc {
  padding-bottom: 20px;
}
.surights {
  text-align: center;
}
.sufol_right {
  float: right;
}
.sufol {
  float: left;
  margin-top: 50px;
}
.avatar-uploader {
  position: absolute;
  right: 0;
  display: inline-block;
  margin-right: 10px;
}
.avatar-uploader .el-upload {
  margin: 0 auto;
  display: block;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  text-align: center;
}
.subott-title {
  font-size: 14px;
}
.subott-content {
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}
</style>
