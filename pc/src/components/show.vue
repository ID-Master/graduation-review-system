<template>
  <div class="tarRwi container-scroll">
    <div class="tarRis">
      <div id="exportPdf" ref="exportPdf">
        <div class="tarRcolor" id="eight">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <nodet :shows="false" />
          <stuatsname :valueTotal="piData" />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="one">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <college-student
            :tdData="UniversityCoreData"
            :trData="trSchoolData"
            :row="0"
            :txData="contentTextList"
            class="target-node-item"
            ref="university"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="two">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <college-courses
            :tdData="SchoolPackageData"
            :trData="trSchoolData"
            :row="0"
            :txData="contentTextList"
            class="target-node-item"
            ref="school"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="three">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <core-courses
            :tdData="MajorRequiredCoursesData"
            :trData="trSchoolData"
            :txData="contentTextList"
            :row="0"
            ref="major"
            class="target-node-item"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="fore">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <professional-elective
            :tdData="MajorElectiveCoursesDataA"
            :trData="trSchoolData"
            :txData="contentTextList"
            :row="0"
            ref="profess"
            class="target-node-item"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="five">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <professionalElectiveB
            :tdData="MajorElectiveCoursesDataB"
            :trData="trSchoolData"
            :row="0"
            class="target-node-item"
            ref="professB"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="six">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <freeElectives
            :txData="contentTextList"
          />
          <FreeElectiveData
            :tdData="FreeElectiveData"
            :trData="trSchoolData"
            :row="0"
            :txData="contentTextList"
            class="target-node-item"
            ref="FreeE"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
        <div name="calc" class="calc tar-margin"></div>
        <div class="tarRcolor" id="seven">
          <div name="calc" class="calc tarRcolor-padding"></div>
          <statement
            :statementData="statementData"
            :isCheckBox="isCheckBox"
            :teacherCheckBox="teacherCheckBox"
            :auditBtn="auditBtn"
          />
          <div name="calc" class="calc tarRcolor-padding"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  
}
</script>
<style scoped>

</style>