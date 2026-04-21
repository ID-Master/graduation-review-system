<template>
  <div>
    <div class="elflexs eltose">
      <div class="elone"><span>Course Code</span></div>
      <div class="eltwe"><span>Course Title</span></div>
      <div class="elsan"><span>Units</span></div>
      <div class="elsi"><span>Self-check ( √ /NR/IP)</span></div>
      <div class="elsi" v-if="shows">
        <span>Minor <br />(Minor Required/Minor Elective)</span>
      </div>
      <div :class="shows ? 'elblics' : 'elblic'">
        <div class="elpoin">
          <div>Remark</div>
          <div>(Credit Transfer for equivalent course/Course substitution)</div>
        </div>
      </div>
    </div>
    <div class="elflexs">
      <div class="elone"><span>1234567890</span></div>
      <div class="eltwe"><span>asdasdasdasd asd asd asd asd asd </span></div>
      <div class="elsan"><span>3</span></div>
      <div class="elsi">
        <span>
          <el-select v-model="value" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option> </el-select
        ></span>
      </div>
      <div class="elsi" v-if="shows">
        <span>
          <el-select v-model="value1" placeholder="请选择">
            <el-option
              v-for="item in options1"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option> </el-select
        ></span>
      </div>
      <div :class="shows ? 'elblics' : 'elblic'">
        <span></span>
      </div>
    </div>
    <div class="elflexs">
      <div class="elone"><span></span></div>
      <div class="eltwe"><span> </span></div>
      <div class="elsan"><span></span></div>
      <div class="elsi"><span></span></div>
      <div class="elsi" v-if="shows"><span></span></div>
      <div :class="shows ? 'elblics' : 'elblic'">
        <span></span>
      </div>
    </div>
    <div class="elbotflex" v-if="botshow">
      <div class="elbotone">
        <span>
          Units of Credit Transfer for Mandatory Major Elective Courses - nonequivalent
          course (if applicable)</span
        >
      </div>
      <div class="elbotwe">
        <span>
          <div></div>
          <div class="ellin"></div>
        </span>
      </div>
      <div class="elbosan">
        <span>
          <div></div>
          <div class="ellin"></div>
        </span>
      </div>
      <div class="elbowei">
        <span>
          *"IP* means that you have already submitted the credit transfer
          applications to schools but under process.
        </span>
      </div>
    </div>
    <div class="elbotflex" v-if="botshow">
      <div class="elbotone">
        <span>
          Units of Credit Transfer for Mandatory Major Elective Courses - nonequivalent
          course (if applicable)</span
        >
      </div>
      <div class="elbotwe">
        <span>
          <div></div>
          <div class="ellin"></div>
        </span>
      </div>
      <div class="elbosan">
        <span>
          <div></div>
          <div class="ellin"></div>
        </span>
      </div>
      <div class="elbowei">
        <span>
          *"IP* means that you have already submitted the credit transfer
          applications to schools but under process.
        </span>
      </div>
    </div>
    <div class="elzong">
      <div>
        Total units of passed and not-released UC courses:
        <div class="elad"></div>
      </div>
      <div>
        Total units of in-process UC courses:
        <div class="elad"></div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  props: {
    //总数
    valueTotal: {
      type: String,
      default: '1',
    },
    //是否显示Minor
    shows: {
      type: Boolean,
      default: true,
    },
    //是否显示下面分数
    botshow: {
      type: Boolean,
      default: false,
    },
  },

  data() {
    return {
      options: [
        {
          value: '√',
          label: '√',
        },
        {
          value: 'NR',
          label: 'NR',
        },
        {
          value: 'IP',
          label: 'IP',
        },
      ],
      value: '√',
      options1: [
        {
          value: 'Minor Required',
          label: 'Minor Required',
        },
        {
          value: 'Minor Elective',
          label: 'Minor Elective',
        },
      ],
      value1: '',
    }
  },

  methods: {},
  mounted() {},
}
</script>

<style  scoped>
.elblic {
  text-align: left;
  width: 45%;
  position: relative;
}
.elblics {
  text-align: left;
  width: 27%;
  position: relative;
}
.elpoin {
  position: absolute;
  left: 20px;
}
p {
  margin: 0;
  padding: 0;
  width: 100%;
}
.elone {
  width: 10% !important;
  border-right: 1px solid rgb(201, 201, 201);
}
.eltwe {
  width: 17% !important;
  text-align: center;
  border-right: 1px solid rgb(201, 201, 201);
}

.elsan {
  width: 10% !important;
  border-right: 1px solid rgb(201, 201, 201);
}
.elsi {
  width: 18% !important;
  border-right: 1px solid rgb(201, 201, 201);
}
/* .elwidht {
  width: 1863px;
} */
.elflexs {
  min-width: 1300px;
  display: flex;
  border: 1px solid rgb(201, 201, 201);
  border-top: 0px solid rgb(201, 201, 201);
  align-items: stretch;
  position: relative;
}
.elflexs:first-child {
  border-top: 1px solid rgb(201, 201, 201);
}
.eltose {
  background: #6a1d72;
  color: #fff;
}
.elflexs > div {
  min-height: 50px;
  padding: 10px 0;
  display: flex;
  align-items: center;
  justify-content: center;
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
.elbotflex {
  min-width: 1300px;
  display: flex;
  border: 1px solid rgb(201, 201, 201);
  border-top: 0px solid rgb(201, 201, 201);
  align-items: stretch;
}
.elbotflex > div {
  padding: 10px 0px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.elbotone {
  width: calc(27% + 1px) !important;
  border-right: 1px solid rgb(201, 201, 201);
}
.elbotone span {
  padding: 10px;
}
.elbotwe {
  width: 10% !important;
  position: relative;
  border-right: 1px solid rgb(201, 201, 201);
}
.elbosan {
  width: 18% !important;
  position: relative;
  border-right: 1px solid rgb(201, 201, 201);
}
.elbosan span div {
  /* width: 245px; */
  text-align: center;
}
.elbotwe span div {
  /* width: 75px; */
  text-align: center;
}
.elbosan span div:first-child {
  padding-bottom: 10px;
  padding-top: 5px;
  border-bottom: 1px solid rgb(201, 201, 201);
}

.elbotwe span div:first-child {
  padding-bottom: 10px;
  padding-top: 5px;
  border-bottom: 1px solid rgb(201, 201, 201);
}
.ellin {
  padding-top: 8px;
}
.elbowei {
  width: 45% !important;
}
.elbowei span {
  padding: 10px;
}
.elanniu {
  position: absolute;
  right: -90px;
  top: 50%;
  transform: translate(0%, -50%);
}
.ellanse {
  color: #3a62d7 !important;
  border-radius: 100%;
  font-size: 25px;
  margin-right: 20px;
}
.elheise {
  color: #979797 !important;
  border-radius: 100%;
  font-size: 25px;
}
</style>
<style >
.elflexs .el-input__inner {
  border: 0;
  border-radius: 0px;
  height: 60px;
  box-shadow: none;
}
</style>
