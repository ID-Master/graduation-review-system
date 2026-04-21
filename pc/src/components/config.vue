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
          <div @click="$router.push('/report')">Report</div>
          <div @click="addout">Logout</div>
        </div>
      </div>
    </div>
    <div class="p-container">
      <el-tabs v-model="tabActive" @tab-click="tabSwitchInit">
        <el-tab-pane label="System" name="system">
          <el-form ref="systemForm" size="small" :model="systemFormData" label-position="right" label-width="120px">
            <div class="p-form-title"></div>
            <el-row :gutter="24" style="margin-top: 20px;">
              <el-col :span="10">
                <el-form-item label="开放时间" prop="startEnd" :rules="[{ required: true, message: '请选择', trigger: 'change' }]">
                  <el-date-picker
                    v-model="systemFormData.startEnd"
                    type="datetimerange"
                    style="width: 100%;"
                    range-separator="~"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    format="yyyy-MM-dd HH:mm:ss"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="10">
                <el-form-item label="提示语" prop="message" :rules="[{ required: true, message: '请输入', trigger: 'input, change' }]">
                  <el-input v-model="systemFormData.message" type="textarea" rows="4"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="20">
                <el-form-item label="Note 1" prop="noteOne">
                  <div id="note1" ref="note1" style="height: 240px; background: #ffffff"></div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="20">
                <el-form-item label="Note 7" prop="noteSeven">
                  <div id="note7" ref="note7" style="height: 240px; background: #ffffff"></div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="20">
                <el-form-item label="Note 8" prop="noteEight">
                  <div id="note8" ref="note8" style="height: 240px; background: #ffffff"></div>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24" style="margin-top: 20px;">
              <el-col :span="20" style="text-align: center;">
                <el-button @click="handleSystemSave" :loading="systemLoading" size="small" type="primary">Save</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Course Template List" name="curriculum">
          <tarpush :mydata="tableData" @addpush="addlist" :meiju="meiju"></tarpush>
        </el-tab-pane>
        <el-tab-pane label="Major List" name="major">
          <div style="text-align: left;padding: 0 10px 15px 0;">
            <el-button icon="el-icon-plus" size="small" type="primary" @click="handleAddMajor" class="riColor" round>Add</el-button>
          </div>
          <div :key="tableKey">
            <el-table :data="majors.records" tooltip-effect="dark" style="width: 100%">
              <el-table-column prop="name" width="240" label="Name" align="center"></el-table-column>
              <el-table-column prop="value" label="Major" align="center"></el-table-column>
              <el-table-column prop="status" width="100" label="Status" align="center">
                <template slot-scope="scope">
                  {{ scope.row.status == 1 ? 'Enable' : 'Disable' }}
                </template>
              </el-table-column>
              <el-table-column prop="sortIndex" width="160" label="Sort" align="center"></el-table-column>
              <el-table-column prop="id" width="200" label="Operations" align="center">
                <template slot-scope="scope">
                  <el-button @click="handleEditMajor(scope.row)" type="text" size="small" icon="el-icon-edit">Edit</el-button>
                  <el-button @click="handleDeleteMajor(scope.row.id)" type="text" size="small" icon="el-icon-delete">Delete</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="margin-top: 10px;text-align: right;">
            <el-pagination
              @size-change="handleMajorFilter"
              @current-change="loadMajor"
              :current-page.sync="majorParam.current"
              :page-sizes="[10, 20, 30, 40]"
              :page-size.sync="majorParam.size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="majors.total"
            >
            </el-pagination>
          </div>
        </el-tab-pane>
        <el-tab-pane v-if="isAdmin" label="Permissions" name="teacherConfig">
          <el-form ref="teacherConfigForm" size="small" :model="teacherFormData" label-position="right" label-width="120px">
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="教师邮箱" prop="message" :rules="[{ required: true, message: '请输入', trigger: 'input, change' }]">
                  <el-input v-model="teacherFormData.teacherList" type="textarea" rows="8"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="24">
              <el-col :span="12" style="padding-left: 132px;color: #d61518;font-size: 14px;font-weight: 700;">
                *回车分隔多个教师邮箱
              </el-col>
            </el-row>
            <el-row :gutter="24" style="margin-top: 20px;">
              <el-col :span="12" style="text-align: center;">
                <el-button @click="handleTeacherSave" :loading="systemLoading" size="small" type="primary">Save</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Upload" name="student">
          <div class="p-form-title">
            导入学生名单 <el-button @click="handleDownload" type="text" size="small">下载模板</el-button>
          </div>
          <el-upload
            style="margin-top: 20px;"
            drag
            :action="''"
            :before-upload="beforeAvatarUpload"
            :http-request="onOssUpload"
            :show-file-list="false"
            multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xls/xlsx文件</div>
          </el-upload>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
      title="Add / Edit"
      :visible.sync="majorVisible"
      width="50%"
    >
      <el-form
        :model="major"
        ref="majorForm"
        label-width="120px"
        class="demo-ruleForm"
      >
        <el-form-item label="Name" prop="name" :rules="[{ required: true, message: '请输入', trigger: 'input' }]">
          <el-input size="small" v-model="major.name"></el-input>
        </el-form-item>
        <el-form-item label="Major" prop="value" :rules="[{ required: true, message: '请输入', trigger: 'input' }]">
          <el-input size="small" v-model="major.value" ></el-input>
        </el-form-item>
        <el-form-item label="Status" prop="status" :rules="[{ required: true, message: '请选择', trigger: 'change' }]">
          <el-select
            style="width: 100%;"
            v-model="major.status"
            placeholder=""
            size="small"
          >
            <el-option label="Enable" :value="1"></el-option>
            <el-option label="Disable" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Sort" prop="sortIndex" :rules="[{ required: true, message: '请输入', trigger: 'input' }]">
          <el-input size="small" v-model="major.sortIndex" type="number"></el-input>
        </el-form-item>
      </el-form>
      <div class="tarBU" style="text-align: center;">
        <el-button class="tarhui" size="small" @click="majorVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="majorLoading" size="small" @click="handleMajorSave()">Save</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import _ from 'lodash';
import tarpush from './teachers/tarpush.vue'
import E from 'wangeditor'
import Cookies from 'js-cookie'

import {
  codeMajor,
  categoryList,
  systemConfig,
  getSystemConfig,
  uploadSuess,
  majorList,
  modifyMajor,
  deleteMajor,
  linkAdfsLogout,
} from '@/api/index'
import url from '@/api/url'

export default {
  components: {
    tarpush,
  },
  data() {
    return {
      tabActive: 'system',
      tableData: {},
      meiju: [],
      nameCh: '',
      systemFormData: {
        startEnd: [],
        message: '',
      },
      teacherFormData: {
        teacherList: '',
      },
      systemLoading: false,
      note1Editor: null,
      note7Editor: null,
      note8Editor: null,
      majors: {},
      majorVisible: false,
      major: {
        id: '',
        name: '',
        value: '',
        status: 1,
        sortIndex: '',
      },
      majorParam: {
        current: 1,
        size: 10,
        condition: {
          name: '',
          value: '',
        },
      },
      majorLoading: false,
      tableKey: 0,
    }
  },
  computed: {
    isAdmin() {
      return localStorage.getItem('admin') === 'true';
    },
  },
  created() {
    this.nameCh = localStorage.getItem('nameCh')
    this.tabActive = window.localStorage.getItem('tab-active') || 'system';
    this.tabSwitchInit();
  },
  mounted() {
    this.$nextTick(() => {
      this.initEditor();
    });
  },
  methods: {
    handleMajorFilter() {
      this.majorParam.current = 1;
      this.loadMajor();
    },
    handleAddMajor() {
      this.majorVisible = true;
      this.major.id = '';
      this.major.name = '';
      this.major.value = '';
      this.major.status = 1;
      this.major.sortIndex = '';
    },
    handleEditMajor(data) {
      this.majorVisible = true;
      this.major.id = data.id || '';
      this.major.name = data.name;
      this.major.value = data.value;
      this.major.status = data.status;
      this.major.sortIndex = data.sortIndex;
    },
    handleDeleteMajor(id) {
      this.$confirm('此操作将删除该数据，是否确定？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
         const result = await deleteMajor(id);

         if (result.code == 200) {
           if (this.majors.records.length === 1 && this.majorParam.current > 1) {
             this.majorParam.current--;
           }
           this.loadMajor();
           this.$message.success('Delete succeeded');
         } else {
            this.$message.error(result.message || 'Deletion failed');
         }
      });
    },
    handleMajorSave() {
      this.majorLoading = true;
      this.$refs.majorForm.validate(async (valid) => {
        if (valid) {
          const result = await modifyMajor(this.major);

          this.majorLoading = false;
          if (result.code == 200) {
            this.$message.success('Saved successfully');
            this.loadMajor();
            this.majorVisible = false;
          } else {
            this.$message.error(result.message || 'Save failed');
          }
        } else {
          this.majorLoading = false;
        }
      });
    },
    async loadMajor() {
      const result = await majorList(this.majorParam);

      if (result.code === 200) {
        this.majors = result.data || {};
      } else {
        this.$message.error(result.message);
      }
    },
    initEditor() {
      this.note1Editor = new E(this.$refs.note1)
      this.note1Editor.create()

      this.note7Editor = new E(this.$refs.note7)
      this.note7Editor.create()

      this.note8Editor = new E(this.$refs.note8)
      this.note8Editor.create()
    },
    handleDownload() {
      window.open(`${url}/biz/course-master/student/template`);
    },
    async tabSwitchInit() {
      if (this.tabActive === 'system') {
        const result = await getSystemConfig();
        if (result.success) {
          this.systemFormData.message = result.data.message || '';

          if (result.data.startTime && result.data.endTime) {
            this.systemFormData.startEnd = [result.data.startTime, result.data.endTime];
          } else {
            this.systemFormData.startEnd = [];
          }

          if (!this.note1Editor) {
            this.initEditor();
          }
          this.systemFormData.noteOne = result.data.noteOne || '';
          this.systemFormData.noteSeven = result.data.noteSeven || '';
          this.systemFormData.noteEight = result.data.noteEight || '';

          this.note1Editor.txt.html(this.systemFormData.noteOne);
          this.note7Editor.txt.html(this.systemFormData.noteSeven);
          this.note8Editor.txt.html(this.systemFormData.noteEight);

          this.teacherFormData.teacherList = result.data.teacherList;
        }
      } else if (this.tabActive === 'curriculum') {
        this.plateList({
          current: 1,
          size: 10,
        });
        this.codeMa();
      } else if (this.tabActive === 'major') {
        this.$nextTick(() => {
          this.tableKey++;
          this.handleMajorFilter();
        });
      }

      window.localStorage.setItem('tab-active', this.tabActive);
    },
    async plateList(data) {
      // major: this.formDar.major,
      // grade: this.formDar.grade,
      const storageParams = JSON.parse(window.localStorage.getItem('course-template-list-param') || '{}');

      const params = {
        condition: {
          templateKeyword: '',
          major: storageParams.major,
          grade: storageParams.grade,
          ...data.condition,
        },
        current: data.current,
        size: data.size,
        sorts: [
          {
            asc: true,
            field: "",
          },
        ],
      };

      let res = await categoryList(params)

      window.localStorage.setItem('course-template-list-param', JSON.stringify({
        major: params.condition.major,
        grade: params.condition.grade,
      }))
      this.tableData = res.data
    },
    async codeMa() {
      let res = await codeMajor()
      this.meiju = res.data
    },
    // 添加删除返回刷新
    addlist(data) {
      this.plateList(data)
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
    handleSystemSave() {
      this.systemFormData.noteOne = this.note1Editor.txt.html() || '';
      if (!this.systemFormData.noteOne) {
        this.$message.error('请输入Note 1');
        return;
      }

      this.systemFormData.noteSeven = this.note7Editor.txt.html() || '';
      if (!this.systemFormData.noteSeven) {
        this.$message.error('请输入Note 7');
        return;
      }

      this.systemFormData.noteEight = this.note8Editor.txt.html() || '';
      if (!this.systemFormData.noteEight) {
        this.$message.error('请输入Note 8');
        return;
      }

      this.systemLoading = true;
      this.$refs.systemForm.validate(async (valid) => {
        if (valid) {
          const result = await systemConfig({
            startTime: this.systemFormData.startEnd.length === 2 ? this.systemFormData.startEnd[0] : '',
            endTime: this.systemFormData.startEnd.length === 2 ? this.systemFormData.startEnd[1] : '',
            message: this.systemFormData.message,
            noteOne: this.systemFormData.noteOne,
            noteSeven: this.systemFormData.noteSeven,
            noteEight: this.systemFormData.noteEight,
          });

          this.systemLoading = false;
          if (result.success) {
            this.$message.success('保存成功');
          } else {
            this.$message.error(result.message || '保存失败');
          }
        } else {
          this.systemLoading = false;
        }
      });
    },
    handleTeacherSave() {
      this.systemLoading = true;
      this.$refs.systemForm.validate(async (valid) => {
        if (valid) {
          const result = await systemConfig({
            teacherList: this.teacherFormData.teacherList,
          });

          this.systemLoading = false;
          if (result.success) {
            this.$message.success('保存成功');
          } else {
            this.$message.error(result.message || '保存失败');
          }
        } else {
          this.systemLoading = false;
        }
      });
    },
    beforeAvatarUpload(file) {
      if (_.endsWith(file.name, '.xls') || _.endsWith(file.name, '.xlsx')) {
        return true;
      } else {
        this.$message.error('请选择Excel文件上传');
        return false;
      }
    },
    async onOssUpload(param) {
      const loading = this.$loading({
        lock: true,
        text: 'Uploading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.2)'
      });
      const { action, filename, file } = param
      const form = new FormData()
      form.append(filename || 'file', file)
      let res = await uploadSuess(form)

      loading.close();
      if (res.success) {
        this.$message.success('学生名单导入成功');
      } else {
        if (res.data instanceof Array && res.data.length > 0) {
          ;
          this.$alert(res.data.join('<br/>'), '提示', {
            dangerouslyUseHTMLString: true
          });
        } else {
          this.$message.error(res.message || '学生名单导入失败');
        }
      }
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
  z-index: 99;
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
  height: 150px;
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
.suRtui > div {
  line-height: 30px;
}
.sulid {
  font-size: 20px;
  vertical-align: middle !important;
  margin-right: 10px;
}
.p-container {
  padding: 100px 40px;
}
.riColor {
  background: #6a1d72;
  border: 1px solid #6a1d72;
  color: #fff;
  font-family: "Book Antiqua";
}
.riColor:focus,.riColor:hover {
  background: #531659;
  color: #fff;
  font-family: "Book Antiqua";
}

</style>
<style>
.w-e-text-container {
  height: calc(100% - 86px) !important;
}
.w-e-toolbar {
  z-index: 20 !important;
}
.w-e-text-container {
  z-index: 10 !important;
}
</style>
