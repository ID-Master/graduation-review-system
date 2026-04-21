<template>
  <div>
    <div name="calc" class="calc stDfou">{{ title }}</div>
    <div v-if="content" name="calc" desc="content" class="calc content" v-html="content" />
    <puretable :tdData="defaultTdData" :trData="trData" :row="row">
      <template v-slot:thBottom>
        <tr v-if="fixedTdData.length > 0">
          <td colspan="2" rowspan="2">
            Units of Credit Transfer for Major Elective Courses –
            non-equivalent course (if applicable)
          </td>
          <td v-text="fixedTdData[0].Units"></td>
          <td>{{ fixedTdData[0].selfCheck }}</td>
          <td colspan="2" rowspan="2">
            *“IP” means that you have already submitted the credit transfer
            applications to schools but under process.
          </td>
        </tr>
        <tr v-if="fixedTdData.length > 1">
          <td v-text="fixedTdData[1].Units"></td>
          <td>{{ fixedTdData[1].selfCheck }}</td>
        </tr>
      </template>
      <template>
        <div name="calc" class="calc" style="text-align: right; padding: 10px 0">
          <span> Total units of passed and not-released Major Elective Courses:</span>
          <input
            type="text"
            class="iptText"
            :readonly="true"
            v-model="totalUnitsPassed"
          />
        </div>
        <div name="calc" class="calc" style="text-align: right; padding: 10px 0">
          <span> Total units of in-process Major Elective Courses:</span>
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
import _ from 'lodash';
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
    isMajor: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    defaultTdData() {
      if (this.isMajor) {
        // 课程预览，没有最后2条虚拟数据
        return this.tdData;
      } else {
        return _.slice(this.tdData, 0, this.tdData.length - 2);
      }
    },
    fixedTdData() {
      return _.slice(this.tdData, this.tdData.length - 2, this.tdData.length);
    },
    textData() {
      return this.txData.filter(x => x.categoryCode === 'Major Elective Courses' && x.part === 'B');
    },
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
  },
  data() {
    return {
      totalUnitsPassed: 0,
      totalUnitsInProcess: 0,
    }
  },
  methods: {
    setUnitsVal() {
      this.totalUnitsPassed = 0;
      this.totalUnitsInProcess = 0;
      this.tdData.forEach((item) => {
        let units = item.Units
        if(units == ''){ units = 0 }
        let selfCheck = item.selfCheck
        if(['✔','NR'].includes(selfCheck)){
          this.totalUnitsPassed += Number(units)
        }        
        if(['IP'].includes(selfCheck)){
          this.totalUnitsInProcess += Number(units)
        }
      });
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
