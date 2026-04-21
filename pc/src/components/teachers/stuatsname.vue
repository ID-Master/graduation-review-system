<template>
  <div class="stpabot">
    <div name="calc" class="calc stpabot-margin"></div>
    <div v-if="valueTotal.studentVO" class="stDoe calc" name="calc">
      <div class="stRde stWid">
        <div>学号</div>
        <div class="stDis">
          <div>student I.D.No:</div>
          <div class="stDxu">{{valueTotal.studentVO.loginName}}</div>
        </div>
      </div>
      <div class="stRde stWidsan">
        <div>姓名（英文）</div>
        <div class="stDis">
          <div>Name：（in English）</div>
          <div class="stDxu">{{valueTotal.studentVO.nameEn}}</div>
        </div>
      </div>
      <div class="stRde stWidtwe">
        <div>（中文）</div>
        <div class="stDis">
          <div>（in Chinese）</div>
          <div class="stDxs">{{valueTotal.studentVO.nameCh}}</div>
        </div>
      </div>
      <div class="stRde stWid">
        <div>主修 / 课程</div>
        <div class="stDis">
          <div>Major / Programme:</div>
          <div class="stDxu">{{valueTotal.studentVO.major}}</div>
        </div>
      </div>
      <div class="stRde stWidsan">
        <div>预期毕业年份 / 学期</div>
        <div class="stDis">
          <div>Expected Year / Term of Graduation:</div>
          <div class="stDxu">{{ schoolYear[valueTotal.studentVO.expectedYear] }}</div>
        </div>
      </div>
      <div class="stRde stWidtwe">
        <div>联络电话</div>
        <div class="stDis">
          <div>Contact Tel.No</div>
          <div class="stDxs">{{valueTotal.studentVO.areaCode}}{{valueTotal.studentVO.contactTel}}</div>
        </div>
      </div>
      <div class="stRde  stminwi">
        <div>辅修 / 课程（如适用）</div>
        <div class="stDis">
          <div>Minor / Programme:（if applicable）</div>
          <div class="stDxus">{{valueTotal.studentVO.minor}}</div>
        </div>
      </div>
    </div>
    <div name="calc" class="calc stpabot-margin"></div>
  </div>
</template>

<script type="text/ecmascript-6">
import { getSchoolYear } from '@/api/index'
export default {
  props: {
    //
    valueTotal: {
      type: Object,
      default: {},
    },
  },

  data() {
    return {
      schoolYear: {},
    };
  },

  methods: {},
  async mounted() {
    const result = await getSchoolYear();
    this.schoolYear = result.data || {};
  },
};
</script>

<style  scoped>
.stDoe {
  width: 100%;
  padding: 20px 0 0 0;
  border-top: 1px dashed #000;
  border-bottom: 1px dashed #000;
}
.stpabot-margin {
  height: 30px;
}
.stDindex{
    padding-left:20px
}
.stDoe .stRde {
  display: inline-block;
  margin-bottom: 20px;
}
.stDfou{
    font-weight: 600;
}
.stRde > div {
  margin-bottom: 10px;
}
.stRde > div:last-child {
  margin-bottom: 0px;
}
.stWid {
  width: 37%;
}
.stWidsan {
  width: 37%;
}
.stRtex {
  text-align: left;
}
.stWidtwe {
  width: 24.8%;
}
.stDis div {
  display: inline-block;
}
.stDxu {
  min-width: 150px;
  text-align: center;

  border-bottom: 1px solid #000;
}
.stDxus{
    min-width:500px;
  text-align: center;

  border-bottom: 1px solid #000;
}
.stminwi{
  max-width: 800px !important;
}
.stDxs {
  min-width: 170px;
  border-bottom: 1px solid #000;
}
.stDmsi{
    margin: 10px 0;
}
.stpabot{
    /* padding-bottom:80px; */
}
</style>
