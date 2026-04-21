<template>
  <div>
      
    <div class="stPadd">
      <div class="noDIv">
        <div class="noName">
          <div>香港中文大学(深圳)</div>
          <div>THE CHINESE UNIVERSITY OF HONG KONG, SHENZHEN</div>
          <div class="noFowei">经管学院</div>
          <div>School of Management and Economics</div>
          <div class="noFowei">毕业审核自查表</div>
          <div class="noText">GRADUATION AUDIT SELF-CHECK FORM </div>
        </div>
        <div class="noMar">
          <div>iis:</div>
          <div>Note:</div>
        </div>
        <div>
          <div>（一）请附上最新的非正式成绩单以供参考。</div>
          <p>
            Please provide a copy of the most-updated unofficial transcript for reference.
          </p>
        </div>
        <div>
          <div>（二）请仔细对照最新的成绩单和对应的修读计划填写表格。</div>
          <p>
            Please fill in this form and refer to the most-updated transcript and corresponding study scheme carefully.
          </p>
        </div>
        <div>
          <div>
           （三）以下情况请在自我审查一列打钩（√）已完成: 若课程已通过且成绩合格以上；或课程通过转学分/课程替代等形式修读且已获得学分，并在备注一栏标注转学分/课程替代。
          </div>
          <p>
             以下情况请在自我审查一列标注 NR(Not Released)未公布：若课程已完成且成绩待公布（具体仍视乎课程成绩通过与否）
          </p>
          <p>
            以下情况请在自我审查一列标注 IP(In Process)正在进行 ：若课程为当前已选/在读; 或转学分申请已提交，但尚在处理过程中
          </p>
          <p class="noPbc">
            Please follow the instruction as below to mark the self-check column:
          </p>
          <p>
            √: Please mark “√” in the self-check column if: the course is completed and passed; or the credit transfer application is finished to obtain the credits.
          </p>
          <p>
            NR: Please mark “NR” in the self-check column and count into the total units, if the course grade is pending to be released (result is still subject to the final grade of the course).,
          </p>
          <p>
            IP: Please mark “IP” in the self-check column if: the course is currently registered; or the course units are applied to be obtained by credit transfer or course exemption, while the application is  under process.
          </p>
          <p class="noPbc">
            Please follow the instruction as below to mark the remark column:
          </p>
          <p>
            Credit Transfer: If the course units are obtained by credit transfer or course exemption, please mark “Credit Transfer” in the remark column.
          </p>
        </div>
        <div>
          <div>
            （四）若申请辅修课程，请在用于辅修课程修读计划的相应课程后的辅修一栏标注。辅修必修课程请标注“Minor Required-*辅修专业缩写”, 辅修选修课程请标注“Minor Elective-*辅修专业缩写”。
          </div>
  
          <p>
             If you have applied for Minor Programme, please mark “Minor Required-*abbr. Minor Program” and “Minor Elective-*abbr. Minor Program” (e.g. “Minor Required-FIN”, ”Minor 
             Elective-FIN” )  in  the column  of“Minor” for corresponding courses.
          </p>
        </div>
        <div>
          <div>（五）各专业的修读计划及本科学术课程信息可查看教务处官网。</div>
          <p>
            For information on study scheme and details of academic curriculum, please check at: 
            <a href="https://registry.cuhk.edu.cn/en/basic/170"
              >https://registry.cuhk.edu.cn/en/basic/170</a
            >
          </p>
        </div>
        <div class="noBott">
          <el-button type="primary" @click="indexs = 2">Next</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">

export default {
  props: {
      //总数
      valueTotal:{
            type: String,
            default: '1'
      },
  
  },
  data() {
    return {
       
    };
  },

  methods: {
   
     
  },
  mounted() {
   
  }
};
</script>

<style  scoped>
.stPadd {
  margin: 0;
}

.stov {
  overflow: hidden;
}

.stDiv {
  width: 30px;
  height: 10px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #b6b5b5;
  background: #fff;
}

.stDiv:hover {
  width: 40px;
  height: 10px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #1989fa;
  background: #fff;
}

.stDivs {
  width: 40px;
  height: 10px;
  display: inline-block;
  border-radius: 6px;
  border: 1px solid #1989fa;
  background: #fff;
}

.stTitle .stldiv {
  width: calc(100% / 7);
}

.stDivname {
  padding: 10px 6px;
}

.stpoleft {
  background: #fff;
}

.noName {
  text-align: center;
  padding: 0;
}

.noFowei {
  font-weight: 600;
}

.noText {
  text-decoration: underline;
}

.noName div {
  margin-bottom: 0;
}

.noName div:nth-child(2n) {
  margin-bottom: 0;
}

p {
  padding: 0;
  margin: 5px 0 5px 46px;
}

.noPbc {
  padding: 0;
  margin: 20px 0 20px 46px;
}

.noMar {
  margin-bottom: 0;
}

.noDIv {
  padding-bottom: 200px;
  /* width: 100%; */
  width: 80%;
  min-width: 1300px;
}

.sotpd {
  position: fixed;
  height: 200px;
  background: #fff;
}
.noBott {
  padding: 30px 0;
  text-align: center;
}
</style>