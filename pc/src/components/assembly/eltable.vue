<template>
  <div class="wrap">
    <table class="hovertable">
      <thead>
        <tr>
          <th style="width: 80px">
            <!--
            <i
              v-if="[0, 2].includes(isStatus) || isStatus == null"
              class="el-icon-circle-plus-outline"
              style="font-size: 20px; cursor: pointer"
              @click="add"
            />
            -->
            <!-- <span v-if="!([0, 2].includes(isStatus) || isStatus == null)"></span> -->
            <template v-if="currentCode == 'Free Elective' && [0, 2].includes(isStatus)">
              <el-tooltip effect="dark" content="Click the button to add the corresponding courses" placement="top">
                <el-button @click="add" icon="el-icon-circle-plus-outline" size="small" type="text" style="color: #fff;">Add</el-button>
              </el-tooltip>
            </template>
            <span v-else>No</span>
          </th>
          <th
            v-for="(col, index) in trData"
            :key="index"
            v-html="col.text"
            :style="{ width: col.width + 'px' }"
          />
        </tr>
      </thead>

      <tbody>
        <tr v-for="(item, trIndex) in tempTableData" :key="trIndex">
          <td>
            <!-- <div class="operation" v-if="[0, 2].includes(isStatus) || isStatus == null"> -->
            <div class="operation" v-if="currentCode == 'Free Elective' && [0, 2].includes(isStatus)">
              <i class="el-icon-circle-plus-outline" @click="add"/>
              <i class="el-icon-remove-outline" @click="reduce(trIndex)"/>
            </div>
            <span v-else>{{ trIndex + 1 }}</span>
          </td>
          <td v-for="(it, tdIndex) in trData" :key="tdIndex">
            <template v-if="it.type == 'courseCode'">
              <!-- v-if="hasCurrentInx" -->
              <el-input
                v-model="item['courseCode']"
                :disabled="hasCurrentInx"
              ></el-input>
              <!-- <course-code-input v-else v-model="item['courseCode']" /> -->
            </template>
            <el-input
              v-if="it.type == 'courseTitle'"
              v-model="item['courseTitle']"
              :disabled="hasCurrentInx"
              clearable
            />
            <el-input-number
              v-model="item['units']"
              controls-position="right"
              :min="0"
              :max="item['max']"
              v-if="it.type == 'units'"
              @change="setPageNum(trIndex, 'units')"
              :disabled="stepCode != 'Free Elective' || [1, 3].includes(isStatus)"
            />
            <el-select
              v-model="item['selfCheck']"
              placeholder="Please select"
              v-if="it.type == 'selfCheck'"
              @change="setPageNum(trIndex, 'selfCheck')"
              :disabled="[1, 3].includes(isStatus) || limitCodes.includes(item.courseCode)"
              clearable
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
            <el-select
              v-model="item['progress']"
              placeholder="Please select"
              v-if="it.type == 'progress'"
              @change="setPageNum(trIndex, 'progress')"
              :disabled="[1, 3].includes(isStatus)"
              clearable
            >
              <el-option
                v-for="item in progressOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
            <div v-if="it.type == 'minor'" class="el-input" :class="{ 'is-disabled': [1, 3].includes(isStatus) || limitCodes.includes(item.courseCode) }">
              <input
                class="l-input el-input__inner"
                v-model="item['minor']"
                :disabled="[1, 3].includes(isStatus) || limitCodes.includes(item.courseCode)"
                clearable
              />  
            </div>
            <el-input
              v-if="it.type == 'remark'"
              v-model="item['remark']"
              :disabled="[1, 3].includes(isStatus) || limitCodes.includes(item.courseCode) || progressVisible"
              clearable
            />
          </td>
        </tr>
        <slot name="thBottom" />
      </tbody>
    </table>
    <slot />
  </div>
</template>
<script>
import vuex from 'vuex'
import CourseCodeInput from './course-code-input';

export default {
  name: 'puretable',
  components: {
    CourseCodeInput,
  },
  props: {
    tdData: {
      type: Array,
      default: () => {
        return []
      },
    },
    trData: {
      type: Array,
      default: () => {
        return []
      },
    },
    tableData: {
      type: Array,
      default: () => {
        return []
      },
    },
    coursePart: {
      type: String,
      default: () => {
        return ''
      },
    },
    couresList: {
      type: Array,
      default: () => {
        return []
      },
    },
    isStatus: {
      type: Number,
      default: 0,
    },
    stepCode: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      currentCode: 'Note',
      tempTableData: [],
      codeVal: '',
      titleVal: '',
      rowData: [],
      options: [
        {
          value: '0',
          label: '/',
        },
        {
          value: '1',
          label: '✔',
        },
        {
          value: '2',
          label: 'NR',
        },
        {
          value: '3',
          label: 'IP',
        },
      ],
      progressOptions: [
        {
          value: 1,
          label: '已完成修读',
        },
        {
          value: 2,
          label: '正在修读',
        },
        {
          value: 3,
          label: '未修读',
        },
      ],
      value: '',
    }
  },
  watch: {
    tableData: {
      deep: true,
      immediate: true,
      handler(newVal) {
        this.tempTableData = newVal
      },
    },
    isExceedTotalScore() {
      const field = this.templateField.field;
      const index = this.templateField.index;

      if (this.isExceedTotalScore && field) {
        this.tempTableData[index][field] = '0';
        this.$emit('getTotalNum', this.tempTableData, 'reduce', this.coursePart)
        setTimeout(() => {
          this.$store.dispatch('clear');
        }, 100);
      }
    }
  },
  created() {
    this.addRowData()
    this.getCurrentInx()
    this.initDataCode();
  },
  computed: {
    ...vuex.mapState(['partHaveValA', 'partHaveValB']),
    hasCurrentInx: function () {
      if (this.currentCode == 'Free Elective' && [0, 2].includes(this.isStatus)) {
        return false
      } else {
        return true
      }
    },
    isExceedTotalScore() {
      return this.$store.state.isExceedTotalScore;
    },
    templateField() {
      return this.$store.state.templateField;
    },
    limitCodes() {
      if (this.coursePart === 'A') {
        return this.partHaveValB;
      } else if (this.coursePart === 'B') {
        return this.partHaveValA;
      } else {
        return [];
      }
    },
    /** 是否展示进度列 */
    progressVisible() {
      return this.trData.some((tr) => tr.type === 'progress');
    },
  },
  mounted() {},
  methods: {
    initDataCode() {
      if (this.coursePart) {
        const data = {
          attribute: this.coursePart === 'A' ? 'partHaveValA' : 'partHaveValB',
          codes: this.tempTableData.filter(x => x.selfCheck > 0).map(x => x.courseCode),
        };
        this.$store.dispatch('initPartHaveVal', data);
      }
    },
    getCurrentInx() {
      this.currentCode = localStorage.getItem('current-code')
    },
    handleChangeCourse(code, rowIndex) {
      let index = this.couresList.findIndex((v) => code == v.courseCode) || 0
      this.tempTableData[rowIndex].courseTitle =
        this.couresList[index].courseTitle
      this.tempTableData[rowIndex].units = this.couresList[index].units
      this.tempTableData[rowIndex].max = this.couresList[index].units
      this.$set(
        this,
        'tempTableData',
        JSON.parse(JSON.stringify(this.tempTableData))
      )
      this.$emit('getTotalNum', this.tempTableData, 'reduce', this.coursePart)
    },
    setPageNum(index, field) {
      this.$store.dispatch('setTemplateField', { index, field });
      this.$emit('getTotalNum', this.tempTableData, 'reduce', this.coursePart)

      if (this.coursePart) {
        const data = {
          attribute: this.coursePart === 'A' ? 'partHaveValA' : 'partHaveValB',
          type: this.tempTableData[index][field] > 0 ? 'push' : 'reduce',
          code: this.tempTableData[index].courseCode,
        };
        this.$store.dispatch('setPartHaveVal', data);
      }
    },
    initTable() {
      if (this.tempTableData.length === 0) {
        this.add();
        this.add();
        this.add();
      }
    },
    add() {
      let params = {
        courseCategoryCode: '',
        courseTemplateId: '',
        courseCode: '',
        courseTitle: '',
        units: 0,
        selfCheck: '',
        frontShow: 0,
        minor: '',
        remark: '',
        max: 9999,
        coursePart: this.coursePart !== '' ? this.coursePart : '',
      }
      this.tempTableData.push(params)
      this.$emit('getTotalNum', this.tempTableData, 'add', this.coursePart)
    },
    reduce(index) {
      this.tempTableData.splice(index, 1)
      this.$emit('getTotalNum', this.tempTableData, 'reduce', this.coursePart)
    },
    addRowData() {},
  },
}
</script>
<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  border-spacing: 0;
}

td,
th {
  padding: 0;
}

table.hovertable {
  text-align: center;
  width: 100%;
  font-family: verdana, arial, sans-serif;
  font-size: 11px;
  color: #333333;
  border-width: 1px;
  border-color: #999999;
  border-collapse: collapse;
}
table.hovertable th {
  background-color: #6a1d72;
  border-width: 1px;
  padding: 8px;
  border-right: 1px solid rgb(201, 201, 201);
  color: #fff;
}
table.hovertable thead tr {
  background-color: #6a1d72;
  border: 1px solid rgb(201, 201, 201);
  border-top: 0px solid rgb(201, 201, 201);
}
table.hovertable tbody tr {
  background-color: #fff;
  border: 1px solid rgb(201, 201, 201);
  border-top: 0px solid rgb(201, 201, 201);
}
table.hovertable td {
  border-width: 1px;
  padding: 6px;
  border-right: 1px solid rgb(201 201 201);
  min-height: 20px;
  height: 20px;
}
.operation {
  display: flex;
}
.operation i {
  flex: 1;
  font-size: 20px;
  cursor: pointer;
}
.elzong {
  text-align: right;
  font-weight: 600;
  margin: 30px 10px;
  width: 100%;
}
.elzong > div {
  margin-bottom: 10px;
}
.elad {
  width: 100px;
  border-bottom: 1px solid #939393;
  display: inline-block;
}
</style>
<style>
.el-input__inner {
  color: #333 !important;
}
.classObject {
  border: 1px solid red;
  color: red;
  border-radius: 5px;
}
.classObject .el-select .el-input.is-focus .el-input__inner {
  border-color: transparent;
}
.l-input {

}
</style>
