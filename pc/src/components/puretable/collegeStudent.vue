<template>
  <div>
    <div name="calc" class="calc stDfou">{{ title }}</div>
    <div name="calc" class="calc content" desc="content" v-html="content" />
    <puretable :tdData="tdData" :trData="trData" :row="row">
      <template v-if="!progressVisible">
        <div name="calc" class="calc" style="text-align: right; padding: 10px 0">
          <span> Total units of passed and not-released UC courses： </span>
          <input
            type="text"
            class="iptText"
            :readonly="true"
            v-model="totalUnitsPassed"
          />
        </div>
        <div name="calc" class="calc" style="text-align: right; padding: 10px 0">
          <span> Total units of in-process UC courses： </span>
          <input
            type="text"
            class="iptText"
            :readonly="true"
            v-model="totalUnitsInProcess"
          />
        </div>
      </template>
    </puretable>
  </div>
</template>
<script>
import puretable from './index'
export default {
  name: 'collegeStudent',
  components: {
    puretable,
  },
  props: {
    tdData: {
      type: Array,
      default: [],
    },
    trData: {
      type: Array,
      default: [],
    },
    row: {
      type: Number,
      default: 0,
    },
    txData: {
      type: Array,
      default: [],
    },
    code: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      totalUnitsPassed: 0,
      totalUnitsInProcess: 0,
      textData: [],
    }
  },
  computed: {
    title() {
      if (this.textData && this.textData.length > 0) {
        return this.textData[0].title;
      } else {
        return '';
      }
    },
    content() {
      if (this.textData && this.textData.length > 0) {
        return this.textData[0].content;
      } else {
        return '';
      }
    },
    /** 是否展示进度列 */
    progressVisible() {
      return this.trData.some((tr) => tr.type === 'progress');
    },
  },
  watch: {
    txData: {
      deep: true,
      immediate: true,
      handler(val) {
        this.textData = val.filter((item) => item.categoryCode === (this.code || 'University Core'));
      },
    },
  },
  methods: {
    setUnitsVal() {
      this.totalUnitsPassed = 0;
      this.totalUnitsInProcess = 0;
      this.tdData.forEach((item) => {
        let units = item.Units
        if(units == ''){ units = 0 }
        let selfCheck = item.selfCheck
        if (['✔', 'NR'].includes(selfCheck)) {
          this.totalUnitsPassed += Number(units)
        }
        if (['IP'].includes(selfCheck)) {
          this.totalUnitsInProcess += Number(units)
        }
      })
    },
  },
}
</script>
<style scoped>
.stDfou {
  padding: 10px 0;
  font-weight: 600;
}
input {
  text-align: center;
}
</style>
