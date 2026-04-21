<template>
  <div class="stPadd">
    <div class="noName">
      <div class="title text-color" v-html="currentDetail.title"></div>
      <div class="content" v-html="currentDetail.content"></div>
      <!-- <div class="text-color">
        专业必修科目 Major Required Courses (18 units)
      </div> -->
    </div>
    <eltable
      :tdData="tdData"
      :trData="trData"
      :tableData="tableData"
      @getTotalNum="getTotalNum"
      :couresList="couresList"
      :isStatus="isStatus"
    >
      <div class="elzong">
        <div>
          Total units of passed and not-released MR courses:
          <div class="elad">{{ totalUnitsPassed == 0 ? '' : totalUnitsPassed }}</div>
        </div>
        <div>
          Total units of in-process MR courses:
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
      type: Array,
      default: [],
    },
    trData: {
      type: Array,
      default: [],
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
  data() {
    return {
      totalNum: '',
      totalReally: {
        val: '',
      },
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
.elad {
  text-align: center;
}
</style>
