<template>
  <div class="inBei">
    <div class="effective" v-if="!isEffective"></div>
    <div class="inBei inover">
      <img class="inBj" src="./beijin.jpg" alt="" />
    </div>
    <div class="inLogin loshows" v-if="shows">
      <div class="inLogos">
        <img src="./logo.png" />
      </div>
      <el-form
        :model="fromname"
        :rules="rules"
        ref="fromname"
        label-width="200px"
        class="demo-ruleForm"
        label-position="left"
      >
        <el-form-item label="" prop="loginName" class="label-text">
          <label slot="label">Student I.D</label>
          <div>
            <span>{{ addrot(fromname.loginName) }}</span>
          </div>
        </el-form-item>
        <el-form-item label="" prop="major" class="label-text">
          <label slot="label">Major</label>
          <div>
            <el-select
              v-model="fromname.major"
              placeholder="Please select"
            >
              <el-option
                v-for="(item, index) in options"
                :key="index"
                :label="item.value"
                :value="item.value"
              ></el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="" prop="minor" class="label-text">
          <label slot="label">Minor(if applicable)</label>
          <div class="mg-30">
            <el-input v-model.trim="fromname.minor" >{{ addrot(fromname.minor)}}</el-input>
          </div>
        </el-form-item>
        <el-form-item label="" prop="contactTel" class="label-text">
          <label slot="label">Contact tel</label>
          <div class="mg-30">
            <el-input v-model.trim="fromname.contactTel" placeholder="">{{ addrot(fromname.contactTel)}}</el-input>
          </div>
        </el-form-item>
        <el-form-item label="" prop="expectedYear" class="label-text">
          <label slot="label">Expected Graduation Term</label>
          <div>
            <el-select v-model="fromname.expectedYear" size="small" style="width: 100%;" placeholder="">
              <el-option v-for="(year, key) of schoolYear" :key="key" :label="year" :value="key"></el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item v-if="isTestUser" label="" prop="grade" class="label-text">
          <label slot="label">Admitted Year</label>
          <div class="mg-30">
            <el-date-picker
              size="small"
              v-model="fromname.grade"
              type="year"
              value-format="yyyy"
              placeholder="Admitted Year">
            </el-date-picker>
            <!-- <el-input v-model.trim="fromname.grade" placeholder="">{{ addrot(fromname.grade)}}</el-input> -->
          </div>
        </el-form-item>
        <el-form-item label="International Student(Non-Chinese Nationality)" prop="internationalStudent" class="label-text">
          <div>
            <el-select v-model="fromname.internationalStudent" size="small" style="width: 100%;" placeholder="">
              <el-option label="No" :value="0"></el-option>
              <el-option label="Yes" :value="1"></el-option>
            </el-select>
          </div>
        </el-form-item>
        <div class="logbutts">
          <el-button :disabled="!isEffective" type="primary" @click="addstuck('fromname')">Submit</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { authgetUserInfo, payWayadd, codeMajor, getSchoolYear, getSystemConfig } from '@/api/index'
import { linkAdfsLogin as linkAdfsLoginApi, getAdfsUser, linkAdfsLogout } from "@/api/index";
import { studentSubmit } from "@/api/student";
import Cookies from 'js-cookie';
export default {
  data() {
    return {
      fromname: {
        loginName: '',
        major: '',
        minor: '',
        contactTel: '',
        expectedYear: '',
        grade: '',
        internationalStudent: null,
      },
      rules: {
        major: [{ required: true, message: '请选择专业', trigger: 'change' }],
        contactTel: [{ required: true, message: '请填写手机号', trigger: 'change' }],
        expectedYear: [
          { required: true, message: '请选择预计学期', trigger: 'change' },
        ],
        internationalStudent: [{ required: true, message: 'Please select', trigger: 'change' }],
      },
      keepState: false, //保持登录状态
      userType: 'STUDENT',
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      input: '',
      shows: false,
      value1: '',
      options: [],
      value: '',
      schoolYear: [],
      isEffective: true,
      /** 是否测试账号 */
      isTestUser: false,
    }
  },
  created() {
    console.log(Cookies.get('XSRF-TOKEN'));
    if (Cookies.get('XSRF-TOKEN')) {
      localStorage.setItem('XSRF-TOKEN', Cookies.get('XSRF-TOKEN'));
      this.getAdfsInfo();
    } else {
      // 跳转ADFS 登录
      linkAdfsLoginApi();
    }
    this.codeMajors();
  },
  methods: {
    getAdfsInfo() {
      this.adfsLoginToken().then(key => {
        if (key == 401) {
          linkAdfsLoginApi();
          return;
        }
        console.log(this.userType);
        if(this.userType != 'TEACHER'){
          this.shows = true;
        }
      });
    },
    adfsLoginToken() {
      return new Promise((resolve, reject) => {
        getAdfsUser().then(
          res => {
            if (res.code != 200) {
              console.log("登录失败");
              reject();
              return;
            }
            const { data } = res;
            localStorage.setItem('userType', data.userType || '');
            localStorage.setItem('name', data.name || '');
            localStorage.setItem('nameCh', data.nameCh || '');
            localStorage.setItem('nameEn', data.nameEn || '');
            localStorage.setItem('user-info', JSON.stringify(data));
            this.userType = data.userType || '';

            this.fromname.loginName = data.name || data.loginName || '';
            this.fromname.major = data.major || '';
            this.fromname.minor = data.minor || '';
            this.fromname.contactTel = data.contactTel || '';
            this.fromname.expectedYear = data.expectedYear || '';
            this.fromname.grade = data.grade;
            this.fromname.internationalStudent = data.internationalStudent !== null ? Number(data.internationalStudent) : null;

            this.isTestUser = data.studentId && data.studentId.indexOf('studenttest') > -1;

            if (this.userType !== 'TEACHER') {
              this.studentHint();
            } else {
              localStorage.setItem('admin', data.isAdmin || false);

              if (data.isTrustForTeacher) {
                this.$router.push({ path: this.redirect || '/list', query: this.otherQuery })
              } else {
                this.$alert('该教师账号无权限登录', 'Hint', {
                  confirmButtonText: 'Log out',
                  showClose: false,
                }).then(() => {
                  Cookies.remove('XSRF-TOKEN')
                  window.localStorage.clear()
                  linkAdfsLogout()
                });
              }
            }

            resolve();
          },
          () => {
            resolve(401);
          }
        );
      });
    },

    async addstuck(data) {
      if (![0, 1].includes(this.fromname.internationalStudent)) {
        this.$message.error('请选择你是否为非中国籍的国际生，中国籍学生请选”No”。Please select whether you are an international student (Non-Chinese Nationality)， if you are, please select "Yes".');
        return;
      }

      this.$refs[data].validate((valid) => {
        if (valid) {
          this.csadd()
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    async csadd() {
      let fromname = JSON.parse(JSON.stringify(this.fromname))
      let time = new Date(fromname.expectedYear)
      fromname.expectedYear = time.getFullYear()
      let res = await studentSubmit(fromname)
      if (res.data) {
        this.$router.push({ path: '/student' })
      } else {
        this.$message.error(res.message)
      }
    },
    //枚举
    async codeMajors() {
      let res = await codeMajor()
      this.options = res.data

      const result = await getSchoolYear();
      this.schoolYear = result.data;
    },
    async studentHint() {
      const result2 = await getSystemConfig();
      this.isEffective = result2.data.invalid !== true;
      if (!this.isEffective && result2.data.message) {
        // 不在有效区间，学生不能使用系统
        this.$alert(result2.data.message, 'Hint', {
          confirmButtonText: 'Log out',
          showClose: false,
        }).then(() => {
          Cookies.remove('XSRF-TOKEN')
          window.localStorage.clear()
          linkAdfsLogout()
        });
      }
    },
    //登录账号
    async addlogin() {
      let data = {
        loginName: this.input,
      }
      let res = await payWayadd(data)
      if ((res.code = 200)) {
        localStorage.setItem('nameEn', res.data.nameEn)
        localStorage.setItem('nameCh', res.data.nameCh)
      }
      console.log(res)
      res.data ? this.logins() : this.$message.error('账号有误')
    },
    //获取详情
    async logins() {
      if (this.keepState) {
        localStorage.setItem('usename', this.input)
      }

      let res = await authgetUserInfo()
      sessionStorage.setItem('user', res.data)
      if (res.data.userType == 'TEACHER') {
        this.$router.push({ path: '/Richtext' })
      } else {
        this.fromname = res.data
        this.shows = !this.shows
      }
    },
    //转null
    addrot(data) {
      return data === null ? (data = '') : data
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.inBei {
  width: 100%;
  height: 100%;
}
.inover {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}
.inBj {
  width: 100%;
  height: 100%;
}
.inLogin {
  position: absolute;
  top: 35%;
  left: 50%;
  width: 360px;
  padding: 20px;
  background: #fff;
  border-radius: 6px;
  transform: translate(-50%, -50%);
}
.ininput {
  position: relative;
}
.ininput .iconleft {
  position: absolute;
  top: 17px;
  left: 10px;
  color: #606266;
  font-size: 20px;
}
.iconright {
  position: absolute;
  top: 17px;
  right: 10px;
  color: #606266;
  font-size: 20px;
}

.inbutton {
  padding: 10px;
  margin-top: 40px;
}
.inbutton .el-button {
  width: 100%;
  height: 60px;
  background: #3a62d7;
  font-size: 15px;
  font-weight: 600;
}
.inbutton .el-button:hover {
  background: #284fc1;
  font-size: 15px;
  font-weight: 600;
}

.indiv .inpovie {
  position: absolute;
  right: 33px;
  text-align: right;
  /* top:0; */
}

.inpaddtop {
  padding-top: 5px;
}

.insend {
  position: absolute;
  right: 10px;
  color: #5995ea;
  top: 20px;
  width: 100px !important;
}
.inLogo {
  margin-bottom: 20px;
}
.inLogo img {
  display: block;
  margin: 0 auto;
  height: 50px;
}
.role-text {
  color: #909399;
  margin: 20px 0;
  line-height: 25px;
  font-size: 13px;
}
.role-text span {
  font-style: oblique;
}

.prompt-Text p {
  font-size: 14px;
  color: #909399;
}
.prompt-Text p span {
  cursor: pointer;
  color: #3a8ee6;
}
.user-help {
  margin: 10px 0;
}
.user-help span {
  font-size: 14px;
  cursor: pointer;
  color: #3a8ee6;
  padding-right: 10px;
}
.next-step {
  float: right;
  background-color: #943ae6;
  color: #fff;
}
.mg-10 {
  margin: 0 10%;
}
.label-text {
  color: darkgray;
}
.logTxt {
  font-size: 18px;
  padding: 10px 0;
}
.prompt-Text span {
  font-weight: 600;
  margin: 0 4px;
}
.user-help span {
  font-weight: 600;
  margin: 0 4px;
}
.inLogos {
  text-align: center;
}
.inLogos img {
  width: 300px;
  margin-bottom: 20px;
}
.logbutts {
  text-align: center;
}
.logbutts .el-button {
  width: 300px;
  font-size: 17px;
  margin-top: 30px;
  background-color: rgba(58, 98, 215, 1);
}
</style>
<style >
.ininput input {
  /* text-indent: 2em; */
  border: none;
  border-radius: 0px;
  height: 60px;
  font-size: 17px;
  box-shadow: none;
  border-bottom: 1px solid #efeff0;
}
.indiv .el-input--prefix .el-input__inner {
  padding-left: 0;
  height: 34px;
  min-width: 220px;
  text-indent: 2em;
  border: none;
  border-radius: 0px;
  box-shadow: none;
  border-bottom: 1px solid #efeff0;
}
.incode .el-input__inner {
  padding: 0;
  width: 60px;
  height: 34px;
  border: none;
  border-radius: 0px;
  box-shadow: none;
  border-bottom: 1px solid #efeff0;
}
.indiv .inpovie .el-input__inner {
  width: 150px;
  height: 34px;
  border: none;
  border-radius: 0px;
  box-shadow: none;
  border-bottom: 1px solid #efeff0;
}
.incdiv .el-input__inner {
  padding: 0;
  width: 60px;
  margin-left: 6px;
  height: 34px;
  border: none;
  border-radius: 0px;
  box-shadow: none;
  border-bottom: 1px solid #efeff0;
}
.incdiv .el-input__icon {
  display: none;
}
.loshows .el-form-item {
  margin-bottom: 17px;
}
.loshows .el-form-item__error {
  left: 60px;
}
.loshows .el-date-editor.el-input,.loshows .el-date-editor.el-input__inner {
  width: 100%;
}
.loshows .el-input--suffix .el-input__inner {
    padding-right: 35px;
}
.demo-ruleForm .el-select {
  width: 100% !important;
}
.effective {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 888;
  background: #000;
  opacity: 0.2;
}
</style>
