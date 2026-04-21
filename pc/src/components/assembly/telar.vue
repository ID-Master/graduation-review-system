<template>
  <div class="stPadd">
    <div class="noName">
      <div class="title text-color" v-html="currentDetail.title"></div>
      <div class="content" v-html="currentDetail.content"></div>
      <!-- <div>Note 备注:</div>
      <div>
        1）GEA/B/C/D额外修读的课程将被记为自由选修课程（例：GEB要求3学分，但是已获得9学分，那么超出的6学分计为自由选修学分）。
      </div>
      <p>
        Extra course(s) from GEA/B/C/D area will be recorded as free elective
        courses. (e.g., GEB requires 3 units while 9 units of GEB obtained, the
        6 unitswill be considered as free elective).
      </p>
      <div>2） 请把额外修读的GEA/B/C/D课程填在自由选修的课程方框内。</div>
      <p>
        Please remark the extra course(s) from GEA/B/C/D in the box of free
        elective courses below.
      </p> -->
    </div>
    <eltable
      :tableData="tableData"
      :tdData="tdData"
      :trData="trData"
      :couresList="couresList"
      :isStatus="isStatus"
      @getTotalNum="getTotalNum"
    >
      <div v-if="!progressVisible" class="elzong">
        <div>
          Total units of passed and not-released UC courses:
          <div class="elad">
            {{ totalUnitsPassed == 0 ? '' : totalUnitsPassed }}
          </div>
        </div>
        <div>
          Total units of in-process UC courses:
          <div class="elad">
            {{ totalUnitsInProcess == 0 ? '' : totalUnitsInProcess }}
            <!-- <span>{{ totalReally.val }}</span> -->
          </div>
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
      default: [],
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
    couresList: {
      type: Array,
      default: [],
    },
    isStatus: {
      type: Number,
      default: 0,
    },
    currentDetail:{
      type: Object,
      default: {},
    },
  },
  components: { eltable },
  computed: {
    /** 是否展示进度列 */
    progressVisible() {
      return this.trData.some((tr) => tr.type === 'progress');
    },
  },
  data() {
    return {
      totalNum: '',
      totalReally: {
        val: '',
      },
      tempTableData: [],
    }
  },
  methods: {
    getTotalNum(data, type) {
      this.$emit('refresh');
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
  margin-left: 28px;
}
.noName {
  text-align: left;
}
.noName div {
  margin-bottom: 0;
}
.text-color {
  font-weight: bold;
}
.elad {
  text-align: center;
}
</style>
