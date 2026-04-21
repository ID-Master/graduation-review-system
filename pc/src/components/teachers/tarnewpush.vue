<template>
  <div>
    <div>
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
      </div>
      <div>
        <el-table
          ref="multipleTable"
          :data="mydata.records"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="60px"> </el-table-column>
          <el-table-column
            label="Course Code"
            prop="courseCode"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="courseTitle"
            label="Course Title"
            align="center"
          >
          </el-table-column>
          <!-- <el-table-column prop="type" label="Type" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.type == 1" type="success">Units</el-tag>
                <el-tag v-if="scope.row.type == 2">Progress</el-tag>
              </template>
          </el-table-column> -->
          <!-- <el-table-column prop="part" label="Part" align="center"
                           width="120px"
          > -->
          <!-- </el-table-column> -->
          <el-table-column prop="units" label="Units" align="center" min-width="100px"></el-table-column>
          <el-table-column prop="remark" label="Remark" align="center"></el-table-column>
          <!-- <el-table-column prop="sortIndex" label="Sort" align="center"
                           width="50px"
          >
          </el-table-column> -->
          <!-- <el-table-column
            prop="updateNameEn"
            label="Update Name"
            width="250px"
            align="center"
            sortable
          >
            <template slot-scope="scope">
              <span class="font-decoration">
                {{scope.row.updateNameEn ? scope.row.updateNameEn : '-'}}
              </span>
            </template>
          </el-table-column> -->
          <el-table-column
            prop="createdDate"
            label="Update Time"
            align="center"
            min-width="180px"
          >
          </el-table-column>
          <el-table-column
            fixed="right"
            label="Operations"
            width="120">
            <template slot-scope="scope">
              <el-button @click="handleEdit(scope.row)" type="text" size="small" icon="el-icon-edit">Edit</el-button>
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
        width="60%"
      >
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          label-width="140px"
          class="demo-ruleForm"
        >
          <el-form-item label="Course Code" prop="courseCode">
            <!--  @keyup.native="btKeyUp" -->
            <el-input v-model="ruleForm.courseCode" size="small"></el-input>
          </el-form-item>
          <el-form-item label="Course Title" prop="courseTitle">
            <el-input v-model="ruleForm.courseTitle" size="small"></el-input>
          </el-form-item>
          <!--
          <el-form-item label="Part" prop="part">
            <el-select
              v-model="ruleForm.part"
              placeholder="A"
              clearable
              style="width:100%"
            >
              <el-option
                v-for="item in partList"
                :key="item.value"
                :label="item.value"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          -->
          <!-- <el-form-item label="Type" prop="type">
            <el-radio-group v-model="ruleForm.type" size="small">
              <el-radio-button :label="1">Units</el-radio-button>
              <el-radio-button :label="2">Progress</el-radio-button>
            </el-radio-group>
          </el-form-item> -->
          <el-form-item label="Units" prop="units">
            <el-input
              v-model.number="ruleForm.units"
              type="number"
              size="small"
            ></el-input>
          </el-form-item>
          <el-form-item label="Remark" prop="remark">
            <el-input v-model.number="ruleForm.remark" size="small" ></el-input>
          </el-form-item>
          <!-- <el-form-item label="Sort" prop="sortIndex">
            <el-input
              v-model.number="ruleForm.sortIndex"
              type="number"
              :min="0"
            ></el-input>
          </el-form-item> -->
        </el-form>
        <div class="tarBU">
          <el-button size="small" class="tarhui" @click="dialogVisible=false">Cancel</el-button>
          <el-button size="small" type="primary" @click="addpushMajor('ruleForm')">Save</el-button>
        </div>
      </el-dialog>
    </div>
    <div></div>
  </div>
</template>

<script type="text/ecmascript-6">
import { templateInsert, templateRemoves,templateDetail,templateUpdate } from "@/api/index"; //教师端接口
export default {
  props: {
    // //列表数据
    mydata: {
      type: Object,
      default: {},
    },
  },

  data() {
    return {
      //新增编辑时表单的数据
      ruleForm: {
        id: undefined,
        courseCode: "",
        courseTitle: "",
        part: "A",
        units: 0,
        sortIndex: 0,
        type: 1,
        remark: '',
      },
      //校验表单数据
      rules: {
        courseCode: [
          { required: true, message: "Please enter the course code", trigger: "blur" },
        ],
        courseTitle: [
          { required: true, message: "Please enter the course title", trigger: "blur" },
        ],
      },
      //弹出框
      dialogVisible: false,
      //分页
      currentPage4: 4,
      formDar: {
        //输入框
        input: "",
        //选择框
        value: "",
      },
      //选择框枚举
      options: [],
      current: 1,
      size: 10,
      //选好的列表
      multipleSelection: [],
      partList: [
        {
          value: 'A',
        },
        {
          value: 'B',
        },
      ],
    };
  },

  methods: {
    async handleClick(data) {},
    //删除列表
    async delData() {
      if (Object.keys(this.multipleSelection).length === 0) {
      } else {
        let data = [];
        let name = [];
        this.multipleSelection.forEach((red) => {
          data = [...data, red.id];
          name = [...name, red.courseCode];
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
      let res = await templateRemoves(datas);
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
      let data = {
        ...this.ruleForm,
        courseCategoryId: this.mydata.courseCategoryId,
      };
      console.log('---->',this.ruleForm.id);
      if(this.ruleForm.id){
        let res = await templateUpdate(data);
        if (res.data == null) {
          this.$message.error(res.message);
        }
      }else{
        let res = await templateInsert(data);
        if (res.data == null) {
          this.$message.error(res.message);
        }
      }
      this.$message({
        message: "操作成功！",
        type: "success",
      });
      this.dialogVisible = false;
      this.forpust();
    },
    openDialog(){
      this.ruleForm ={
        courseCode: "",
        courseTitle: "",
        part: "A",
        units: 0,
        sortIndex: 0,
        type: 1,
      }
      this.dialogVisible = true;
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
      let data = this.mydata;
      data.size = this.size;
      data.current = this.current;
      data.id = this.mydata.courseCategoryId;
      this.$emit("addpush", data);
    },
    btKeyUp(e) {
      this.ruleForm.courseCode = e.target.value.replace(/[\u4e00-\u9fa5/\s+/]|[`~!@#$%^&()\+=<>?"{}|,\/;'\\[\]·~！@#￥%……&（）——\+={}|《》？：“”【】、；‘’，。、]/g,"");
    },
    async handleEdit(data){
      let res = await templateDetail(data.id);
      console.log(res);
      if(res.data){
        this.ruleForm.id = res.data.id;
        this.ruleForm.courseCode = res.data.courseCode;
        this.ruleForm.courseTitle = res.data.courseTitle;
        this.ruleForm.part = res.data.part;
        this.ruleForm.units = res.data.units;
        this.ruleForm.sortIndex = res.data.sortIndex;
        this.ruleForm.type = res.data.type;
        this.ruleForm.remark = res.data.remark;
      }
      this.dialogVisible = true;
    },
  },
  mounted() {},
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

.el-select {
  width: 300px;
  margin-right: 10px;
  border-radius: 30px;
}

.el-select .el-input {
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
</style>
<style >
.tarpic .el-pager li.active {
  color: #6a1d72;
}
</style>
