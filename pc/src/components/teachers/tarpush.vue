<template>
  <div>
    <div v-show="addlistdata">
      <div class="riButton">
        <div class="riwidth">
          <el-button
            class="riColor"
            icon="el-icon-plus"
            @click="openDialog"
            size="small"
            round
            >Add</el-button
          >
          <el-button
            size="small"
            icon="el-icon-delete"
            class="riColor"
            @click="delData"
            round
            >Delete</el-button
          >
        </div>
        <div class="riwidths riright">
          <el-date-picker v-model="formDar.grade" placeholder="Admitted Year" size="small" value-format="yyyy" type="year" />

          <el-select
            v-model="formDar.major"
            placeholder="Major"
            size="small"
            clearable
          >
            <el-option
              v-for="item in meiju"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            >
            </el-option>
          </el-select>

          <!-- <el-input
            placeholder="Please enter"
            v-model="formDar.templateKeyword"
            size="small"
            clearable
          >
          </el-input> -->
          <el-button
            class="riColor"
            icon="el-icon-search"
            @click="forpust"
            size="small"
            round
            >Query</el-button
          >
          <el-button
            @click="handlePreview"
            size="small"
            class="riColor"
            :disabled="!(formDar.major && formDar.grade)"
            round
            >Preview</el-button
          >
        </div>
      </div>
      <div>
        <el-table
          ref="multipleTable"
          :data="mydata.records"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="60px" />
          <el-table-column
            label="Admitted Year"
            :width="120"
            prop="grade"
            align="center"
          ></el-table-column>
          <el-table-column
            label="Major"
            prop="major"
            align="center"
            min-width="200px"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="categoryCode"
            label="Template Code"
            align="center"
            min-width="180px"
          >
            <template slot-scope="scope">
              <el-button
                @click="handleClick(scope.row)"
                type="text"
                size="small"
              >{{scope.row.categoryCode}}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="categoryName"
            label="Template Name"
            align="center"
            min-width="180px"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-button
                @click="handleClick(scope.row)"
                type="text"
                size="small"
              >{{scope.row.categoryName}}</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="title"
            label="Title"
            align="center"
            min-width="200px"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-button
                @click="handleClick(scope.row)"
                type="text"
                size="small"
              >{{scope.row.title}}</el-button>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="units"
            label="Units（Max）"
            align="center"
            width="110px"
          >
            <template slot-scope="scope">
              <el-button
                @click="handleClick(scope.row)"
                type="text"
                size="small"
              >{{scope.row.units}}</el-button>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            prop="updateNameEn"
            label="Update Name"
            width="200px"
            align="center"
            sortable
          >
            <template slot-scope="scope">
              <span class="font-decoration">
                {{scope.row.updateNameEn ? scope.row.updateNameEn : '-'}}
              </span>
            </template>
          </el-table-column> -->
          <el-table-column prop="type" label="Type" align="center" width="100px">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type == 1" type="success">Units</el-tag>
                <el-tag v-if="scope.row.type == 2">Progress</el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="part" label="Part" align="center" width="100px"></el-table-column>
          <el-table-column prop="sortIndex" width="100px" label="Sort" align="center"></el-table-column>
          <el-table-column
            prop="createdDate"
            label="Update Time"
            width="180px"
            align="center"
          >
          </el-table-column>
          <el-table-column
            fixed="right"
            label="Operations"
            width="140">
            <template slot-scope="scope">
              <!-- <el-button @click="handleView(scope.row)" type="text" size="small" icon="el-icon-view">View</el-button> -->
              <el-button @click="handleEdit(scope.row)" type="text" size="small" icon="el-icon-edit">Edit</el-button>
              <el-button @click="handleCopy(scope.row)" type="text" size="small" icon="el-icon-document-copy">Copy</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="tarpic">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="mydata.total"
        >
        </el-pagination>
      </div>
      <el-dialog
        title="Add / Edit"
        :visible.sync="dialogVisible"
        @opened="foundE()"
        @close="closeDialog"
        width="60%"
      >
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="150px"
          class="demo-ruleForm"
        >
          <el-form-item label="Admitted Year" prop="grade">
            <el-date-picker v-model="ruleForm.grade" value-format="yyyy" type="year" size="small"/>
          </el-form-item>
          <el-form-item label="Major" prop="major">
            <el-select
              v-model="ruleForm.major"
              placeholder=""
              clearable
              size="small"
            >
              <el-option
                v-for="item in meiju"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Template Code" prop="categoryCode">
            <el-input v-model="ruleForm.categoryCode" size="small"></el-input>
          </el-form-item>
          <el-form-item label="Template Name" prop="categoryName">
            <el-input v-model="ruleForm.categoryName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="Title" prop="title">
            <el-input v-model="ruleForm.title" size="small"></el-input>
          </el-form-item>
          <el-form-item label="Part" prop="part">
            <el-select v-model="ruleForm.part" size="small">
              <el-option value="A" label="A"></el-option>
              <el-option value="B" label="B"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Sort" prop="sortIndex" :rules="[{ required: true, message: '请输入排序值', trigger: 'input' }]">
            <el-input size="small" v-model="ruleForm.sortIndex" type="number"></el-input>
          </el-form-item>
          <el-form-item label="Type" prop="type">
            <el-radio-group v-model="ruleForm.type" size="small">
              <el-radio-button :label="1">Units</el-radio-button>
              <el-radio-button :label="2">Progress</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="International Student" prop="internationalStudent">
            <el-radio-group v-model="ruleForm.internationalStudent" size="small">
              <el-radio-button :label="0">N</el-radio-button>
              <el-radio-button :label="1">Y</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <!-- <el-form-item label="Units（Max）" prop="units">
            <el-input v-model="ruleForm.units"></el-input>
          </el-form-item> -->
          <el-form-item label="Content">
            <div
              id="websiteEditorElem"
              ref="websiteEditorElem"
              style="height: 350px; background: #ffffff"
            ></div>
          </el-form-item>
        </el-form>
        <div class="tarBU">
          <el-button size="small" class="tarhui" @click="dialogVisible=false">Cancel</el-button>
          <el-button size="small" type="primary" @click="addpushMajor('ruleForm')">Save</el-button>
        </div>
      </el-dialog>
    </div>
    <el-dialog
      title="Copy"
      :visible.sync="copyDialog.visible"
      width="40%"
    >
      <el-form
        :model="copyDialog.form"
        ref="copyForm"
        label-width="120px"
      >
        <el-form-item label="Admitted Year" prop="grade" :rules="[{ required: true, message: 'Please select', trigger: 'change' }]">
          <el-date-picker v-model="copyDialog.form.grade" placeholder="Admitted Year" size="small" value-format="yyyy" type="year" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="Major" prop="major" :rules="[{ required: true, message: 'Please select', trigger: 'change' }]">
          <el-select
              v-model="copyDialog.form.major"
              placeholder="Major"
              size="small"
              clearable
            >
              <el-option
                v-for="item in meiju"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              >
              </el-option>
            </el-select>
        </el-form-item>
      </el-form>
      <template slot="footer">
        <el-button size="small" @click="copyDialog.visible = false">Cancel</el-button>
        <el-button size="small" type="primary" :loading="copyDialog.loading" @click="handleCopyConfirm">Confirm</el-button>
      </template>
    </el-dialog>
    <div v-show="!addlistdata">
      <el-button
        class="kismar"
        icon="el-icon-arrow-left"
        @click="addlistdata = !addlistdata"
        size="small"
        round
      ></el-button>

      <tarnewpush :mydata="minmydata" @addpush="handleClick"></tarnewpush>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { templateList, categoryInsert, templateRemove, categoryDetail, categoryUpdate, categoryCopy } from "@/api/index"; //教师端接口
import E from 'wangeditor'
import tarnewpush from "./tarnewpush.vue"; //教师端接口
export default {
  props: {
    //列表数据
    mydata: {
      type: Object,
      default: {},
    },
    //枚举数据
    meiju: {
      type: Array,
      default: [],
    },
  },
  components: {
    tarnewpush,
  },
  data() {
    return {
      //切换子表格
      addlistdata: true,
      //新增编辑时表单的数据
      ruleForm: {
        id: "",
        grade: '',
        major: "",
        categoryCode: "",
        categoryName: "",
        title: "",
        content: "",
        units: "",
        part: "A",
        sortIndex: 0,
        type: 1,
      },
      //校验表单数据
      rules: {
        grade: [{ required: true, message: "请选择入学年份", trigger: "change" }],
        major: [{ required: true, message: "请选择专业", trigger: "change" }],
        categoryCode: [
          { required: true, message: "请输入课程分类编号", trigger: "blur" },
        ],
        categoryName: [
          { required: true, message: "请输入课程分类名称", trigger: "blur" },
        ],
        title: [
          { required: true, message: "请输入课程分类标题", trigger: "blur" },
        ],
        part: [
          { required: true, message: "请输入课程子目录", trigger: "blur" },
        ],
        type: [
          { required: true, message: "请选择课程类型", trigger: "blur" },
        ],
        internationalStudent: [
          { required: true, message: "请选择是否为国际生", trigger: "blur" },
        ],
      },
      //弹出框
      dialogVisible: false,
      //分页
      currentPage4: 4,
      formDar: {
        //输入框
        major: "",
        grade: '',
        //选择框
        templateKeyword: "",
      },
      //选择框枚举
      options: [],
      current: 1,
      size: 10,
      //选好的列表
      multipleSelection: [],
      //接受子列表
      minmydata: {},
      phoneEditor: null, //  富文本实例
      disableStatus: false,
      isView: false,
      copyDialog: {
        visible: false,
        loading: false,
        form: {
          id: '',
          grade: '',
          major: '',
        },
      },
    };
  },

  methods: {
    openDialog(){
      this.ruleForm = {
        id: "",
        major: "",
        categoryCode: "",
        categoryName: "",
        title: "",
        content: "",
        units: "",
        part: "A",
        grade: '',
        type: 1,
        internationalStudent: 0,
      }
      this.dialogVisible = true;
    },
    async handleView(data){
      let res = await categoryDetail(data.id);
      if(res.data){
        this.ruleForm.id = res.data.id;
        this.ruleForm.major = res.data.major;
        this.ruleForm.categoryCode = res.data.categoryCode;
        this.ruleForm.categoryName = res.data.categoryName;
        this.ruleForm.title = res.data.title;
        this.ruleForm.content = res.data.content;
        this.ruleForm.units = res.data.units;
        this.ruleForm.part = res.data.part || '';
        this.ruleForm.grade = res.data.grade || '';
        this.ruleForm.type = res.data.type;
        this.ruleForm.internationalStudent = res.data.internationalStudent;
        this.disableStatus = true;
        this.isView = true;
      }
      this.dialogVisible = true;
    },
    async handleEdit(data){
      let res = await categoryDetail(data.id);
      console.log(res);
      if(res.data){
        this.ruleForm.id = res.data.id;
        this.ruleForm.major = res.data.major;
        this.ruleForm.categoryCode = res.data.categoryCode;
        this.ruleForm.categoryName = res.data.categoryName;
        this.ruleForm.title = res.data.title;
        this.ruleForm.content = res.data.content;
        this.ruleForm.units = res.data.units;
        this.ruleForm.sortIndex = res.data.sortIndex;
        this.ruleForm.part = res.data.part || '';
        this.ruleForm.grade = res.data.grade || '';
        this.ruleForm.type = res.data.type;
        this.ruleForm.internationalStudent = res.data.internationalStudent;
        this.disableStatus = true;
      }
      this.dialogVisible = true;
    },
    handleCopy(data) {
      this.copyDialog.visible = true;
      this.copyDialog.loading = false;
      this.copyDialog.form = {
        id: data.id,
        grade: '',
        major: '',
      };
    },
    handleCopyConfirm() {
      this.copyDialog.loading = true;
      this.$refs.copyForm.validate(async (valid) => {
        if (valid) {
          const result = await categoryCopy(this.copyDialog.form);

          this.copyDialog.loading = false;
          if (result.data) {
            this.forpust();
            this.copyDialog.visible = false;
            this.$message({
              message: "复制成功",
              type: "success",
            });
          } else {
            this.$message.error(result.message);
          }
        } else {
          this.copyDialog.loading = false;
        }
      });
    },
    //详情
    async handleClick(data) {
      let datas = {
        // courseCategoryId: data.id,w
        condition:{courseCategoryId: data.id},
        current: data.current ? data.current : 1,
        size: data.size ? data.size : 10,
      };
      let res = await templateList(datas);
      res.data.courseCategoryId = data.id;
      res.data.categoryCode = data.categoryCode;
      this.minmydata = res.data;
      //  console.log(res,3321)
      this.addlistdata = false;
    },
    //删除列表
    async delData() {
      if (Object.keys(this.multipleSelection).length === 0) {
      } else {
        let data = [];
        let name = [];
        this.multipleSelection.forEach((red) => {
          data = [...data, red.id];
          name = [...name, red.categoryCode];
        });
        name = JSON.stringify(name);
        this.$confirm(`此操作将永久删除该信息${name}`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.pushdel(data);
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      }
    },
    async pushdel(data) {
      let datas = {
        ids: data,
      };
      let res = await templateRemove(datas);
      if (res.data) {
        this.$message({
          message: "删除成功",
          type: "success",
        });
        this.forpust();
      } else {
        this.$message.error(res.message);
      }
    },
    handlePreview() {
      if (this.formDar.major && this.formDar.grade) {
        window.localStorage.setItem('course-template-list-param', JSON.stringify({
          major: this.formDar.major,
          grade: this.formDar.grade,
        }))

        this.$router.push(`/preview?major=${this.formDar.major}&grade=${this.formDar.grade}`);
      }
    },
    //校验弹出框表单
    addpushMajor(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.pushData();
        } else {
          return false;
        }
      });
    },
    //新增表单
    async pushData() {
      let res = undefined
      if(this.ruleForm.id){
        this.ruleForm.content = this.phoneEditor.txt.html();
        res = await categoryUpdate(this.ruleForm);
      }else{
        res = await categoryInsert(this.ruleForm);
      }
      if (res.data == null) {
        this.$message.error(res.message);
      } else {
        this.$message({
          message: "保存成功",
          type: "success",
        });
        this.forpust();
        this.dialogVisible = false;
      }
    },
    //获取点击的是哪个列表
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    //获取点击的是哪个列表
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    //分页
    handleSizeChange(val) {
      this.size = val;
      this.forpust();
    },
    //分页
    handleCurrentChange(val) {
      this.current = val;
      this.forpust();
    },
    forpust() {
      let data = {
        condition: {
          templateKeyword: this.formDar.templateKeyword,
          major: this.formDar.major,
          grade: this.formDar.grade,
        },
        current: this.current,
        size: this.size,
        sorts: [
          {
            asc: true,
            field: "",
          },
        ],
      };
      this.$emit("addpush", data);
    },
    // 创建富文本
    foundE() {
      this.phoneEditor = new E(this.$refs.websiteEditorElem);
      this.phoneEditor.create();
      this.phoneEditor.txt.html(this.ruleForm.content);
      // 富文本内容
    },
    closeDialog(){
      this.beforeDestroy();
    },
    beforeDestroy() {
      // 销毁编辑器
      this.phoneEditor.destroy()
      this.phoneEditor = null
    }
  },
  mounted() {
    const params = JSON.parse(window.localStorage.getItem('course-template-list-param') || '{}');

    this.formDar.major = params.major || '';
    this.formDar.grade = params.grade || '';
  },
};
</script>

<style  scoped>
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
  font-family: "Book Antiqua";
}
.tarpic {
  padding: 15px;
  text-align: right;
  background: #fff;
}
.riColor:focus,.riColor:hover {
  background: #531659;
  color: #fff;
  font-family: "Book Antiqua";
}

.riColorchen {
  background: #e6a823;
  color: #000;
  font-family: "Book Antiqua";
}

.riColorchen:hover {
  background: #d29b23;
  color: #000;
  font-family: "Book Antiqua";
}

.riright {
  text-align: right;
}

.riButton .el-input {
  width: 200px;
  margin-right: 10px;
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

.riButton .el-select {
  width: 300px;
  margin-right: 10px;
  border-radius: 30px;
}

.riButton .el-select .el-input {
  width: 300px;
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
  font-family: "Book Antiqua";
}
.tarBU {
  text-align: center;
  padding: 20px 0;
}
.tarBU .el-button {
  margin-right: 20px;
}
.tarhui {
  background-color: rgba(194, 199, 204, 1);
  color: #fff;
  border: 1px solid rgba(194, 199, 204, 1);
}
.kismar {
  margin: 30px 0 10px 0;
}
.kismar:focus,.kismar:hover {
  background-color: #531659;
  color: #fff;
}
.el-dialog .el-select{
  width: 100%;
}
/* .w-e-text-container {
  height: 200px !important;
} */
</style>
<style >
.tarpic .el-pager li.active {
  color: #6a1d72;
}
</style>
