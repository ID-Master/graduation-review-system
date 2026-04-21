<template>
  <div class="stPadd">
    <div class="noName">
      <template v-if="tableDataB.length > 0">
        <div v-if="currentDetail.categoryName" class="title text-color" v-text="currentDetail.categoryName"></div>
        <div v-if="currentDetail.content" class="content" v-html="currentDetail.content"></div>
        <div v-if="currentDetail.title" class="title text-color" v-text="currentDetail.title"></div>
      </template>
      <template v-else>
        <div v-if="currentDetail.categoryName" class="title text-color" v-text="currentDetail.categoryName"></div>
        <div v-if="currentDetail.content" class="content" v-html="currentDetail.content"></div>
      </template>
      <!-- <div class="text-color">
        专业选修科目 Major Elective Courses (18 units)
      </div>
      <div>Note备注:</div>
      <div>
        1)
        在这部分，可选择任意已修读且成绩合格以上的课程作为专业选修课程来满足专业选修的学分要求。超过专业选修学分要求的课程可被记为自由选修课程（例：专业选修学分要求15学分，但是已获得
        18 学分，那么超出的任意3学分计为自由选修学分）。
      </div>
      <p>
        For this part, please select any course that you passed as major
        elective courses to satisfy the credit requirement. Course(s) beyond the
        requirement of major elective can be recorded as free elective courses
        (e.g., major elective requires 15 units while 18 units obtained, the 3
        units will be considered as free elective).
      </p>
      <div>2) 此表格中课程属性的划分不影响主修科目积点的计算。</div>
      <p>
        The division of courses category in this form has no influence on the
        calculation of major GPA.
      </p>
      <div>3) 请把超出专业选修学分要求的课程填在自由选修的课程方框内。</div>
      <p>
        Please remark the course(s) which beyond the major elective requirement
        in the box of free elective courses below.
      </p> -->
    </div>
    <div>
      <eltable
        :tdData="tdData"
        :trData="trData"
        :tableData="tableDataA"
        coursePart="A"
        :isStatus="isStatus"
        :couresList="couresListA"
        @getTotalNum="getTotalNum"
      >
        <template slot="thBottom">
          <tr>
            <td colspan="3" rowspan="2">
              Units of Credit Transfer for Major Elective Courses –
              non-equivalent course (if applicable)
            </td>
            <td >       
              <el-input-number 
              v-model="passedNumA"
              controls-position="right"
              @change="getTotalNum"
              :min="0"
              :step="0.5"
              :disabled="[1, 3].includes(isStatus)"
              />
            </td>
            <td>Passed (√)</td>
            <td colspan="3" rowspan="2">
              *“IP” means that you have already submitted the credit transfer
              applications to schools but under process.
            </td>
          </tr>
          <tr>
             <td>       
              <el-input-number 
              v-model="passedNumB"
              :min="0"
              :step="0.5"
              controls-position="right"
              @change="getTotalNum('passedNumB')"
              :disabled="[1, 3].includes(isStatus)"
              />
            </td>
            <td>IP</td>
          </tr>
        </template>
        <div class="elzong">
          <div>
            Total units of passed and not-released major electives:
            <div class="elad">{{ totalUnitsPassedA == 0 ? '' : totalUnitsPassedA }}</div>
          </div>
          <div>
            Total units of in-process major electives:
            <div class="elad">{{ totalUnitsInProcessA == 0 ? '' : totalUnitsInProcessA }}</div>
          </div>
        </div>
      </eltable>
    </div>
    <div v-if="currentDetailB.title" class="title text-color" v-html="currentDetailB.title"></div>
    <div v-if="currentDetailB.content" class="content" v-html="currentDetailB.content"></div>
    <div v-if="tableDataB.length > 0">
      <eltable
        :tdData="tdData"
        :trData="trData"
        :tableData="tableDataB"
        coursePart="B"
        :couresList="couresListB"
        @getTotalNum="getTotalNum"
        :isStatus="isStatus"
      >
        <template slot="thBottom">
          <tr>
            <td colspan="3" rowspan="2">
              Units of Credit Transfer for Major Elective Courses –
              non-equivalent course (if applicable)
            </td>
            <td>
              <el-input-number 
              v-model="passedNumC"
              @change="getTotalNum"
              controls-position="right"
              :min="0"
              :step="0.5"
              :disabled="[1, 3].includes(isStatus)"
              />
            </td>
            <td>Passed (√)</td>
            <td colspan="3" rowspan="2">
              *“IP” means that you have already submitted the credit transfer
              applications to schools but under process.
            </td>
          </tr>
          <tr>
            <td>
              <el-input-number
              v-model="passedNumD"
              :min="0"
              :step="0.5"
              @change="getTotalNum('passedNumD')"
              controls-position="right"
              :disabled="[1, 3].includes(isStatus)"
              />
            </td>
            <td>IP</td>
          </tr>
        </template>
        <div class="elzong">
          <div>
            Total units of passed and not-released major electives:
            <div class="elad">{{ totalUnitsPassedB == 0 ? '' : totalUnitsPassedB }}</div>
          </div>
          <div>
            Total units of in-process major electives:
            <div class="elad">{{ totalUnitsInProcessB == 0 ? '' : totalUnitsInProcessB }}</div>
          </div>
        </div>
      </eltable>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import eltable from './eltable.vue'
export default {
  props: {
    //总数
    valueTotal: {
      type: String,
      default: '1',
    },
    tdData: {
      type: [Array, Object],
      default: () => {
        return []
      },
    },
    trData: {
      type: [Array, Object],
      default: () => {
        return []
      },
    },
    tableDataA: {
      type: Array,
      default: [],
    },
    tableDataB: {
      type: Array,
      default: [],
    },
    totalUnitsA: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsB: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsPassedA: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsPassedB: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsInProcessA: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsInProcessB: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    isStatus: {
      type: Number,
      default: 0,
    },
    couresList: {
      type: Array,
      default: [],
    },
    couresListA: {
      type: Array,
      default: [],
    },
    couresListMerge: {
      type: Array,
      default: [],
    },
    couresListB: {
      type: Array,
      default: [],
    },
    currentDetail:{
      type: Object,
      default: {},
    },
    currentDetailB:{
      type: Object,
      default: {},
    },
  },
  components: { eltable },
  watch: {
    couresListMerge: {
      immediate: true,
      deep: true,
      handler(val, oldVal) {
        // && val.map(x => x.units).join('-') !==  oldVal.map(x => x.units).join('-')
        if (val.length === 4) {
          this.passedNumA = val[0].units;
          this.passedNumB = val[1].units;
          this.passedNumC = val[2].units;
          this.passedNumD = val[3].units;
          setTimeout(() => {
            this.getTotalNum();
          }, 800);
        }
      },
    },
    isExceedTotalScore() {
      if (this.isExceedTotalScore && ['passedNumB', 'passedNumD'].includes(this.passedNumField)) {
        this[this.passedNumField] = 0;
        this.getTotalNum();
        setTimeout(() => {
          this.$store.dispatch('clear');
        }, 100);
      }
    }
  },
  data() {
    return {
      totalNumA: '',
      totalNumB: '',
      totalReally: {
        valA: '',
        valB: '',
      },
      nums: 1,
      passedNumA: 0,
      passedNumB: 0,
      passedNumC: 0,
      passedNumD: 0
    }
  },
  computed: {
    isExceedTotalScore() {
      return this.$store.state.isExceedTotalScore;
    },
    passedNumField() {
      return this.$store.state.passedNumField;
    },
  },
  methods: {
    getTotalNum(field) {
      if (['passedNumB', 'passedNumD'].includes(field)) {
        this.$store.dispatch('setPassedNumField', field);
      }

      const couresList = this.couresListMerge;
      couresList[0].units = this.passedNumA;
      couresList[1].units = this.passedNumB;
      couresList[2].units = this.passedNumC;
      couresList[3].units = this.passedNumD;

      this.$emit('special', couresList);
    },
  },
  mounted() {},
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
  line-height: 20px;
  margin-left: 28px;
}
.noName {
  text-align: left;
}
.noName div {
  margin-bottom: 0;
}
.maNme {
  font-weight: 600;
  margin-bottom: 0;
}
.elad {
  text-align: center;
}
.title {
  padding: 6px 0;
}
</style>
