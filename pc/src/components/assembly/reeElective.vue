<template>
  <div class="stPadd">
    <div class="noName">
      <div class="title text-color" v-html="currentDetail.title"></div>
      <div class="content" v-html="currentDetail.content"></div>
      <!-- <div class="text-color">自由选修 Free Elective (18 units)</div>
      <div>
        自由选修包括以下课程类型Units of free elective can be consist of:
      </div>
      <div>
        1)
        不在主修专业修读计划的课程（例：ENG3001，PED2002，FRN1001，MAT4001等）；
      </div>
      <p>
        Course(s) not listed in the major programme requirement of study scheme
        (e.g., ENG3001, PED2002, FRN1001, MAT4001 etc.);
      </p>
      <div>
        2)
        超过专业选修学分要求的课程（例：专业选修学分要求15学分，但是已获得18学分，那么超出的任意3学分计为自由选修学分）；
      </div>
      <p>
        Course(s) beyond the requirement of major elective (e.g., major elective
        requires 15 units while 18 units obtained, the 3 units will be
        considered as free elective);
      </p>
      <div>
        3)
        GEA/B/C/D额外修读的课程（例：GEB要求3学分，但是已获得9学分，那么超出的6学分计为自由选修学分）；
      </div>
      <p>
        Extra course(s) from GEA/B/C/D area (e.g., GEB requires 3 units while 9
        units of GEB obtained, the 6 units will be considered as free elective).
      </p> -->
    </div>
    <eltable
      ref="eltable"
      :tdData="tdData"
      :trData="trData"
      :tableData="tableData"
      :isStatus="isStatus"
      :couresList="couresList"
      :stepCode="stepCode"
      @getTotalNum="getTotalNum"
    >
      <template slot="thBottom">
        <tr>
          <td colspan="3" rowspan="2">
            Units of Credit Transfer for Free Elective – non-equivalent course
          </td>
          <td>     
            <el-input-number 
            :min="0"
            :step="0.5"
            v-model="passedNumA"
            @change="getTotalNum"
            controls-position="right"
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
            :min="0"
            :step="0.5"
            v-model="passedNumB"
            @change="getTotalNum('passedNumB')"
            controls-position="right"
            :disabled="[1, 3].includes(isStatus)"
            />
          </td>
          <td>IP</td>
        </tr>
        <tr>
          <td colspan="3" rowspan="2">
            <!-- Units of Credit Transfer for Major Elective Courses – non-equivalent course (if applicable) -->
            Units of Credit Transfer for Minor Electives – non-equivalent course(if applicable)
          </td>
          <td>            
            <el-input-number 
            :min="0"
            :step="0.5"
            v-model="passedNumC"
            @change="getTotalNum"
            controls-position="right"
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
            :min="0"
            :step="0.5"
            v-model="passedNumD"
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
          Total units of passed and not-released free electives:
          <div class="elad">{{ totalUnitsPassed == 0 ? '' : totalUnitsPassed }}</div>
        </div>
        <div>
          Total units of in-process free electives:
          <div class="elad">{{ totalUnitsInProcess == 0 ? '' : totalUnitsInProcess }}</div>
        </div>
      </div>
    </eltable>
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
    tableData: {
      type: Array,
      default: [],
    },
    totalUnits: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsPassed: {
      type: [Number, String],
      default: () => {
        return 0
      },
    },
    totalUnitsInProcess: {
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
    stepCode: {
      type: String,
      default: '',
    },
    currentDetail:{
      type: Object,
      default: {},
    },
    couresListMerge: {
      type: Array,
      default: [],
    },
  },
  components: { eltable },
  watch: {
    couresListMerge: {
      immediate: true,
      deep: true,
      handler(val) {
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
  computed: {
    isExceedTotalScore() {
      return this.$store.state.isExceedTotalScore;
    },
    passedNumField() {
      return this.$store.state.passedNumField;
    },
  },
  data() {
    return {
      totalNum: '',
      totalReally: {
        val: '',
      },
      passedNumA: 0,
      passedNumB: 0,
      passedNumC: 0,
      passedNumD: 0
    }
  },
  methods: {
    getTotalNum(field) {
      if (['passedNumB', 'passedNumD'].includes(field)) {
        this.$store.dispatch('setPassedNumField', field);
      }

      const couresList = this.couresListMerge || [];

      if (couresList.length < 4) {
        return;
      }

      couresList[0].units = this.passedNumA;
      couresList[1].units = this.passedNumB;
      couresList[2].units = this.passedNumC;
      couresList[3].units = this.passedNumD;

      this.$emit('special', couresList);
    },
    addLineThree() {
      this.$refs.eltable.initTable();
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
.elad {
  text-align: center;
}
</style>
