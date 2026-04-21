<template>
  <div class="p-config page">
    <div class="riImg">
      <div class="rioneimg chenser">
        <img src="./title.png" alt="" />
      </div>
      <div class="rioneimgs">
        <img src="./title2.png" alt="" />
      </div>
      <div class="stRen">
        <i class="el-icon-user-solid sulid"></i>{{ nameCh }}
        <div class="suRtui">
          <div @click="$router.push('/list')">Query self inspection</div>
          <div @click="$router.push('/config')">Configuration</div>
          <div @click="addout">Logout</div>
        </div>
      </div>
    </div>
    <div class="p-container">
      <div class="p-container__btn"><el-button @click="handleDownload" :loading="tableLoading" icon="el-icon-download" type="primary" size="small">下载</el-button></div>
      <el-table :data="reports.records" tooltip-effect="dark" style="width: 100%" v-loading="tableLoading">
        <el-table-column width="80" prop="grade" label="序号" align="center">
          <template slot-scope="scope">
            {{ getSerialNumber(scope.$index) }}
          </template>
        </el-table-column>
        <el-table-column width="120" prop="grade" label="年级" align="center"></el-table-column>
        <el-table-column prop="major" label="专业" align="center"></el-table-column>
        <el-table-column width="120" prop="total" label="总（人数）" align="center"></el-table-column>
        <el-table-column width="120" prop="submitTotal" label="已填（人数）" align="center"></el-table-column>
        <el-table-column width="120" prop="unfilledTotal" label="未提交（人数）" align="center"></el-table-column>
        <el-table-column prop="selfDeclarationTotal" width="180" label="申报本学期毕业（人数）" align="center"></el-table-column>
        <el-table-column width="150" prop="officerCheckedTotal" label="满足毕业（人数）" align="center"></el-table-column>
      </el-table>
      <div style="margin-top: 10px;text-align: right;">
        <el-pagination
          @size-change="handleFilter"
          @current-change="loadReport"
          :current-page.sync="param.current"
          :page-sizes="[10, 20, 30, 40]"
          :page-size.sync="param.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="reports.total"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import _ from 'lodash';

import { report, linkAdfsLogout } from '@/api/index'
import url from '@/api/url'

export default {
  data() {
    return {
      tableLoading: false,
      reports: {},
      param: {
        condition: {},
        sorts: [
          {
            asc: true,
            field: '',
          },
        ],
        current: 1,
        size: 10,
      },
    }
  },
  created() {
    this.nameCh = localStorage.getItem('nameCh')
    this.loadReport();
  },
  methods: {
    getSerialNumber(index) {
      return (this.param.current - 1) * this.param.size + index + 1;
    },
    handleFilter() {
      this.param.current = 1;
      this.loadReport();
    },
    async loadReport() {
      this.tableLoading = true;
      const result = await report(this.param);

      this.tableLoading = false;
      if (result.code === 200) {
        this.reports = result.data || {};
      } else {
        this.$message.error(result.message);
      }
    },
    handleDownload() {
      window.open(`${url}/biz/course-master/detail/data/export-list`);
    },
    async addout() {
      this.$confirm('此操作将退出返回登陆页面?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.$message({
            type: 'success',
            message: '退出成功!',
          })
          this.outlogin()
        })
    },
    async outlogin() {
      Cookies.remove('XSRF-TOKEN')
      window.localStorage.clear()
      linkAdfsLogout()
    },
  },
}
</script>
<style scoped>
.p-config {
  background: #fff;
}

.riImg {
  background: #6a1d72;
  width: 100vw;
  height: 80px;
  position: fixed;
  z-index: 99;
  top: 0;
}

.rioneimg {
  height: 60px;
  padding: 10px;
  display: inline-block;
  vertical-align: middle;
}
.rioneimgs {
  height: 80px;
  display: inline-block;
  vertical-align: middle;
}
.rioneimgs img {
  height: 100%;
  margin-left: 10px;
}

.chenser {
  background: #ddaa43;
}
.rioneimg img {
  height: 100%;
}
.ritext {
  text-align: center;
  width: 350px;
  display: inline-block;
  color: #eee;
}
.ritext div:first-child {
  letter-spacing: 6px;
  font-family: 'Book Antiqua';
}

.stRen {
  position: absolute;
  top: 30px;
  right: 50px;
  color: #fff;
  font-size: 16px;
  cursor: pointer;
  z-index: 99999;
  height: 30px;
  overflow: hidden;
}
.stRen:hover {
  height: 150px;
}
.stRen span {
  vertical-align: middle;
}

.suRtui {
  border: 1px solid #6a1d72;
  color: #6a1d72;
  background: #fff;
  border-radius: 6px;
  font-weight: 600;
  padding: 8px 10px;
  margin-top: 10px;
}
.suRtui > div {
  line-height: 30px;
}
.sulid {
  font-size: 20px;
  vertical-align: middle !important;
  margin-right: 10px;
}

.p-container {
  padding: 100px 40px;
}

.p-container__btn {
  text-align: right;
  margin-bottom: 20px;
}

.p-container__btn > button {
  background: #6a1d72;
  border-color: #6a1d72;
}
</style>
<style>
.w-e-text-container {
  height: calc(100% - 86px) !important;
}
.w-e-toolbar {
  z-index: 20 !important;
}
.w-e-text-container {
  z-index: 10 !important;
}
</style>
